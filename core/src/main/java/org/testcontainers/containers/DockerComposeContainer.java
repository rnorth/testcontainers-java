package org.testcontainers.containers;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.IndefiniteWaitOneShotStartupCheckStrategy;
import org.testcontainers.utility.AuditLogger;
import org.testcontainers.utility.Base58;
import org.testcontainers.utility.CommandLine;
import org.testcontainers.utility.DockerLoggerFactory;
import org.testcontainers.utility.LogUtils;
import org.testcontainers.utility.MountableFile;
import org.testcontainers.utility.ResourceReaper;
import org.testcontainers.utility.TestcontainersConfiguration;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static org.testcontainers.containers.BindMode.READ_ONLY;
import static org.testcontainers.containers.BindMode.READ_WRITE;

/**
 * Container which launches Docker Compose, for the purposes of launching a defined set of containers.
 */
public class DockerComposeContainer<SELF extends DockerComposeContainer<SELF>> extends FailureDetectingExternalResource {

    /**
     * Random identifier which will become part of spawned containers names, so we can shut them down
     */
    private final String identifier;
    private final List<File> composeFiles;
    private final Set<String> spawnedContainerIds = new HashSet<>();
    private final Set<String> spawnedNetworkIds = new HashSet<>();
    private final Map<String, Integer> scalingPreferences = new HashMap<>();
    private DockerClient dockerClient;
    private boolean localCompose;
    private boolean pull = true;
    private boolean tailChildContainers;

    private String project;

    private final AtomicInteger nextAmbassadorPort = new AtomicInteger(2000);
    private final Map<String, Map<Integer, Integer>> ambassadorPortMappings = new ConcurrentHashMap<>();
    private final SocatContainer ambassadorContainer = new SocatContainer();

    private static final Object MUTEX = new Object();

    /**
     * Properties that should be passed through to all Compose and ambassador containers (not
     * necessarily to containers that are spawned by Compose itself)
     */
    private Map<String, String> env = new HashMap<>();

    @Deprecated
    public DockerComposeContainer(File composeFile, String identifier) {
        this(identifier, composeFile);
    }

    public DockerComposeContainer(File... composeFiles) {
        this(Arrays.asList(composeFiles));
    }

    public DockerComposeContainer(List<File> composeFiles) {
        this(Base58.randomString(6).toLowerCase(), composeFiles);
    }

    public DockerComposeContainer(String identifier, File... composeFiles) {
        this(identifier, Arrays.asList(composeFiles));
    }

    public DockerComposeContainer(String identifier, List<File> composeFiles) {

        this.composeFiles = composeFiles;

        // Use a unique identifier so that containers created for this compose environment can be identified
        this.identifier = identifier;
        project = randomProjectId();

        this.dockerClient = DockerClientFactory.instance().client();
    }

    @Override
    @VisibleForTesting
    public void starting(Description description) {
        synchronized (MUTEX) {
            registerContainersForShutdown();
            if (pull) {
                pullImages();
            }
            applyScaling(); // scale before up, so that all scaled instances are available first for linking
            createServices();
            if (tailChildContainers) {
                tailChildContainerLogs();
            }
            startAmbassadorContainers();
        }
    }

    private void pullImages() {
        runWithCompose("pull");
    }


    private void createServices() {
        // Run the docker-compose container, which starts up the services
        runWithCompose("up -d");
    }

    private void tailChildContainerLogs() {
        listChildContainers().forEach(container ->
                LogUtils.followOutput(dockerClient,
                        container.getId(),
                        new Slf4jLogConsumer(logger()).withPrefix(container.getNames()[0]),
                        OutputFrame.OutputType.STDOUT,
                        OutputFrame.OutputType.STDERR)
        );
    }

    private void runWithCompose(String cmd) {
        final DockerCompose dockerCompose;
        if (localCompose) {
            dockerCompose = new LocalDockerCompose(composeFiles, project);
        } else {
            dockerCompose = new ContainerisedDockerCompose(composeFiles, project);
        }

        dockerCompose
                .withCommand(cmd)
                .withEnv(env)
                .invoke();
    }

    private void applyScaling() {
        // Apply scaling
        if (!scalingPreferences.isEmpty()) {
            StringBuilder sb = new StringBuilder("scale");
            for (Map.Entry<String, Integer> scale : scalingPreferences.entrySet()) {
                sb.append(" ").append(scale.getKey()).append("=").append(scale.getValue());
            }

            runWithCompose(sb.toString());
        }
    }

    private void registerContainersForShutdown() {
        ResourceReaper.instance().registerFilterForCleanup(Arrays.asList(
                new SimpleEntry<>("label", "com.docker.compose.project=" + project)
        ));
    }

    private List<Container> listChildContainers() {
        return dockerClient.listContainersCmd()
                .withShowAll(true)
                .exec().stream()
                .filter(container -> Arrays.stream(container.getNames()).anyMatch(name ->
                        name.startsWith("/" + project)))
                .collect(toList());
    }

    private void startAmbassadorContainers() {
        ambassadorContainer.start();
    }

    private Logger logger() {
        return LoggerFactory.getLogger(DockerComposeContainer.class);
    }

    @Override
    @VisibleForTesting
    public void finished(Description description) {


        synchronized (MUTEX) {
            try {
                // shut down the ambassador container
                ambassadorContainer.stop();

                // Kill the services using docker-compose
                try {
                    runWithCompose("down -v");

                    // If we reach here then docker-compose down has cleared networks and containers;
                    //  we can unregister from ResourceReaper
                    spawnedContainerIds.forEach(ResourceReaper.instance()::unregisterContainer);
                    spawnedNetworkIds.forEach(ResourceReaper.instance()::unregisterNetwork);
                } catch (Exception e) {
                    // docker-compose down failed; use ResourceReaper to ensure cleanup

                    // kill the spawned service containers
                    spawnedContainerIds.forEach(ResourceReaper.instance()::stopAndRemoveContainer);

                    // remove the networks after removing the containers
                    spawnedNetworkIds.forEach(ResourceReaper.instance()::removeNetworkById);
                }

                spawnedContainerIds.clear();
                spawnedNetworkIds.clear();
            } finally {
                project = randomProjectId();
            }
        }
    }

    public SELF withExposedService(String serviceName, int servicePort) {

        if (!serviceName.matches(".*_[0-9]+")) {
            serviceName += "_1"; // implicit first instance of this service
        }

        /*
         * For every service/port pair that needs to be exposed, we register a target on an 'ambassador container'.
         *
         * The ambassador container's role is to link (within the Docker network) to one of the
         * compose services, and proxy TCP network I/O out to a port that the ambassador container
         * exposes.
         *
         * This avoids the need for the docker compose file to explicitly expose ports on all the
         * services.
         *
         * {@link GenericContainer} should ensure that the ambassador container is on the same network
         * as the rest of the compose environment.
         */

        // Ambassador container will be started together after docker compose has started
        int ambassadorPort = nextAmbassadorPort.getAndIncrement();
        ambassadorPortMappings.computeIfAbsent(serviceName, __ -> new ConcurrentHashMap<>()).put(servicePort, ambassadorPort);
        ambassadorContainer.withTarget(ambassadorPort, serviceName, servicePort);
        ambassadorContainer.addLink(new FutureContainer(this.project + "_" + serviceName), serviceName);
        return self();
    }

    public DockerComposeContainer withExposedService(String serviceName, int instance, int servicePort) {
        return withExposedService(serviceName + "_" + instance, servicePort);
    }

    /**
     * Get the host (e.g. IP address or hostname) that an exposed service can be found at, from the host machine
     * (i.e. should be the machine that's running this Java process).
     * <p>
     * The service must have been declared using DockerComposeContainer#withExposedService.
     *
     * @param serviceName the name of the service as set in the docker-compose.yml file.
     * @param servicePort the port exposed by the service container.
     * @return a host IP address or hostname that can be used for accessing the service container.
     */
    public String getServiceHost(String serviceName, Integer servicePort) {
        return ambassadorContainer.getContainerIpAddress();
    }

    /**
     * Get the port that an exposed service can be found at, from the host machine
     * (i.e. should be the machine that's running this Java process).
     * <p>
     * The service must have been declared using DockerComposeContainer#withExposedService.
     *
     * @param serviceName the name of the service as set in the docker-compose.yml file.
     * @param servicePort the port exposed by the service container.
     * @return a port that can be used for accessing the service container.
     */
    public Integer getServicePort(String serviceName, Integer servicePort) {
        return ambassadorContainer.getMappedPort(ambassadorPortMappings.get(serviceName).get(servicePort));
    }

    public SELF withScaledService(String serviceBaseName, int numInstances) {
        scalingPreferences.put(serviceBaseName, numInstances);

        return self();
    }

    public SELF withEnv(String key, String value) {
        env.put(key, value);
        return self();
    }

    public SELF withEnv(Map<String, String> env) {
        env.forEach(this.env::put);
        return self();
    }

    /**
     * Use a local Docker Compose binary instead of a container.
     *
     * @return this instance, for chaining
     */
    public SELF withLocalCompose(boolean localCompose) {
        this.localCompose = localCompose;
        return self();
    }

    /**
     * Whether to pull images first.
     *
     * @return this instance, for chaining
     */
    public SELF withPull(boolean pull) {
        this.pull = pull;
        return self();
    }

    /**
     * Whether to tail child container logs.
     *
     * @return this instance, for chaining
     */
    public SELF withTailChildContainers(boolean tailChildContainers) {
        this.tailChildContainers = tailChildContainers;
        return self();
    }

    private SELF self() {
        return (SELF) this;
    }

    private String randomProjectId() {
        return identifier + Base58.randomString(6).toLowerCase();
    }
}

interface DockerCompose {
    String ENV_PROJECT_NAME = "COMPOSE_PROJECT_NAME";
    String ENV_COMPOSE_FILE = "COMPOSE_FILE";

    DockerCompose withCommand(String cmd);

    DockerCompose withEnv(Map<String, String> env);

    void invoke();

    default void validateFileList(List<File> composeFiles) {
        checkNotNull(composeFiles);
        checkArgument(!composeFiles.isEmpty(), "No docker compose file have been provided");
    }
}

/**
 * Use Docker Compose container.
 */
class ContainerisedDockerCompose extends GenericContainer<ContainerisedDockerCompose> implements DockerCompose {

    private static final String DOCKER_SOCKET_PATH = "/var/run/docker.sock";
    private static final String DOCKER_CONFIG_FILE = "/root/.docker/config.json";
    private static final String DOCKER_CONFIG_ENV = "DOCKER_CONFIG_FILE";
    private static final String DOCKER_CONFIG_PROPERTY = "dockerConfigFile";
    public static final char UNIX_PATH_SEPERATOR = ':';

    public ContainerisedDockerCompose(List<File> composeFiles, String identifier) {

        super(TestcontainersConfiguration.getInstance().getDockerComposeContainerImage());
        validateFileList(composeFiles);

        addEnv(ENV_PROJECT_NAME, identifier);

        // Map the docker compose file into the container
        final File dockerComposeBaseFile = composeFiles.get(0);
        final String pwd = dockerComposeBaseFile.getAbsoluteFile().getParentFile().getAbsolutePath();
        final String containerPwd = MountableFile.forHostPath(pwd).getFilesystemPath();

        final List<String> absoluteDockerComposeFiles = composeFiles.stream()
                .map(File::getAbsolutePath)
                .map(MountableFile::forHostPath)
                .map(MountableFile::getFilesystemPath)
                .collect(toList());
        final String composeFileEnvVariableValue = Joiner.on(UNIX_PATH_SEPERATOR).join(absoluteDockerComposeFiles); // we always need the UNIX path separator
        logger().debug("Set env COMPOSE_FILE={}", composeFileEnvVariableValue);
        addEnv(ENV_COMPOSE_FILE, composeFileEnvVariableValue);
        addFileSystemBind(pwd, containerPwd, READ_ONLY);

        // Ensure that compose can access docker. Since the container is assumed to be running on the same machine
        //  as the docker daemon, just mapping the docker control socket is OK.
        // As there seems to be a problem with mapping to the /var/run directory in certain environments (e.g. CircleCI)
        //  we map the socket file outside of /var/run, as just /docker.sock
        addFileSystemBind(getDockerSocketHostPath(), "/docker.sock", READ_WRITE);
        addEnv("DOCKER_HOST", "unix:///docker.sock");
        setStartupCheckStrategy(new IndefiniteWaitOneShotStartupCheckStrategy());
        setWorkingDirectory(containerPwd);

        String dockerConfigPath = determineDockerConfigPath();
        if (dockerConfigPath != null && !dockerConfigPath.isEmpty()) {
            addFileSystemBind(dockerConfigPath, DOCKER_CONFIG_FILE, READ_ONLY);
        }
    }

    private String determineDockerConfigPath() {
        String dockerConfigEnv = System.getenv(DOCKER_CONFIG_ENV);
        String dockerConfigProperty = System.getProperty(DOCKER_CONFIG_PROPERTY);
        Path dockerConfig = Paths.get(System.getProperty("user.home"), ".docker", "config.json");

        if (dockerConfigEnv != null && !dockerConfigEnv.trim().isEmpty() && Files.exists(Paths.get(dockerConfigEnv))) {
            return dockerConfigEnv;
        } else if (dockerConfigProperty != null && !dockerConfigProperty.trim().isEmpty() && Files.exists(Paths.get(dockerConfigProperty))) {
            return dockerConfigProperty;
        } else if (Files.exists(dockerConfig)) {
            return dockerConfig.toString();
        } else {
            return null;
        }
    }

    private String getDockerSocketHostPath() {
        return SystemUtils.IS_OS_WINDOWS
                ? "/" + DOCKER_SOCKET_PATH
                : DOCKER_SOCKET_PATH;
    }

    @Override
    public void invoke() {
        super.start();

        this.followOutput(new Slf4jLogConsumer(logger()));

        // wait for the compose container to stop, which should only happen after it has spawned all the service containers
        logger().info("Docker Compose container is running for command: {}", Joiner.on(" ").join(this.getCommandParts()));
        while (this.isRunning()) {
            logger().trace("Compose container is still running");
            Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
        }
        logger().info("Docker Compose has finished running");

        AuditLogger.doComposeLog(this.getCommandParts(), this.getEnv());

        final Integer exitCode = this.dockerClient.inspectContainerCmd(containerId)
                .exec()
                .getState()
                .getExitCode();

        if (exitCode == null || exitCode != 0) {
            throw new ContainerLaunchException(
                    "Containerised Docker Compose exited abnormally with code " +
                            exitCode +
                            " whilst running command: " +
                            StringUtils.join(this.getCommandParts(), ' '));
        }
    }
}

/**
 * Use local Docker Compose binary, if present.
 */
class LocalDockerCompose implements DockerCompose {
    /**
     * Executable name for Docker Compose.
     */
    private static final String COMPOSE_EXECUTABLE = SystemUtils.IS_OS_WINDOWS ? "docker-compose.exe" : "docker-compose";

    private final List<File> composeFiles;
    private final String identifier;
    private String cmd = "";
    private Map<String, String> env = new HashMap<>();

    public LocalDockerCompose(List<File> composeFiles, String identifier) {
        validateFileList(composeFiles);

        this.composeFiles = composeFiles;
        this.identifier = identifier;
    }

    @Override
    public DockerCompose withCommand(String cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public DockerCompose withEnv(Map<String, String> env) {
        this.env = env;
        return this;
    }

    @Override
    public void invoke() {
        // bail out early
        if (!CommandLine.executableExists(COMPOSE_EXECUTABLE)) {
            throw new ContainerLaunchException("Local Docker Compose not found. Is " + COMPOSE_EXECUTABLE + " on the PATH?");
        }

        final Map<String, String> environment = Maps.newHashMap(env);
        environment.put(ENV_PROJECT_NAME, identifier);

        final File dockerComposeBaseFile = composeFiles.get(0);
        final File pwd = dockerComposeBaseFile.getAbsoluteFile().getParentFile().getAbsoluteFile();
        environment.put(ENV_COMPOSE_FILE, new File(pwd, dockerComposeBaseFile.getAbsoluteFile().getName()).getAbsolutePath());

        logger().info("Local Docker Compose is running command: {}", cmd);

        final List<String> command = Splitter.onPattern(" ")
                .omitEmptyStrings()
                .splitToList(COMPOSE_EXECUTABLE + " " + cmd);

        try {
            new ProcessExecutor().command(command)
                    .redirectOutput(Slf4jStream.of(logger()).asInfo())
                    .redirectError(Slf4jStream.of(logger()).asError())
                    .environment(environment)
                    .directory(pwd)
                    .exitValueNormal()
                    .executeNoTimeout();

            logger().info("Docker Compose has finished running");

        } catch (InvalidExitValueException e) {
            throw new ContainerLaunchException("Local Docker Compose exited abnormally with code " +
                    e.getExitValue() + " whilst running command: " + cmd);

        } catch (Exception e) {
            throw new ContainerLaunchException("Error running local Docker Compose command: " + cmd, e);
        }
    }

    /**
     * @return a logger
     */
    private Logger logger() {
        return DockerLoggerFactory.getLogger(COMPOSE_EXECUTABLE);
    }
}

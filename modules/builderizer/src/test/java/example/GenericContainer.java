package example;

import org.testcontainers.containercore.builderizer.ContainerBuilderDSL;

import java.util.Arrays;

/**
 * TODO: Javadocs
 */
@ContainerBuilderDSL
public abstract class GenericContainer implements StartStoppable, Container {
    protected String name;

    @Override
    public String toString() {
        return "GenericContainer{" +
            "name='" + name + '\'' +
            '}';
    }

    public void exec(String... cmd) {
        System.err.println("exec " + Arrays.toString(cmd));
    }

    public void start() {
        System.err.println("start " + name);
    }

    public void stop() {
        System.err.println("stop " + name);
    }
}


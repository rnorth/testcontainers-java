# Open Issues Review
Generated: 2026-03-06 | Total open issues reviewed: 200

---

## Summary Stats

| Category | Count |
|---|---|
| Bug reports (labeled) | 109 |
| Enhancements | 46 |
| Features | 13 |
| Questions | 4 |
| Waiting for info | 12 |
| Acknowledged | 9 |

---

## Issues With Unmerged Fix PRs

These bugs already have a fix ready to review/merge:

| Issue | PR | Description |
|---|---|---|
| [#11489](https://github.com/testcontainers/testcontainers-java/issues/11489) | [PR #11490](https://github.com/testcontainers/testcontainers-java/pull/11490) | `!override` tag not supported in Docker Compose |
| [#11487](https://github.com/testcontainers/testcontainers-java/issues/11487) | [PR #11488](https://github.com/testcontainers/testcontainers-java/pull/11488) | Host UID/GID leakage in `copyFileToContainer` TAR entries |
| [#11483](https://github.com/testcontainers/testcontainers-java/issues/11483) | [PR #11484](https://github.com/testcontainers/testcontainers-java/pull/11484) | Running TC changes global uncaught exception handler (Awaitility) |
| [#11416](https://github.com/testcontainers/testcontainers-java/issues/11416) | [PR #11417](https://github.com/testcontainers/testcontainers-java/pull/11417) | `LoggedPullImageResultCallback` ArithmeticException (div-by-zero on sub-second pulls) |
| [#11215](https://github.com/testcontainers/testcontainers-java/issues/11215) | [PR #11431](https://github.com/testcontainers/testcontainers-java/pull/11431) | `getLogs()` returns empty when container fails to start |
| [#10375](https://github.com/testcontainers/testcontainers-java/issues/10375), [#9605](https://github.com/testcontainers/testcontainers-java/issues/9605) | [PR #11420](https://github.com/testcontainers/testcontainers-java/pull/11420) | K3sContainer not working under WSL2 |
| [#8797](https://github.com/testcontainers/testcontainers-java/issues/8797) | [PR #11499](https://github.com/testcontainers/testcontainers-java/pull/11499) | Add `getR2dbcUrl()` helper to `JdbcDatabaseContainer` |

---

## Confirmed Duplicates

| Keep | Close | Topic |
|---|---|---|
| [#9220](https://github.com/testcontainers/testcontainers-java/issues/9220) | [#8281](https://github.com/testcontainers/testcontainers-java/issues/8281) | "Library source does not match the bytecode for class GenericContainer" — identical issue |
| [#9053](https://github.com/testcontainers/testcontainers-java/issues/9053) | [#7236](https://github.com/testcontainers/testcontainers-java/issues/7236) | MySQL can't chown `/etc/mysql/conf.d` in rootless Docker — same root cause |

Likely duplicates (need individual triage):
- **Ryuk connection timeouts**: [#9120](https://github.com/testcontainers/testcontainers-java/issues/9120), [#4954](https://github.com/testcontainers/testcontainers-java/issues/4954), [#5122](https://github.com/testcontainers/testcontainers-java/issues/5122), [#8577](https://github.com/testcontainers/testcontainers-java/issues/8577) — multiple reports of Ryuk failing to connect, likely same root cause
- **"Could not find valid Docker environment"**: [#11560](https://github.com/testcontainers/testcontainers-java/issues/11560), [#9050](https://github.com/testcontainers/testcontainers-java/issues/9050), [#10788](https://github.com/testcontainers/testcontainers-java/issues/10788), [#10094](https://github.com/testcontainers/testcontainers-java/issues/10094) — several independent reports across different Docker versions/platforms

---

## Feature Requests (59 issues)

These are labeled `type/feature` or `type/enhancement`. Not bugs.

### Core API / Architecture
- [#8612](https://github.com/testcontainers/testcontainers-java/issues/8612) Shrink core JAR / rethink shading
- [#10284](https://github.com/testcontainers/testcontainers-java/issues/10284) Migrate nullability annotations to jspecify
- [#10286](https://github.com/testcontainers/testcontainers-java/issues/10286) Apply Gradle best practices
- [#6164](https://github.com/testcontainers/testcontainers-java/issues/6164) Throw specific exception type if no Docker env found
- [#7337](https://github.com/testcontainers/testcontainers-java/issues/7337) JPMS / Automatic-Module-Name support

### Container lifecycle / configuration
- [#9479](https://github.com/testcontainers/testcontainers-java/issues/9479) Container startup attempt backoff strategy
- [#6069](https://github.com/testcontainers/testcontainers-java/issues/6069) When all startup attempts fail, run wait strategy for diagnostics
- [#6066](https://github.com/testcontainers/testcontainers-java/issues/6066) Inverse of `waitingFor()` to catch failing containers
- [#7708](https://github.com/testcontainers/testcontainers-java/issues/7708) Allow `withExposedPorts` to be called multiple times (additive)
- [#9530](https://github.com/testcontainers/testcontainers-java/issues/9530) `setCommand` should allow space escaping
- [#7046](https://github.com/testcontainers/testcontainers-java/issues/7046) Allow `testcontainers.properties` filename/path to be injectable
- [#6388](https://github.com/testcontainers/testcontainers-java/issues/6388) Make `execInContainer` asynchronous
- [#5853](https://github.com/testcontainers/testcontainers-java/issues/5853) Throw exception if user sets network mode "host" with exposed ports
- [#8004](https://github.com/testcontainers/testcontainers-java/issues/8004) Lazy authentication in private registries before image pull
- [#10725](https://github.com/testcontainers/testcontainers-java/issues/10725) Make it possible to set `responseTimeout`
- [#9922](https://github.com/testcontainers/testcontainers-java/issues/9922) Allow custom host aliases in addition to `host.testcontainers.internal`

### Docker Compose
- [#7893](https://github.com/testcontainers/testcontainers-java/issues/7893) Image name variable substitution in Compose file not performed
- [#5687](https://github.com/testcontainers/testcontainers-java/issues/5687) Add exposing host port support to `DockerComposeContainer`
- [#5126](https://github.com/testcontainers/testcontainers-java/issues/5126) `DockerComposeContainer` doesn't report underlying issue on start failure
- [#7420](https://github.com/testcontainers/testcontainers-java/issues/7420) Create volumes for testcontainers

### Docker connectivity / environments
- [#9408](https://github.com/testcontainers/testcontainers-java/issues/9408) SSH support for remote docker_host
- [#5837](https://github.com/testcontainers/testcontainers-java/issues/5837) Support different docker context
- [#8167](https://github.com/testcontainers/testcontainers-java/issues/8167) Add ability to supply network to Ryuk container
- [#7964](https://github.com/testcontainers/testcontainers-java/issues/7964) Allow Ryuk in CI environments where Docker socket is unavailable
- [#8537](https://github.com/testcontainers/testcontainers-java/issues/8537) Support `TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE` read from env var
- [#5275](https://github.com/testcontainers/testcontainers-java/issues/5275) Improve OOB experience by consistently using full container names (Podman)
- [#9573](https://github.com/testcontainers/testcontainers-java/issues/9573) Allow varying the OS credential key and registry name patterns (Windows)

### Module-specific
- [#10718](https://github.com/testcontainers/testcontainers-java/issues/10718) Allow user configuration of `advertised.listeners` (Kafka)
- [#10451](https://github.com/testcontainers/testcontainers-java/issues/10451) Support InfluxDB v3
- [#8657](https://github.com/testcontainers/testcontainers-java/issues/8657) Presto module uses outdated image (`ghcr.io/trinodb/trino`)
- [#8575](https://github.com/testcontainers/testcontainers-java/issues/8575) Selenium/BrowserWebDriver: replace VNC recorder with CDP
- [#7098](https://github.com/testcontainers/testcontainers-java/issues/7098) Application Server Module
- [#4861](https://github.com/testcontainers/testcontainers-java/issues/4861) Kafka Connect container
- [#5050](https://github.com/testcontainers/testcontainers-java/issues/5050) Couchbase: support Scopes in bucket
- [#6427](https://github.com/testcontainers/testcontainers-java/issues/6427) Couchbase: change storageBackend or bucket type
- [#6662](https://github.com/testcontainers/testcontainers-java/issues/6662) Couchbase: modules use deprecated API calls
- [#8780](https://github.com/testcontainers/testcontainers-java/issues/8780) K6: add example test to docs
- [#8797](https://github.com/testcontainers/testcontainers-java/issues/8797) Add `getR2dbcUrl()` helper to `JdbcDatabaseContainer` *([PR #11499](https://github.com/testcontainers/testcontainers-java/pull/11499) open)*
- [#5728](https://github.com/testcontainers/testcontainers-java/issues/5728) MSSQL: honor additional properties on connection URL
- [#11231](https://github.com/testcontainers/testcontainers-java/issues/11231) MSSQL: support `GO` batch separator
- [#8634](https://github.com/testcontainers/testcontainers-java/issues/8634) Support running init script against a specific database
- [#7257](https://github.com/testcontainers/testcontainers-java/issues/7257) `ScriptUtils::runInitScript` should load script from filesystem
- [#5028](https://github.com/testcontainers/testcontainers-java/issues/5028) Allow setting env vars when using JDBC support
- [#4799](https://github.com/testcontainers/testcontainers-java/issues/4799) Postgres with default config doesn't work with mapped ports
- [#5359](https://github.com/testcontainers/testcontainers-java/issues/5359) PostgreSQL: not starting correctly if image already contains data
- [#9452](https://github.com/testcontainers/testcontainers-java/issues/9452) Set compatibility for `container-registry.oracle.com/mysql` images
- [#10360](https://github.com/testcontainers/testcontainers-java/issues/10360) `JdbcDatabaseContainer`: improve `waitUntilContainerStarted`

### JUnit integration
- [#8823](https://github.com/testcontainers/testcontainers-java/issues/8823) Containers declared with `@Container` cannot depend on each other
- [#6401](https://github.com/testcontainers/testcontainers-java/issues/6401) JUnit Jupiter extension for container reuse

### Observability / logging
- [#9876](https://github.com/testcontainers/testcontainers-java/issues/9876) Debug messages should use lazy argument evaluation
- [#6833](https://github.com/testcontainers/testcontainers-java/issues/6833) Reduce exceptions caused by `Unreliables.retryUntilTrue`
- [#10780](https://github.com/testcontainers/testcontainers-java/issues/10780) Improve logging for WaitStrategies

### Build / project
- [#7445](https://github.com/testcontainers/testcontainers-java/issues/7445) Add OWASP dependency check plugin
- [#6901](https://github.com/testcontainers/testcontainers-java/issues/6901) Publish GPG signing keys
- [#10449](https://github.com/testcontainers/testcontainers-java/issues/10449) Update documentation with `DockerModelRunner` example
- [#11122](https://github.com/testcontainers/testcontainers-java/issues/11122) Deploy relocation artifacts

---

## Possible User Error / Documentation Improvement

Issues that appear to be misunderstandings or configuration mistakes — may warrant documentation improvements rather than code changes:

| Issue | Reason |
|---|---|
| [#5286](https://github.com/testcontainers/testcontainers-java/issues/5286) | Can't connect to Private Docker Registry — waiting for info, likely auth config |
| [#4912](https://github.com/testcontainers/testcontainers-java/issues/4912) | Images pulled by anonymous user — likely auth config issue |
| [#5245](https://github.com/testcontainers/testcontainers-java/issues/5245) | Set order and waiting time for docker-compose containers — asking how to do it |
| [#5153](https://github.com/testcontainers/testcontainers-java/issues/5153) | Plans to move Docker credentials parsing — question/discussion |
| [#7785](https://github.com/testcontainers/testcontainers-java/issues/7785) | Newcomer example becomes troubleshooting — docs/UX friction, not a TC bug |
| [#9199](https://github.com/testcontainers/testcontainers-java/issues/9199) | Oracle Stored Procedure — appears to be SQL script formatting issue |

---

## Environment-Specific Issues

### Windows-only (~25 issues)
- [#11482](https://github.com/testcontainers/testcontainers-java/issues/11482) MalformedChunkCodingException (httpclient5 + Named Pipes)
- [#11236](https://github.com/testcontainers/testcontainers-java/issues/11236) Shaded jackson-databind / annotations version mismatch
- [#10365](https://github.com/testcontainers/testcontainers-java/issues/10365) Post-hook script not triggered on Windows
- [#10362](https://github.com/testcontainers/testcontainers-java/issues/10362) dockerConfigFile property not used
- [#10270](https://github.com/testcontainers/testcontainers-java/issues/10270) `BrowserWebDriverContainer` VncRecordingMode.RECORD_FAILING fails
- [#10257](https://github.com/testcontainers/testcontainers-java/issues/10257) Switch docker-java transport to httpclient5 (Java 21 issue on Windows)
- [#10191](https://github.com/testcontainers/testcontainers-java/issues/10191) GraalVM native image with mount fails
- [#8776](https://github.com/testcontainers/testcontainers-java/issues/8776) Init script UTF-8 encoding broken on Windows
- [#8773](https://github.com/testcontainers/testcontainers-java/issues/8773) Docker communication error using DOCKER_HOST localhost
- [#8370](https://github.com/testcontainers/testcontainers-java/issues/8370) Document/tooling for running TC on Windows natively
- [#8059](https://github.com/testcontainers/testcontainers-java/issues/8059) Can't create SQL procedure (line ending issue)
- [#6163](https://github.com/testcontainers/testcontainers-java/issues/6163) UTF-8 stdout corrupted on Windows
- [#5621](https://github.com/testcontainers/testcontainers-java/issues/5621) Windows Container (WCOW) support
- [#4696](https://github.com/testcontainers/testcontainers-java/issues/4696) BrowserWebDriverContainer Chrome fails on Windows 10
- (and more)

### macOS / Apple Silicon (~20 issues)
- [#11560](https://github.com/testcontainers/testcontainers-java/issues/11560) Docker 29.2.1 can't find valid docker environment
- [#11483](https://github.com/testcontainers/testcontainers-java/issues/11483) Global uncaught exception handler changed (Awaitility + ARM)
- [#11416](https://github.com/testcontainers/testcontainers-java/issues/11416) ArithmeticException in LoggedPullImageResultCallback (macOS CI)
- [#11222](https://github.com/testcontainers/testcontainers-java/issues/11222) Volume fails to mount (compose, ARM)
- [#11206](https://github.com/testcontainers/testcontainers-java/issues/11206) ScriptUtils mangles adjacent SQL string literals (ARM)
- [#10527](https://github.com/testcontainers/testcontainers-java/issues/10527) DockerImageName compatibility check fails with digest (ARM)
- [#10068](https://github.com/testcontainers/testcontainers-java/issues/10068) Container startup failed for docker:24.0.2 (ARM)
- [#9956](https://github.com/testcontainers/testcontainers-java/issues/9956) Can no longer get logs of container (ARM)
- [#9368](https://github.com/testcontainers/testcontainers-java/issues/9368) RedpandaContainer custom listener (ARM/macOS)
- [#9227](https://github.com/testcontainers/testcontainers-java/issues/9227) duct-tape thread leak (ARM)
- [#9220](https://github.com/testcontainers/testcontainers-java/issues/9220) Library source mismatch (ARM)
- [#9120](https://github.com/testcontainers/testcontainers-java/issues/9120) Deadlock between DockerClientFactory and RyukResourceReaper (ARM)
- [#8452](https://github.com/testcontainers/testcontainers-java/issues/8452) VNC recorder exits on macOS when closing pop-ups
- [#6420](https://github.com/testcontainers/testcontainers-java/issues/6420) Failed to close response (ARM)
- [#5246](https://github.com/testcontainers/testcontainers-java/issues/5246) MountableFile "group id '498'" error on macBook
- [#5101](https://github.com/testcontainers/testcontainers-java/issues/5101) [lima] Containers not removed after tests

### Podman-specific (~10 issues)
- [#11254](https://github.com/testcontainers/testcontainers-java/issues/11254) Can't overwrite Docker socket path
- [#10381](https://github.com/testcontainers/testcontainers-java/issues/10381) DOCKER_HOST env var not respected
- [#10362](https://github.com/testcontainers/testcontainers-java/issues/10362) dockerConfigFile not used
- [#8368](https://github.com/testcontainers/testcontainers-java/issues/8368) Building container inside another with mounts
- [#7593](https://github.com/testcontainers/testcontainers-java/issues/7593) execInContainer localhost:2375 failed to respond
- [#7241](https://github.com/testcontainers/testcontainers-java/issues/7241) Unable to mount file into running container
- [#7236](https://github.com/testcontainers/testcontainers-java/issues/7236) MySQL can't chown `/etc/mysql/conf.d` *(duplicate of [#9053](https://github.com/testcontainers/testcontainers-java/issues/9053))*
- [#7310](https://github.com/testcontainers/testcontainers-java/issues/7310) HTTP connection state issues
- [#5351](https://github.com/testcontainers/testcontainers-java/issues/5351) Podman with Docker Compose not working
- [#5475](https://github.com/testcontainers/testcontainers-java/issues/5475) Nested containers from a pod

### Kubernetes / CI (~10 issues)
- [#11139](https://github.com/testcontainers/testcontainers-java/issues/11139) DIND in Kubernetes pods
- [#10125](https://github.com/testcontainers/testcontainers-java/issues/10125) k3s fails to start in Concourse CI
- [#10037](https://github.com/testcontainers/testcontainers-java/issues/10037) Podman as sidecar in K8s
- [#9605](https://github.com/testcontainers/testcontainers-java/issues/9605) Pods not starting in K3sContainer *([PR #11420](https://github.com/testcontainers/testcontainers-java/pull/11420) open)*
- [#7958](https://github.com/testcontainers/testcontainers-java/issues/7958) Doesn't work when container runtime needs special config
- [#7086](https://github.com/testcontainers/testcontainers-java/issues/7086) Can't run tests on K8s 1.24 without Docker CR
- [#6807](https://github.com/testcontainers/testcontainers-java/issues/6807) Failing in Jenkins Agent running as container
- [#6402](https://github.com/testcontainers/testcontainers-java/issues/6402) MongoDB container not starting (K8s/CI)
- [#6623](https://github.com/testcontainers/testcontainers-java/issues/6623) VaultContainer doesn't work on GitLab

---

## Confirmed Bugs — Candidate Failing Tests

Ranked by ease of writing a reliable failing test.

### Very Easy — Pure unit tests, no Docker required

#### [#7326](https://github.com/testcontainers/testcontainers-java/issues/7326) — `HttpWaitStrategy` strips newlines from response body
- **Bug**: `getResponseBody()` uses `BufferedReader.readLine()` and concatenates lines without any separator. The `responsePredicate` receives a string with all newlines removed. This breaks predicates relying on line-based formats (e.g. Prometheus text).
- **Fix**: Append `\n` between lines (one-liner).
- **Test**: Unit test with a minimal HTTP server returning a multi-line body; assert the predicate receives the correct content including newlines.

#### [#11206](https://github.com/testcontainers/testcontainers-java/issues/11206) — `ScriptUtils` collapses adjacent SQL string literals onto one line
- **Bug**: Adjacent string literals separated by a newline (valid PostgreSQL syntax: `IS 'Sentence. '\n    'continued'`) are collapsed to a single line, producing invalid SQL.
- **Fix**: Preserve newlines in the script parser.
- **Test**: Extend existing `ScriptUtils` unit tests with an adjacent-literal SQL fragment; assert it survives `splitSqlScript` unchanged.

#### [#10359](https://github.com/testcontainers/testcontainers-java/issues/10359) — `MariaDBContainer.getLivenessCheckPortNumbers()` returns internal port
- **Bug**: Returns the container-internal port (3306) instead of the mapped host port. `HostPortWaitStrategy` then checks an unreachable port.
- **Fix**: Return `getMappedPort(3306)` instead of the hardcoded internal port.
- **Test**: Start a `MariaDBContainer`, call `getLivenessCheckPortNumbers()`, assert the value equals `getMappedPort(3306)`.

#### [#8590](https://github.com/testcontainers/testcontainers-java/issues/8590) — `withStartupTimeoutSeconds(int)` silently ignored
- **Bug**: The method sets a field that is never wired through to the `WaitStrategy`'s timeout. The container still uses the default 60-second timeout.
- **Fix**: Propagate the value to the wait strategy.
- **Test**: Call `withStartupTimeoutSeconds(180)` on a container; inspect the resulting wait strategy and assert its configured timeout is 180 seconds.

### Easy — Integration tests, Docker required

#### [#11492](https://github.com/testcontainers/testcontainers-java/issues/11492) — NPE in `ResourceReaper.stopAndRemoveContainer` when container never started
- **Bug**: `ConcurrentHashMap.remove(null)` is called when `stop()` is invoked on a container that failed to start (container ID is null).
- **Fix**: Null-guard before the `remove()` call.
- **Test**: Trigger a container startup failure (invalid image name), then call `stop()` — assert no `NullPointerException`.

#### [#9227](https://github.com/testcontainers/testcontainers-java/issues/9227) — `duct-tape:1.0.8` leaks daemon threads
- **Bug**: `Timeouts.doWithTimeout(...)` uses a static `CachedThreadPool` that is never shut down. Threads named `ducttape-N` persist indefinitely. Detected by thread-leak-aware test frameworks.
- **Fix**: Replace or internalize `duct-tape` (track this against the dependency replacement work).
- **Test**: Enumerate live threads; call any API path that uses `Timeouts.doWithTimeout`; assert no `ducttape-*` threads remain after completion.

#### [#10035](https://github.com/testcontainers/testcontainers-java/issues/10035) — `KafkaContainer.withListener()` hardcodes `PLAINTEXT` protocol
- **Bug**: `KafkaHelper.resolveListeners()` always writes `TC-<N>:PLAINTEXT` into `KAFKA_LISTENER_SECURITY_PROTOCOL_MAP`, ignoring the user-specified protocol (e.g. `SASL_PLAINTEXT`).
- **Fix**: Respect the protocol from the listener configuration.
- **Test**: Configure a `KafkaContainer` with a custom listener using a non-PLAINTEXT protocol; inspect the resulting env var and assert `PLAINTEXT` was not injected for that listener.

#### [#11241](https://github.com/testcontainers/testcontainers-java/issues/11241) — `TESTCONTAINERS_CHECKS_DISABLE=true` still pulls the Alpine check image
- **Bug**: Setting the env var to disable startup checks does not prevent the Alpine image pull that is part of those checks.
- **Test**: Set `TESTCONTAINERS_CHECKS_DISABLE=true`; start any container; assert the Alpine image was never pulled (intercept `pullImageCmd` or check audit logs).

### Moderate — Integration tests with more setup

#### [#11415](https://github.com/testcontainers/testcontainers-java/issues/11415) — `withLocalCompose(false)` regression (method removed, no migration path)
- **Bug**: `ComposeContainer.withLocalCompose(boolean)` was removed in [PR #9871](https://github.com/testcontainers/testcontainers-java/pull/9871), hardcoding `localCompose=true`. Users who relied on containerised Docker Compose (no host binary required) are broken.
- **Test**: Construct a `ComposeContainer` without a host `docker` binary available; assert it starts via the containerised path.

#### [#9575](https://github.com/testcontainers/testcontainers-java/issues/9575) — `ImageNameSubstitutor` cannot set compatibility on substituted image
- **Bug**: Custom substitutor implementations cannot call `asCompatibleSubstituteFor()` on the returned image. Compatibility is checked against the constructor arg, not the returned image.
- **Test**: Implement a custom substitutor that maps image A to image B; assert compatibility check passes.

#### [#6413](https://github.com/testcontainers/testcontainers-java/issues/6413) — `TESTCONTAINERS_HUB_IMAGE_NAME_PREFIX` incorrectly applied to `ImageFromDockerfile` names
- **Bug**: A locally-built image named e.g. `redis-sentinel` has the hub prefix applied when it is run, even though it is not a Docker Hub image.
- **Test**: Set a hub prefix; build an image via `ImageFromDockerfile` with an explicit name; assert the container starts without prefix-substitution errors.

#### [#6932](https://github.com/testcontainers/testcontainers-java/issues/6932) — `DockerComposeContainer` services cannot resolve `host.testcontainers.internal`
- **Bug**: `Testcontainers.exposeHostPorts()` only works for `GenericContainer`; Docker Compose services are not joined to the Testcontainers network and receive no `extra_hosts` injection.
- **Test**: Expose a host port; start a compose service that connects back to `host.testcontainers.internal` on that port; assert the connection succeeds.

#### [#5868](https://github.com/testcontainers/testcontainers-java/issues/5868) — `LogConsumer` silently stops after a compose service restarts
- **Bug**: The log-following mechanism is attached once at startup and not re-attached after the container process restarts. Log output after a restart is lost.
- **Test**: Start a compose service with `restart: always` that exits immediately; attach a `LogConsumer`; assert log output is received both before and after restart.

#### [#6310](https://github.com/testcontainers/testcontainers-java/issues/6310) — `TrinoContainer` reports ready before worker nodes are available
- **Bug**: The JDBC readiness check returns success after `statement.execute()` but before the `ResultSet` can be consumed. Subsequent queries fail with "No nodes available".
- **Test**: Start a `TrinoContainer`; immediately after `start()` run a real query; assert it succeeds (currently flaky/fails).

---

*Last updated: 2026-03-06*

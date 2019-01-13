package generic;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.junit.Assert.assertTrue;

public class WaitStrategiesTest {

    @Rule
    // waitForNetworkListening {
    public GenericContainer nginx = new GenericContainer("nginx:1.9.4")
        .withExposedPorts(80);
    // }

    @Rule
    // waitForSimpleHttp {
    public GenericContainer nginxWithHttpWait = new GenericContainer("nginx:1.9.4")
        .withExposedPorts(80)
        .waitingFor(Wait.forHttp("/"));
    // }

    private static final HttpWaitStrategy MULTI_CODE_HTTP_WAIT =
    // waitForHttpWithMultipleStatusCodes {
        Wait.forHttp("/")
            .forStatusCode(200)
            .forStatusCode(301)
    // }
        ;

    private static final HttpWaitStrategy PREDICATE_HTTP_WAIT =
    // waitForHttpWithStatusCodePredicate {
        Wait.forHttp("/all")
            .forStatusCodeMatching(it -> it >= 200 && it < 300 || it == 401)
    // }
        ;

    private static final HttpWaitStrategy TLS_HTTP_WAIT =
    // waitForHttpWithTls {
        Wait.forHttp("/all")
            .usingTls()
    // }
        ;

    @Test
    public void testContainersAllStarted() {
        assertTrue(nginx.isRunning());
        assertTrue(nginxWithHttpWait.isRunning());
    }
}

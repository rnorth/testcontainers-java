package generic;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

public class MultiplePortsExposedTest {

    @Rule
    // rule {
    public GenericContainer container = new GenericContainer("nats:1.3.0")
        .withExposedPorts(4222, 8222);
    // }

    @Test
    public void fetchPortsByNumber() {
        Integer firstMappedPort = container.getMappedPort(4222);
        Integer secondMappedPort = container.getMappedPort(8222);
    }

    @Test
    public void fetchFirstMappedPort() {
        Integer firstMappedPort = container.getFirstMappedPort();
    }
}

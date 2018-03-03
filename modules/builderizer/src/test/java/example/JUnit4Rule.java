package example;

import org.testcontainers.containercore.builderizer.BuildWrapper;
import org.testcontainers.containercore.builderizer.Wrappable;

/**
 * TODO: Javadocs
 */
public class JUnit4Rule implements BuildWrapper, Container {

    private final Container container;

    public JUnit4Rule(Wrappable container) {
        this.container = (Container) container;
    }

    @Override
    public void start() {
        container.start();
    }

    @Override
    public void stop() {
container.stop();
    }

    @Override
    public void exec(String... cmd) {
container.exec(cmd);
    }
}

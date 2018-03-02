package example;

import org.testcontainers.containercore.builderizer.BuildWrapper;
import org.testcontainers.containercore.builderizer.Wrappable;

/**
 * TODO: Javadocs
 */
public class JUnit4Rule implements BuildWrapper {

    private final Container wrappedContainer;

    public JUnit4Rule(Wrappable wrappedContainer) {
        this.wrappedContainer = (Container) wrappedContainer;
    }

    @Override
    public String toString() {
        return "JUnit4Rule{" +
            "wrappedContainer=" + wrappedContainer +
            '}';
    }

    public void doSomeMethod() {
        wrappedContainer.someMethod();
    }
}

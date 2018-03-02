package example;

import org.testcontainers.containercore.builderizer.Buildable;

/**
 * TODO: Javadocs
 */
@Buildable
public abstract class Parent implements Container {
    protected String foo;

    @Override
    public String toString() {
        return "Parent{" +
            "foo='" + foo + '\'' +
            '}';
    }

    public void someMethod() {
        System.err.println("someMethod");
    }
}


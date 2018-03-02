package example;

import org.testcontainers.containercore.builderizer.Buildable;

/**
 * TODO: Javadocs
 */
@Buildable
public abstract class Parent {
    protected String foo;

    @Override
    public String toString() {
        return "Parent{" +
            "foo='" + foo + '\'' +
            '}';
    }
}


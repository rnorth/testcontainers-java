package example;

import org.testcontainers.containercore.builderizer.Buildable;

@Buildable
public abstract class Child extends Parent {
    protected String bar;
}

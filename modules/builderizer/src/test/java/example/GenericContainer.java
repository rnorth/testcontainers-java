package example;

import org.testcontainers.containercore.builderizer.Buildable;
import org.testcontainers.containercore.builderizer.Wrappable;

import java.util.Arrays;

/**
 * TODO: Javadocs
 */
@Buildable
public abstract class GenericContainer implements Wrappable, Container {
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


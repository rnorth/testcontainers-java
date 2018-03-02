package example;

import org.junit.Test;

/**
 * TODO: Javadocs
 */
public class SimpleTest {

    @Test
    public void name() {
        final Child child = ChildBuilder.newBuilder()
            .withBar("bar")
            .withFoo("foo")
            .build();

        final Parent parent = ParentBuilder.newBuilder()
            .withFoo("foo")
            .build();

        System.err.println(child);
        System.err.println(parent);
    }
}

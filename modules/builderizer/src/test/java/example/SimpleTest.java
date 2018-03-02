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

        System.err.println(child);
    }
}

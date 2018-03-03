package example.usage;

import example.Child;
import example.ChildBuilder;
import example.JUnit4Rule;
import example.Parent;
import example.ParentBuilder;
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


        final JUnit4Rule rule = ChildBuilder.newBuilder()
            .withBar("bar")
            .withFoo("foo")
            .buildAs(JUnit4Rule.class);

        child.someMethod();
        parent.someMethod();

        rule.doSomeMethod();

        System.err.println(child);
        System.err.println(parent);
        System.err.println(rule);
    }
}

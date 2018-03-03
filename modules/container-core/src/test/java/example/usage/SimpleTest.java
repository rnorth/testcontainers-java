package example.usage;

import example.JUnit4Rule;
import example.specialized.SpecializedContainer;
import example.specialized.SpecializedContainerBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containercore.core.GenericContainer;
import org.testcontainers.containercore.core.GenericContainerBuilder;

/**
 * TODO: Javadocs
 */
public class SimpleTest {

    private final SpecializedContainer child = SpecializedContainerBuilder.newBuilder()
        .withName("bar")
        .withSpecializedOnlyProperty("foo")
        .build();

    private final GenericContainer parent = GenericContainerBuilder.newBuilder()
        .withName("foo")
        .build();

    @Rule
    public JUnit4Rule rule = JUnit4Rule.rule(parent, child);


    @Test
    public void name() {
        child.exec("foobar");
        parent.exec("foobar");

        System.err.println(child);
        System.err.println(parent);
        System.err.println(rule);
    }
}

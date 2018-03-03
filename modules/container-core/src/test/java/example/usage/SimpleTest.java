package example.usage;

import org.testcontainers.containercore.core.GenericContainer;
import example.GenericContainerBuilder;
import example.JUnit4Rule;
import example.SpecializedContainer;
import example.KafkaContainerBuilder;
import org.junit.Rule;
import org.junit.Test;

import static example.JUnit4Rule.rule;

/**
 * TODO: Javadocs
 */
public class SimpleTest {

    private final SpecializedContainer child = KafkaContainerBuilder.newBuilder()
        .withName("bar")
        .withKafkaOnlyProperty("foo")
        .build();

    private final GenericContainer parent = GenericContainerBuilder.newBuilder()
        .withName("foo")
        .build();

    @Rule
    public JUnit4Rule rule = rule(parent, child);


    @Test
    public void name() {
        child.exec("foobar");
        parent.exec("foobar");

        System.err.println(child);
        System.err.println(parent);
        System.err.println(rule);
    }
}

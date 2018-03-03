package example.usage;

import example.GenericContainer;
import example.GenericContainerBuilder;
import example.JUnit4Rule;
import example.KafkaContainer;
import example.KafkaContainerBuilder;
import org.junit.Test;

/**
 * TODO: Javadocs
 */
public class SimpleTest {

    @Test
    public void name() {
        final KafkaContainer child = KafkaContainerBuilder.newBuilder()
            .withName("bar")
            .withKafkaOnlyProperty("foo")
            .build();

        final GenericContainer parent = GenericContainerBuilder.newBuilder()
            .withName("foo")
            .build();


        final JUnit4Rule rule = KafkaContainerBuilder.newBuilder()
            .withName("bar")
            .withKafkaOnlyProperty("foo")
            .buildAs(JUnit4Rule.class);

        child.start();
        parent.start();

        child.exec("foobar");
        parent.exec("foobar");

        rule.start();


        System.err.println(child);
        System.err.println(parent);
        System.err.println(rule);
    }
}

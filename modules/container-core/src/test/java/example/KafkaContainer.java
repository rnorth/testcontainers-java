package example;

import org.testcontainers.containercore.builderprocessor.ContainerBuilderDSL;

@ContainerBuilderDSL
public class KafkaContainer extends GenericContainer {
    protected String kafkaOnlyProperty;

    protected KafkaContainer() {
    }

    @Override
    public String toString() {
        return "KafkaContainer{" +
            "kafkaOnlyProperty='" + kafkaOnlyProperty + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}

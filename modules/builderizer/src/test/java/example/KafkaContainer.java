package example;

import org.testcontainers.containercore.builderizer.ContainerBuilderDSL;

@ContainerBuilderDSL
public abstract class KafkaContainer extends GenericContainer {
    protected String kafkaOnlyProperty;

    @Override
    public String toString() {
        return "KafkaContainer{" +
            "kafkaOnlyProperty='" + kafkaOnlyProperty + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}

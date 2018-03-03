package example;

import org.testcontainers.containercore.builderprocessor.ContainerBuilderDSL;
import org.testcontainers.containercore.core.GenericContainer;

@ContainerBuilderDSL
public class SpecializedContainer extends GenericContainer {
    protected String specializedOnlyProperty;

    protected SpecializedContainer() {
    }

    @Override
    public String toString() {
        return "SpecializedContainer{" +
            "specializedOnlyProperty='" + specializedOnlyProperty + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}

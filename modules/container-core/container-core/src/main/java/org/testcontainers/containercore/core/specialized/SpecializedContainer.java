package org.testcontainers.containercore.core.specialized;

import org.testcontainers.containercore.builderprocessor.ContainerBuilderDSL;
import org.testcontainers.containercore.core.GenericContainer;

@ContainerBuilderDSL
public abstract class SpecializedContainer extends GenericContainer {
    protected String specializedOnlyProperty;

    @Override
    public String toString() {
        return "SpecializedContainer{" +
            "specializedOnlyProperty='" + specializedOnlyProperty + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}

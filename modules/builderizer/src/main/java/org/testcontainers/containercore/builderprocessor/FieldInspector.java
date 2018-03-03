package org.testcontainers.containercore.builderprocessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Javadocs
 */
public class FieldInspector {

    private final Types typeUtils;

    public FieldInspector(Types typeUtils) {
        this.typeUtils = typeUtils;
    }

    public List<VariableElement> getFieldsOnClassOrSuperclass(TypeElement targetClass) {
        List<VariableElement> results = new ArrayList<>();

        for (Element field : targetClass.getEnclosedElements()) {
            if (field.getKind() == ElementKind.FIELD &&
                !field.getModifiers().contains(Modifier.STATIC) &&
                !field.getModifiers().contains(Modifier.PRIVATE)) {
                results.add((VariableElement) field);
            }
        }

        final TypeElement superClass = ((TypeElement) typeUtils.asElement(targetClass.getSuperclass()));
        if (!superClass.toString().equals(Object.class.getName())) {
            results.addAll(getFieldsOnClassOrSuperclass(superClass));
        }

        return results;
    }
}

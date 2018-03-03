package org.testcontainers.containercore.builderizer;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO: Javadocs
 */
public class Generator {

    private final Messager messager;
    private final Types typeUtils;
    private final Filer filer;

    public Generator(Messager messager, Types typeUtils, Filer filer) {
        this.messager = messager;
        this.typeUtils = typeUtils;
        this.filer = filer;
    }

    public void generate(Element element) throws IOException {

        TypeElement targetClass = (TypeElement) element;

        List<VariableElement> fields = new ArrayList<>(getFieldsOnClassOrSuperclass(targetClass));

        final ClassName targetClassName = ClassName.get(targetClass);
        generateBuilderClass(targetClassName, fields);
    }

    private void presentError(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private Collection<? extends VariableElement> getFieldsOnClassOrSuperclass(TypeElement targetClass) {
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

    private void generateBuilderClass(ClassName targetClassName, List<VariableElement> fields) throws IOException {

        final String builderClassName = targetClassName.simpleName() + "Builder";
        final ClassName builderTypeClassName = ClassName.get(targetClassName.packageName(), builderClassName);

        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(builderClassName)
            .addAnnotation(generatedAnnotation())
            .addModifiers(Modifier.PUBLIC);

        builderClassBuilder.addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .build());

        builderClassBuilder.addMethod(MethodSpec.methodBuilder("newBuilder")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(builderTypeClassName)
            .addStatement("return new $T()", builderTypeClassName)
            .build());

        final MethodSpec.Builder buildMethodBuilder = MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC)
            .returns(targetClassName)
            .addStatement("$T instance = new $T()", targetClassName, targetClassName);

        for (VariableElement attribute : fields) {
            final String fieldName = attribute.getSimpleName().toString();
            final TypeName fieldType = TypeName.get(attribute.asType());

            final String setterName = "with" +
                fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1);

            builderClassBuilder.addField(FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE).build());

            builderClassBuilder.addMethod(MethodSpec.methodBuilder(setterName)
                .addModifiers(Modifier.PUBLIC)
                .returns(builderTypeClassName)
                .addParameter(fieldType, fieldName)
                .addStatement("this.$1L = $1L", fieldName)
                .addStatement("return this")
                .build());

            buildMethodBuilder.addStatement("instance.$1L = this.$1L", fieldName);
        }

        builderClassBuilder.addMethod(buildMethodBuilder
            .addStatement("return instance")
            .build());

        final JavaFile javaFile = JavaFile.builder(targetClassName.packageName(), builderClassBuilder.build())
            .build();

        javaFile.writeTo(filer);
    }

    private AnnotationSpec generatedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", this.getClass().getCanonicalName())
            .build();
    }
}


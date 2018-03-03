package org.testcontainers.containercore.builderprocessor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.List;

import static javax.lang.model.element.Modifier.*;

/**
 * TODO: Javadocs
 */
public class Generator {

    private final Filer filer;

    public Generator(Filer filer) {
        this.filer = filer;
    }

    public void generate(TypeElement element, List<VariableElement> fields) throws IOException {
        final ClassName targetClassName = ClassName.get(element);
        final String builderClassName = targetClassName.simpleName() + "Builder";
        final String packageName = targetClassName.packageName();
        final ClassName builderTypeClassName = ClassName.get(packageName, builderClassName);

        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(builderClassName)
            .addAnnotation(generatedAnnotation())
            .addModifiers(PUBLIC);

        final MethodSpec.Builder buildMethodBuilder = MethodSpec.methodBuilder("build")
            .addModifiers(PUBLIC)
            .returns(targetClassName)
            .addStatement("$T instance = new $T()", targetClassName, targetClassName);

        for (VariableElement field : fields) {
            final String fieldName = field.getSimpleName().toString();
            final TypeName fieldType = TypeName.get(field.asType());

            builderClassBuilder.addField(FieldSpec.builder(fieldType, fieldName, PRIVATE).build());

            final String setterName = setterName(fieldName);

            builderClassBuilder.addMethod(MethodSpec.methodBuilder(setterName)
                .addModifiers(PUBLIC)
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

        builderClassBuilder.addMethod(privateConstructor());
        builderClassBuilder.addMethod(builderStaticFactoryMethod(builderTypeClassName));

        final JavaFile javaFile = JavaFile.builder(packageName, builderClassBuilder.build())
            .build();

        javaFile.writeTo(filer);
    }

    private String setterName(String fieldName) {
        return "with" +
            fieldName.substring(0, 1).toUpperCase() +
            fieldName.substring(1);
    }

    private MethodSpec privateConstructor() {
        return MethodSpec.constructorBuilder()
            .addModifiers(PRIVATE)
            .build();
    }

    private MethodSpec builderStaticFactoryMethod(ClassName builderTypeClassName) {
        return MethodSpec.methodBuilder("newBuilder")
            .addModifiers(PUBLIC, STATIC)
            .returns(builderTypeClassName)
            .addStatement("return new $T()", builderTypeClassName)
            .build();
    }

    private AnnotationSpec generatedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", this.getClass().getCanonicalName())
            .build();
    }
}


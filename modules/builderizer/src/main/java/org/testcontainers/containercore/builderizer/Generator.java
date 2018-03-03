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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.List;

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


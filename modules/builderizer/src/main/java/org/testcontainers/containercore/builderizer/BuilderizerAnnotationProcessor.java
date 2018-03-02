package org.testcontainers.containercore.builderizer;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * TODO: Javadocs
 */
public class BuilderizerAnnotationProcessor extends AbstractProcessor {

    private Messager messager;
    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Buildable.class)) {
            try {
                generateBuilder(annotatedElement);
            } catch (Exception e) {
                presentError(annotatedElement, e.getMessage());
                return true;
            }
        }

        return true;
    }

    private void presentError(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Buildable.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void generateBuilder(Element element) throws IOException {

        TypeElement targetClass = (TypeElement) element;

        List<VariableElement> fields = new ArrayList<>(getFieldsOnClassOrSuperclass(targetClass));

        final ClassName targetClassName = ClassName.get(targetClass);
        final ClassName concreteClassName = generateConcreteClass(targetClassName);
        generateBuilderClass(targetClassName, concreteClassName, fields);
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

    private ClassName generateConcreteClass(ClassName targetClass) throws IOException {
        final String concreteClassNameString = targetClass.simpleName() + "Concrete";
        final ClassName concreteClassName = ClassName.get(targetClass.packageName(), concreteClassNameString);

        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(concreteClassName)
            .addAnnotation(generatedAnnotation())
            .superclass(targetClass);

        final TypeSpec concreteClass = builderClassBuilder.build();
        final JavaFile javaFile = JavaFile.builder(targetClass.packageName(), concreteClass)
            .build();

        javaFile.writeTo(filer);

        return concreteClassName;
    }

    private void generateBuilderClass(ClassName targetClassName, ClassName concreteClassName, List<VariableElement> fields) throws IOException {

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

        builderClassBuilder.addMethod(MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC)
            .returns(targetClassName)
            .addStatement("return new $T()", concreteClassName)
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

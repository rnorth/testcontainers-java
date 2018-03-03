package org.testcontainers.containercore.builderprocessor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * TODO: Javadocs
 */
public class DSLAnnotationProcessor extends AbstractProcessor {

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
        final Generator generator = new Generator(filer);
        final FieldInspector fieldInspector = new FieldInspector(typeUtils);
        for (Element element : roundEnv.getElementsAnnotatedWith(ContainerBuilderDSL.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                presentError(element, ContainerBuilderDSL.class.getSimpleName() + " may only be used on classes");
                return true;
            }

            final TypeElement typeElement = (TypeElement) element;

            try {
                final List<VariableElement> fields = fieldInspector.getFieldsOnClassOrSuperclass(typeElement);
                generator.generate(typeElement, fields);
            } catch (Exception e) {
                presentError(element, e.getMessage());
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
        return Collections.singleton(ContainerBuilderDSL.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}

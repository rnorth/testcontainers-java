package org.testcontainers.containercore.builderizer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Collections;
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
        final Generator generator = new Generator(messager, typeUtils, filer);
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ContainerBuilderDSL.class)) {
            try {
                generator.generate(annotatedElement);
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
        return Collections.singleton(ContainerBuilderDSL.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}

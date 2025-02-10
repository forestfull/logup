package com.forestfull.logger.util;

import com.forestfull.lombok.ObservableArguments;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.forestfull.lombok.ObservableArguments")
@SupportedSourceVersion(SourceVersion.RELEASE_6) // JDK 1.6 이상 지원
public class LombokProcessor extends AbstractProcessor  {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element element : roundEnv.getElementsAnnotatedWith(ObservableArguments.class)) {
            if (element instanceof ExecutableElement) {
                ExecutableElement methodElement = (ExecutableElement) element;
                messager.printMessage(Diagnostic.Kind.NOTE,
                        "[KoLogger] Processing method: " + methodElement.getSimpleName());
            }
        }
        return true;
    }
}
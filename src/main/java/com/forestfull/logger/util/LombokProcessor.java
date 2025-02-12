package com.forestfull.logger.util;

import com.forestfull.lombok.ObservableArguments;
import com.forestfull.lombok.ObservableReturnValue;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class LombokProcessor extends AbstractProcessor  {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final HashSet<String> supportedAnnotationTypes = new HashSet<String>();
        supportedAnnotationTypes.add(ObservableArguments.class.getName());
        supportedAnnotationTypes.add(ObservableReturnValue.class.getName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_6;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ObservableArguments.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing: " + element.toString());
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(ObservableReturnValue.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing: " + element.toString());
        }
        return true;
    }
}
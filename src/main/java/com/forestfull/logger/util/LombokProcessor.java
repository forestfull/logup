package com.forestfull.logger.util;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.forestfull.lombok.ObservableArguments", "com.forestfull.lombok.ObservableReturnValue"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class LombokProcessor extends AbstractProcessor  {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("LombokProcessor");
        return true;
    }
}
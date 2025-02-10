package com.forestfull.logger.util;

import com.forestfull.logger.annotation.ObservableArguments;
import com.forestfull.logger.annotation.ObservableReturnValue;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes(value = {"com.forestfull.logger.annotation.ObservableArguments", "com.forestfull.logger.annotation.ObservableReturnValue"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class KoLoggerAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                // 어노테이션 처리 로직 추가
                System.out.println("Custom annotation detected: " + element.getSimpleName());
            }
        }

        Set<? extends Element> argumentsAnnotationClass = roundEnv.getElementsAnnotatedWith(ObservableArguments.class);
        Set<? extends Element> returnValueAnnotationClass = roundEnv.getElementsAnnotatedWith(ObservableReturnValue.class);
        return true;
    }
}

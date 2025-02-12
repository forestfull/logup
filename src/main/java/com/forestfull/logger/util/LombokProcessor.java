package com.forestfull.logger.util;

import com.forestfull.lombok.ObservableArguments;
import com.forestfull.lombok.ObservableReturnValue;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes({"com.forestfull.lombok.ObservableArguments", "com.forestfull.lombok.ObservableReturnValue"})
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
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "hi");
        System.out.println("hi");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ObservableArguments.class);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Magic annotation을 " + element.getSimpleName() + "에서는 사용할 수 없습니다.");
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + element.getSimpleName());
            }

            TypeElement typeElement = (TypeElement) element;
            ClassName className = ClassName.get(typeElement);

            // 메서드 먼저 만들기
            MethodSpec pullOut = MethodSpec.methodBuilder("pullOut")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", "Rabbit!")
                    .build();

            // class 만들기
            TypeSpec hatFactory = TypeSpec.classBuilder("HatFactory")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(className)
                    .addMethod(pullOut)
                    .build();
            // 여기 까지하면 메모리상에 객체로 정의한것

            // 소스파일로 만들기
            Filer filer = processingEnv.getFiler();
            try {
                JavaFile.builder(className.packageName(), hatFactory)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "[FATAL ERROR]: " + e.getMessage());
            }
        }
        return true;
    }
}
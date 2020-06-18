package io.tinypiggy.tools.processor;

import io.tinypiggy.tools.annotation.MyTag;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes(value = {"io.tinypiggy.tools.annotation.MyTag"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        System.out.println("====== enter annotation processor ======");
        for (Element element : roundEnvironment.getElementsAnnotatedWith(MyTag.class)){
            if (element.getKind() == ElementKind.CLASS){
                TypeElement typeElement = (TypeElement)element;
                System.out.println(typeElement.getSimpleName());
            }
        }
        return false;
    }
}

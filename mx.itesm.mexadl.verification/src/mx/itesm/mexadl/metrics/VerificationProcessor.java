package mx.itesm.mexadl.metrics;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("mx.itesm.mexadl.*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class VerificationProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment environment) {
        Messager messager;
        TypeElement typeElement;
        MaintainabilityMetrics maintainabilityMetrics;

        messager = processingEnv.getMessager();
        for (TypeElement annotation : annotations) {
            for (Element element : environment.getElementsAnnotatedWith(annotation)) {
                typeElement = ((TypeElement) element);
                maintainabilityMetrics = typeElement.getAnnotation(MaintainabilityMetrics.class);
                messager.printMessage(Kind.NOTE, "" + typeElement);
                for(Method method : maintainabilityMetrics.getClass().getDeclaredMethods()) {
                    System.out.println(method.getName());
                }
            }
        }

        return true;
    }

}
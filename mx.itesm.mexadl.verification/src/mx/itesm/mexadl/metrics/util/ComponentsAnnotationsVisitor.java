package mx.itesm.mexadl.metrics.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * The ComponentsAnnotationsVisitor class is responsible for obtaining the
 * Component Types associated to the different components of a system.
 * 
 * @author jccastrejon
 * 
 */
public class ComponentsAnnotationsVisitor extends EmptyVisitor {

    /**
     * Name of the class being analyzed.
     */
    private String componentName;

    /**
     * Component type of the class being analyzed.
     */
    private String componentType;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.componentName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        // Recognize only MexADL component annotations
        if (Type.getType(desc).getClassName().contains("mx.itesm.mexadl.components")) {
            this.componentType = Type.getType(desc).getClassName().replace("mx.itesm.mexadl.components.Component_", "");
        }

        return null;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentType() {
        return componentType;
    }

}

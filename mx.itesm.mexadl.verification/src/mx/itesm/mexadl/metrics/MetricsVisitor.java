/*
 * Copyright 2010 jccastrejon
 *  
 * This file is part of MexADL.
 *
 * MexADL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MexADL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with MexADL.  If not, see <http://www.gnu.org/licenses/>.
*/
package mx.itesm.mexadl.metrics;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * The MetricsVisitor class is responsible of obtaining the quality metrics
 * information associated to java classes.
 * 
 * @author jccastrejon
 * 
 */
public class MetricsVisitor extends EmptyVisitor {

    /**
     * Name of the current annotation being analyzed.
     */
    private String currentAnnotation;

    /**
     * Type being analyzed.
     */
    private String type;

    /**
     * Quality metrics associated to the type being analyzed.
     */
    private Map<String, Map<String, Object>> metrics;

    /**
     * Default constructor.
     */
    public MetricsVisitor() {
        this.metrics = new HashMap<String, Map<String, Object>>();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.type = name.replace('/', '.');
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        AnnotationVisitor returnValue;

        returnValue = null;
        if (Type.getType(desc).getClassName().equals(MaintainabilityMetrics.class.getName())) {
            returnValue = this;
            currentAnnotation = MaintainabilityMetrics.class.getName();
        }

        return returnValue;
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String desc) {
        this.currentAnnotation = name;
        return this;
    }

    @Override
    public void visit(final String name, final Object value) {
        this.getAnnotationMetrics(currentAnnotation).put(name, value);
    }

    @Override
    public void visitEnum(final String name, final String desc, final String value) {
        this.visit(name, value);
    }

    /**
     * Get the quality metrics associated to a given annotation.
     * 
     * @param name
     */
    private Map<String, Object> getAnnotationMetrics(final String name) {
        Map<String, Object> returnValue;

        returnValue = this.metrics.get(name);
        if (returnValue == null) {
            returnValue = new HashMap<String, Object>();
            this.metrics.put(name, returnValue);
        }

        return returnValue;
    }

    // Getters

    /**
     * @return
     */
    public Map<String, Map<String, Object>> getMetrics() {
        return this.metrics;
    }

    /**
     * @return
     */
    public String getType() {
        return this.type;
    }
}
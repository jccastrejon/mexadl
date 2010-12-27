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
package mx.itesm.mexadl.metrics.tools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

/**
 * Retrieve quality metrics from <a href="http://pmd.sourceforge.net/">PMD</a>
 * results.
 * 
 * @author jccastrejon
 * 
 */
public class PmdMetrics extends XmlFileMetrics {

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Map<String, Integer>> doGetMetrics(final List<Element> elements, final File resultsDir)
            throws Exception {
        int currentDuplicationLines;
        Map<String, Integer> classMetrics;
        Map<String, Map<String, Integer>> returnValue;

        returnValue = new HashMap<String, Map<String, Integer>>(elements.size());
        for (Element duplication : elements) {
            for (Element file : (List<Element>) duplication.getChildren("file")) {
                classMetrics = returnValue.get(file.getText());
                if (classMetrics == null) {
                    currentDuplicationLines = 0;
                    classMetrics = new HashMap<String, Integer>();
                    returnValue.put(file.getText(), classMetrics);
                } else {
                    currentDuplicationLines = classMetrics.get("duplication");
                }

                // Aggregate the number of duplicated lines in a class
                currentDuplicationLines += Integer.parseInt(duplication.getAttributeValue("lines"));
                classMetrics.put("duplication", currentDuplicationLines);
            }
        }

        return returnValue;
    }

    @Override
    protected File getReportPath(final File resultsDir) {
        return new File(resultsDir, "/cpd.xml");
    }

    @Override
    protected String getXPathExpression() {
        return "//duplication";
    }
}
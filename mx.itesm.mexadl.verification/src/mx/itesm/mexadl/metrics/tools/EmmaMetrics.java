package mx.itesm.mexadl.metrics.tools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

/**
 * Retrieve quality metrics from <a href="http://emma.sourceforge.net/">EMMA</a>
 * results.
 * 
 * @author jccastrejon
 * 
 */
public class EmmaMetrics extends XradarMetrics {

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Map<String, Integer>> doGetMetrics(final List<Element> elements, final File resultsDir)
            throws Exception {
        String className;
        String coverageValue;
        Map<String, Integer> classMetrics;
        Map<String, Map<String, Integer>> returnValue;

        returnValue = new HashMap<String, Map<String, Integer>>(elements.size());
        for (Element clazz : elements) {
            className = clazz.getAttributeValue("name");
            classMetrics = new HashMap<String, Integer>();
            returnValue.put(clazz.getParentElement().getAttributeValue("name") + "."
                    + className.substring(0, className.lastIndexOf('.')), classMetrics);
            for (Element child : (List<Element>) clazz.getChildren()) {
                if (child.getAttributeValue("type").equals("line, %")) {
                    coverageValue = child.getAttributeValue("value");
                    classMetrics.put(child.getName(), Integer.parseInt(coverageValue.substring(0, coverageValue
                            .indexOf('%'))));
                    break;
                }
            }
        }

        return returnValue;
    }

    @Override
    protected File getReportPath(final File resultsDir) {
        return new File(resultsDir, "/xml/emma.xml");
    }

    @Override
    protected String getXPathExpression() {
        return "//srcfile";
    }
}
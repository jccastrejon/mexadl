package mx.itesm.mexadl.metrics.tools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

/**
 * Retrieve quality metrics from <a
 * href="http://www.spinellis.gr/sw/ckjm/">CKJM</a> results.
 * 
 * @author jccastrejon
 * 
 */
public class CkjmMetrics extends XmlFileMetrics {

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Map<String, Integer>> doGetMetrics(final List<Element> elements, final File resultsDir)
            throws Exception {
        Map<String, Integer> classMetrics;
        Map<String, Map<String, Integer>> returnValue;

        returnValue = new HashMap<String, Map<String, Integer>>(elements.size());
        for (Element clazz : elements) {
            classMetrics = new HashMap<String, Integer>();
            returnValue.put(clazz.getChildText("name"), classMetrics);
            for (Element child : (List<Element>) clazz.getChildren()) {
                if (!child.getName().equals("name")) {
                    classMetrics.put(child.getName(), Integer.parseInt(child.getText()));
                }
            }
        }

        return returnValue;
    }

    @Override
    protected File getReportPath(final File resultsDir) {
        return new File(resultsDir, "/ckjm.xml");
    }

    @Override
    protected String getXPathExpression() {
        return "//class";
    }
}
package mx.itesm.mexadl.metrics.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import mx.itesm.mexadl.metrics.MetricsTool;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * The FileMetrics class contains the basic behavior expected from metrics tools
 * depending on the result of XML files.
 * 
 * @author jccastrejon
 * 
 */
public abstract class XmlFileMetrics implements MetricsTool {

    @Override
    public Map<String, Map<String, Integer>> getMetrics(final File resultsDir) throws Exception {
        List<Element> elements;
        Map<String, Map<String, Integer>> returnValue;

        returnValue = null;
        elements = this.getReportElements(this.getXPathExpression(), this.getReportPath(resultsDir));
        if ((elements != null) && (!elements.isEmpty())) {
            returnValue = this.doGetMetrics(elements, resultsDir);
        }

        return returnValue;
    }

    /**
     * Get the elements containing the metrics information for types analyzed by
     * a metrics tool.
     * 
     * @param xPathExpression
     * @param reportPath
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public List<Element> getReportElements(final String xPathExpression, final File reportPath) throws JDOMException,
            IOException {
        XPath classPath;
        Document report;
        List<Element> returnValue;

        returnValue = null;
        if (reportPath.exists()) {
            classPath = XPath.newInstance(xPathExpression);
            report = this.getMetricsReport(reportPath);
            returnValue = classPath.selectNodes(report);
        }

        return returnValue;
    }

    /**
     * Get the report document containing the metrics information.
     * 
     * @param reportPath
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    protected Document getMetricsReport(final File reportPath) throws JDOMException, IOException {
        SAXBuilder saxBuilder;

        saxBuilder = new SAXBuilder();
        return saxBuilder.build(reportPath);
    }

    /**
     * Get the relevant quality metrics associated to the specified elements
     * list.
     * 
     * @param elements
     * @param resultsDir
     * @return
     * @throws Exception
     */
    protected abstract Map<String, Map<String, Integer>> doGetMetrics(final List<Element> elements,
            final File resultsDir) throws Exception;

    /**
     * Get the XPath expression that identifies those elements with the quality
     * metrics to be retrieved.
     * 
     * @return
     */
    protected abstract String getXPathExpression();

    /**
     * Get the file to the Report file that contains the quality metrics data.
     * 
     * @param resultsDir
     * @return
     */
    protected abstract File getReportPath(final File resultsDir);
}
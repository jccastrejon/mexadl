package mx.itesm.mexadl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import mx.itesm.mexadl.util.Util;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

/**
 * The MexAdlAnalyzer class generates MexADL artifacts based on xADL
 * architecture definitions.
 * 
 * @author jccastrejon
 * 
 */
public class MexAdlAnalyzer {

    /**
     * JDOM builder to parse an architecture definition document.
     */
    private static SAXBuilder saxBuilder = new SAXBuilder();

    /**
     * List of MexAdlProcessors that will be used to analyze xADL architectures.
     */
    private static List<MexAdlProcessor> processors;

    static {
        String configProcessors;
        String[] configProcessorsList;

        // Load the MexAdlProcessors from the configuration file
        configProcessors = Util.getConfigurationProperty(MexAdlAnalyzer.class, "processors");
        if (configProcessors != null) {
            configProcessorsList = configProcessors.split(",");
            MexAdlAnalyzer.processors = new ArrayList<MexAdlProcessor>(configProcessorsList.length);
            for (String processor : configProcessorsList) {
                try {
                    MexAdlAnalyzer.processors.add((MexAdlProcessor) Class.forName(processor.trim()).newInstance());
                } catch (Exception e) {
                    System.out.println("Unable to register " + processor + " as MexAdlProcessor");
                }
            }
        }
    }

    /**
     * Analyze an xADL architecture in order to generate the following MexADL
     * artifacts:
     * <ul>
     * <li>AspectJ architecture description aspect (XPI)</li>
     * <li>AspectJ metrics data aspect</li>
     * </ul>
     * 
     * @param xArch
     *            xADL architecture definition.
     * @param xArchFilePath
     *            Path to the file containing the xADL architecture definition.
     */
    public static void analyzeXArch(final String xArch, final String xArchFilePath) {
        Document document;

        try {
            // If there are any processors configured, execute them with the
            // current xADL architecture
            if ((MexAdlAnalyzer.processors != null) && (!MexAdlAnalyzer.processors.isEmpty())) {
                document = MexAdlAnalyzer.saxBuilder.build(new ByteArrayInputStream(xArch.getBytes()));
                for (MexAdlProcessor processor : processors) {
                    try {
                        processor.processDocument(document, xArchFilePath);
                    } catch (Exception e) {
                        System.out.println("An error ocurred while executing " + processor + " : " + e.getMessage());
                    }
                }
            } else {
                System.out.println("No MexAdlProcessors found to analyze the xADL architecture");
            }
        } catch (Exception e) {
            System.out.println("Error analyzing xArch: " + e);
            e.printStackTrace();
        }
    }
}

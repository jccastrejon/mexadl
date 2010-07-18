package mx.itesm.mexadl.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

/**
 * The Util class provides helper methods for the generation of MexADL
 * artifacts.
 * 
 * @author jccastrejon
 * 
 */
public class Util {

    /**
     * XPath expression to identify the description of an xADL architecture.
     */
    private static XPath xArchDescriptionPath;

    /**
     * Indicates whether the velocity properties have been initialized or not.
     */
    private static boolean velocityInit = false;

    /**
     * XML Schema-instance namespace.
     */
    public static final Namespace XSI_NAMESPACE = Namespace.getNamespace("xsi",
            "http://www.w3.org/2001/XMLSchema-instance");

    /**
     * xADL Types namespace.
     */
    public static final Namespace XADL_TYPES_NAMESPACE = Namespace.getNamespace("types",
            "http://www.ics.uci.edu/pub/arch/xArch/types.xsd");

    /**
     * xADL Instance namespace.
     */
    public static final Namespace XADL_INSTANCE_NAMESPACE = Namespace.getNamespace("types",
            "http://www.ics.uci.edu/pub/arch/xArch/instance.xsd");

    /**
     * MexADL properties.
     */
    private static ResourceBundle properties = ResourceBundle.getBundle("mx.itesm.mexadl.util.Configuration");

    /**
     * MexADL namespace.
     */
    public static final Namespace MEXADL_NAMESPACE = Namespace.getNamespace("mexadl", "http://mx.itesm/mexadl.xsd");

    /**
     * XML XLink namespace.
     */
    public static final Namespace XLINK_NAMESPACE = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");

    static {
        try {
            Util.xArchDescriptionPath = XPath.newInstance("/instance:xArch/types:archStructure/types:description");
        } catch (Exception e) {
            System.out.println("Error while loading Util: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Get the Java implementation class associated to the specified link.
     * 
     * @param document
     * @param linkId
     * @return
     * @throws JDOMException
     */
    public static String getLinkImplementationClass(final Document document, final String linkId) throws JDOMException {
        String typeId;
        XPath typePath;
        Element element;
        String returnValue;

        returnValue = null;
        element = Util.getLinkEndpoint(document, linkId);
        if (element != null) {

            // Check if the component/connector has any implementation
            // associated
            typeId = Util.getTypeId(element);

            if (typeId != null) {
                typePath = XPath
                        .newInstance("/instance:xArch/types:archTypes/types:componentType[@types:id=\""
                                + typeId
                                + "\"]/implementation:implementation/javaimplementation:mainClass/javaimplementation:javaClassName");

                element = ((Element) typePath.selectSingleNode(document));
                if (element != null) {
                    returnValue = element.getTextTrim();
                }
            }
        }

        return returnValue;
    }

    /**
     * Get the type associated to the specified link id. The type can be either
     * a component or a connector.
     * 
     * @param document
     * @param id
     * @return
     * @throws JDOMException
     */
    public static Element getLinkEndpoint(final Document document, final String id) throws JDOMException {
        return Util.getEndpoint(document, id,
                "/instance:xArch/types:archStructure/types:$typeName[@types:id=\"$typeId\"]");
    }

    /**
     * Get the element associated to the end-point represented by the specified
     * XPath expression.
     * 
     * @param document
     * @param id
     * @param xPathExpression
     * @return
     * @throws JDOMException
     */
    public static Element getEndpoint(final Document document, final String id, String xPathExpression)
            throws JDOMException {
        Element returnValue;

        // Component interface
        returnValue = (Element) XPath.newInstance(
                xPathExpression.replace("$typeName", "component").replace("$typeId", id)).selectSingleNode(document);

        // Connector interface
        if (returnValue == null) {
            returnValue = (Element) XPath.newInstance(
                    xPathExpression.replace("$typeName", "connector").replace("$typeId", id))
                    .selectSingleNode(document);
        }

        return returnValue;
    }

    /**
     * Create an aspect file from a Velocity template.
     * 
     * @param document
     * @param xArchFilePath
     * @param aspectTemplate
     * @param properties
     * @param prefix
     * @throws IOException
     * @throws JDOMException
     */
    public static void createAspectFile(final Document document, final String xArchFilePath,
            final Template aspectTemplate, final Map<String, Object> properties, final String prefix)
            throws IOException, JDOMException {
        Writer writer;
        File xArchFile;
        File outputFile;
        String architectureName;
        VelocityContext context;

        // Create the aspect where the group data will be saved
        xArchFile = new File(xArchFilePath);
        architectureName = Util.getValidName(((Element) Util.xArchDescriptionPath.selectSingleNode(document))
                .getValue());
        outputFile = new File(xArchFile.getParentFile(), prefix + "_" + architectureName + ".aj");
        outputFile.delete();
        outputFile.createNewFile();
        writer = new BufferedWriter(new FileWriter(outputFile));

        // Use the velocity template to generate the aspect content
        context = new VelocityContext();
        context.put("architectureName", architectureName);
        for (String key : properties.keySet()) {
            context.put(key, properties.get(key));
        }
        aspectTemplate.merge(context, writer);
        writer.close();
    }

    /**
     * Get a velocity template from the classpath.
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public static Template getVelocityTemplate(final Class<?> clazz) throws Exception {
        Template returnValue;
        Properties velocityProperties;

        if (!Util.velocityInit) {
            velocityProperties = new Properties();
            velocityProperties.put("resource.loader", "class");
            velocityProperties.put("class.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init(velocityProperties);
            Util.velocityInit = true;
        }

        returnValue = Velocity.getTemplate(Util.getConfigurationProperty(clazz, "template"));
        return returnValue;
    }

    /**
     * Get a valid name that can be used as an identifier.
     * 
     * @param name
     *            Name to be validated.
     * @return Valid identifier.
     */
    public static String getValidName(final String name) {
        return name.replaceAll("[^\\p{Alnum}]", "_");
    }

    /**
     * Get a valid XLink Id to be used in the architecture definition.
     * 
     * @param linkId
     *            XLink Id.
     * @return Valid XLink Id.
     */
    public static String getValidLinkId(final String linkId) {
        String returnValue;

        returnValue = linkId;
        if ((returnValue != null) && (returnValue.startsWith("#"))) {
            returnValue = returnValue.substring(1);
        }

        return returnValue;
    }

    /**
     * Get the ID attribute of a given Element.
     * 
     * @param element
     * @return
     */
    public static String getIdValue(final Element element) {
        return Util.getValidLinkId(element.getAttributeValue("id", Util.XADL_TYPES_NAMESPACE));
    }

    /**
     * Get the HREF attribute of a given Element.
     * 
     * @param element
     * @return
     */
    public static String getHrefValue(final Element element) {
        return Util.getValidLinkId(element.getAttributeValue("href", Util.XLINK_NAMESPACE));
    }

    /**
     * Get the XML Schema-instance type attribute of the given element.
     * 
     * @param element
     * @return
     */
    public static String getXsiType(final Element element) {
        return element.getAttribute("type", Util.XSI_NAMESPACE).getValue();
    }

    /**
     * Get the TYPE Id value for a given Element.
     * 
     * @param element
     * @return
     */
    public static String getTypeId(final Element element) {
        String returnValue;
        Element typeElement;

        returnValue = null;
        typeElement = element.getChild("type", Util.XADL_TYPES_NAMESPACE);

        if (typeElement != null) {
            returnValue = Util.getHrefValue(typeElement);
        }

        return returnValue;
    }

    /**
     * Get the value associated to the specified property in the MexADL
     * configuration file.
     * 
     * @param clazz
     * @param name
     * @return
     */
    public static String getConfigurationProperty(final Class<?> clazz, final String name) {
        return Util.properties.getString(clazz.getName() + "." + name);
    }
}
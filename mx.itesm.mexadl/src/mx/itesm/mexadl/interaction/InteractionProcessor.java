package mx.itesm.mexadl.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.itesm.mexadl.MexAdlProcessor;
import mx.itesm.mexadl.util.Util;

import org.apache.velocity.Template;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * The InteractionProcessor class generates an aspect to enforce the valid
 * interactions between components defined in an xADL architecture definition.
 * 
 * @author jccastrejon
 * 
 */
public class InteractionProcessor implements MexAdlProcessor {

    /**
     * Class logger.
     */
    private static Logger logger = Logger.getLogger(InteractionProcessor.class.getName());

    /**
     * XPath expression to identify the links defined in an xADL document.
     */
    private static XPath linkPath;

    /**
     * AspectJ template that defines the valid interactions within types of a
     * sytem's architecture.
     */
    private static Template aspectTemplate;

    static {
        try {
            InteractionProcessor.aspectTemplate = Util.getVelocityTemplate(InteractionProcessor.class, "aspect");
            InteractionProcessor.linkPath = XPath.newInstance("//types:link");
        } catch (Exception e) {
            InteractionProcessor.logger.log(Level.WARNING, "Error loading InteractionProcessor: ", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processDocument(final Document document, final String xArchFilePath) throws Exception {
        String component;
        String endpointId;
        List<Element> links;
        List<Element> points;
        Element currentEndpoint;
        List<String> linkEndpoints;
        String firstImplementation;
        String secondImplementation;
        List<List<String>> linksList;
        Map<String, Object> properties;
        Map<String, String> endpointComponents;
        Map<String, Set<String>> validInteractions;
        List<Map<String, Object>> interactionsList;

        // Resolve link end-points
        links = InteractionProcessor.linkPath.selectNodes(document);
        if ((links != null) && (!links.isEmpty())) {
            linksList = new ArrayList<List<String>>();
            endpointComponents = new HashMap<String, String>(links.size() * 2);
            for (Element link : links) {
                points = this.getPoints(link);

                linkEndpoints = new ArrayList<String>(points.size());
                linksList.add(linkEndpoints);
                for (Element point : points) {
                    // Save the end-points that compose the link
                    currentEndpoint = this.getPointEndpoint(document, point);
                    endpointId = Util.getIdValue(currentEndpoint);
                    linkEndpoints.add(endpointId);

                    // Keep a reference to the component to which each end-point
                    // is associated
                    component = Util.getIdValue(currentEndpoint.getParentElement());
                    endpointComponents.put(endpointId, component);
                }
            }

            // Associate valid interactions to each Type
            validInteractions = new HashMap<String, Set<String>>();
            for (List<String> link : linksList) {
                firstImplementation = Util.getLinkImplementationClass(document, endpointComponents.get(link.get(0)));
                secondImplementation = Util.getLinkImplementationClass(document, endpointComponents.get(link.get(1)));

                if ((firstImplementation != null) && (secondImplementation != null)) {
                    this.addTypesDependencies(validInteractions, firstImplementation, secondImplementation, true);
                } else {
                    // If two components are connected through a connector, and
                    // the connector doesn't have an implementation class
                    // associated, we'll try to connect the components directly
                    // (provided they do have implementations associated)
                    if ((this.isConnector(endpointComponents.get(link.get(0)))) && (secondImplementation != null)) {
                        // Get the implementations of objects associated to
                        // links that have this connector as their second
                        // end-point
                        for (List<String> connectorLink : linksList) {
                            if (endpointComponents.get(connectorLink.get(1))
                                    .equals(endpointComponents.get(link.get(0)))) {
                                firstImplementation = Util.getLinkImplementationClass(document, endpointComponents
                                        .get(connectorLink.get(0)));
                                this.addTypesDependencies(validInteractions, firstImplementation, secondImplementation,
                                        true);
                            }
                        }
                    } else if ((this.isConnector(endpointComponents.get(link.get(1)))) && (firstImplementation != null)) {
                        // Get the implementations of objects associated to
                        // links that have this connector as their first
                        // end-point
                        for (List<String> connectorLink : linksList) {
                            if (endpointComponents.get(connectorLink.get(0))
                                    .equals(endpointComponents.get(link.get(1)))) {
                                secondImplementation = Util.getLinkImplementationClass(document, endpointComponents
                                        .get(connectorLink.get(1)));
                                this.addTypesDependencies(validInteractions, firstImplementation, secondImplementation,
                                        true);
                            }
                        }
                    }
                }
            }

            interactionsList = new ArrayList<Map<String, Object>>();
            for (String key : validInteractions.keySet()) {
                properties = new HashMap<String, Object>();

                properties.put("type", key);
                properties.put("typeName", Util.getValidName(key));
                properties.put("interactions", validInteractions.get(key));
                interactionsList.add(properties);
            }

            // Create the interactions aspect only if valid associations were
            // found
            if (!validInteractions.isEmpty()) {
                properties = new HashMap<String, Object>();
                properties.put("typesList", validInteractions.keySet());
                properties.put("warningsList", interactionsList);
                Util.createFile(document, xArchFilePath, InteractionProcessor.aspectTemplate, properties,
                        "InteractionsAspect", Util.getDocumentName(document), Util.JAVA_EXTENSION);
            }
        }
    }

    /**
     * Get the end-point associated to a specified point in a link.
     * 
     * @param document
     * @param point
     * @return
     * @throws JDOMException
     */
    private Element getPointEndpoint(final Document document, final Element point) throws JDOMException {
        return this.getInterfaceEndpoint(document, Util.getHrefValue(point.getChild("anchorOnInterface",
                Util.XADL_INSTANCE_NAMESPACE)));
    }

    /**
     * Get the interface element associate to the specified link id. The
     * interface can be associated to either a component or a connector.
     * 
     * @param document
     * @param id
     * @return
     * @throws JDOMException
     */
    private Element getInterfaceEndpoint(final Document document, final String id) throws JDOMException {
        return Util.getEndpoint(document, id,
                "/instance:xArch/types:archStructure/types:$typeName/types:interface[@types:id=\"$typeId\"]");
    }

    /**
     * Add the dependency between two types defined in an xADL architecture
     * definition.
     * 
     * @param dependencies
     * @param firstType
     * @param secondType
     * @param reverse
     */
    private void addTypesDependencies(final Map<String, Set<String>> dependencies, final String firstType,
            final String secondType, final boolean reverse) {
        Set<String> currentInteractions;

        // Only work with non-null dependencies
        if ((firstType != null) && (secondType != null)) {

            currentInteractions = dependencies.get(firstType);
            if (currentInteractions == null) {
                currentInteractions = new HashSet<String>();
                dependencies.put(firstType, currentInteractions);
            }
            currentInteractions.add(Util.getValidName(secondType));

            // If interfaces have no direction, add the dependencies to both
            // of them
            if (reverse) {
                currentInteractions = dependencies.get(secondType);
                if (currentInteractions == null) {
                    currentInteractions = new HashSet<String>();
                    dependencies.put(secondType, currentInteractions);
                }
                currentInteractions.add(Util.getValidName(firstType));
            }
        }
    }

    /**
     * Get the point elements associated to an Element.
     * 
     * @param element
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Element> getPoints(final Element element) {
        return (List<Element>) element.getChildren("point", Util.XADL_TYPES_NAMESPACE);
    }

    /**
     * Check if the object represented by the given Id is aan xADL connector.
     * 
     * @param id
     * @return
     */
    private boolean isConnector(final String id) {
        return ((id != null) && (id.contains("connector")));
    }
}

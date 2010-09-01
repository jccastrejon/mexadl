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

    /**
     * Valid interface directions.
     * 
     * @author jccastrejon
     * 
     */
    enum EndpointDirection {
        NONE {
            @Override
            public boolean isValidInteraction(final EndpointDirection other) {
                return ((other == NONE) || (other == IN));
            }
        },
        IN {
            @Override
            public boolean isValidInteraction(final EndpointDirection other) {
                return false;
            }
        },
        OUT {
            @Override
            public boolean isValidInteraction(final EndpointDirection other) {
                return (other == IN);
            }
        },
        INOUT {
            @Override
            public boolean isValidInteraction(final EndpointDirection other) {
                return ((other == IN) || (other == INOUT));
            }
        };

        /**
         * Determine if a valid link can be formed by considering this direction
         * as starting point, and the specified EndpointDirection.
         * 
         * @param other
         * @return
         */
        public abstract boolean isValidInteraction(final EndpointDirection other);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processDocument(final Document document, final String xArchFilePath) throws Exception {
        String component;
        String endpointId;
        boolean isReverse;
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
        Map<String, EndpointDirection> endpointsDirections;

        // Resolve link end-points
        links = InteractionProcessor.linkPath.selectNodes(document);
        if ((links != null) && (!links.isEmpty())) {
            linksList = new ArrayList<List<String>>();
            endpointComponents = new HashMap<String, String>(links.size() * 2);
            endpointsDirections = new HashMap<String, EndpointDirection>(links.size() * 2);
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

                    // End-point direction
                    endpointsDirections.put(endpointId, this.getEndpointDirection(currentEndpoint));
                }
            }

            // Associate valid interactions to each Type
            validInteractions = new HashMap<String, Set<String>>();
            for (List<String> link : linksList) {
                link = this.getOrderedEndpoints(endpointsDirections, link);
                isReverse = this.isReverseConnection(endpointsDirections, link.get(0), link.get(1));
                if (link != null) {
                    firstImplementation = Util
                            .getLinkImplementationClass(document, endpointComponents.get(link.get(0)));
                    secondImplementation = Util.getLinkImplementationClass(document, endpointComponents
                            .get(link.get(1)));

                    // Directly connected components (no connector)
                    if ((firstImplementation != null) && (secondImplementation != null)) {
                        this.addTypesDependencies(validInteractions, firstImplementation, secondImplementation,
                                isReverse);
                    } else {
                        // If two components are connected through a connector,
                        // and the connector doesn't have an implementation
                        // class associated, we'll try to connect the components
                        // directly (provided they do have implementations
                        // associated)
                        if ((this.isConnector(endpointComponents.get(link.get(0)))) && (secondImplementation != null)) {
                            // Get the implementations of objects associated to
                            // links that have this connector as their second
                            // end-point
                            for (List<String> connectorLink : linksList) {
                                connectorLink = this.getOrderedEndpoints(endpointsDirections, connectorLink);
                                if (endpointComponents.get(connectorLink.get(1)).equals(
                                        endpointComponents.get(link.get(0)))) {
                                    firstImplementation = Util.getLinkImplementationClass(document, endpointComponents
                                            .get(connectorLink.get(0)));
                                    this.addTypesDependencies(validInteractions, firstImplementation,
                                            secondImplementation, isReverse);
                                }
                            }
                        } else if ((this.isConnector(endpointComponents.get(link.get(1))))
                                && (firstImplementation != null)) {
                            // Get the implementations of objects associated to
                            // links that have this connector as their first
                            // end-point
                            for (List<String> connectorLink : linksList) {
                                connectorLink = this.getOrderedEndpoints(endpointsDirections, connectorLink);
                                if (endpointComponents.get(connectorLink.get(0)).equals(
                                        endpointComponents.get(link.get(1)))) {
                                    secondImplementation = Util.getLinkImplementationClass(document, endpointComponents
                                            .get(connectorLink.get(1)));
                                    this.addTypesDependencies(validInteractions, firstImplementation,
                                            secondImplementation, isReverse);
                                }
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
                properties.put("annotations", Util.getAnnotations(document));
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
            this.initializeInteractions(firstType, dependencies);
            this.initializeInteractions(secondType, dependencies);

            // One way
            currentInteractions = dependencies.get(secondType);
            currentInteractions.add(Util.getValidName(firstType));

            // Both ways
            if (reverse) {
                currentInteractions = dependencies.get(firstType);
                currentInteractions.add(Util.getValidName(secondType));
            }
        }
    }

    /**
     * Initialize the valid interactions that the specified Component may have,
     * including self-interactions.
     * 
     * @param type
     * @param dependencies
     * @return
     */
    private void initializeInteractions(final String type, final Map<String, Set<String>> dependencies) {
        Set<String> currentInteractions;

        currentInteractions = dependencies.get(type);
        if (currentInteractions == null) {
            currentInteractions = new HashSet<String>();
            currentInteractions.add(Util.getValidName(type));
            dependencies.put(type, currentInteractions);
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
     * Check if the object represented by the given Id is an xADL connector.
     * 
     * @param id
     * @return
     */
    private boolean isConnector(final String id) {
        return ((id != null) && (id.contains("connector")));
    }

    /**
     * Get the direction associated to an interface.
     * 
     * @param element
     * @return
     */
    private EndpointDirection getEndpointDirection(final Element element) {
        return EndpointDirection
                .valueOf(element.getChildTextTrim("direction", Util.XADL_TYPES_NAMESPACE).toUpperCase());
    }

    /**
     * Determine if a link can be traversed in both ways.
     * 
     * @param endpointsDirections
     * @param first
     * @param second
     * @return
     */
    private boolean isReverseConnection(final Map<String, EndpointDirection> endpointsDirections, final String first,
            final String second) {
        return ((endpointsDirections.get(first) == EndpointDirection.INOUT) && (endpointsDirections.get(second) == EndpointDirection.INOUT));
    }

    /**
     * Order the end-points that make up a link, according to the directions of
     * their associated interfaces.
     * 
     * @param endpointsDirections
     * @param link
     * @return
     */
    private List<String> getOrderedEndpoints(final Map<String, EndpointDirection> endpointsDirections,
            final List<String> link) {
        List<String> returnValue;

        if (endpointsDirections.get(link.get(0)).isValidInteraction(endpointsDirections.get(link.get(1)))) {
            returnValue = new ArrayList<String>(2);
            returnValue.add(link.get(0));
            returnValue.add(link.get(1));
        } else if (endpointsDirections.get(link.get(1)).isValidInteraction(endpointsDirections.get(link.get(0)))) {
            returnValue = new ArrayList<String>(2);
            returnValue.add(link.get(1));
            returnValue.add(link.get(0));
        } else {
            returnValue = null;
        }

        return returnValue;
    }
}
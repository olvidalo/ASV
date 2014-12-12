package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.GenericTreeModelProviderFactory;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanelListener;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import nl.mpi.metadatabrowser.wicket.components.HeaderPanel;
import nl.mpi.metadatabrowser.wicket.components.NodesPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.LinkType;
import org.apache.wicket.extensions.markup.html.tree.Tree;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jeafer
 * @param <SerializableCorpusNode>
 */
public class HomePage<SerializableCorpusNode extends CorpusNode & Serializable> extends WebPage {

    private static final long serialVersionUID = 1L;

    private static final String ROOT_NODE_PARAM = "rootnode";
    private static final String OPEN_HANDLE_PARAM = "openhandle";
    private static final String OPEN_PATH_PARAM = "openpath";

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    @SpringBean
    private GenericTreeModelProviderFactory treeModelProviderFactory;
    @SpringBean
    private CorpusStructureProvider csProvider;
    @SpringBean(required = false)
    private ArchiveTreeNodeIconProvider<CorpusNode> treeIconProvider;
    @SpringBean
    private AuthenticationHolder auth;
    @SpringBean
    private TreeExpander treeExpander;

    private final NodesPanel nodesPanel;
    private final ArchiveTreePanel treePanel;

    /**
     * Constructor
     *
     * @param parameters that will be needed for openpath
     */
    public HomePage(final PageParameters parameters) {
        super(parameters);

        //Add a panel hosting the user information.
        final HeaderPanel headerPanel = new HeaderPanel("headerPanel", auth.getPrincipalName());
        add(headerPanel);

        final URI rootNodeUri = getRootNodeURI(parameters);
        final CorpusNode rootNode = csProvider.getNode(rootNodeUri);
        final GenericTreeModelProvider treeModelProvider = treeModelProviderFactory.createTreeModelProvider(rootNode);

        // Add a panel hosting the archive tree, taking its structure from the injected tree model provider
        treePanel = new ArchiveTreePanel("treePanel", treeModelProvider, treeIconProvider);
        treePanel.setLinkType(LinkType.AJAX_FALLBACK);
        add(treePanel);

        // Add a panel to show information and actions on the currently selected node(s)
        nodesPanel = new NodesPanel("nodesPanel", new CollectionModel(treePanel.getSelectedNodes()));
        nodesPanel.setOutputMarkupId(true);

        final Tree archiveTree = treePanel.getTree();
        final Object rootObj = archiveTree.getModelObject().getRoot();// get rootNode object
        archiveTree.getTreeState().expandNode(rootObj);// open tree to first children

        // Link up the tree panel and nodes panel so that changes in the former get reflected in the latter
        treePanel.addArchiveTreePanelListener(new ArchiveTreePanelListener<SerializableCorpusNode>() {
            @Override
            public void nodeSelectionChanged(AjaxRequestTarget target, ArchiveTreePanel<SerializableCorpusNode> treePanel) {
                if (!treePanel.getSelectedNodes().isEmpty()) {
                    nodesPanel.setModelObject(treePanel.getSelectedNodes());
                    if (target != null) {
                        target.add(nodesPanel);
                    }
                }
            }
        });

        checkForOpenpathParameter(parameters, rootObj);
        nodesPanel.setModelObject(treePanel.getSelectedNodes());// display presentation for selected node (Welocome Page if openpath doesn' exist)
        add(nodesPanel);
    }

    /**
     * Determines the root URI used to initialise the tree provider; if the
     * {@link #ROOT_NODE_PARAM} parameter is set and it is a valid URI (usually
     * a node ID URI or handle is expected depending on the provider) this is
     * returned; in all other cases the root node URI provided by the
     * CorpusStructure provider is returned
     *
     * @param parameters Wicket page parameters
     * @return root URI to use
     * @see #ROOT_NODE_PARAM
     * @see CorpusStructureProvider#getRootNodeURI()
     */
    private URI getRootNodeURI(final PageParameters parameters) {
        final StringValue rootNodeParam = parameters.get(ROOT_NODE_PARAM);
        if (!rootNodeParam.isEmpty()) {
            try {
                logger.debug("Using custom root node {}", rootNodeParam);
                return new URI(rootNodeParam.toString());
            } catch (URISyntaxException ex) {
                logger.error("Invalid root node URI: {}; falling back to database default");
            }
        }
        return csProvider.getRootNodeURI();
    }

    /**
     * Method that will check for an 'openpath' or 'openhandle' parameter
     *
     * 'openpath' is assumed to have a node ID or a URI with 'hdl' scheme as its
     * value 'openhandle' can have a bare handle or a URI with 'hdl' scheme as
     * its value
     *
     * @param parameters PageParameters, looks for openpath to continue
     * @param treePanel ArchivetreePanel, the treePanel for which node will be
     * expanded
     * @param rootObj Object, the root node
     * @return boolean for openpath exist or not
     */
    private Boolean checkForOpenpathParameter(PageParameters parameters, Object rootObj) {
        final URI nodeUri = getNodeUriToOpen(parameters);
        if (nodeUri == null) {
            return false;
        } else {
            final boolean nodeFound = treeExpander.openPath(treePanel.getTree(), rootObj, nodeUri);
            if (!nodeFound) {
                logger.info("Node with URI {} requested but not found", nodeUri);
                if (nodeUri.getScheme().equalsIgnoreCase("hdl")) {
                    error(String.format("The node with the handle '%s' does not exist in our archive. Please check for typos or transposed digits.", nodeUri.getSchemeSpecificPart()));
                } else {
                    error(String.format("The node with the ID '%s' does not exist in our archive. Please check for typos or transposed digits.", nodeUri.getSchemeSpecificPart()));
                }
            }
            return nodeFound;
        }
    }

    private URI getNodeUriToOpen(PageParameters parameters) {
        String uriString = parameters.get(OPEN_PATH_PARAM).toString();
        if (uriString == null) {
            // no open path, try openhandle
            final String handle = parameters.get(OPEN_HANDLE_PARAM).toString();
            if (handle != null) {
                // already a handle URI? If not, prepend scheme
                if (handle.startsWith("hdl:")) {
                    uriString = handle;
                } else {
                    uriString = "hdl:" + handle;
                }
            }
        }

        if (uriString == null) {
            return null;
        } else {
            try {
                final URI nodeUri = new URI(uriString);
                final String uriScheme = nodeUri.getScheme();
                if (uriScheme == null
                        || (!uriScheme.equalsIgnoreCase("node") //not an explicit node URI
                        && !uriScheme.equalsIgnoreCase("hdl") //not a handle
                        && !uriScheme.startsWith("http"))) { // not an ordinary URL
                    // assume it's a node ID, so fall back to node URI
                    return new URI("node:" + nodeUri.getSchemeSpecificPart());
                } else {
                    return nodeUri;
                }
            } catch (URISyntaxException ex) {
                logger.warn("the URI for node {} gives an error {}", uriString, ex);
                return null;
            }
        }
    }
}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jeafer
 * @param <SerializableCorpusNode>
 */
public class HomePage<SerializableCorpusNode extends CorpusNode & Serializable> extends WebPage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private static final long serialVersionUID = 1L;
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

        //TODO: Allow parameter for *custom* root node
        final URI rootNodeUri = csProvider.getRootNodeURI();
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
            return treeExpander.openPath(treePanel.getTree(), rootObj, nodeUri);
        }

    }

    private URI getNodeUriToOpen(PageParameters parameters) {
        String uriString = parameters.get("openpath").toString();
        if (uriString == null) {
            // no open path, try openhandle
            final String handle = parameters.get("openhandle").toString();
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
                //TODO: Deal with openhandle=MPI123# format (=>node:123)
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

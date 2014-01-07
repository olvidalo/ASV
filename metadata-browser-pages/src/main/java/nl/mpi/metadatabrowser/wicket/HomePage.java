package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.servlet.http.HttpServletRequest;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.archiving.tree.swingtree.GenericTreeSwingTreeNodeWrapper;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanelListener;
import nl.mpi.metadatabrowser.wicket.components.HeaderPanel;
import nl.mpi.metadatabrowser.wicket.components.NodesPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.LinkType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.request.cycle.RequestCycle;
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

    private static final long serialVersionUID = 1L;
    @SpringBean
    private CorpusStructureProvider csprovider;
    @SpringBean
    private GenericTreeModelProvider treeModelProvider;
    @SpringBean(required = false)
    private ArchiveTreeNodeIconProvider<CorpusNode> treeIconProvider;
    private NodesPanel nodesPanel;
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    /**
     * Constructor
     *
     * @param parameters that will be needed for openpath
     */
    public HomePage(final PageParameters parameters) {
        super(parameters);
        HttpServletRequest request = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
        //Add a panel hosting the user information.
        final HeaderPanel headerPanel = new HeaderPanel("headerPanel", request);
        add(headerPanel);

        // Add a panel hosting the archive tree, taking its structure from the injected tree model provider
        final ArchiveTreePanel treePanel = new ArchiveTreePanel("treePanel", treeModelProvider, treeIconProvider);
        treePanel.setLinkType(LinkType.AJAX_FALLBACK);
        add(treePanel);

        // Add a panel to show information and actions on the currently selected node(s)
        nodesPanel = new NodesPanel("nodesPanel", new CollectionModel(treePanel.getSelectedNodes()));
        nodesPanel.setOutputMarkupId(true);
        Object rootObj = treePanel.getTree().getModelObject().getRoot();// get rootNode object
        treePanel.getTree().getTreeState().expandNode(rootObj);// open tree to first children

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

        List<URI> parentNodes = new ArrayList<URI>();
        boolean isOpenPath = checkForOpenpathParameter(parameters, treePanel, parentNodes, rootObj);
        if (!isOpenPath) {
            treePanel.getTree().getTreeState().selectNode(rootObj, true);
        }
        nodesPanel.setModelObject(treePanel.getSelectedNodes());// display presentation for selected node (root node if openpath doesn' exist)
        add(nodesPanel);
    }

    /**
     * Method that will check for openpath parameter
     *
     * @param parameters PageParameters, looks for openpath to continue
     * @param treePanel ArchivetreePanel, the treePanel for which node will be
     * expanded
     * @param parentNodes List<URI>, list of URI that gather the uri of all the
     * parents (hierarchically) for a specific node
     * @param rootObj Object, the root node
     * @return boolean for openpath exist or not
     */
    private Boolean checkForOpenpathParameter(PageParameters parameters, ArchiveTreePanel treePanel, List<URI> parentNodes, Object rootObj) {
        String node = parameters.get("openpath").toString();
        if (node != null) {
            try {
                parentNodes.add(new URI(node));
                getParentNode(new URI(node), treePanel, parentNodes, rootObj);
            } catch (URISyntaxException ex) {
                logger.error("the URI for node {} gives an error {}", node, ex);
            } catch (UnknownNodeException ex) {
                logger.error("node {} is unknow {}", node, ex);
            }
            return true;
        }
        return false;

    }

    /**
     * Recursive method that collects all the parents of a given node up to the
     * root node.
     *
     * @param node URI, the uri of the child node for which we lookup the parent
     * @param treePanel ArchiveTreePanel, treePanel from which the parentNode
     * will be expanded
     * @param parentNodes List<URI>, stored list of parents URI
     * @param rootObj Object, the root node
     * @throws URISyntaxException
     * @throws UnknownNodeException
     */
    private void getParentNode(URI node, ArchiveTreePanel treePanel, List<URI> parentNodes, Object rootObj) throws URISyntaxException, UnknownNodeException {
        URI parentNodeURI = csprovider.getCanonicalParent(node);// get parent URI
        if (parentNodeURI != null) {
            parentNodes.add(parentNodeURI); //add parent to the list
            getParentNode(parentNodeURI, treePanel, parentNodes, rootObj);// find next parent
        } else {// no more parents
            expandTreeToSelectedNode(parentNodes, treePanel, rootObj);
        }
    }

    /**
     * Method that expand the nodes
     *
     * @param parentNodes List<URI>, list of node URI that need to be expanded
     * @param treePanel ArchivePanel, treePanel in which node will be expanded
     * @param rootObj Object, the root node which is already expanded
     */
    private void expandTreeToSelectedNode(List<URI> parentNodes, ArchiveTreePanel treePanel, Object rootObj) {
        // Generate an iterator. Start just after the last element.(reverse reading)
        ListIterator<URI> li = parentNodes.listIterator(parentNodes.size());
        Object currentTreeNode = rootObj;// this is root node (already expanded)
        li.previous();
        while (li.hasPrevious()) {
            URI nodeUri = li.previous();
            currentTreeNode = findTreeNodeObject((GenericTreeSwingTreeNodeWrapper) currentTreeNode, nodeUri);
            if (currentTreeNode == null) {
                // No match, throw exception or something
            } else {
                treePanel.getTree().getTreeState().expandNode(currentTreeNode);
                treePanel.getTree().getTreeState().selectNode(currentTreeNode, true);
            }
        }
    }

    /**
     * Method that will find the object of a given node. Goes through all the
     * children of a node to find the perfect match
     *
     * @param currentTreeNode GenericTreeSwingTreeNodeWrapper, object to be
     * matched with nopdeURI
     * @param nodeUri URI, uri of the node that has to be expanded
     * @return treeNodeObject GenericTreeSwingTreeNodeWrapper, object of a node
     * to be expanded
     */
    private Object findTreeNodeObject(GenericTreeSwingTreeNodeWrapper currentTreeNode, URI nodeUri) {
        for (int i = 0; i < currentTreeNode.getChildCount(); i++) {
            final GenericTreeSwingTreeNodeWrapper treeNodeObject = (GenericTreeSwingTreeNodeWrapper) currentTreeNode.getChildAt(i);
            GenericTreeNode contentNode = treeNodeObject.getContentNode();
            if (contentNode instanceof CorpusNode) {
                if (nodeUri.equals(((CorpusNode) contentNode).getNodeURI())) {
                    return treeNodeObject;
                }
            }
        }
        return null;
    }
}

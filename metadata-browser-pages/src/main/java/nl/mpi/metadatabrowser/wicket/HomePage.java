package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.wicket.components.NodeActionsPanel;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage<SerializableCorpusNode extends CorpusNode & Serializable> extends WebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private GenericTreeModelProvider treeModelProvider;
    @SpringBean
    private NodeTypeIdentifier nodeTypeIdentifier;
    @SpringBean
    private NodeActionsProvider nodeActionsProvider;
    @SpringBean
    private NodePresentationProvider nodePresentationProvider;
    // Child components
    private final NodeActionsPanel nodeActionsPanel;
    private final MarkupContainer nodePresentationContainer;

    public HomePage(final PageParameters parameters) {
	super();

	final MetadataBrowserTreePanel treePanel = new MetadataBrowserTreePanel("treePanel", treeModelProvider);
	add(treePanel);

	nodeActionsPanel = new NodeActionsPanel("nodeActions");
	nodeActionsPanel.setOutputMarkupId(true);
	add(nodeActionsPanel);

	nodePresentationContainer = new WebMarkupContainer("nodePresentationContainer");
	nodePresentationContainer.add(new WebMarkupContainer("nodePresentation"));
	nodePresentationContainer.setOutputMarkupId(true);
	add(nodePresentationContainer);
    }

    private class MetadataBrowserTreePanel extends ArchiveTreePanel<SerializableCorpusNode> {

	public MetadataBrowserTreePanel(String id, GenericTreeModelProvider provider) {
	    super(id, provider);
	}

	//TODO: Loop over selected nodes
	@Override
	protected void onNodeLinkClicked(AjaxRequestTarget target, SerializableCorpusNode node) {
	    // Get the node type from the node type identifier
	    final NodeType nodeType = nodeTypeIdentifier.getNodeType(node.getUri());

	    // Get the node actions and update the model of the node actions panel
	    final List<NodeAction> selectedNodeActions = nodeActionsProvider.getNodeActions(node.getUri(), nodeType);
	    nodeActionsPanel.setModelObject(new NodeActionsStructure(node.getUri(), selectedNodeActions));
	    target.add(nodeActionsPanel);

	    // Add the node presentation component to the presentation container
	    final Component nodePresentation = nodePresentationProvider.getNodePresentation("nodePresentation", node.getUri(), nodeType);
	    nodePresentationContainer.addOrReplace(nodePresentation);
	    target.add(nodePresentationContainer);
	}
    };
}

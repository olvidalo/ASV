package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanelListener;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.wicket.components.NodeActionsPanel;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import nl.mpi.metadatabrowser.wicket.model.TypedSerializableCorpusNode;
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

	final ArchiveTreePanel treePanel = new ArchiveTreePanel("treePanel", treeModelProvider);
	treePanel.addArchiveTreePanelListener(new MetadataBrowserTreePanelListener());
	add(treePanel);

	nodeActionsPanel = new NodeActionsPanel("nodeActions");
	nodeActionsPanel.setOutputMarkupId(true);
	add(nodeActionsPanel);

	nodePresentationContainer = new WebMarkupContainer("nodePresentationContainer");
	nodePresentationContainer.add(new WebMarkupContainer("nodePresentation"));
	nodePresentationContainer.setOutputMarkupId(true);
	add(nodePresentationContainer);
    }

    private class MetadataBrowserTreePanelListener implements ArchiveTreePanelListener<SerializableCorpusNode>, Serializable {

	@Override
	public void nodeSelectionChanged(AjaxRequestTarget target, ArchiveTreePanel<SerializableCorpusNode> treePanel) {
	    final Collection<SerializableCorpusNode> selectedNodes = treePanel.getSelectedNodes();

	    final Collection<TypedCorpusNode> typedNodes = new ArrayList<TypedCorpusNode>(selectedNodes.size());
	    for (SerializableCorpusNode node : selectedNodes) {
		// Get the node type from the node type identifier
		final NodeType nodeType = nodeTypeIdentifier.getNodeType(node);
		typedNodes.add(new TypedSerializableCorpusNode(node, nodeType));
	    }

	    // Get the node actions and update the model of the node actions panel
	    final List<NodeAction> selectedNodeActions = nodeActionsProvider.getNodeActions(typedNodes);
	    nodeActionsPanel.setModelObject(new NodeActionsStructure(typedNodes, selectedNodeActions));
	    target.add(nodeActionsPanel);

	    // Add the node presentation component to the presentation container
	    final Component nodePresentation = nodePresentationProvider.getNodePresentation("nodePresentation", typedNodes);
	    nodePresentationContainer.addOrReplace(nodePresentation);
	    target.add(nodePresentationContainer);
	}
    };
}

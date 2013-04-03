package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
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

    public HomePage(final PageParameters parameters) {
	super();

	final Model<String> nodeTypeLabelModel = new Model<String>("Select a node");
	final Label nodeTypeLabel = new Label("nodeTypeLabel", nodeTypeLabelModel);
	nodeTypeLabel.setOutputMarkupId(true);
	add(nodeTypeLabel);

	final MarkupContainer nodeActionsContainer = new WebMarkupContainer("nodeActionsListContainer");
	nodeActionsContainer.setOutputMarkupId(true);
	add(nodeActionsContainer);

	final List<NodeAction> nodeActions = new ArrayList<NodeAction>();
	final ListView<NodeAction> nodeActionsView = new ListView<NodeAction>("nodeActionsList", nodeActions) { // add model for node actions
	    @Override
	    protected void populateItem(ListItem<NodeAction> item) {
		item.add(new Label("nodeAction", item.getModelObject().getName()));
	    }
	};
	nodeActionsContainer.add(nodeActionsView);

	final MarkupContainer nodePresentationContainer = new WebMarkupContainer("nodePresentationContainer");
	nodePresentationContainer.add(new WebMarkupContainer("nodePresentation"));
	nodePresentationContainer.setOutputMarkupId(true);
	add(nodePresentationContainer);

	final ArchiveTreePanel<SerializableCorpusNode> treePanel = new ArchiveTreePanel<SerializableCorpusNode>("treePanel", treeModelProvider) {
	    //TODO: Loop over selected nodes
	    @Override
	    protected void onNodeLinkClicked(AjaxRequestTarget target, SerializableCorpusNode node) {
		// Add the node type to the node type label
		NodeType nodeType = nodeTypeIdentifier.getNodeType(node.getUri());
		nodeTypeLabelModel.setObject(nodeType.getName());
		target.add(nodeTypeLabel);

		// Add the node actions to the actions list
		nodeActions.clear();
		final List<NodeAction> selectedNodeActions = nodeActionsProvider.getNodeActions(node.getUri(), nodeType);
		if (selectedNodeActions != null) {
		    nodeActions.addAll(selectedNodeActions);
		}
		target.add(nodeActionsContainer);

		// Add the node presentation component to the presentation container
		final Component nodePresentation = nodePresentationProvider.getNodePresentation("nodePresentation", node.getUri(), nodeType);
		nodePresentationContainer.addOrReplace(nodePresentation);
		target.add(nodePresentationContainer);
	    }
	};
	add(treePanel);
    }
}

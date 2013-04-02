package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
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

	final ArchiveTreePanel<SerializableCorpusNode> treePanel = new ArchiveTreePanel<SerializableCorpusNode>("treePanel", treeModelProvider) {
	    @Override
	    protected void onNodeLinkClicked(AjaxRequestTarget target, SerializableCorpusNode node) {
		NodeType nodeType = nodeTypeIdentifier.getNodeType(node.getUri());
		nodeTypeLabelModel.setObject(nodeType.getName());
		target.add(nodeTypeLabel);
	    }
	};
	add(treePanel);
    }
}

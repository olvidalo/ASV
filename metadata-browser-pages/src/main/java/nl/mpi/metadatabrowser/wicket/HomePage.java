package nl.mpi.metadatabrowser.wicket;

import java.io.Serializable;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeIconProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanelListener;
import nl.mpi.metadatabrowser.wicket.components.NodesPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.tree.LinkType;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage<SerializableCorpusNode extends CorpusNode & Serializable> extends WebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private GenericTreeModelProvider treeModelProvider;
    @SpringBean(required = false)
    private ArchiveTreeNodeIconProvider<CorpusNode> treeIconProvider;
    private NodesPanel nodesPanel;

    public HomePage(final PageParameters parameters) {
	super();

	// Add a panel hosting the archive tree, taking its structure from the injected tree model provider
	final ArchiveTreePanel treePanel = new ArchiveTreePanel("treePanel", treeModelProvider, treeIconProvider);
	treePanel.setLinkType(LinkType.AJAX_FALLBACK);
	add(treePanel);

	// Add a panel to show information and actions on the currently selected node(s)
	nodesPanel = new NodesPanel("nodesPanel", new CollectionModel(treePanel.getSelectedNodes()));
	nodesPanel.setOutputMarkupId(true);
	add(nodesPanel);

	// Link up the tree panel and nodes panel so that changes in the former get reflected in the latter
	treePanel.addArchiveTreePanelListener(new ArchiveTreePanelListener<SerializableCorpusNode>() {
	    @Override
	    public void nodeSelectionChanged(AjaxRequestTarget target, ArchiveTreePanel<SerializableCorpusNode> treePanel) {
		nodesPanel.setModelObject(treePanel.getSelectedNodes());
		if (target != null) {
		    target.add(nodesPanel);
		}
	    }
	});
    }
}

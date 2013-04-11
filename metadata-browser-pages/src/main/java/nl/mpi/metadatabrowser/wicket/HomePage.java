package nl.mpi.metadatabrowser.wicket;

import nl.mpi.metadatabrowser.wicket.components.NodesPanel;
import java.io.Serializable;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeModelProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanel;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreePanelListener;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage<SerializableCorpusNode extends CorpusNode & Serializable> extends WebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private GenericTreeModelProvider treeModelProvider;
    private NodesPanel nodesPanel;

    public HomePage(final PageParameters parameters) {
	super();

	final ArchiveTreePanel treePanel = new ArchiveTreePanel("treePanel", treeModelProvider);
	add(treePanel);

	nodesPanel = new NodesPanel("nodesPanel", new CollectionModel(treePanel.getSelectedNodes()));
	nodesPanel.setOutputMarkupId(true);
	add(nodesPanel);

	treePanel.addArchiveTreePanelListener(new ArchiveTreePanelListener<SerializableCorpusNode>() {
	    @Override
	    public void nodeSelectionChanged(AjaxRequestTarget target, ArchiveTreePanel<SerializableCorpusNode> treePanel) {
		nodesPanel.setModelObject(treePanel.getSelectedNodes());
		target.add(nodesPanel);
	    }
	});
    }
}

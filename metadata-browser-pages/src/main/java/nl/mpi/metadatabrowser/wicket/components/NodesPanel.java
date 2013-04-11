/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.metadatabrowser.wicket.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import nl.mpi.metadatabrowser.wicket.model.TypedSerializableCorpusNode;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel combining an actions panel and a node presentation component based on a collection of corpus nodes (typically a selection from the
 * tree)
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodesPanel<SerializableCorpusNode extends CorpusNode & Serializable> extends GenericPanel<Collection<SerializableCorpusNode>> implements Serializable {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private NodeTypeIdentifier nodeTypeIdentifier;
    @SpringBean
    private NodeActionsProvider nodeActionsProvider;
    @SpringBean
    private NodePresentationProvider nodePresentationProvider;
    // Child components
    private final NodesActionsPanel nodeActionsPanel;
    private final MarkupContainer nodePresentationContainer;

    public NodesPanel(String id, IModel<Collection<SerializableCorpusNode>> model) {
	super(id, model);

	nodeActionsPanel = new NodesActionsPanel("nodeActions");
	nodeActionsPanel.setOutputMarkupId(true);
	add(nodeActionsPanel);

	nodePresentationContainer = new WebMarkupContainer("nodePresentationContainer");
	nodePresentationContainer.add(new WebMarkupContainer("nodePresentation"));
	nodePresentationContainer.setOutputMarkupId(true);
	add(nodePresentationContainer);
    }

    @Override
    protected void onModelChanged() {
	super.onModelChanged();

	final Collection<TypedCorpusNode> typedNodes = getTypedCorpusNodes(getModelObject());
	updateNodeActions(typedNodes);
	updateNodePresentation(typedNodes);
    }

    private Collection<TypedCorpusNode> getTypedCorpusNodes(final Collection<SerializableCorpusNode> selectedNodes) {
	final Collection<TypedCorpusNode> typedNodes = new ArrayList<TypedCorpusNode>(selectedNodes.size());
	for (SerializableCorpusNode node : selectedNodes) {
	    // Get the node type from the node type identifier
	    final NodeType nodeType = nodeTypeIdentifier.getNodeType(node);
	    typedNodes.add(new TypedSerializableCorpusNode(node, nodeType));
	}
	return typedNodes;
    }

    private void updateNodeActions(final Collection<TypedCorpusNode> typedNodes) {
	// Get the node actions and update the model of the node actions panel
	final List<NodeAction> selectedNodeActions = nodeActionsProvider.getNodeActions(typedNodes);
	nodeActionsPanel.setModelObject(new NodeActionsStructure(typedNodes, selectedNodeActions));
    }

    private void updateNodePresentation(final Collection<TypedCorpusNode> typedNodes) {
	// Add the node presentation component to the presentation container (or remove if none is available)
	final Component nodePresentation = nodePresentationProvider.getNodePresentation("nodePresentation", typedNodes);
	if (nodePresentation == null) {
	    nodePresentationContainer.addOrReplace(new WebMarkupContainer("nodePresentation"));
	} else {
	    nodePresentationContainer.addOrReplace(nodePresentation);
	}
    }
}

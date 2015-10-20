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

import nl.mpi.metadatabrowser.wicket.NodeViewLinkModel;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import nl.mpi.metadatabrowser.wicket.HighlightSearchTermBehavior;
import nl.mpi.metadatabrowser.wicket.NodeActionProxy;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import nl.mpi.metadatabrowser.wicket.model.TypedSerializableCorpusNode;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel combining an actions panel and a node presentation component based on a
 * collection of corpus nodes (typically a selection from the tree)
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodesPanel<SerializableCorpusNode extends CorpusNode & Serializable> extends GenericPanel<Collection<SerializableCorpusNode>> implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodesPanel.class);
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
    private final Component bookmarkLink;

    public NodesPanel(String id, final IModel<Collection<SerializableCorpusNode>> model) {
        super(id, model);

        // add a panel to show feedback information (updated on model change, i.e. node selection)
        add(new FeedbackPanel("feedbackPanel")).setOutputMarkupId(true);

        final ModalWindow bookmarkDialogue = createBookmarkDialogue();
        bookmarkLink = createBookmarkLink(model, bookmarkDialogue);
        add(bookmarkLink);

        // Add a panel to show the actions available for the selected nodes (updated on model change, i.e. node selection)
        nodeActionsPanel = new NodesActionsPanel("nodeActions");
        nodeActionsPanel.setOutputMarkupId(true);
        add(nodeActionsPanel);

        // Add a panel for node visualisation and/or output of node actions (updated on model change, i.e. node selection)
        nodePresentationContainer = new WebMarkupContainer("nodePresentationContainer");
        nodePresentationContainer.add(new WebMarkupContainer("nodePresentation"));
        nodePresentationContainer.setOutputMarkupId(true);
        add(nodePresentationContainer);

        add(new HighlightSearchTermBehavior() {

            @Override
            protected String getComponentSelector(String componentId) {
                return String.format("#%s #nodePresentation", componentId);
            }

            @Override
            protected String getQueryParam() {
                return "token";
            }

        });
    }

    private ModalWindow createBookmarkDialogue() {
        final ModalWindow modalWindow = new ModalWindow("bookmarkdialogue");
        modalWindow
                .setTitle("Bookmark or link to this node")
                // Dimensions
                .setInitialWidth(36).setWidthUnit("em")
                .setInitialHeight(10).setHeightUnit("em")
                .setResizable(false)
                // Looks
                .setCssClassName(ModalWindow.CSS_CLASS_GRAY)
                .setMaskType(ModalWindow.MaskType.SEMI_TRANSPARENT)
                // Don't try to prevent the user from clicking a link in the dialogue
                .showUnloadConfirmation(false);
        add(modalWindow);
        return modalWindow;
    }

    private Component createBookmarkLink(final IModel<Collection<SerializableCorpusNode>> model, final ModalWindow bookmarkDialogue) {
        final Component link = new ExternalLink("bookmark", new NodeViewLinkModel(model));
        link.add(new AjaxEventBehavior("onclick") {

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                bookmarkDialogue.addOrReplace(new BookmarkDialogue(bookmarkDialogue.getContentId(), model));
                bookmarkDialogue.show(target);
            }
        });
        return link;
    }

    @Override
    protected void onModelChanged() {
        // This gets called when the node selection changes, e.g. one or more new nodes get selected or unselected
        super.onModelChanged();

        final Collection<TypedCorpusNode> typedNodes = getTypedCorpusNodes(getModelObject());
        updateNodeActions(typedNodes);
        updateNodePresentation(typedNodes);
    }

    /**
     * Wraps the selected nodes into typed corpus nodes (using the
     * {@link NodeTypeIdentifier} to get the node type for each node)
     *
     * @param selectedNodes nodes to get typed versions for
     * @return
     */
    private Collection<TypedCorpusNode> getTypedCorpusNodes(final Collection<SerializableCorpusNode> selectedNodes) {
        final Collection<TypedCorpusNode> typedNodes = new ArrayList<TypedCorpusNode>(selectedNodes.size());
        for (SerializableCorpusNode node : selectedNodes) {
            // Get the node type from the node type identifier
            try {
                final NodeType nodeType = nodeTypeIdentifier.getNodeType(node);
                typedNodes.add(new TypedSerializableCorpusNode(node, nodeType));
            } catch (NodeTypeIdentifierException ex) {
                logger.warn("Could not determine node type for node {}. Adding as unknown type.", node, ex);
                warn("Error identifying node type: " + ex.getMessage());
                typedNodes.add(new TypedSerializableCorpusNode(node, NodeType.UNKNOWN));
            }
        }
        return typedNodes;
    }

    /**
     * Gets the node actions and update the model of the node actions panel
     *
     * @param typedNodes selected nodes with type information
     */
    private void updateNodeActions(final Collection<TypedCorpusNode> typedNodes) {
        final List<NodeAction> selectedNodeActions = nodeActionsProvider.getNodeActions(typedNodes);
        // convert node actions to actions that are safe for serialisation
        final List<NodeAction> safeNodeActions = NodeActionProxy.getProxyList(selectedNodeActions);
        nodeActionsPanel.setModelObject(new NodeActionsStructure(typedNodes, safeNodeActions));
    }

    /**
     * Adds the node presentation component to the presentation container (or
     * remove if none is available)
     *
     * @param typedNodes selected nodes with type information
     */
    private void updateNodePresentation(final Collection<TypedCorpusNode> typedNodes) {
        try {
            final Component nodePresentation = nodePresentationProvider.getNodePresentation("nodePresentation", typedNodes);
            if (nodePresentation == null) {
                nodePresentationContainer.addOrReplace(new WebMarkupContainer("nodePresentation"));
            } else {
                nodePresentationContainer.addOrReplace(nodePresentation);
            }
        } catch (NodePresentationException ex) {
            logger.warn("Error while updating node presentation for {}", typedNodes, ex);
            error(ex.getMessage());
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        // render a canonical URL header so that Google can index stateless references
        final String viewLink = new NodeViewLinkModel(getModel()).getObject();
        if (!Strings.isEmpty(viewLink)) {
            final URI cannonicalRef
                    = URI.create(((HttpServletRequest) getRequest().getContainerRequest()).getRequestURL().toString()).resolve(viewLink);

            response.render(new StringHeaderItem(String.format(""
                    + "<link rel=\"canonical\" "
                    + "href=\"%s\" />", Strings.escapeMarkup(cannonicalRef.toString()))));
        }
    }

    @Override
    protected void onConfigure() {
        bookmarkLink.setVisible(getModelObject().size() == 1);
    }

}

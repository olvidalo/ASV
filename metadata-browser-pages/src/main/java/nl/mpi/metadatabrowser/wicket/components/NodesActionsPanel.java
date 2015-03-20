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

import java.util.Collection;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.StyleSpecifier;
import nl.mpi.metadatabrowser.model.TargetSpecifier;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsListModel;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.proxy.ILazyInitProxy;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * Panel that shows {@link NodeActionLink}s for each of the actions in the model
 * of type {@link NodeActionsStructure}
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public final class NodesActionsPanel extends GenericPanel<NodeActionsStructure> {
    // overlaping javascript caused tree to reload on each request. animateLoad.js is now inline in correpsonding html. JQuery was duplicated with wicket.

    private final static CssResourceReference NodesActionsPanel_CSS = new CssResourceReference(NodesActionsPanel.class, "res/nodeActionsPanel.css");

    /**
     *
     * @param id
     */
    public NodesActionsPanel(String id) {
        this(id, new NodeActionsStructure());
    }

    /**
     * Method that build the nodeAction button using wicket and decide whether
     * to open in a new window/tab Handle renaming if necessary, class
     * attribution and title.
     *
     * @param id
     * @param model
     */
    public NodesActionsPanel(String id, NodeActionsStructure model) {
        super(id, new Model<>(model));

        add(new ListView<NodeAction>("nodeActions", new NodeActionsListModel(getModel())) {
            @Override
            protected void populateItem(ListItem<NodeAction> item) {
                final NodeAction action = item.getModelObject();
                
                // inner node action that may not be serializable but implements all original interfaces
                final NodeAction innerAction;
                if (action instanceof ILazyInitProxy) {
                    //unwrap proxy
                    final Object proxyTarget = ((ILazyInitProxy) action).getObjectLocator().locateProxyTarget();
                    innerAction = (NodeAction) proxyTarget;
                } else {
                    innerAction = action;
                }
                
                final Collection<TypedCorpusNode> nodes = NodesActionsPanel.this.getModelObject().getNodes();
                final Link actionLink = new NodeActionLink("nodeActionLink", nodes, action);

                // the action may explicitly require the result to be shown in a new tab/window
                if (innerAction instanceof TargetSpecifier && ((TargetSpecifier) innerAction).openInNew()) {
                    actionLink.add(new AttributeModifier("target", "_blank"));
                }

                actionLink.add(new Label("linkLabel", action.getName()));
                actionLink.add(new AttributeAppender("class", "btn btn-3 btn-3b " + getClassName(innerAction)));
                item.add(actionLink);
                item.add(new AttributeAppender("title", action.getTitle()));
            }
        });
        
        final ModalWindow modalWindow = new ModalWindow("actionselectiondialogue");
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
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(NodesActionsPanel_CSS));
    }

    private String getClassName(final NodeAction action) {
        final String className;
        if (action instanceof StyleSpecifier) {
            className = ((StyleSpecifier) action).getStyleClass();
        } else {
            className = action.getName().replaceAll("\\s", "");
        }
        return className;
    }
}

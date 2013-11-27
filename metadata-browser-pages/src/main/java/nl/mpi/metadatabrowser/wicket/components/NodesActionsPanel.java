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
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsListModel;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Panel that shows {@link NodeActionLink}s for each of the actions in the model
 * of type {@link NodeActionsStructure}
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public final class NodesActionsPanel extends GenericPanel<NodeActionsStructure> {
    // overlaping javascript caused tree to reload on each request. animateLoad.js is now inline in correpsonding html. JQuery was duplicated with wicket.
//    private static final JavaScriptResourceReference JQuery = new JavaScriptResourceReference(NodesActionsPanel.class, "res/jquery-1.3.2.js");
//    private static final JavaScriptResourceReference ANIMATE = new JavaScriptResourceReference(NodesActionsPanel.class, "res/animateOnLoad.js");
    private final static CssResourceReference NodesActionsPanel_CSS = new CssResourceReference(NodesActionsPanel.class, "res/nodeActionsPanel.css");

    public NodesActionsPanel(String id) {
        this(id, new NodeActionsStructure());
    }

    public NodesActionsPanel(String id, NodeActionsStructure model) {
        super(id, new Model<NodeActionsStructure>(model));

        add(new FeedbackPanel("feedbackPanel")).setOutputMarkupId(true);
        add(new ListView<NodeAction>("nodeActions", new NodeActionsListModel(getModel())) {
            @Override
            protected void populateItem(ListItem<NodeAction> item) {
                final NodeAction action = item.getModelObject();
                final Collection<TypedCorpusNode> nodes = NodesActionsPanel.this.getModelObject().getNodes();
                Link actionLink = new NodeActionLink("nodeActionLink", nodes, action);
                if (action.getName().equals("Metadata Search") || action.getName().equals("Resource Access (RRS)") || action.getName().equals("Annotation Content Search") || action.getName().equals("Manage Access Rights")) {
                    actionLink.add(new AttributeModifier("target", "_blank"));
                }
                actionLink.add(new Label("linkLabel", action.getName()));
                item.add(actionLink);
                String className = action.getName().replaceAll("\\s", "");
                item.add(new AttributeAppender("class", className));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        //response.render(JavaScriptReferenceHeaderItem.forReference(JQuery));
        //response.render(JavaScriptReferenceHeaderItem.forReference(ANIMATE));
        response.render(CssHeaderItem.forReference(NodesActionsPanel_CSS));
    }
}

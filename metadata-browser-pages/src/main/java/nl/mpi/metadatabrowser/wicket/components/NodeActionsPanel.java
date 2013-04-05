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

import java.net.URI;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsListModel;
import nl.mpi.metadatabrowser.wicket.model.NodeActionsStructure;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public final class NodeActionsPanel extends GenericPanel<NodeActionsStructure> {

    public NodeActionsPanel(String id) {
	this(id, new NodeActionsStructure());
    }

    public NodeActionsPanel(String id, NodeActionsStructure model) {
	super(id, new Model<NodeActionsStructure>(model));

	final Form form = new Form("nodeActionsForm");

	form.add(new FeedbackPanel("feedbackPanel")).setOutputMarkupId(true);

	form.add(new ListView<NodeAction>("nodeActions", new NodeActionsListModel(getModel())) {
	    @Override
	    protected void populateItem(ListItem<NodeAction> item) {
		final NodeAction action = item.getModelObject();
		final URI nodeUri = NodeActionsPanel.this.getModelObject().getNodeUri();
		item.add(new NodeActionButton("nodeActionButton", nodeUri, action));
	    }
	});
	add(form);
    }
}

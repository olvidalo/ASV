/*
 * Copyright (C) 2015 Max Planck Institute for Psycholinguistics
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
import java.util.List;
import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.StyleSpecifier;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.proxy.ILazyInitProxy;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ActionSelectionPanel extends GenericPanel<ActionSelectionRequest> {

    @SpringBean(name = "actionRequestHandler")
    private ControllerActionRequestHandler actionRequestHandler;

    public ActionSelectionPanel(String id, final IModel<ActionSelectionRequest> model, final ModalWindow modalWindow) {
        super(id, model);
        add(new ListView<NodeAction>("action", new PropertyModel<List<NodeAction>>(model, "nodeActions")) {

            @Override
            protected void populateItem(final ListItem<NodeAction> item) {
                final IModel<NodeAction> actionModel = item.getModel();
                final Link actionLink = new AjaxFallbackLink("select") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (modalWindow != null && target != null) {
                            modalWindow.close(target);
                        }

                        // this action
                        final NodeAction action = actionModel.getObject();

                        // nodes to apply on
                        final Collection<TypedCorpusNode> nodes = model.getObject().getNodes();

                        final NodeActionHandler nodeActionHandler = new NodeActionHandler(action, nodes);
                        nodeActionHandler.handle(actionRequestHandler, this, target);
                    }
                };

                actionLink.add(new Label("name", new PropertyModel(actionModel, "name")));
                actionLink.add(new Label("description", new PropertyModel(actionModel, "title")));

                actionLink.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {

                    @Override
                    public String getObject() {

                        final NodeAction action = actionModel.getObject();
                        // inner node action that may not be serializable but implements all original interfaces
                        final NodeAction innerAction;
                        if (action instanceof ILazyInitProxy) {
                            //unwrap proxy
                            final Object proxyTarget = ((ILazyInitProxy) action).getObjectLocator().locateProxyTarget();
                            innerAction = (NodeAction) proxyTarget;
                        } else {
                            innerAction = action;
                        }
                        return NodesActionsPanel.getClassName(innerAction);
                    }
                }));

                item.add(actionLink);

            }
        });
    }

}

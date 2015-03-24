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
package nl.mpi.metadatabrowser.wicket.services.impl;

import java.util.Collection;
import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.components.ActionSelectionPanel;
import nl.mpi.metadatabrowser.wicket.components.NodeActionHandler;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Handles action selection requests, if possible by presenting the user with an
 * action selection dialogue; has a non-javascript fallback in case the action
 * provides a default action
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @see ActionSelectionPanel
 */
public class ActionSelectionRequestHandler implements ControllerActionRequestHandler<ActionSelectionRequest> {

    private final static Logger logger = LoggerFactory.getLogger(ActionSelectionRequestHandler.class);

    @Autowired
    @Qualifier("actionRequestHandler")
    private ControllerActionRequestHandler actionRequestHandler;

    protected ActionSelectionRequestHandler() {
    }

    protected ActionSelectionRequestHandler(ControllerActionRequestHandler actionRequestHandler) {
        this.actionRequestHandler = actionRequestHandler;
    }

    @Override
    public void handleActionRequest(RequestCycle requestCycle, ActionSelectionRequest actionRequest, Page originatingPage, AjaxRequestTarget target) throws RequestHandlerException {
        if (target == null) {
            // no Ajax request, we cannot ask user (modal window requires Javascript) so execute default action
            executeDefaultAction(actionRequest, originatingPage);
        } else {
            // show a modal window that allows the user to select an action
            askUser(actionRequest, originatingPage, target);
        }
    }

    /**
     * tries to execute the default action
     *
     * @param actionRequest
     * @param originatingPage
     */
    private void executeDefaultAction(ActionSelectionRequest actionRequest, Page originatingPage) {
        final NodeAction defaultAction = actionRequest.getDefaultAction();
        
        if (defaultAction == null) {
            logger.warn("Action selection request does not define a default action");
            Session.get().warn("There is no default action. Selection dialogue could not be shown. Please enable javascript.");
        } else {
            final Collection<TypedCorpusNode> nodes = actionRequest.getNodes();
            final NodeActionHandler nodeActionHandler = new NodeActionHandler(defaultAction, nodes);
            nodeActionHandler.handle(actionRequestHandler, originatingPage, null);
        }
    }

    /**
     * opens a modal dialogue that allows the user to choose an action
     *
     * @param actionRequest
     * @param originatingPage
     * @param target
     */
    private void askUser(ActionSelectionRequest actionRequest, Page originatingPage, AjaxRequestTarget target) {
        // get the modal window component in the node actions panel
        final Component component = originatingPage.get("nodesPanel:nodeActions:actionselectiondialogue");
        if (component instanceof ModalWindow) {
            final ModalWindow modalWindow = (ModalWindow) component;

            // create a panel with action options 
            final IModel<ActionSelectionRequest> actionsModel = Model.of(actionRequest);
            final ActionSelectionPanel actionSelectionPanel = new ActionSelectionPanel(modalWindow.getContentId(), actionsModel, modalWindow);
            modalWindow.addOrReplace(actionSelectionPanel);
            modalWindow.show(target);
        } else {
            // unexpected - wrong page?
            logger.warn("Could not find modal window component to show action selection!");
            Session.get().error("Could not show action options");
        }
    }

}

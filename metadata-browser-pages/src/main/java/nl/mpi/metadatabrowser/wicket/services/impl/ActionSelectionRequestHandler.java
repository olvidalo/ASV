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

import java.util.List;
import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.wicket.components.ActionSelectionPanel;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ActionSelectionRequestHandler implements ControllerActionRequestHandler<ActionSelectionRequest> {
    
    private final static Logger logger = LoggerFactory.getLogger(ActionSelectionRequestHandler.class);
    
    @Override
    public void handleActionRequest(RequestCycle requestCycle, ActionSelectionRequest actionRequest, Page originatingPage, AjaxRequestTarget target) throws RequestHandlerException {
        // get the modal window component in the node actions panel
        final Component modalWindowComponent = originatingPage.get("nodesPanel:nodeActions:actionselectiondialogue");
        if (modalWindowComponent instanceof ModalWindow) {
            showActions((ModalWindow) modalWindowComponent, actionRequest.getNodeActions());
        } else {
            // unexpected - wrong page?
            logger.warn("Could not find modal window component to show action selection!");
            Session.get().error("Could not show action options");
        }
    }
    
    private void showActions(ModalWindow modalWindow, List<NodeAction> nodeActions) {
        // create a panel with action options 
        final ActionSelectionPanel actionSelectionPanel = new ActionSelectionPanel(modalWindow.getContentId(), new ListModel<>(nodeActions));
        modalWindow.addOrReplace(actionSelectionPanel);
    }
    
}

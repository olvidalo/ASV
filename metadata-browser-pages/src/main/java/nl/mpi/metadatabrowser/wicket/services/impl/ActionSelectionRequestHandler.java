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

import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.wicket.components.ActionSelectionPanel;
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

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ActionSelectionRequestHandler implements ControllerActionRequestHandler<ActionSelectionRequest> {

    private final static Logger logger = LoggerFactory.getLogger(ActionSelectionRequestHandler.class);

    @Override
    public void handleActionRequest(RequestCycle requestCycle, ActionSelectionRequest actionRequest, Page originatingPage, AjaxRequestTarget target) throws RequestHandlerException {
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

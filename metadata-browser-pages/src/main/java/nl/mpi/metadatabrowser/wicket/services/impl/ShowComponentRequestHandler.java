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
package nl.mpi.metadatabrowser.wicket.services.impl;

import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ShowComponentRequestHandler implements ControllerActionRequestHandler<ShowComponentRequest> {

    @Override
    public void handleActionRequest(RequestCycle requestCycle, ShowComponentRequest actionRequest, Page originatingPage) throws RequestHandlerException {
	try {
	    final Component component = actionRequest.getComponent("nodePresentation");
	    final MarkupContainer container = (MarkupContainer) originatingPage.get("nodesPanel:nodePresentationContainer");
	    container.addOrReplace(component);
	} catch (ControllerActionRequestException ex) {
	    throw new RequestHandlerException("Error while getting component to show for action result", ex);
	}
    }
}

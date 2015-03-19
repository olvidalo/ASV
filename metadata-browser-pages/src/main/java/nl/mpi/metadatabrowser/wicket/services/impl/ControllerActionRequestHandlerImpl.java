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

import nl.mpi.metadatabrowser.model.ActionSelectionRequest;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.DownloadRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Delegation action request handler that wraps specialized handlers and calls
 * these depending on the type of the incoming {@link ControllerActionRequest}
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ControllerActionRequestHandlerImpl implements ControllerActionRequestHandler<ControllerActionRequest> {

    private final ControllerActionRequestHandler<NavigationRequest> navigationRequestHandler;
    private final ControllerActionRequestHandler<DownloadRequest> downloadRequestHandler;
    private final ControllerActionRequestHandler<ShowComponentRequest> showComponentRequestHandler;
    private final ControllerActionRequestHandler<ActionSelectionRequest> actionSelectionRequestHandler;

    public ControllerActionRequestHandlerImpl(ControllerActionRequestHandler<NavigationRequest> navigationRequestHandler, ControllerActionRequestHandler<DownloadRequest> downloadRequestHandler, ControllerActionRequestHandler<ShowComponentRequest> showComponentRequestHandler, ControllerActionRequestHandler<ActionSelectionRequest> actionSelectionRequestHandler) {
        this.navigationRequestHandler = navigationRequestHandler;
        this.downloadRequestHandler = downloadRequestHandler;
        this.showComponentRequestHandler = showComponentRequestHandler;
        this.actionSelectionRequestHandler = actionSelectionRequestHandler;
    }

    /**
     * Handles an action request on the provided request cycle. Assumes that the
     * specialized handlers have been set.
     *
     * @param requestCycle current request cycle to act on
     * @param actionRequest action request to handle
     * @throws RequestHandlerException when actionRequest is of a type that this
     * handler cannot process or if an error occurs within one of the wrapped
     * handlers
     */
    @Override
    public void handleActionRequest(RequestCycle requestCycle, ControllerActionRequest actionRequest, Page originatingPage) throws RequestHandlerException {
        if (actionRequest instanceof NavigationRequest) {
            navigationRequestHandler.handleActionRequest(requestCycle, (NavigationRequest) actionRequest, originatingPage);
        } else if (actionRequest instanceof DownloadRequest) {
            downloadRequestHandler.handleActionRequest(requestCycle, (DownloadRequest) actionRequest, originatingPage);
        } else if (actionRequest instanceof ShowComponentRequest) {
            showComponentRequestHandler.handleActionRequest(requestCycle, (ShowComponentRequest) actionRequest, originatingPage);
        } else if (actionRequest instanceof ActionSelectionRequest) {
            actionSelectionRequestHandler.handleActionRequest(requestCycle, (ActionSelectionRequest) actionRequest, originatingPage);
        } else {
            throw new RequestHandlerException("Cannot handle action request of type " + actionRequest.getClass().getName());
        }
    }
}

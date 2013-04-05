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

import nl.mpi.metadatabrowser.model.actionrequest.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.actionrequest.NavigationRequest;
import nl.mpi.metadatabrowser.wicket.HomePage;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of handler for {@link ControllerActionRequest}s
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ControllerActionRequestHandlerImpl implements ControllerActionRequestHandler {

    private final static Logger logger = LoggerFactory.getLogger(ControllerActionRequestHandlerImpl.class);
    private String rrsUrl;

    /**
     * Handles an action request on the provided request cycle
     *
     * @param requestCycle current request cycle to act on
     * @param actionRequest action request to handle
     */
    @Override
    public void handleActionRequest(RequestCycle requestCycle, ControllerActionRequest actionRequest) {
	if (actionRequest instanceof NavigationRequest) {
	    handleNavigationRequest(requestCycle, (NavigationRequest) actionRequest);
	}
    }

    private void handleNavigationRequest(RequestCycle requestCycle, NavigationRequest request) {
	switch (request.getTarget()) {
	    case RRS:
		logger.debug("Received request to navigate to RRS with parameters {}", request.getParameters());
		// Navigate to RRS
		// TODO: Parameters
		requestCycle.scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(rrsUrl));
		break;
	    case NODE:
		logger.debug("Received request to navigate to node with parameters {}", request.getParameters());
		// TODO: Navigate to Node
		requestCycle.setResponsePage(HomePage.class);
		break;
	    default:
		// Other, cannot handle
		logger.warn("Don't know how to handle navigation request target {}", request.getTarget());
	}
    }

    /**
     *
     * @param rrsUrl Base URL of Resource Request System
     * @see NavigationRequest.NavigationTarget#RRS
     */
    public void setRrsUrl(String rrsUrl) {
	this.rrsUrl = rrsUrl;
    }
}

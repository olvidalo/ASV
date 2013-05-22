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

import java.util.EnumMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NavigationRequestHandler implements ControllerActionRequestHandler<NavigationRequest> {

    private final static Logger logger = LoggerFactory.getLogger(NavigationRequestHandler.class);
    private final Map<NavigationTarget, String> targetUrls = new EnumMap<NavigationTarget, String>(NavigationTarget.class);

    @Override
    public void handleActionRequest(RequestCycle requestCycle, NavigationRequest actionRequest, Page originatingPage) throws RequestHandlerException {
	logger.debug("Received request to navigate to RRS with parameters {}", actionRequest.getParameters());
	if (targetUrls.containsKey(actionRequest.getTarget())) {
	    redirectToUrl(requestCycle, targetUrls.get(actionRequest.getTarget()));
	} else {
	    throw new RequestHandlerException("Don't know how to handle navigation request target " + actionRequest.getTarget());
	}
    }

    private void redirectToUrl(RequestCycle requestCycle, String url) {
	// TODO: Parameters?
	requestCycle.scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(url));
    }

    /**
     *
     * @param rrsUrl Base URL of Resource Request System
     * @see NavigationRequest.NavigationTarget#RRS
     */
    public void setRrsUrl(String rrsUrl) {
	targetUrls.put(NavigationTarget.RRS, rrsUrl);
	logger.info("RRS url set to {}", rrsUrl);
    }

    public void setAmsUrl(String amsUrl) {
	targetUrls.put(NavigationTarget.AMS, amsUrl);
	logger.info("AMS url set to {}", amsUrl);
    }

    public void setAnnexUrl(String annexUrl) {
	targetUrls.put(NavigationTarget.ANNEX, annexUrl);
	logger.info("Annex url set to {}", annexUrl);
    }

    public void setContentSearchUrl(String contentSearchUrl) {
	targetUrls.put(NavigationTarget.TROVA, contentSearchUrl);
	logger.info("Content search url set to {}", contentSearchUrl);
    }

    public void setMdSearchUrl(String mdSearchUrl) {
	targetUrls.put(NavigationTarget.CMDISEARCH, mdSearchUrl);
	logger.info("Metadata serach url set to {}", mdSearchUrl);
    }
}

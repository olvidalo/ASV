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

import nl.mpi.metadatabrowser.model.DownloadRequest;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class DownloadRequestHandler implements ControllerActionRequestHandler<DownloadRequest> {

    private final static Logger logger = LoggerFactory.getLogger(DownloadRequestHandler.class);

    @Override
    public void handleActionRequest(RequestCycle requestCycle, DownloadRequest downloadRequest, Page originatingPage, AjaxRequestTarget target) {
	final IResourceStream stream = downloadRequest.getDownloadStream();
	final String fileName = downloadRequest.getFileName();
	logger.debug("Recieved request to offer download from stream as {} with content type {}", fileName, stream.getContentType());
	requestCycle.scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(stream, fileName));
    }
}

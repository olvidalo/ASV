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
package nl.mpi.metadatabrowser.wicket.services;

import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Handler of {@link ControllerActionRequest}s acting on the {@link RequestCycle} depending on the nature and details of the action request
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface ControllerActionRequestHandler<R extends ControllerActionRequest> {

    /**
     * Handles an action request on the provided request cycle
     *
     * @param requestCycle current request cycle to act on
     * @param actionRequest action request to handle
     * @param originatingPage page from which the action was called that returned the request to be handled
     * @param target ajax request target, can be null in case of non-Ajax click
     */
    void handleActionRequest(RequestCycle requestCycle, R actionRequest, Page originatingPage, AjaxRequestTarget target) throws RequestHandlerException;
}

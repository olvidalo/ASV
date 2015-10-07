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
package nl.mpi.metadatabrowser.model.cmdi;

import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.wicket.NodeActionAjaxListener;

/**
 * Immutable node action result that has a
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class SimpleNodeActionResult implements NodeActionResult {

    private final String feedbackMessage;
    private final ControllerActionRequest actionRequest;

    public SimpleNodeActionResult(ControllerActionRequest actionRequest) {
	this(actionRequest, null);
    }

    public SimpleNodeActionResult(String feedbackMessage) {
	this(null, feedbackMessage);
    }

    public SimpleNodeActionResult(ControllerActionRequest actionRequest, String feedbackMessage) {
	this.feedbackMessage = feedbackMessage;
	this.actionRequest = actionRequest;
    }

    @Override
    public final ControllerActionRequest getControllerActionRequest() {
	return actionRequest;
    }

    @Override
    public final String getFeedbackMessage() {
	return feedbackMessage;
    }

    /**
     * NOOP
     * @return null
     */
    @Override
    public NodeActionAjaxListener getAjaxListener() {
        return null;
    }
}

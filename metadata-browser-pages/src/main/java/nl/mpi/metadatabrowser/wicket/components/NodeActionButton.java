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
package nl.mpi.metadatabrowser.wicket.components;

import java.net.URI;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Button for offering and handling {@link NodeAction}s
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
class NodeActionButton extends Button {

    private final static Logger logger = LoggerFactory.getLogger(NodeActionButton.class);
    // Services
    @SpringBean
    private ControllerActionRequestHandler actionRequestHandler;
    // Properties
    private final NodeAction action;
    private final URI nodeUri;

    public NodeActionButton(String id, URI nodeUri, NodeAction action) {
	super(id, new Model<String>(action.getName()));
	this.nodeUri = nodeUri;
	this.action = action;
    }

    @Override
    public void onSubmit() {
	try {
	    final NodeActionResult result = action.execute(nodeUri);
	    handleFeedbackMessage(result);
	    actionRequestHandler.handleActionRequest(getRequestCycle(), result.getControllerActionRequest());
	} catch (NodeActionException ex) {
	    logger.warn("Error in execution of action {} on node {}", action.getName(), nodeUri, ex);
	    error(ex.getMessage());
	}
    }

    private void handleFeedbackMessage(final NodeActionResult result) {
	final String feedbackMessage = result.getFeedbackMessage();
	if (feedbackMessage != null) {
	    logger.debug("Feedback from action {} on node {}: {}", action.getName(), nodeUri, feedbackMessage);
	    info(feedbackMessage);
	}
    }
}

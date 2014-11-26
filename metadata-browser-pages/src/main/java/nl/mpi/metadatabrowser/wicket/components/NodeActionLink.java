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

import java.util.Collection;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Button for offering and handling {@link NodeAction}s on a collection of nodes
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
class NodeActionLink extends Link { //TODO: AjaxFallbackLink

    private final static Logger logger = LoggerFactory.getLogger(NodeActionLink.class);
    // Services
    @SpringBean
    private ControllerActionRequestHandler actionRequestHandler;
    // Properties
    private final NodeAction action;
    private final Collection<TypedCorpusNode> nodes;

    public NodeActionLink(String id, Collection<TypedCorpusNode> nodes, NodeAction action) {
	super(id, Model.of(action.getName()));
	this.nodes = nodes;
	this.action = action;
    }

    @Override
    public void onClick() {
	try {
	    final NodeActionResult result = action.execute(nodes);
	    handleFeedbackMessage(result);
	    handleActionRequest(result);
	} catch (NodeActionException ex) {
	    logger.warn("Error in execution of action {} on nodes {}", action.getName(), nodes, ex);
	    Session.get().error(ex.getMessage());
	} catch (RequestHandlerException ex) {
	    logger.warn("Error in handling action request returned by action {} on nodes {}", action.getName(), nodes, ex);
	    Session.get().warn("Could not complete action. Please contact the administrator.");
	}
    }

    private void handleFeedbackMessage(final NodeActionResult result) {
	final String feedbackMessage = result.getFeedbackMessage();
	if (feedbackMessage != null) {
	    logger.debug("Feedback from action {} on node {}: {}", action.getName(), nodes, feedbackMessage);
	    Session.get().info(feedbackMessage);
	}
    }

    private void handleActionRequest(final NodeActionResult result) throws RequestHandlerException {
	final ControllerActionRequest actionRequest = result.getControllerActionRequest();
	if (actionRequest != null) {
	    actionRequestHandler.handleActionRequest(getRequestCycle(), actionRequest, getPage());
	}
    }

}

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
package nl.mpi.metadatabrowser.wicket.components;

import java.io.Serializable;
import java.util.Collection;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import nl.mpi.metadatabrowser.wicket.services.RequestHandlerException;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler with most of the logic for node action links
 *
 * @see NodeActionLink
 * @see AjaxFallbackNodeActionLink
 * 
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodeActionHandler implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodeActionHandler.class);

    private final NodeAction action;
    private final Collection<TypedCorpusNode> nodes;

    /**
     * 
     * @param action action to handle
     * @param nodes nodes to perform action on
     */
    public NodeActionHandler(NodeAction action, Collection<TypedCorpusNode> nodes) {
        this.action = action;
        this.nodes = nodes;
    }

    /**
     * Handle a node click
     * 
     * @param actionRequestHandler request handler to trigger
     * @param component action component
     * @param target optional Ajax request target (can be null!)
     */
    public void handle(ControllerActionRequestHandler actionRequestHandler, Component component, AjaxRequestTarget target) {
        try {
            final NodeActionResult result = action.execute(nodes);
            handleFeedbackMessage(result);
            handleActionRequest(result, actionRequestHandler, component, target);
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

    private void handleActionRequest(final NodeActionResult result, ControllerActionRequestHandler actionRequestHandler, Component component, final AjaxRequestTarget target) throws RequestHandlerException {
        final ControllerActionRequest actionRequest = result.getControllerActionRequest();
        if (actionRequest != null) {
            actionRequestHandler.handleActionRequest(component.getRequestCycle(), actionRequest, component.getPage(), target);
        }
        if (target != null) {
            target.add(component.getPage().get("nodesPanel"));
        }
    }
}

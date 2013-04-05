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
package nl.mpi.metadatabrowser.wicket.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import nl.mpi.metadatabrowser.model.NodeAction;

/**
 * Structure containing the list of actions for a node identified by its URI
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodeActionsStructure implements Serializable {

    private URI nodeUri;

    public NodeActionsStructure() {
	this(null, Collections.<NodeAction>emptyList());
    }

    public NodeActionsStructure(URI nodeUri, List<NodeAction> nodeActions) {
	this.nodeUri = nodeUri;
	this.nodeActions = nodeActions;
    }

    /**
     * Get the value of nodeUri
     *
     * @return the value of nodeUri
     */
    public URI getNodeUri() {
	return nodeUri;
    }

    /**
     * Set the value of nodeUri
     *
     * @param nodeUri new value of nodeUri
     */
    public void setNodeUri(URI nodeUri) {
	this.nodeUri = nodeUri;
    }
    private List<NodeAction> nodeActions;

    /**
     * Get the value of nodeActions
     *
     * @return the value of nodeActions
     */
    public List<NodeAction> getNodeActions() {
	return nodeActions;
    }

    /**
     * Set the value of nodeActions
     *
     * @param nodeActions new value of nodeActions
     */
    public void setNodeActions(List<NodeAction> nodeActions) {
	this.nodeActions = nodeActions;
    }
}

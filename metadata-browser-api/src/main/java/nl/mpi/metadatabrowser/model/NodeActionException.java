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
package nl.mpi.metadatabrowser.model;

import java.net.URI;

/**
 * Exception thrown if an error occurs during execution of a node action
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @see NodeAction#execute(java.net.URI)
 */
public class NodeActionException extends Exception {

    private final NodeAction action;

    public NodeActionException(NodeAction action, String message) {
	super(message);
	this.action = action;
    }

    public NodeActionException(NodeAction action, Throwable cause) {
	super(cause);
	this.action = action;
    }

    public NodeActionException(NodeAction action, String message, Throwable cause) {
	super(message, cause);
	this.action = action;
    }

    /**
     *
     * @return the node action that was carried out when this exception occurred
     */
    public NodeAction getAction() {
	return action;
    }
}

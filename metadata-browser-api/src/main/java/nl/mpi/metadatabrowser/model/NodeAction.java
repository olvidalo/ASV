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
import java.util.Collection;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;

/**
 * Interface for node actions.
 *
 * Implementers are responsible for executing their action on the specified node in the {@link #execute(java.net.URI) } method and return
 * an appropriate result object. This result object may request further action from the controller by providing a controller action request
 * through {@link NodeActionResult#getControllerActionRequest() }.
 *
 * Generally it should be assumed that NodeActions may be reused, so it is not advised to keep state in the action itself.
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @see NodeActionsProvider
 */
public interface NodeAction {

    /**
     * Provides the name of this action for use on buttons etc.
     *
     * @return the name of this node action
     */
    String getName();

    /**
     * Executes the action on a list of nodes specified by their URIs
     *
     * @param nodeUris URI list of nodes to execute this action on
     * @return the result of the action
     * @throws NodeActionException if any error occurs during node exception
     */
    NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException;
}

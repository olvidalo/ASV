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
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Convenience base class for node actions that only act on single nodes
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public abstract class SingleNodeAction implements NodeAction {

    @Autowired
    protected AuthenticationHolder auth;

    /**
     * Executes {@link #execute(java.net.URI) } for the single node contained by
     * the node URIs list
     *
     * @param nodeUris singleton list of node URIs to execute the action on
     * @return the result of {@link #execute(java.net.URI) }
     * @throws NodeActionException if the provided list of node URIs does not
     * contain exactly one item or whatever gets thrown by
     * {@link #execute(nl.mpi.metadatabrowser.model.TypedCorpusNode) }
     */
    @Override
    public final NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        if (nodes.size() == 1) {
            return execute(nodes.iterator().next());
        } else {
            throw new NodeActionException(this, "This action can only be executed on single nodes");
        }
    }

    /**
     * Implements the action for a node
     *
     * @param node node to execute the action on
     * @return the result of the action
     * @throws NodeActionException if any error occurs during node exception
     */
    protected abstract NodeActionResult execute(TypedCorpusNode node) throws NodeActionException;

    public void setAuthenticationHolder(AuthenticationHolder auth) {
        this.auth = auth;
    }
}

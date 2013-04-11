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
package nl.mpi.metadatabrowser.services;

import java.util.Collection;
import java.util.List;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;

/**
 * Interface for a provider that can map nodes (identified by an URI) to lists of {@link NodeAction}s
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface NodeActionsProvider {

    /**
     * Gets a list of available actions for the specified node
     *
     * @param nodeUri URI of the node to provide actions for
     * @param nodeType node type of the node identified by nodeUri
     * @return a list of actions available for this node
     */
    List<NodeAction> getNodeActions(Collection<TypedCorpusNode> nodes);
}

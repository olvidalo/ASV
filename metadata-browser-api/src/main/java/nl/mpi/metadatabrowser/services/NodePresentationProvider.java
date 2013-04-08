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

import java.net.URI;
import nl.mpi.metadatabrowser.model.NodeType;
import org.apache.wicket.Component;

/**
 * Interface for a class that can provide a Wicket Component rendering a presentation of a given node
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface NodePresentationProvider {

    /**
     * Gets a renderable (through Wicket) component offering a presentation of the specified node
     *
     * @param wicketId string that the returned component should have as its id (see {@link Component#getId() }
     * @param nodeUri URI of the node to render a presentation for
     * @param nodeType the type of the node identified by nodeUri
     * @return a Wicket Component representing
     */
    Component getNodePresentation(String wicketId, URI nodeUri, NodeType nodeType);
}

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

/**
 * Interface for types of nodes. Mainly a 'flag' for the controller to retrieve from the service and pass on to other services, e.g.
 * implementations of {@link nl.mpi.metadatabrowser.services.NodeActionsProvider} and
 * {@link nl.mpi.metadatabrowser.services.NodePresentationProvider}. The valued returned by {@link #getName() } may be used for logging and
 * management purposes.
 *
 * It is advised to override the {@link #equals(java.lang.Object) } method.
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface NodeType {

    /**
     *
     * @return name of the type of node
     */
    String getName();
}

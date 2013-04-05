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
 * Interface for results returned after execution of node actions
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @see NodeAction#execute(java.net.URI)
 */
public interface NodeActionResult {

    /**
     * Provides a feedback message
     *
     * @return feedback message or null if not applicable
     */
    String getFeedbackMessage();

    /**
     * Provides an action request for the controller
     *
     * @return controller action request or null if not applicable
     */
    ControllerActionRequest getControllerActionRequest();
}

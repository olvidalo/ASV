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

import java.io.Serializable;
import nl.mpi.metadatabrowser.wicket.NodeActionAjaxListener;

/**
 * Interface for results returned after execution of node actions.
 *
 * Serializability of all implementations must be assured!
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @see NodeAction#execute(java.net.URI)
 */
public interface NodeActionResult extends Serializable {

    /**
     * Provides a feedback message
     *
     * @return feedback message or null if not applicable
     */
    String getFeedbackMessage();

    /**
     * Provides an action request for the controller. For this to be usable,
     * implementations of this interface should return an implementation of one
     * of the extensions of the ControllerActionRequest interface.
     *
     * @return controller action request or null if not applicable
     * @see NavigationRequest
     * @see DownloadRequest
     */
    ControllerActionRequest getControllerActionRequest();

    /**
     * Provides a listener that may respond to events in the wicket ajax
     * lifecylce
     *
     * @return a listener, can also be null
     */
    NodeActionAjaxListener getAjaxListener();
}

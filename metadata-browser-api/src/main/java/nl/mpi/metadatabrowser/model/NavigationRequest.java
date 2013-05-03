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

import java.util.Map;

/**
 * Interface for a request to navigate to a certain (parameterised) location
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface NavigationRequest extends ControllerActionRequest {

    /**
     * Target of a {@link NavigationRequest}
     */
    public enum NavigationTarget {

	/**
	 * Resource Request System
	 */
	RRS,
        /**
         * AMS
         */
        AMS,
        /**
         * TROVA
         */
        TROVA,
        /**
         * a CMDISEARCH
         */
        CMDISEARCH,
        /**
         * browse statistics for a node
         */
        STATS,
        ANNEX
    }

    /**
     *
     * @return the navigation target
     */
    NavigationTarget getTarget();

    /**
     *
     * @return named parameters (name, value) that should be applied to the navigation action
     */
    Map<String, String> getParameters();
}

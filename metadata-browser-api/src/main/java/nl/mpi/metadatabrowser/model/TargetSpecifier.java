/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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
 * Additional interface for Node Actions that explicitly require their result to
 * be displayed in a new tab or window
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface TargetSpecifier {

    /**
     *
     * @return whether the action result should be presented in a new tab or
     * window
     */
    boolean openInNew();

}

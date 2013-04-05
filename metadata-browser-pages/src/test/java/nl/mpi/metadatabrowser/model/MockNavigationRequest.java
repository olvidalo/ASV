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
import java.util.Map;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNavigationRequest implements NavigationRequest, Serializable {

    private NavigationTarget target;
    private Map<String, String> parameters;

    public void setTarget(NavigationTarget target) {
	this.target = target;
    }

    @Override
    public NavigationTarget getTarget() {
	return target;
    }

    public void setParameters(Map<String, String> parameters) {
	this.parameters = parameters;
    }

    @Override
    public Map<String, String> getParameters() {
	return parameters;
    }
}

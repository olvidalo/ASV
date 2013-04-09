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
package nl.mpi.metadatabrowser.services.mock;

import java.net.URI;
import java.util.List;
import java.util.Map;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeActionsProvider;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNodeActionsProvider implements NodeActionsProvider {

    private final Map<String, List<NodeAction>> uriNodeActionMap;

    public MockNodeActionsProvider(Map<String, List<NodeAction>> uriNodeActionMap) {
	this.uriNodeActionMap = uriNodeActionMap;
    }
    
    @Override
    public List<NodeAction> getNodeActions(URI nodeUri, NodeType nodeType) {
	return uriNodeActionMap.get(nodeUri.toString());
    }
}
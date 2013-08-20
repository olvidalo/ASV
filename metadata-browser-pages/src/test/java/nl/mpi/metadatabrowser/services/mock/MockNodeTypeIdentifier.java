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

import java.util.Map;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNodeTypeIdentifier implements NodeTypeIdentifier {

    private final Map<String, NodeType> uriNodeTypeMap;

    public MockNodeTypeIdentifier(Map<String, NodeType> uriNodeTypeMap) {
	this.uriNodeTypeMap = uriNodeTypeMap;
    }

    @Override
    public NodeType getNodeType(CorpusNode node) {
	if (node == null) {
	    return null;
	} else {
	    return uriNodeTypeMap.get(node.getNodeId().toString());
	}
    }
}

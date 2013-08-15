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
package nl.mpi.metadatabrowser.wicket.model;

import java.io.Serializable;
import java.net.URI;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;

/**
 * Wrapper for {@link CorpusNode} that also encapsulates a {@link NodeType}
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class TypedSerializableCorpusNode<SerializableCorpusNode extends CorpusNode & Serializable> implements TypedCorpusNode, Serializable {

    private final SerializableCorpusNode corpusNode;
    private final NodeType nodeType;

    public TypedSerializableCorpusNode(SerializableCorpusNode corpusNode, NodeType nodeType) {
	this.corpusNode = corpusNode;
	this.nodeType = nodeType;
    }

    @Override
    public NodeType getNodeType() {
	return nodeType;
    }

    @Override
    public URI getNodeId() {
	return corpusNode.getNodeId();
    }

    @Override
    public String getName() {
	return corpusNode.getName();
    }

    @Override
    public URI getUri() {
	return corpusNode.getUri();
    }

    public CorpusNode getCorpusNode() {
	return corpusNode;
    }

    @Override
    public String toString() {
	return corpusNode.toString();
    }
}

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
import java.util.Date;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.core.FileInfo;
import nl.mpi.archiving.corpusstructure.core.service.CorpusNodeWrapper;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;

/**
 * Wrapper for {@link CorpusNode} that also encapsulates a {@link NodeType}
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @param <SerializableCorpusNode>
 */
public class TypedSerializableCorpusNode<SerializableCorpusNode extends CorpusNode & Serializable> implements TypedCorpusNode, CorpusNodeWrapper, Serializable {

    private final SerializableCorpusNode corpusNode;
    private final NodeType nodeType;

    public TypedSerializableCorpusNode(SerializableCorpusNode corpusNode, NodeType nodeType) {
	this.corpusNode = corpusNode;
	this.nodeType = nodeType;
    }

    public CorpusNode getCorpusNode() {
	return corpusNode;
    }

    @Override
    public NodeType getNodeType() {
	return nodeType;
    }

    @Override 
    public URI getNodeURI() {
	return corpusNode.getNodeURI();
    }

    @Override
    public String getName() {
	return corpusNode.getName();
    }

    @Override
    public URI getProfile() {
	return corpusNode.getProfile();
    }

    @Override
    public FileInfo getFileInfo() {
	return corpusNode.getFileInfo();
    }

    @Override
    public CorpusNodeType getType() {
	return corpusNode.getType();
    }

    @Override
    public Date getLastUpdate() {
	return corpusNode.getLastUpdate();
    }

    @Override
    public boolean isOnSite() {
	return corpusNode.isOnSite();
    }

    @Override
    public String getFormat() {
	return corpusNode.getFormat();
    }

    @Override
    public String toString() {
	return corpusNode.toString();
    }

    @Override
    public CorpusNode getInnerCorpusNode() {
	return corpusNode;
    }

    @Override
    public CorpusNode getOlderVersion() {
        return corpusNode.getOlderVersion();
    }

    @Override
    public CorpusNode getNewerVersion() {
        return corpusNode.getNewerVersion();
    }
}

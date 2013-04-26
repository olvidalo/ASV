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
package nl.mpi.metadatabrowser.services.cmdi.mock;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.metadatabrowser.model.cmdi.CorpusNodeType;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockCorpusNode implements CorpusNode, Serializable {

    private int nodeId;
    private String name;
    private URI uri;
    private String profileId;
    private MockCorpusNode parent;
    private List<MockCorpusNode> children;
    private CorpusNodeType corpusNodeType = CorpusNodeType.METADATA;

    public void setCorpusNodeType(CorpusNodeType corpusNodeType) {
	this.corpusNodeType = corpusNodeType;
    }

    public void setProfileId(String profileId) {
	this.profileId = profileId;
    }

    public void setNodeId(int nodeId) {
	this.nodeId = nodeId;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public void setParent(GenericTreeNode parent) {
	this.parent = (MockCorpusNode) parent;
    }

    public void setChildren(List<MockCorpusNode> children) {
	this.children = children;
    }

    public String getProfileId() {
	return profileId;
    }

    @Override
    public int getNodeId() {
	return nodeId;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public URI getUri() {
	return uri;
    }

    @Override
    public GenericTreeNode getChild(int index) {
	return children.get(index);
    }

    @Override
    public int getChildCount() {
	return children.size();
    }

    @Override
    public int getIndexOfChild(GenericTreeNode child) {
	return children.indexOf(child);
    }

    @Override
    public GenericTreeNode getParent() {
	return parent;
    }

    public CorpusNodeType getCorpusNodeType() {
	return corpusNodeType;
    }

    protected MockCorpusNode getChildRecursive(int nodeId) {
	if (nodeId == this.nodeId) {
	    return this;
	}

	if (children != null) {
	    for (MockCorpusNode child : children) {
		MockCorpusNode childRecursive = child.getChildRecursive(nodeId);
		if (childRecursive != null) {
		    return childRecursive;
		}
	    }
	}
	return null;
    }

    public List<MockCorpusNode> getChildren() {
	if (children == null) {
	    return Collections.emptyList();
	} else {
	    return children;
	}
    }
}

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
import nl.mpi.archiving.tree.GenericTreeNode;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockTypedCorpusNode implements TypedCorpusNode, Serializable {

    private NodeType nodeType;
    private int nodeId;
    private String name;
    private URI uri;
    private MockTypedCorpusNode parent;
    private List<MockTypedCorpusNode> children;

    public void setNodeTypeName(String name) {
	this.nodeType = new MockNodeType(name);
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
	this.parent = (MockTypedCorpusNode) parent;
    }

    public void setChildren(List<MockTypedCorpusNode> children) {
	this.children = children;
    }

    @Override
    public NodeType getNodeType() {
	return nodeType;
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

    protected MockTypedCorpusNode getChildRecursive(int nodeId) {
	if (nodeId == this.nodeId) {
	    return this;
	}

	if (children != null) {
	    for (MockTypedCorpusNode child : children) {
		MockTypedCorpusNode childRecursive = child.getChildRecursive(nodeId);
		if (childRecursive != null) {
		    return childRecursive;
		}
	    }
	}
	return null;
    }

    public List<MockTypedCorpusNode> getChildren() {
	if (children == null) {
	    return Collections.emptyList();
	} else {
	    return children;
	}
    }

    private static class MockNodeType implements NodeType, Serializable {

	private final String name;

	public MockNodeType(String name) {
	    this.name = name;
	}

	@Override
	public String getName() {
	    return name;
	}
    }
}

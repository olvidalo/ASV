/*
 * Copyright (C) 2012 Max Planck Institute for Psycholinguistics
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
package nl.mpi.archiving.tree.mock;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.archiving.tree.LinkedTreeNode;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockCorpusNode implements CorpusNode, LinkedTreeNode, Serializable {

    private LinkedTreeNode parent;
    private List<LinkedTreeNode> children = Collections.emptyList();
    private String name = "";
    private URI nodeId;

    public void setChildren(List<LinkedTreeNode> children) {
	this.children = children;
    }

    @Override
    public LinkedTreeNode getChild(int index) {
	return children.get(index);
    }

    @Override
    public int getChildCount() {
	return children.size();
    }

    @Override
    public int getIndexOfChild(LinkedTreeNode child) {
	return children.indexOf(child);
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public LinkedTreeNode getParent() {
	return parent;
    }

    public void setParent(LinkedTreeNode parent) {
	this.parent = parent;
    }

    @Override
    public URI getNodeId() {
	return nodeId;
    }

    public void setNodeId(URI nodeId) {
	this.nodeId = nodeId;
    }

    @Override
    public String toString() {
	return name;
    }
}

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CorpusNodeType;
import nl.mpi.corpusstructure.ArchiveAccessContext;
import nl.mpi.corpusstructure.Node;
import nl.mpi.corpusstructure.NodeIdUtils;
import nl.mpi.corpusstructure.UnknownNodeException;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import nl.mpi.util.OurURL;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockCmdiCorpusStructureDB implements CmdiCorpusStructureDB, Serializable {

    private MockCorpusNode rootNode;

    public void setRootNode(MockCorpusNode rootNode) {
	this.rootNode = rootNode;
    }

    private MockCorpusNode getNode(int nodeId) {
	return rootNode.getChildRecursive(nodeId);
    }

    @Override
    public List<CorpusNode> getChildrenCMDIs(int nodeId) throws UnknownNodeException {
	final MockCorpusNode node = getNode(nodeId);
	if (node != null) {
	    return Collections.<CorpusNode>unmodifiableList(node.getChildren());
	} else {
	    return Collections.emptyList();
	}
    }

    @Override
    public URI getObjectURI(int id) throws UnknownNodeException {
	final CorpusNode node = getNode(id);
	if (node != null) {
	    return node.getUri();
	} else {
	    return null;
	}
    }

    @Override
    public String getProfileId(URI uri) {
	return "profile";
    }

    @Override
    public ArchiveAccessContext getArchiveRoots() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Node getNode(String nodeId) throws UnknownNodeException {
	CorpusNode node = getNode(NodeIdUtils.TOINT(nodeId));
	if (node != null) {
	    return new Node(nodeId, Node.SESSION, "test/test", node.getName(), node.getName());
	} else {
	    return null;
	}
    }

    @Override
    public nl.mpi.corpusstructure.CorpusNode getCorpusNode(String nodeId) throws UnknownNodeException {
	CorpusNode node = getNode(NodeIdUtils.TOINT(nodeId));
	if (node != null) {
	    return new nl.mpi.corpusstructure.CorpusNode(nodeId, Node.SESSION, "test/test", node.getName(), node.getName());
	} else {
	    return null;
	}
    }

    @Override
    public String getRootNodeId() {
	return NodeIdUtils.TONODEID(rootNode.getNodeId());
    }

    @Override
    public String[] getSubnodes(String nodeId) throws UnknownNodeException {
	List<CorpusNode> children = getChildrenCMDIs(NodeIdUtils.TOINT(nodeId));
	List<String> subNodes = new ArrayList<String>(children.size());
	for (CorpusNode child : children) {
	    subNodes.add(NodeIdUtils.TONODEID(child.getNodeId()));
	}
	return subNodes.toArray(new String[]{});
    }

    @Override
    public Node[] getChildrenNodes(String nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getParentNodes(String nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getDescendants(String nodeId, int nodeType, String format) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getDescendants(String nodeId, int nodeType, String[] formats) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getDescendants(String nodeId, int nodeType, String[] formats, String user, boolean onsite) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public nl.mpi.corpusstructure.CorpusNode[] getDescendantResources(String nodeId, boolean onsiteOnly, String userToRead, String userToWrite) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getSessions(String nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getSessionsAndCatalogues(String nodeId) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getResourcesFromArchive(int nodeType, String[] formats, String user, boolean onlyAvailable, boolean onlyOnSite) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getResourcesAccessInfo() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCanonicalVPath(String nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNamePath(String nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] translateToNamePath(String[] nodeids) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String resolveNameInAnnotationContext(String annotationNodeId, String name, String function) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String resolveNameInSessionContext(String sessionNodeId, String name, String function) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCanonicalParent(String nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAdminKey(String name) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
    }

    @Override
    public boolean getStatus() {
	return true;
    }

    @Override
    public OurURL getObjectURL(String toString, int HTTP_URL) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CorpusNodeType getCorpusNodeType(int nodeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
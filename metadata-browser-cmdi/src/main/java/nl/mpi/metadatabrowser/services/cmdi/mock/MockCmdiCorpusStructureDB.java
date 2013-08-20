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
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockCmdiCorpusStructureDB implements CorpusStructureProvider, Serializable {

    private MockCorpusNode rootNode;

    public void setRootNode(MockCorpusNode rootNode) {
	this.rootNode = rootNode;
    }

    @Override
    public MockCorpusNode getNode(URI nodeId) {
	return rootNode.getChildRecursive(nodeId);
    }

    private List<CorpusNode> getChildrenCMDIs(URI nodeId) throws UnknownNodeException {
	final MockCorpusNode node = getNode(nodeId);
	if (node != null) {
	    return Collections.<CorpusNode>unmodifiableList(node.getChildren());
	} else {
	    return Collections.emptyList();
	}
    }

    //@Override
    public String getProfileId(URI uri) {
	return "profile";
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
    public URI getHandleResolverURI() {
	return URI.create("http://hdl.handle.net/");
    }

    @Override
    public String getHandle(URI nodeid) {
	return "11142/00-021C2EA5-66DB-4363-A957-CE9FEE4226CD";
    }

    @Override
    public Timestamp getObjectFileTime(URI node) {
	return new Timestamp(1368520487);
    }

    @Override
    public String getObjectChecksum(URI node) {
	return "adab88ba00910f9591df887f45a05419";
    }

    @Override
    public long getObjectSize(URI node) {
	return 2640;
    }

    @Override
    public URI getRootNodeId() {
	return rootNode.getNodeId();
    }

    @Override
    public List<URI> getChildNodeIds(URI nodeId) throws UnknownNodeException {
	List<CorpusNode> childrenNodes = getChildNodes(nodeId);
	List<URI> subUris = new ArrayList<URI>(childrenNodes.size());
	for (CorpusNode node : childrenNodes) {
	    subUris.add(node.getNodeId());
	}
	return subUris;
    }

    @Override
    public List<CorpusNode> getChildNodes(URI nodeId) throws UnknownNodeException {
	return getChildrenCMDIs(nodeId);
    }

    @Override
    public List<URI> getParentNodeIds(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getDescendants(URI nodeId, nl.mpi.archiving.corpusstructure.core.CorpusNodeType nodeType, Collection<String> formats) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getDescendants(URI nodeId, nl.mpi.archiving.corpusstructure.core.CorpusNodeType nodeType, Collection<String> formats, String user, boolean onsite) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getResourcesFromArchive(nl.mpi.archiving.corpusstructure.core.CorpusNodeType nodeType, Collection<String> formats, String user, boolean onlyAvailable, boolean onlyOnSite) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getProfileSchemaLocation(URI nodeId) throws UnknownNodeException {
	return URI.create("profile");
    }

    @Override
    public CorpusNodeType getCorpusNodeType(URI nodeId) throws UnknownNodeException {
	return getNode(nodeId).getCorpusNodeType();
    }

    @Override
    public String getCanonicalVPath(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNamePath(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCanonicalParent(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getObjectsByChecksum(String checksum) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getObjectTimestamp(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAccessible(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOnSite(URI nodeId) throws UnknownNodeException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasReadAccess(URI nodeid, String uid) throws UnknownNodeException {
	return true;
    }

    @Override
    public List<URI> getNewArchiveObjectsSince(Date timestamp, boolean onsiteonly, boolean urlformat, boolean usefiletime) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AccessInfo getObjectAccessInfo(URI nodeId) throws UnknownNodeException {
	return new MockAccessInfo();
    }
}

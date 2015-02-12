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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;

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

    private List<CorpusNode> getChildrenCMDIs(URI nodeId) {
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
    public URI getRootNodeURI() {
	return rootNode.getNodeURI();
    }

    @Override
    public List<URI> getChildNodeURIs(URI nodeId) {
	List<CorpusNode> childrenNodes = getChildNodes(nodeId);
	List<URI> subUris = new ArrayList<URI>(childrenNodes.size());
	for (CorpusNode node : childrenNodes) {
	    subUris.add(node.getNodeURI());
	}
	return subUris;
    }

    @Override
    public List<CorpusNode> getChildNodes(URI nodeId) {
	return getChildrenCMDIs(nodeId);
    }

    @Override
    public List<URI> getParentNodeURIs(URI nodeId) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCanonicalVPath(URI nodeId) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getCanonicalParent(URI nodeId) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<URI> getObjectsByChecksum(String checksum) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildNodeCount(URI nodeUri) {
	return getChildNodeURIs(nodeUri).size();
    }

    @Override
    public boolean hasChildNodes(URI nodeUri) {
        return !getChildNodeURIs(nodeUri).isEmpty();
    }

    @Override
    public void initialize() {
    }

    @Override
    public List<URI> getCanonicalVPathURIs(URI nodeUri) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<URI> getDescendants(URI nodeUri) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<URI> getDescendantsByType(URI nodeUri, List<CorpusNodeType> types) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<CorpusNode> getDescendantNodes(URI nodeUri) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<CorpusNode> getDescendantNodesByType(URI nodeUri, List<CorpusNodeType> types) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

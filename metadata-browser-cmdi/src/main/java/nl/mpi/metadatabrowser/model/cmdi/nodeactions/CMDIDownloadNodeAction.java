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
package nl.mpi.metadatabrowser.model.cmdi.nodeactions;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.corpusstructure.AccessInfo;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class CMDIDownloadNodeAction extends SingleNodeAction implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "download";
    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;
    //TODO: decide where does userid comes from and implement accordingly
    private String userid;

    public CMDIDownloadNodeAction(CorpusStructureProvider csdb, NodeResolver nodeResolver) {
	this.csdb = csdb;
	this.nodeResolver = nodeResolver;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
	logger.debug("Action [{}] invoked on {}", getName(), node);
	final URL nodeUri = nodeResolver.getUrl(node);
	final URI nodeId = node.getNodeId();

	// HANDLE download action here
	final String fileName = new File(nodeUri.getPath()).getName();

	//String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));

	try {
	    boolean hasaccess;
	    try {
		if (userid == null || userid.equals("") || userid.equals("anonymous")) {
		    hasaccess = csdb.hasReadAccess(nodeId, AccessInfo.EVERYBODY);
		} else {
		    hasaccess = csdb.hasReadAccess(nodeId, userid);
		}
	    } catch (UnknownNodeException ex) {
		throw new NodeActionException(this, ex);
	    }
	    logger.debug("resource-download, access for " + nodeUri.toString() + ", " + userid + ", " + hasaccess);
	    if (hasaccess) {
		File file = new File(nodeUri.getPath());
		IResourceStream resStream = new FileResourceStream(file);
		DownloadActionRequest.setStreamContent(resStream);
		DownloadActionRequest.setFileName(fileName);
	    } else {
		return new SimpleNodeActionResult("User " + userid + " has no access to this node " + nodeUri.toString());
	    }
	    final DownloadActionRequest request = new DownloadActionRequest();
	    return new SimpleNodeActionResult(request);
	} catch (NullPointerException e) {
	    logger.error("unvalid type of file. Could not find path for this file : {}", fileName);
	}
	return new SimpleNodeActionResult("Download action could not be performed due to a invalid path with the file. Filepath = " + nodeUri.getPath());
    }
}

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
import java.net.URL;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.services.cmdi.impl.CorpusNodeResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO: Merge with CMDIDownloadNodeAction?
 * TODO: Add support for multiple node selection?
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public final class CMDIDownloadNodeAction extends SingleNodeAction implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "download";
    private final NodeResolver nodeResolver;
    //TODO: decide where does userid comes from and implement accordingly
    private String userid;

    @Autowired
    public CMDIDownloadNodeAction(NodeResolver nodeResolver) {
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

	// HANDLE download action here
	if (userHasAccess(node, nodeUri)) {
	    final IResourceStream resStream = new CorpusNodeResourceStream(nodeResolver, node);
	    final String fileName = new File(nodeUri.getPath()).getName();
	    final DownloadActionRequest request = new DownloadActionRequest(fileName, resStream);
	    return new SimpleNodeActionResult(request);
	} else {
	    return new SimpleNodeActionResult(String.format("User %s has no access to the node %s", userid, nodeUri));
	}
    }

    private boolean userHasAccess(TypedCorpusNode node, final URL nodeUri) {
	//String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
	final AccessInfo nodeAuthorization = node.getAuthorization();
	final boolean hasaccess;
	if (userid == null || userid.equals("") || userid.equals("anonymous")) {
	    hasaccess = nodeAuthorization.hasReadAccess(AccessInfo.EVERYBODY);
	} else {
	    hasaccess = nodeAuthorization.hasReadAccess(userid);
	}
	logger.debug("resource-download, access for {}, {}: {}", nodeUri, userid, hasaccess);
	return hasaccess;
    }
}

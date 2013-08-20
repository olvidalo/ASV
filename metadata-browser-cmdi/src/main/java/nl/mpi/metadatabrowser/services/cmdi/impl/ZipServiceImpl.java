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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ZipServiceImpl implements ZipService, Serializable {

    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;
    private final Logger logger = LoggerFactory.getLogger(ZipServiceImpl.class);
    private static final long MAX_LIMIT = FileUtils.ONE_GB * 2; // 200000000L ; //4000000000L  //4GB

    public ZipServiceImpl(CorpusStructureProvider csdb, NodeResolver nodeResolver) {
	this.csdb = csdb;
	this.nodeResolver = nodeResolver;
    }

    @Override
    public File createZipFileForNodes(List<? extends CorpusNode> childrenNodes, String userid) throws IOException, UnknownNodeException, FileNotFoundException {
	//create object of FileOutputStream
	File tmp = File.createTempFile("mdtbrowser", ".zip");
	FileOutputStream fout = new FileOutputStream(tmp);
	//create object of ZipOutputStream from FileOutputStream
	ZipOutputStream zout = new ZipOutputStream(fout);
	int itemsAdded = 0;
	boolean hasaccess;

	// HANDLE multiple download action here
	if (childrenNodes.size() > 0) {
	    for (CorpusNode childNode : childrenNodes) {
		final URI childUri = childNode.getNodeURI();
		if (itemsAdded == 0) { // check if at least one resource is accessible for user
		    if (childUri != null) {
			hasaccess = checkAccess(userid, childNode.getNodeURI());// get access rights for each resource
			logger.debug("resources-download, access for " + childUri + ", " + userid + ", " + hasaccess);
			if (hasaccess) {
			    itemsAdded++;
			}
		    }//else start to zip
		}
	    }
	    if (itemsAdded > 0) { // must be minimum 1 to proceed = 1 accessible resource for user
		long overallSize = 0;
		byte[] buffer = new byte[1024];
		for (CorpusNode childNode : childrenNodes) {
		    final URL childNodeUrl = nodeResolver.getUrl(childNode);
		    if (overallSize > MAX_LIMIT) { // check size limit 4GB
			overallSize = 0;
			zout.close();
			logger.info("maximum size limit of 4GB reached");
		    }
		    if (childNodeUrl != null) {
			hasaccess = checkAccess(userid, childNode.getNodeURI());// get access rights for each resource
			if (hasaccess) {
			    logger.info("resources-download: " + childNodeUrl.toString());

			    String fileName = new File(childNodeUrl.getPath()).getName();
			    //String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
			    final InputStream is = childNodeUrl.openStream();
			    try {
				final ZipEntry ze = new ZipEntry(fileName);
				zout.putNextEntry(ze);

				int length;
				while ((length = is.read(buffer)) > 0) {
				    zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				overallSize += ze.getCompressedSize();
				logger.info("Copied resource: " + childNodeUrl + "  to zipFile");
			    } catch (NullPointerException e) {
				logger.error("unvalid type of file. Could not find path for this file : {}", fileName, e);
			    } finally {
				is.close();
			    }
			} else {
			    logger.info("User " + userid + " has no access to " + childNodeUrl);
			}

		    } else {
			logger.error("Error: nodeurl for resourcenode " + childNode + " was not found");
		    }
		}
	    }
	} else {
	    logger.error("Error: called resources-download for  node without children");
	}
	zout.close();
	fout.close();
	return tmp;
    }

    private boolean checkAccess(String userid, URI nodeId) throws UnknownNodeException {
	boolean hasaccess;
	if (userid == null || userid.equals("") || userid.equals("anonymous")) {
	    hasaccess = csdb.getObjectAccessInfo(nodeId).hasReadAccess(AccessInfo.EVERYBODY);
	} else {
	    hasaccess = csdb.getObjectAccessInfo(nodeId).hasReadAccess(userid);
	}
	logger.debug("resource-download, access for " + nodeId + ", " + userid + ", " + hasaccess);
	return hasaccess;
    }
}

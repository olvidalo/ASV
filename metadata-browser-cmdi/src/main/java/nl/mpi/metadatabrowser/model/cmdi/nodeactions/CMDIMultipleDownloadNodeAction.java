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
import java.io.IOException;
import java.io.Serializable;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO: Merge with CMDIDownloadNodeAction?
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIMultipleDownloadNodeAction extends SingleNodeAction implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final ZipService zipService;

    @Autowired
    public CMDIMultipleDownloadNodeAction(ZipService zipService) {
        this.zipService = zipService;

    }

    @Override
    public String getName() {
        return "Download Subtree";
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), node);
        try {
            String userid = auth.getPrincipalName();
            final File zipFile = zipService.createZipFileForNodes(node, userid);
            if (zipFile == null) {
                logger.error("none of the files are accessible to user : " + userid);
                return new SimpleNodeActionResult(String.format("User %s has no access to any of the nodes. No zip could be created.", userid));
            }
            IResourceStream resStream = new FileResourceStream(zipFile) {
                @Override
                public void close() throws IOException {
                    super.close();
                    logger.debug("Zip file download completed. Removing {} from file system.", zipFile);
                    if (!zipFile.delete()) {
                        logger.warn("Could not remove zip file: {}", zipFile);
                    }
                }
            };
            final String filename = String.format("package_%s.zip", FilenameUtils.getBaseName(node.getName()));
            final DownloadActionRequest request = new DownloadActionRequest(filename, resStream);

            return new SimpleNodeActionResult(request);
        } catch (IOException ex) {
            logger.error("an exception has occured when trying to download package of : " + node + " || " + ex);
            throw new NodeActionException(this, ex);
        } catch (UnknownNodeException ex) {
            throw new NodeActionException(this, ex);
        }
    }
}

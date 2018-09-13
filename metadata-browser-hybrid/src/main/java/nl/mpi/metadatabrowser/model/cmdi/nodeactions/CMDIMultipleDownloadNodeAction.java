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
import java.net.URI;

import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.DownloadActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO: Merge with CMDIDownloadNodeAction?
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIMultipleDownloadNodeAction extends SingleNodeAction implements NodeActionSingletonBean, Serializable, BeanNameAware {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final ZipService zipService;
    private String beanName;

    @Autowired
    public CMDIMultipleDownloadNodeAction(ZipService zipService) {
        this.zipService = zipService;

    }

    @Override
    public String getName() {
        return "Download All";
    }

    @Override
    public String getTitle() {
        return "Download all resources of this bundle (to which you have access)";
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        logger.debug("Multiple download action invoked on {}", node);
        final String userid = auth.getPrincipalName();
        try {
            final File zipFile = zipService.createZipFileForNodes(node, userid);
            if (zipFile == null) {
                logger.info("DownloadAll {}, {}, denied", node.getNodeURI(), userid);
                logger.error("none of the files are accessible to user : " + userid);
                return new SimpleNodeActionResult(String.format("User %s has no access to one or more of the child nodes. No zip could be created. Log in and/or request access, then try again.", userid));
            } else {
                final URI nodeUri = node.getNodeURI();
                logger.info("DownloadAll {}, {}, granted", nodeUri, userid);
                final IResourceStream resStream = new FileResourceStream(zipFile) {
                    @Override
                    public void close() throws IOException {
                        super.close();

                        // called when download is complete - file can be removed
                        logger.debug("Zip file download completed. Removing {} from file system.", zipFile);

                        if (!zipFile.delete()) {
                            logger.warn("Could not remove zip file: {}", zipFile);
                        }
                    }
                };
                final String filename = String.format("package_%s.zip", FilenameUtils.getBaseName(node.getName()));
                final DownloadActionRequest request = new DownloadActionRequest(filename, resStream);

                // when the VM closes, file should be removed if it has not already
                zipFile.deleteOnExit();

                return new SimpleNodeActionResult(request);
            }
        } catch (IOException ex) {
            logger.info("DownloadAll {}, {}, granted, incomplete", node.getNodeURI(), userid);
            logger.error("an exception has occured when trying to download package of : {}", node, ex);
            throw new NodeActionException(this, ex);
        }
    }

    /**
     * Download should not be retrieved in an Ajax request!
     *
     * @return false
     */
    @Override
    public boolean isAjaxAllowed() {
        return false;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

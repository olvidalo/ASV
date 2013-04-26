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
package nl.mpi.metadatabrowser.model.cmdi;

import java.io.*;
import java.net.URI;
import java.util.List;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIMultipleDownloadNodeAction extends SingleNodeAction implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "multidownload";
    private String feedbackMessage;
    private String exceptionMessage;
    //private ControllerActionRequest resultActionRequest;
    //private CorpusStructureDBImpl csdb = new CorpusStructureDBImpl("java:comp/env/jdbc/CSDB", false, "jdbc/CSDB", "start1a");
    private final CmdiCorpusStructureDB csdb;
    private final ZipService zipService;

    public CMDIMultipleDownloadNodeAction(CmdiCorpusStructureDB csdb, ZipService zipService) {
        this.csdb = csdb;
        this.zipService = zipService;
    }


    
    @Override
    public String getName() {
        return name;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        URI nodeUri = node.getUri();
        logger.info("Action [{}] invoked on {}", getName(), nodeUri);
        int nodeid = node.getNodeId();

        try {
            List<CorpusNode> childrenNodes = csdb.getChildrenCMDIs(nodeid);
            final File zipFile = zipService.createZipFileForNodes(childrenNodes);
            IResourceStream resStream = new FileResourceStream(zipFile){

                @Override
                public void close() throws IOException {
                    super.close();
                    zipFile.delete();
                }
                
            };
            DownloadActionRequest.setStreamContent(resStream);
            DownloadActionRequest.setFileName("package_" + node.getName());
        } catch (IOException ioe) {
            System.out.println("IOException :" + ioe);
        }
        if (exceptionMessage == null) {
            return new NodeActionResult() {

                @Override
                public String getFeedbackMessage() {
                    if (feedbackMessage == null) {
                        return null;
                    } else {
                        logger.info("Returning feedback message \"{}\" for {}", feedbackMessage, this);
                        return feedbackMessage;
                    }
                }

                @Override
                public ControllerActionRequest getControllerActionRequest() {
                    return new DownloadActionRequest();
                }
            };
        } else {
            logger.info("Throwing NodeActionException \"{}\" for {}", exceptionMessage, this);
            throw new NodeActionException(this, exceptionMessage);
        }
    }
}

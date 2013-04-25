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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIBookmarkNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "bookmark";
    private String feedbackMessage;
    private String exceptionMessage;
    private Map<String, String> parameters = new HashMap<String, String>();
    private final CmdiCorpusStructureDB csdb;

    public CMDIBookmarkNodeAction(CmdiCorpusStructureDB csdb) {
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        URI nodeUri = node.getUri();
        logger.info("Action [{}] invoked on {}", getName(), nodeUri);
        String nodeName = node.getName();
        String nodeId = Integer.toString(node.getNodeId());
        String resolver = csdb.getArchiveRoots().getHandleProxy();
        if (resolver == null) {
            resolver = "";
        }
        if (!resolver.endsWith("/")) {
            resolver = resolver + "/";
        }

        String archive_name = csdb.getArchiveRoots().getArchiveName();
        if (archive_name == null) {
            archive_name = "unknown";
        }

        String url = null;
        try {
            url = nodeUri.toURL().toString();
        } catch (MalformedURLException ex) {
            logger.error("url error while getting URL", ex);
        }


        // HANDLE bookmark action here
        parameters.put("nodeId", nodeId);
        parameters.put("name", nodeName);
        parameters.put("uri", nodeUri.toString());
        parameters.put("url", url);
        parameters.put("last_modified", "lastmodified");
        parameters.put("title", "title");
        parameters.put("format", "format");
        parameters.put("archive_name", archive_name);
        parameters.put("resolver", resolver);

        ShowComponentActionRequest.setParameters(parameters);

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
                    return new ShowComponentActionRequest();
                }
            };
        } else {
            logger.info("Throwing NodeActionException \"{}\" for {}", exceptionMessage, this);
            throw new NodeActionException(this, exceptionMessage);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

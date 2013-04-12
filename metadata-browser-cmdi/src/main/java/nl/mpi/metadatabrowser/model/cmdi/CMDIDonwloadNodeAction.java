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

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import nl.mpi.metadatabrowser.model.*;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class CMDIDonwloadNodeAction extends SingleNodeAction implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private String name = "download";
    private String feedbackMessage;
    private String exceptionMessage;
    //private ControllerActionRequest resultActionRequest;

    public CMDIDonwloadNodeAction(String name) {
        setName(name);
    }

    CMDIDonwloadNodeAction() {
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        URI nodeUri = node.getUri();
        logger.info("Action [{}] invoked on {}", getName(), nodeUri);

        // HANDLE download action here
        String fileName = nodeUri.toString().substring(nodeUri.toString().lastIndexOf('/') + 1, nodeUri.toString().length());
        String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));


        File file = new File(nodeUri.getPath());
        IResourceStream resStream = new FileResourceStream(file);
        DownloadActionRequest.setStreamContent(resStream);
        DownloadActionRequest.setFileName(fileNameWithoutExtn);

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

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

import java.net.URI;
import java.util.Map;
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.metadatabrowser.model.NavigationRequest.NavigationTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIRRSAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private String feedbackMessage;
    private String exceptionMessage;
    private String name ="rrs";
    private Map<String, String> parameters;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NodeActionResult execute(URI nodeUri) throws NodeActionException {
        logger.info("Action [{}] invoked on {}", getName(), nodeUri);

        // TODO HANDLE rrs navigation action here
NavigationActionRequest.setTarget(NavigationTarget.RRS);
parameters.put("nodeId", getNodeID(nodeUri));
NavigationActionRequest.setParameters(parameters);

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
                    return new NavigationActionRequest();
                }
            };
        } else {
            logger.info("Throwing NodeActionException \"{}\" for {}", exceptionMessage, this);
            throw new NodeActionException(this, exceptionMessage);
        }
    }

    
    private String getNodeID(URI nodeUri) {
        //TODO call corpusStructure on URI to get nodeid;
        return "MPI#TEST1";
    }
}

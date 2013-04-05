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
package nl.mpi.metadatabrowser.model;

import java.io.Serializable;
import java.net.URI;
import nl.mpi.metadatabrowser.model.actionrequest.ControllerActionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNodeAction implements NodeAction, Serializable {

    private final static Logger logger = LoggerFactory.getLogger(MockNodeAction.class);
    private String name;
    private String feedbackMessage;
    private String exceptionMessage;
    private ControllerActionRequest resultActionRequest;

    @Override
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setFeedbackMessage(String feedbackMessage) {
	this.feedbackMessage = feedbackMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
	this.exceptionMessage = exceptionMessage;
    }

    public void setResultActionRequest(ControllerActionRequest resultActionRequest) {
	this.resultActionRequest = resultActionRequest;
    }

    @Override
    public NodeActionResult execute(URI nodeUri) throws NodeActionException {
	logger.info("Action [{}] invoked on {}", getName(), nodeUri);

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
		    return resultActionRequest;
		}
	    };
	} else {
	    logger.info("Throwing NodeActionException \"{}\" for {}", exceptionMessage, this);
	    throw new NodeActionException(this, exceptionMessage);
	}
    }

    @Override
    public String toString() {
	return getName();
    }
}

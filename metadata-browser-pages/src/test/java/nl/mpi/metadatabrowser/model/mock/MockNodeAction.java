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
package nl.mpi.metadatabrowser.model.mock;

import java.io.Serializable;
import java.util.Collection;
import nl.mpi.metadatabrowser.model.ControllerActionRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.NodeActionAjaxListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockNodeAction implements NodeAction, Serializable {

    private final static Logger logger = LoggerFactory.getLogger(MockNodeAction.class);
    private String name;
    private String title;
    private String feedbackMessage;
    private String exceptionMessage;
    private ControllerActionRequest resultActionRequest;
    private NodeActionAjaxListener ajaxListener;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void settitle(String title) {
        this.title = title;
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

    public NodeActionAjaxListener getAjaxListener() {
        return ajaxListener;
    }

    public void setAjaxListener(NodeActionAjaxListener ajaxListener) {
        this.ajaxListener = ajaxListener;
    }

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.info("Action [{}] invoked on {}", getName(), nodes);

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

                @Override
                public NodeActionAjaxListener getAjaxListener() {
                    return ajaxListener;
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

    @Override
    public boolean isAjaxAllowed() {
        return true;
    }
}

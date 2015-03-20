/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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

import java.net.URI;
import java.util.Collection;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ExternalFramePanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public abstract class EmbeddedPageNodeAction implements NodeActionSingletonBean  {

    private final static Logger logger = LoggerFactory.getLogger(EmbeddedPageNodeAction.class);

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);
        final URI targetURI = getTarget(nodes);

        final ShowComponentRequest componentRequest = new ShowComponentRequest() {
            @Override
            public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                return new ExternalFramePanel(id, targetURI.toString()) {

                    @Override
                    protected IModel<String> getOpenInNewLabelModel() {
                        return EmbeddedPageNodeAction.this.getOpenInNewLabelModel();
                    }

                };
            }
        };
        return new SimpleNodeActionResult(componentRequest);
    }

    /**
     * Override to provide a custom link text
     *
     * @return In this implementation, a model wrapping the constant "Click here
     * to proceed"
     */
    protected IModel<String> getOpenInNewLabelModel() {
        return Model.of("Click here to proceed");
    }

    /**
     * Default implementation allows Ajax
     *
     * @return true
     */
    @Override
    public boolean isAjaxAllowed() {
        return true;
    }

    protected abstract URI getTarget(Collection<TypedCorpusNode> nodes) throws NodeActionException;

}

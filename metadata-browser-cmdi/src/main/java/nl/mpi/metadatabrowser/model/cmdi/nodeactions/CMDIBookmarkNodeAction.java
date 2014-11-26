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

import java.io.UnsupportedEncodingException;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelShowComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIBookmarkNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), node);

        final ShowComponentRequest request = new ShowComponentRequest() {
            @Override
            public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                // create panel form for citation action
                return new PanelShowComponent(id, node);
            }
        };
        return new SimpleNodeActionResult(request);
    }

    @Override
    public String getName() {
        return "Citation";
    }

    @Override
    public String getTitle() {
        return "Get a persistent link to this branch or resource in order to cite it in a publication or link to it on a web site";
    }
}

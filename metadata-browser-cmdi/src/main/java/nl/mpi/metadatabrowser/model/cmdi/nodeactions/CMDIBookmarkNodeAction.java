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

import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelShowComponent;
import org.apache.wicket.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIBookmarkNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "bookmark";
    private final CorpusStructureProvider csdb;

    public CMDIBookmarkNodeAction(CorpusStructureProvider csdb) {
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), node);

        final ShowComponentRequest request = new ShowComponentRequest() {

            @Override
            public Component getComponent(String id) throws ControllerActionRequestException {
                try {
                    // create panel form for bookmark action
                    return new PanelShowComponent(id, node, csdb);
                } catch (UnknownNodeException ex) {
                    throw new ControllerActionRequestException("Error creating display panel for node " + node.getNodeId(), ex);
                }
            }
        };
        return new SimpleNodeActionResult(request);
    }

    @Override
    public String getName() {
        return name;
    }
}

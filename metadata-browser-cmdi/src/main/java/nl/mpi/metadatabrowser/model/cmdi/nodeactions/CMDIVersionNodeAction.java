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
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelVersionComponent;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockVersioningAPI;
import org.apache.wicket.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIVersionNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "version";
    private final CorpusStructureProvider csdb;
    //TODO : decide where does userId comes from and implement accordingly
    private String userId;

    public CMDIVersionNodeAction(CorpusStructureProvider csdb) {
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), node);

        //TO DO connect to versionAPI DB
        final MockVersioningAPI versions = new MockVersioningAPI("jdbcurl");

        final ShowComponentRequest request = new ShowComponentRequest() {

            @Override
            public Component getComponent(String id) {
                // create panel form for version action
                return new PanelVersionComponent(id, node, csdb, userId, versions);
            }
        };
        return new SimpleNodeActionResult(request);
    }

    @Override
    public String getName() {
        return name;
    }
}

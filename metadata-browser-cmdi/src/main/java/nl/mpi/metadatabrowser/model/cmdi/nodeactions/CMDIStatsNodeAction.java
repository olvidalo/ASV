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

import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIStatsNodeAction extends SingleNodeAction implements NodeActionSingletonBean  {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    @Override
    public String getName() {
        return "accessStats";
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), node);
        // DEPRECATED : HANDLE access statistics action here

        final NavigationActionRequest request = new NavigationActionRequest(null);

        return new SimpleNodeActionResult(request);


    }
}

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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that calls redirect to CMDI Search
 */
@Component
public class CMDISearchNodeAction implements NodeAction {

    private NodeActionsConfiguration nodeActionsConfiguration;
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    @Autowired
    CMDISearchNodeAction(NodeActionsConfiguration nodeActionsConfiguration) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
    }

    @Override
    public String getName() {
        return "Metadata Search";
    }

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);       
       URI targetURI;
        NavigationActionRequest request = null;
        UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getMdSearchURL());
        for (TypedCorpusNode node : nodes) {
            //Buil redirect to CMDI Search
            URI nodeId = node.getNodeURI();
            uriBuilder = uriBuilder.queryParam("nodeid", nodeId);
        }
        try {
            targetURI = uriBuilder.queryParam("jsessionID", "session_number").build();
            request = new NavigationActionRequest(targetURI.toURL());
        } catch (MalformedURLException ex) {
            logger.error("URL syntax exception:" + ex);
        }

        return new SimpleNodeActionResult(request);
    }
}

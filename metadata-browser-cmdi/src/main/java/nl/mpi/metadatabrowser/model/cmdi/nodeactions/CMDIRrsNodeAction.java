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

import java.net.URI;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelEmbedActionDisplay;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class that call redirect to RRS
 */
@Component
public class CMDIRrsNodeAction implements NodeAction {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final static Logger logger = LoggerFactory.getLogger(CMDIRrsNodeAction.class);
    private FilterNodeIds filterNodeId;

    @Autowired
    public CMDIRrsNodeAction(NodeActionsConfiguration nodeActionsConfiguration, FilterNodeIds filterNodeIds) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.filterNodeId = filterNodeIds;
    }

    @Override
    public String getName() {
        return "Request Access";
    }

    @Override
    public String getTitle() {
        return "Request access to the selected branch or resource";
    }

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);
        URI targetURI = null;
        ShowComponentRequest request;
        UriBuilder uriBuilder = UriBuilder.fromUri(nodeActionsConfiguration.getRrsURL() + nodeActionsConfiguration.getRrsIndexURL());
        for (TypedCorpusNode node : nodes) {
            //Buil redirect to RRS
            URI nodeId = node.getNodeURI();
            String nodeid = filterNodeId.getURIParam(nodeId);
            targetURI = uriBuilder.queryParam("nodeid", nodeid).queryParam("jsessionID", "session_id").build();
        }
        if (targetURI != null) {
            final String redirectURL = targetURI.toString();
            request = new ShowComponentRequest() {

                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    return new PanelEmbedActionDisplay(id, redirectURL);
                }
            };
            return new SimpleNodeActionResult(request);
        } else {
            throw new NodeActionException(this, "target uri could not be build. This is likely to happen when no node was found. If this is not the case please check configuration paramters.");
        }
//        try {
//            request = new NavigationActionRequest(targetURI.toURL());
//        } catch (MalformedURLException ex) {
//            logger.error("URL syntax exception:" + ex);
//        }
//        return new SimpleNodeActionResult(request);
    }
}

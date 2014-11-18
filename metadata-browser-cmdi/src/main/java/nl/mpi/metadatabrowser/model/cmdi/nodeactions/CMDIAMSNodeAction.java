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
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelEmbedActionDisplay;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * Class Action to call AMS (Manage Access Rights).
 */
@Component
public class CMDIAMSNodeAction extends SingleNodeAction implements NodeAction {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final NodeIdFilter filterIdProvider;
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    @Autowired
    public CMDIAMSNodeAction(NodeActionsConfiguration nodeActionsCongiguration, NodeIdFilter filterIdProvider) {
        this.nodeActionsConfiguration = nodeActionsCongiguration;
        this.filterIdProvider = filterIdProvider;
    }

    @Override
    public String getName() {
        return "Manage Access";
    }

    @Override
    public String getTitle() {
        return "Manage access permissions for the selected branch (requires management permissions)";
    }

    @Override
    public NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), node);
        // Build redirect to AMS
        URI nodeId = node.getNodeURI();
        String nodeid = filterIdProvider.getURIParam(nodeId);
        URI targetURI;
        if (node.getNodeType() instanceof CMDIMetadataType || node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType || node.getNodeType() instanceof CMDICollectionType) {
            targetURI = UriBuilder.fromUri(nodeActionsConfiguration.getAmsURLForcs2()).queryParam("nodeid", nodeid).build();
        } else {
            targetURI = UriBuilder.fromUri(nodeActionsConfiguration.getAmsURL()).queryParam("nodeid", nodeid).queryParam("jsessionID", "session_id").build();
        }
        ShowComponentRequest request = null;
        if (targetURI != null) {
            final String redirectURL = targetURI.toString();
            request = new ShowComponentRequest() {

                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    return new PanelEmbedActionDisplay(id, redirectURL);
                }
            };
        }
        return new SimpleNodeActionResult(request);
//        try {
//            request = new NavigationActionRequest(targetURI.toURL());
//        } catch (MalformedURLException ex) {
//            logger.error("URL syntax exception:" + ex);
//        }
//        return new SimpleNodeActionResult(request);
    }
}

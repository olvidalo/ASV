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
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
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

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final FilterNodeIds filterNodeId;
    private final NodeResolver nodeResolver;

    @Autowired
    CMDISearchNodeAction(NodeActionsConfiguration nodeActionsConfiguration, FilterNodeIds filterNodeIds, NodeResolver nodeResolver) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.filterNodeId = filterNodeIds;
        this.nodeResolver = nodeResolver;
    }

    @Override
    public String getName() {
        return "Metadata Search";
    }

    @Override
    public String getTitle() {
        return "Search the metadata descriptions of the archived material below the selected branch";
    }

    @Override
    public NodeActionResult execute(Collection<TypedCorpusNode> nodes) throws NodeActionException {
        logger.debug("Action [{}] invoked on {}", getName(), nodes);
        URI targetURI = null;
        NavigationActionRequest request = null;
        UriBuilder uriBuilder;
        String wraphandleOrNodeURL;
        for (TypedCorpusNode node : nodes) {
            if (node.getNodeType() instanceof CMDIMetadataType || node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType || node.getNodeType() instanceof CMDICollectionType) {
                //Buil redirect to CMDI Search
                uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getYamsSearchURL());
                URI handle = node.getPID();
                if (handle.toString() == null) { // can be null, pass URL instead
                    wraphandleOrNodeURL = nodeActionsConfiguration.processLinkProtocol(nodeResolver.getUrl(node).toString(), nodeActionsConfiguration.getForceHttpOrHttps().equals("https"));
                    targetURI = uriBuilder.queryParam("url", wraphandleOrNodeURL).build();
                } else {
                    wraphandleOrNodeURL = handle.toString();
                    if (handle.toString().contains(":")) {
                        wraphandleOrNodeURL = handle.toString().split(":")[1];
                    }
                    targetURI = uriBuilder.queryParam("hdl", wraphandleOrNodeURL).build();
                }

            } else {
                //Buil redirect to IMDI Search
                URI nodeId = node.getNodeURI();
                String nodeid = filterNodeId.getURIParam(nodeId);
                uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getMdSearchURL());
                uriBuilder = uriBuilder.queryParam("nodeid", nodeid);
                targetURI = uriBuilder.queryParam("jsessionID", "session_number").build();
            }
        }
        try {
            request = new NavigationActionRequest(targetURI.toURL());
        } catch (MalformedURLException ex) {
            logger.error("URL syntax exception:" + ex);
        }

        return new SimpleNodeActionResult(request);
    }
}

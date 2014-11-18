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
import java.net.URISyntaxException;
import java.util.Collection;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelEmbedActionDisplay;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.URIFilter;
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
    private final NodeIdFilter nodeIdFilter;
    private final NodeResolver nodeResolver;
    private final URIFilter uriFilter;

    /**
     *
     * @param nodeActionsConfiguration
     * @param nodeIdFilter filter that rewrites node IDs when passed as query
     * parameters
     * @param nodeResolver
     * @param nodeUriFilter filter that rewrites a node URL (e.g. to intercept
     * non-HTTPS URIs)
     */
    @Autowired
    CMDISearchNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeIdFilter nodeIdFilter, NodeResolver nodeResolver, URIFilter nodeUriFilter) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
        this.nodeResolver = nodeResolver;
        this.uriFilter = nodeUriFilter;
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
        String wraphandleOrNodeURL;
        //TODO: deal with multiple nodes properly
        for (TypedCorpusNode node : nodes) {
            if (node.getNodeType() instanceof CMDIMetadataType || node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType || node.getNodeType() instanceof CMDICollectionType) {
                try {
                    //Buil redirect to CMDI Search
                    final UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getYamsSearchURL());
                    final URI handle = nodeResolver.getPID(node);
                    if (handle == null) { // can be null, pass URL instead
                        // allow filter to rewrite, e.g. http->https
                        wraphandleOrNodeURL = uriFilter.filterURI(nodeResolver.getUrl(node).toURI()).toString();
                        targetURI = uriBuilder.queryParam("url", wraphandleOrNodeURL).build();
                    } else {
                        wraphandleOrNodeURL = handle.toString();
                        if (handle.toString().contains(":")) {
                            wraphandleOrNodeURL = handle.toString().split(":")[1];
                        }
                        targetURI = uriBuilder.queryParam("hdl", wraphandleOrNodeURL).build();
                    }
                } catch (URISyntaxException ex) {
                    throw new NodeActionException(this, "Invalid URI for node", ex);
                }
            } else {
                //Buil redirect to IMDI Search
                final URI nodeId = node.getNodeURI();
                final String nodeid = nodeIdFilter.getURIParam(nodeId);
                final UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getMdSearchURL()).queryParam("nodeid", nodeid);
                targetURI = uriBuilder.queryParam("jsessionID", "session_number").build();
            }
        }
        if (targetURI != null) {
            final String redirectURL = targetURI.toString();
            final ShowComponentRequest request = new ShowComponentRequest() {

                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    return new PanelEmbedActionDisplay(id, redirectURL);
                }
            };
            return new SimpleNodeActionResult(request);
        } else {
            throw new NodeActionException(this, "target uri could not be build. This is likely to happen when no node was found. If this is not the case please check configuration paramters.");
        }
    }
}

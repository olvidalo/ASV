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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceWrittenType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.AudioFilePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ExternalFramePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.MediaFilePanel;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
@Component
public class CMDIViewNodeAction extends SingleNodeAction implements NodeActionSingletonBean  {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);   
    @Autowired
    private NodeActionsConfiguration nodeActionsConfiguration;   
    @Autowired
    private NodeIdFilter nodeIdFilter;   
    @Autowired
    private AccessChecker accessChecker;    
    @Autowired
    @Qualifier("browserService")
    private NodeResolver nodeResolver;

    /**
     * Default constructor for spring
     */
    protected CMDIViewNodeAction() {
    }
    
    /**
     *
     * @param nodeActionsConfiguration NodeActionsConfiguration, get Annex url
     * @param nodeResolver node resolver
     * @param nodeIdFilter Node ID filter
     * @param accessChecker access checker
     */
    protected CMDIViewNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeResolver nodeResolver, NodeIdFilter nodeIdFilter, AccessChecker accessChecker) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
        this.accessChecker = accessChecker;
        this.nodeResolver = nodeResolver;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        //Buil redirect to Annex here
        logger.debug("View on {} requested", node);

        try {
            if (isAnnexViewable(node)) {
                return createAnnexRequest(node);
            } else {
                return createViewRequest(node);
            }
        } catch (NodeNotFoundException ex) {
            throw new NodeActionException(this, "Node not found", ex);
        }
    }

    private boolean isAnnexViewable(final TypedCorpusNode node) {
        final NodeType nodeType = node.getNodeType();
        final boolean isAnnexViewable = nodeType instanceof CMDIResourceTxtType
                || nodeType instanceof ResourceWrittenType && node.getName().endsWith(".srt")
                || node.getName().endsWith(".eaf")
                || node.getName().endsWith("tbt")
                || node.getName().endsWith(".cha");
        return isAnnexViewable;
    }

    private NodeActionResult createAnnexRequest(final TypedCorpusNode node) throws IllegalArgumentException, UriBuilderException {
        final URI pid = nodeResolver.getPID(node);
        final URI targetURI;
        {
            final UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getAnnexURL());
            if (pid == null) {
                final String nodeId = nodeIdFilter.getURIParam(node.getNodeURI());
                targetURI = uriBuilder.queryParam("nodeid", nodeId).build();
            } else {
                targetURI = uriBuilder.queryParam("handle", pid.toString()).build();
            }
        }

        try {
            final NavigationActionRequest request = new NavigationActionRequest(targetURI.toURL());
            return new SimpleNodeActionResult(request);
        } catch (MalformedURLException ex) {
            logger.error("URL syntax exception while creating Annex URL", ex);
        }
        return null;
    }

    /**
     *
     * @param node
     * @return a {@link SimpleNodeActionResult} with a component depending on
     * the type of node - either a media panel, audio panel or general IFrame
     * viewer
     */
    private NodeActionResult createViewRequest(final TypedCorpusNode node) throws NodeNotFoundException {
        final NodeType nodeType = node.getNodeType();
        final String userid = auth.getPrincipalName();
        final boolean hasAccess = accessChecker.hasAccess(userid, node.getNodeURI());

        // construct a request to show a component depending on the type of resource
        final ShowComponentRequest componentRequest = new ShowComponentRequest() {
            @Override
            public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {

                if (hasAccess) { // do not show players if the user has no access to the resource
                    if (nodeType instanceof ResourceVideoType) {
                        return new MediaFilePanel(id, node);
                    } else if (nodeType instanceof ResourceAudioType) {
                        return new AudioFilePanel(id, node);
                    }
                }

                // Fallback for non-media files (e.g. images) to be rendered by the browser
                // If resource is not accessible, this will provide more information
                return new ExternalFramePanel(id, nodeResolver.getUrl(node).toString());
            }
        };
        return new SimpleNodeActionResult(componentRequest);
    }

    @Override
    public String getName() {
        return "View";
    }

    @Override
    public String getTitle() {
        return "View this resource";
    }
}

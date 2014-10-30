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
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
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
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.MediaFilePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ViewInfoFile;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
@Component
public class CMDIViewNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final FilterNodeIds nodeIdFilter;

    /**
     * Action class are autowired in nodeActions.xml
     *
     * @param nodeActionsConfiguration NodeActionsConfiguration, get Annex url
     * @param nodeIdFilter Node ID filter
     */
    @Autowired
    public CMDIViewNodeAction(NodeActionsConfiguration nodeActionsConfiguration, FilterNodeIds nodeIdFilter) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        //Buil redirect to Annex here
        logger.debug("View on {} requested", node);

        if (isAnnexViewable(node)) {
            return createAnnexRequest(node);
        } else {
            return createViewRequest(node);
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
        //TODO get session id
        final String nodeId = nodeIdFilter.getURIParam(node.getNodeURI());
        
        final URI targetURI;
        {
            final UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getAnnexURL());
            if ((nodeId.startsWith("hdl")) || nodeId.startsWith("1839")) { //TODO: remove hard coded handle prefix
                targetURI = uriBuilder.queryParam("handle", nodeId).build();
            } else {
                targetURI = uriBuilder.queryParam("nodeid", nodeId).build();
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
    private NodeActionResult createViewRequest(final TypedCorpusNode node) {
        final NodeType nodeType = node.getNodeType();
        final ShowComponentRequest componentRequest = new ShowComponentRequest() {
            @Override
            public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                if (nodeType instanceof ResourceVideoType) {
                    return new MediaFilePanel(id, node);
                } else if (nodeType instanceof ResourceAudioType) {
                    return new AudioFilePanel(id, node);
                } else {
                    return new ViewInfoFile(id, node);
                }
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

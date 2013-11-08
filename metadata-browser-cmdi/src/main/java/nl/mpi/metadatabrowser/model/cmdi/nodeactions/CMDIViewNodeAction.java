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

import edu.emory.mathcs.backport.java.util.Collections;
import java.net.MalformedURLException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIViewNodeAction extends SingleNodeAction implements NodeAction {

    private NodeActionsConfiguration nodeActionsConfiguration;
    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "View Node";
    private boolean navType = false;
    private final NodePresentationProvider presentationProvider;

    @Autowired
    public CMDIViewNodeAction(NodePresentationProvider presentationProvider, NodeActionsConfiguration nodeActionsConfiguration) {
        this.presentationProvider = presentationProvider;
        this.nodeActionsConfiguration = nodeActionsConfiguration;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        //Buil redirect to Annex here
        logger.debug("Action [{}] invoked on {}", getName(), node);
        URI targetURI = null;
        UriBuilder uriBuilder = UriBuilder.fromPath(nodeActionsConfiguration.getAnnexURL());
        if (node.getNodeType() instanceof CMDIResourceTxtType) {
            //TODO get session id
            URI nodeId = node.getNodeURI();
            targetURI = uriBuilder.queryParam("nodeid", nodeId).queryParam("jsessionID", "session_id").build();
            navType = true;
        }
        if (navType == true) {
            try {
                final NavigationActionRequest request = new NavigationActionRequest(targetURI.toURL());
                return new SimpleNodeActionResult(request);
            } catch (MalformedURLException ex) {
                logger.error("URL syntax exception:" + ex);
            }
        } else {
            final ShowComponentRequest componentRequest = new ShowComponentRequest() {
                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
                    try {
                        return presentationProvider.getNodePresentation(id, Collections.singleton(node));
                    } catch (NodePresentationException ex) {
                        throw new ControllerActionRequestException(ex);
                    }
                }
            };
            return new SimpleNodeActionResult(componentRequest);
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}

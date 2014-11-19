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
import javax.ws.rs.core.UriBuilderException;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ExternalFramePanel;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
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
public class CMDIRrsNodeAction  extends RedirectingNodeAction {

    private final NodeActionsConfiguration nodeActionsConfiguration;
    private final NodeIdFilter nodeIdFilter;

    @Autowired
    public CMDIRrsNodeAction(NodeActionsConfiguration nodeActionsConfiguration, NodeIdFilter nodeIdFilter) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
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
    protected URI getTarget(Collection<TypedCorpusNode> nodes)  {
        URI targetURI = null;
        UriBuilder uriBuilder = UriBuilder.fromUri(nodeActionsConfiguration.getRrsURL() + nodeActionsConfiguration.getRrsIndexURL());
        //TODO: properly support multiple nodes
        for (TypedCorpusNode node : nodes) {
            //Buil redirect to RRS
            URI nodeId = node.getNodeURI();
            String nodeid = nodeIdFilter.getURIParam(nodeId);
            targetURI = uriBuilder.queryParam("nodeid", nodeid).queryParam("jsessionID", "session_id").build();
        }
        return targetURI;
    }
}

/*
 * Copyright (C) 2015 Max Planck Institute for Psycholinguistics
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
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
@Component
public class ViewInAnnexAction extends SingleNodeActionSingletonBean {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    @Autowired
    private NodeActionsConfiguration nodeActionsConfiguration;
    @Autowired
    private NodeIdFilter nodeIdFilter;
    @Autowired
    @Qualifier("browserService")
    private NodeResolver nodeResolver;

    /**
     * Default constructor for spring
     */
    protected ViewInAnnexAction() {
    }

    protected ViewInAnnexAction(NodeActionsConfiguration nodeActionsConfiguration, NodeIdFilter nodeIdFilter, NodeResolver nodeResolver) {
        this.nodeActionsConfiguration = nodeActionsConfiguration;
        this.nodeIdFilter = nodeIdFilter;
        this.nodeResolver = nodeResolver;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {
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

    @Override
    public String getName() {
        return "View annotations";
    }

    @Override
    public String getTitle() {
        return "Open in annotation viewer";
    }
}

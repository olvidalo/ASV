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

import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.StyleSpecifier;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.AudioFilePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ExternalFramePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.MediaFilePanel;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
@Component
public class ViewResourceAction extends SingleNodeActionSingletonBean implements StyleSpecifier {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    @Autowired
    private AccessChecker accessChecker;
    @Autowired
    @Qualifier("browserService")
    private NodeResolver nodeResolver;

    /**
     * Default constructor for spring
     */
    protected ViewResourceAction() {
    }

    protected ViewResourceAction(AccessChecker accessChecker, NodeResolver nodeResolver) {
        this.accessChecker = accessChecker;
        this.nodeResolver = nodeResolver;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
        try {
            final String userid = auth.getPrincipalName();
            final boolean hasAccess = accessChecker.hasAccess(userid, node.getNodeURI());

            // construct a request to show a component depending on the type of resource
            final ShowComponentRequest componentRequest = new ShowComponentRequest() {
                @Override
                public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {

                    if (hasAccess) { // do not show players if the user has no access to the resource
                        final NodeType nodeType = node.getNodeType();
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
        } catch (NodeNotFoundException ex) {
            throw new NodeActionException(this, "Node not found", ex);
        }
    }

    @Override
    public String getName() {
        return "View resource content";
    }

    @Override
    public String getTitle() {
        return "Display the resource content in this window according to the browser's built in viewing capacities";
    }

    @Override
    public String getStyleClass() {
        return "ViewResource";
    }

}

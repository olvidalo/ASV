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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.NavigationActionRequest;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.model.MetadataTransformingModel;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import org.apache.wicket.markup.html.basic.Label;
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

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "view Node";
    private Map<String, URI> parameters = new HashMap<String, URI>();
    private boolean navType = false;
    private final NodeResolver nodeResolver;
    private final NodePresentationProvider presentationProvider;

    @Autowired
    public CMDIViewNodeAction(NodeResolver nodeResolver, NodePresentationProvider presentationProvider) {
	this.nodeResolver = nodeResolver;
	this.presentationProvider = presentationProvider;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
	logger.debug("Action [{}] invoked on {}", getName(), node);
	String xmlContent = null;

	if (node.getNodeType() instanceof CMDIResourceTxtType) {
	    //TODO get session id
	    try {
		parameters.put("nodeId", node.getNodeURI());
		parameters.put("jsessionID", new URI("jsessioID"));
		navType = true;
	    } catch (URISyntaxException ex) {
		logger.error("URI syntax exception in parameter session id: " + ex);
	    }
	} else {
	    //TODO: Maybe replace this with some informative message, just URL is a bit pointless
	    // maybe return error message because no other kind of nodes should end up here
	    xmlContent = nodeResolver.getUrl(node).toString();
	}

	if (navType == true) {
	    final NavigationActionRequest request = new NavigationActionRequest(NavigationRequest.NavigationTarget.ANNEX, parameters);
	    return new SimpleNodeActionResult(request);
	} else {
	    final ShowComponentRequest request = new ShowComponentRequest() {
		@Override
		public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
		    try {
			return presentationProvider.getNodePresentation(id, Collections.singleton(node));
		    } catch (NodePresentationException ex) {
			throw new ControllerActionRequestException(ex);
		    }
		}
	    };
	    return new SimpleNodeActionResult(request);
	}
    }

    @Override
    public String getName() {
	return name;
    }
}

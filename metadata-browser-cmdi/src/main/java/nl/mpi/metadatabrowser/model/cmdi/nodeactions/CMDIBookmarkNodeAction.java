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

import java.io.UnsupportedEncodingException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.ControllerActionRequestException;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionException;
import nl.mpi.metadatabrowser.model.NodeActionResult;
import nl.mpi.metadatabrowser.model.ShowComponentRequest;
import nl.mpi.metadatabrowser.model.SingleNodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.SimpleNodeActionResult;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelShowComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class CMDIBookmarkNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "Citation";
    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;

    @Autowired
    public CMDIBookmarkNodeAction(CorpusStructureProvider csdb, NodeResolver nodeResolver) {
	this.csdb = csdb;
	this.nodeResolver = nodeResolver;
    }

    @Override
    protected NodeActionResult execute(final TypedCorpusNode node) throws NodeActionException {
	logger.debug("Action [{}] invoked on {}", getName(), node);

	final ShowComponentRequest request = new ShowComponentRequest() {
	    @Override
	    public org.apache.wicket.Component getComponent(String id) throws ControllerActionRequestException {
		try {
		    // create panel form for bookmark action
		    return new PanelShowComponent(id, node, csdb, nodeResolver);
		//} catch (UnknownNodeException ex) {
		  //  throw new ControllerActionRequestException("Error creating display panel for node " + node.getNodeURI(), ex);
		} catch (UnsupportedEncodingException ex) {
		    throw new ControllerActionRequestException("Error due to encoding problem for creating display panel for node " + node.getNodeURI(), ex);
		}
	    }
	};
	return new SimpleNodeActionResult(request);
    }

    @Override
    public String getName() {
	return name;
    }
}

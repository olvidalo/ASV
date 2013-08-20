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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import java.util.Collection;
import java.util.Iterator;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.common.util.spring.SpringContextLoader;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.authorization.AdvAuthorizationService;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CMDIMetadata;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockAuthorizationService;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockLicenseService;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodePresentationProvider implements NodePresentationProvider {

    private AdvAuthorizationService authoSrv;
    private LicenseService licSrv;
    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;
    //TODO : decide where does userId comes from and implement accordingly
    private String userId;

    public CMDINodePresentationProvider(CorpusStructureProvider csdb, NodeResolver nodeResolver) {
	this.csdb = csdb;
	this.nodeResolver = nodeResolver;
    }

    @Override
    public Component getNodePresentation(String wicketId, Collection<TypedCorpusNode> nodes) throws NodePresentationException {
	//TODO: Implement actual presentation

	//TODO: for resource for id "resourcePresentation" 
	//get TypedCorpusNode node, 
	//getCmdiCorpusStructureDB csdb, 
	//get String userid, 
	//get LicenseService licenseService, 
	//get AdvAuthorizationService aSrv.
	// mostly beans ????
	licSrv = new MockLicenseService();
	authoSrv = new MockAuthorizationService();
	Iterator<TypedCorpusNode> iterator = nodes.iterator();
	while (iterator.hasNext()) {
	    TypedCorpusNode node = iterator.next();
	    try {
		if (node.getNodeType() instanceof CMDIMetadata) {
		    //TODO : implement metadata presentation
		} else if (node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType) {
		    return new ResourcePresentation(wicketId, node, csdb, nodeResolver, userId, licSrv, authoSrv);
		}
	    } catch (UnknownNodeException ex) {
		throw new NodePresentationException("Could not find node while building presentation for node " + node.getNodeId(), ex);
	    }
	}
	return new Label(wicketId, nodes.toString());
    }
}

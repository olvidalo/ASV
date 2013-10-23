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
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.authorization.AuthorizationService;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.CollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.model.cmdi.type.MetadataType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.model.cmdi.wicket.model.MetadataTransformingModel;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodePresentationProvider implements NodePresentationProvider {

    public static final String IMDI_XSL = "/imdi-viewer.xsl";
    public static final String CMDI_XSL = "/cmdi2xhtml.xsl";
    private final AuthorizationService authoSrv;
    private final LicenseService licSrv;
    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;
    //TODO: get these templates injected
    private final Templates imdiTemplates;
    private final Templates cmdiTemplates;

    /**
     *
     * @param csdb
     * @param nodeResolver
     */
    @Autowired
    public CMDINodePresentationProvider(CorpusStructureProvider csdb, NodeResolver nodeResolver, AuthorizationService authoSrv, LicenseService licSrv) {
	this.csdb = csdb;
	this.nodeResolver = nodeResolver;
	this.authoSrv = authoSrv;
	this.licSrv = licSrv;

	//TODO: get these injected
	final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	try {
	    imdiTemplates = transformerFactory.newTemplates(new StreamSource(getClass().getResourceAsStream(IMDI_XSL)));
	    cmdiTemplates = transformerFactory.newTemplates(new StreamSource(getClass().getResourceAsStream(CMDI_XSL)));
	} catch (TransformerException ex) {
	    throw new RuntimeException("Error compiling template", ex);
	}
    }

    @Override
    public Component getNodePresentation(String wicketId, Collection<TypedCorpusNode> nodes) throws NodePresentationException {
	//TODO : decide where does userId comes from and implement accordingly
	final String userId = "";
	if (nodes.size() == 1) {
	    final TypedCorpusNode node = nodes.iterator().next();
	    try {
		if (node.getNodeType() instanceof MetadataType || node.getNodeType() instanceof CollectionType) {
		    return createMetadataTransformation(node, wicketId);
		} else if (node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType) {
		    return new ResourcePresentation(wicketId, node, csdb, nodeResolver, userId, licSrv, authoSrv);
		} else {
		    return new Label(wicketId, node.toString());
		}
	    } catch (UnknownNodeException ex) {
		throw new NodePresentationException("Could not find node while building presentation for node " + node.getNodeURI(), ex);
	    }
	} else {
	    return new Label(wicketId, nodes.toString());
	}
    }

    private Component createMetadataTransformation(final TypedCorpusNode node, String wicketId) throws NodePresentationException {
	final Label contentLabel = new Label(wicketId, new MetadataTransformingModel(nodeResolver, node, getTemplates(node)));
	contentLabel.setEscapeModelStrings(false);
	return contentLabel;
    }

    private Templates getTemplates(final TypedCorpusNode node) {
	final NodeType nodeType = node.getNodeType();
	final Templates templates;
	if (nodeType instanceof IMDICorpusType || nodeType instanceof IMDISessionType) {
	    templates = imdiTemplates;
	} else {
	    templates = cmdiTemplates;
	}
	return templates;
    }
}

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.CollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIInfoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourcePictureType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceWrittenType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.model.cmdi.type.MetadataType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ExternalFramePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.MultiNodePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.WelcomePagePanel;
import nl.mpi.metadatabrowser.model.cmdi.wicket.model.MetadataTransformingModel;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CMDINodePresentationProvider implements NodePresentationProvider, Serializable {

    private final static Logger logger = LoggerFactory.getLogger(CMDINodePresentationProvider.class);
    private static final JavaScriptResourceReference IMDIVIEWER_JS = new JavaScriptResourceReference(CMDINodePresentationProvider.class, "res/imdi-viewer.js");
    private static final JavaScriptResourceReference CMDIVIEWER_JS = new JavaScriptResourceReference(CMDINodePresentationProvider.class, "res/cmdi2html.js");
    private final static CssResourceReference IMDIVIEWER_CSS = new CssResourceReference(CMDINodePresentationProvider.class, "res/imdi-viewer.css");

    private final NodeResolver nodeResolver;

    /**
     *
     * @param nodeResolver
     */
    @Autowired
    public CMDINodePresentationProvider(NodeResolver nodeResolver) {
        this.nodeResolver = nodeResolver;
    }

    @Override
    public Component getNodePresentation(String wicketId, Collection<TypedCorpusNode> nodes) throws NodePresentationException {
        logger.debug("Making node presentation for nodes {}", nodes);
        if (nodes.isEmpty()) {
            return new WelcomePagePanel(wicketId);
        }
        if (nodes.size() == 1) {
            final TypedCorpusNode node = nodes.iterator().next();
            try {
                if (node.getNodeType() instanceof MetadataType || node.getNodeType() instanceof CollectionType) {
                    logger.debug("Metadata: presentation through transformation");
                    return createMetadataTransformation(node, wicketId);
                } else if (node.getNodeType() instanceof CMDIResourceTxtType || node.getNodeType() instanceof CMDIResourceType || node.getNodeType() instanceof ResourceVideoType || node.getNodeType() instanceof ResourcePictureType || node.getNodeType() instanceof ResourceAudioType || node.getNodeType() instanceof ResourceWrittenType) {
                    logger.debug("Resource: presentation of resource info");
                    return new ResourcePresentation(wicketId, node);
                } else if (node.getNodeType() instanceof IMDIInfoType) {
                    logger.debug("Resource presentation for info file");
                    return new ExternalFramePanel(wicketId, nodeResolver.getUrl(node).toString());
                } else {
                    logger.debug("No presentation for node type: {}. Using plain node string representation", node.getNodeType());
                    return new Label(wicketId, node.toString());
                }
                //} catch (UnknownNodeException ex) {
                //   throw new NodePresentationException("Could not find node while building presentation for node " + node, ex);
            } catch (NodeTypeIdentifierException ex) {
                throw new NodePresentationException("could not find node type while building presentation for node " + node, ex);
            }
        } else {
            logger.debug("Multiple nodes, present as string representation of collection");
            return new MultiNodePanel(wicketId, new ListModel(new ArrayList(nodes)));
        }
    }

    private Component createMetadataTransformation(final TypedCorpusNode node, String wicketId) throws NodePresentationException, NodeTypeIdentifierException {
        final Label contentLabel = new Label(wicketId, new MetadataTransformingModel(node, getTemplates(node))) {

            @Override
            public void renderHead(IHeaderResponse response) {
                response.render(JavaScriptReferenceHeaderItem.forReference(IMDIVIEWER_JS));
                response.render(JavaScriptReferenceHeaderItem.forReference(CMDIVIEWER_JS));
                response.render(CssHeaderItem.forReference(IMDIVIEWER_CSS));
            }

        };
        contentLabel.setEscapeModelStrings(false);
        return contentLabel;
    }

    private String getTemplates(final TypedCorpusNode node) {
        final NodeType nodeType = node.getNodeType();
        if (nodeType instanceof IMDICorpusType || nodeType instanceof IMDISessionType || nodeType instanceof IMDICatalogueType) {
            return TemplateKeys.IMDI;
        } else {
            return TemplateKeys.CMDI;
        }
    }
}

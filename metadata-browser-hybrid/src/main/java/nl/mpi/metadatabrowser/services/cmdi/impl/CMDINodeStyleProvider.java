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

import nl.mpi.archiving.corpusstructure.core.AccessLevel;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.tree.wicket.components.ArchiveTreeNodeStyleProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIInfoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourcePictureType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceWrittenType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

;

/**
 * Icon provider for {@link CorpusNode}, primarily CMDI but with support for
 * {@link IMDICorpusType} and {@link IMDISessionType}. Icons are based on the
 * {@link NodeType} and the access level as reported by the
 * {@link CorpusStructureProvider}
 *
 * @param <T>
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CMDINodeStyleProvider<T extends CorpusNode> implements ArchiveTreeNodeStyleProvider<T> {

    private final static Logger logger = LoggerFactory.getLogger(CMDINodeStyleProvider.class);
    private final static String sessionClass = "type-session";
    private final static String catalogueClass = "type-catalogue";
    private final static String corpusClass = "type-corpus";
    private final static String audioClass = "type-audio";
    private final static String videoClass = "type-video";
//    private final static String annotationClass = "type-annotation";
    private final static String pictureClass = "type-image";
    private final static String writtenClass = "type-written";
    private final static String infoClass = "type-info";
    private final static String mediafileClass = "type-mediafile";
    private final static String fileClass = "type-file";
    private final static String cmdiClass = "type-cmdi";
    private final static String unknownTypeClass = "type-unknown";
    private final static String openClass = "access-open";
    private final static String licensedClass = "access-licensed";
    private final static String restrictedClass = "access-restricted";
    private final static String closedClass = "access-closed";
    private final static String externalClass = "access-external";
    private final static String unknownAccessClass = "access-unknown";
    private final NodeTypeIdentifier nodeTypeIdentifier;
    private final AccessInfoProvider accessInfoProvider;
    
    /**
     * Constructor Autowired in providers.xml
     *
     * @param nodeTypeIdentifier NodeTypeIdentifier, give the type of a node
     * @param accessInfoProvider AccessInfoProvider, provide access level for a
     * node
     */
    @Autowired
    public CMDINodeStyleProvider(NodeTypeIdentifier nodeTypeIdentifier, AccessInfoProvider accessInfoProvider) {
        this.nodeTypeIdentifier = nodeTypeIdentifier;
        this.accessInfoProvider = accessInfoProvider;
    }

    /**
     * Method that manage the type of resourceReference returned depending on
     * type of node and accesslevel
     *
     * @param contentNode, node for which type and accesslevel have to be grabed
     * @return ResourceReference
     */
    @Override
    public String getNodeStyle(T contentNode) {
        try {
            final NodeType nodeType = nodeTypeIdentifier.getNodeType(contentNode);
            final String nodeTypeIcon = getNodeTypeIcon(nodeType);
            final String accessIcon = getNodeAccessIcon(accessInfoProvider.getAccessLevel(contentNode.getNodeURI()));
            return nodeTypeIcon + " " + accessIcon;
        } catch (NodeTypeIdentifierException ex) {
            logger.error("Could not identify node type of {}", contentNode, ex);
        } catch (NodeNotFoundException ex) {
            logger.warn("Not not found: {}", contentNode, ex);
        }
        return ""; // no icon for non-node or unknown type...
    }

    private String getNodeTypeIcon(final NodeType nodeType) {
        final String nodeTypeClass;
        if (nodeType instanceof CMDIResourceType) {
            nodeTypeClass = mediafileClass;
        } else if (nodeType instanceof CMDIResourceTxtType) {
            nodeTypeClass = fileClass;
        } else if (nodeType instanceof IMDISessionType) {
            nodeTypeClass = sessionClass;
        } else if (nodeType instanceof IMDICorpusType) {
            nodeTypeClass = corpusClass;
        } else if (nodeType instanceof IMDICatalogueType) {
            nodeTypeClass = catalogueClass;
        } else if (nodeType instanceof CMDIMetadataType) {
            nodeTypeClass = cmdiClass;
        } else if (nodeType instanceof CMDICollectionType) {
            nodeTypeClass = corpusClass; //TODO: Separate CMDI collection type?
        } else if (nodeType instanceof IMDIInfoType) {
            nodeTypeClass = infoClass;
        } else if (nodeType instanceof ResourceVideoType) {
            nodeTypeClass = videoClass;
        } else if (nodeType instanceof ResourceAudioType) {
            nodeTypeClass = audioClass;
        } else if (nodeType instanceof ResourcePictureType) {
            nodeTypeClass = pictureClass;
        } else if (nodeType instanceof ResourceWrittenType) {
            nodeTypeClass = writtenClass;
        } else {
            nodeTypeClass = unknownTypeClass;
        }
        return nodeTypeClass;
    }

    private String getNodeAccessIcon(AccessLevel nodeAccessLevel) {
        if (nodeAccessLevel == null) {
            return unknownAccessClass;
        }
        switch (nodeAccessLevel) {
            case ACCESS_LEVEL_OPEN_EVERYBODY:
                return openClass;
            case ACCESS_LEVEL_OPEN_REGISTERED_USERS:
                return licensedClass;
            case ACCESS_LEVEL_PERMISSION_NEEDED:
                return restrictedClass;
            case ACCESS_LEVEL_CLOSED:
                return closedClass;
            case ACCESS_LEVEL_EXTERNAL:
                return externalClass;
            default:
                return unknownAccessClass;
        }
    }
}

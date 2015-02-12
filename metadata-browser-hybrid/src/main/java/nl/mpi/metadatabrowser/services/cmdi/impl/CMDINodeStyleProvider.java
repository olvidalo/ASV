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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.ImageIcon;
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
import org.apache.wicket.request.resource.ResourceReference;
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
    private final static String sessionIcon = "type-session";
    private final static String catalogueIcon = "type-catalogue";
    private final static String corpusIcon = "type-corpus";
    private final static String audioIcon = "type-audio";
    private final static String videoIcon = "type-video";
//    private final static String annotationIcon = "/Ficons/annotation.gif";
    private final static String pictureIcon = "type-image";
    private final static String writtenIcon = "type-written";
    private final static String infoIcon = "type-info";
    private final static String mediafileIcon = "type-mediafile";
    private final static String fileIcon = "type-file";
    private final static String cmdiIcon = "type-cmdi";
    private final static String unknownIcon = "type-unknown";
    private final static String openIcon = "access-open";
    private final static String licensedIcon = "access-licensed";
    private final static String restrictedIcon = "access-restricted";
    private final static String closedIcon = "access-closed";
    private final static String externalIcon = "access-external";
    private final Map<Entry<ImageIcon, ImageIcon>, ResourceReference> iconMap = new HashMap<>();
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
        final String nodeTypeIcon;
        if (nodeType instanceof CMDIResourceType) {
            nodeTypeIcon = mediafileIcon;
        } else if (nodeType instanceof CMDIResourceTxtType) {
            nodeTypeIcon = fileIcon;
        } else if (nodeType instanceof IMDISessionType) {
            nodeTypeIcon = sessionIcon;
        } else if (nodeType instanceof IMDICorpusType) {
            nodeTypeIcon = corpusIcon;
        } else if (nodeType instanceof IMDICatalogueType) {
            nodeTypeIcon = catalogueIcon;
        } else if (nodeType instanceof CMDIMetadataType) {
            nodeTypeIcon = cmdiIcon;
        } else if (nodeType instanceof CMDICollectionType) {
            nodeTypeIcon = corpusIcon; //TODO: Separate CMDI collection type?
        } else if (nodeType instanceof IMDIInfoType) {
            nodeTypeIcon = infoIcon;
        } else if (nodeType instanceof ResourceVideoType) {
            nodeTypeIcon = videoIcon;
        } else if (nodeType instanceof ResourceAudioType) {
            nodeTypeIcon = audioIcon;
        } else if (nodeType instanceof ResourcePictureType) {
            nodeTypeIcon = pictureIcon;
        } else if (nodeType instanceof ResourceWrittenType) {
            nodeTypeIcon = writtenIcon;
        } else {
            nodeTypeIcon = unknownIcon;
        }
        return nodeTypeIcon;
    }

    private String getNodeAccessIcon(AccessLevel nodeAccessLevel) {
        if (nodeAccessLevel == null) {
            return unknownIcon;
        }
        switch (nodeAccessLevel) {
            case ACCESS_LEVEL_OPEN_EVERYBODY:
                return openIcon;
            case ACCESS_LEVEL_OPEN_REGISTERED_USERS:
                return licensedIcon;
            case ACCESS_LEVEL_PERMISSION_NEEDED:
                return restrictedIcon;
            case ACCESS_LEVEL_CLOSED:
                return closedIcon;
            case ACCESS_LEVEL_EXTERNAL:
                return externalIcon;
            default:
                return unknownIcon;
        }
    }
}

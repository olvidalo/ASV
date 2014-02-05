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

import java.net.URI;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIInfoType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourcePictureType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIResourceWrittenType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;

import static nl.mpi.metadatabrowser.services.NodeTypeIdentifier.UNKNOWN_NODE_TYPE;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CMDINodeTypeIdentifier implements NodeTypeIdentifier {

    public static final String IMDI_MIME_TYPE = "application/imdi+xml";
    public static final URI COLLECTION_PROFILE_ID = URI.create("profile"); //TODO: have a list of profileID or add correct profileID
    private final ProfileIdentifierImpl profileIdentifier;

    /**
     *
     * @param csProvider
     */
    @Autowired
    public CMDINodeTypeIdentifier(CorpusStructureProvider csProvider) {
        this.profileIdentifier = new ProfileIdentifierImpl(csProvider);
    }

    /**
     * Determines the type of the specified corpus node, primarily on basis of
     * the value of {@link CorpusNode#getType() }. Futher disambiguation may
     * happen on basis of other properties.
     *
     * @param node node to determine type for
     * @return metadata browser internal node type
     * @throws NodeTypeIdentifierException
     * @see CorpusNodeType
     */
    @Override
    public NodeType getNodeType(CorpusNode node) throws NodeTypeIdentifierException {
        final CorpusNodeType corpusNodeType = node.getType();

        // Explicit null check, the switch won't cope
        if (corpusNodeType == null) {
            return UNKNOWN_NODE_TYPE;
        }

        String name = node.getName();
        // We have a type in the corpus node, determine CorpusNodeType
        switch (corpusNodeType) {
            case RESOURCE_VIDEO:
                return getResourceVideoType(name);
            case RESOURCE_AUDIO:
                return new CMDIResourceType();
            case RESOURCE_OTHER:
                return new CMDIResourceType();
            case RESOURCE_ANNOTATION:
                return getResourceAnnotationType(name);
            case RESOURCE_LEXICAL:
                return new CMDIResourceTxtType();
            case METADATA:
                return getMetadataType(node);
            case COLLECTION:
                return getCollectionType(node);
            case IMDICATALOGUE:
                return new IMDICatalogueType();
            case IMDIINFO:
                return new IMDIInfoType();
            default:
                return UNKNOWN_NODE_TYPE;
        }
    }

    /**
     * Determines type for non-collection (according to {@link CorpusNode#getType()
     * }) metadata node
     *
     * @param node node to determine type for
     * @return either IMDI session or CMDI type
     * @see CorpusNodeType#METADATA
     */
    private NodeType getMetadataType(CorpusNode node) {
        if (node.getFormat().equals(IMDI_MIME_TYPE)) {
            return new IMDISessionType();
        } else {
            return getCMDIType(node);
        }
    }

    private NodeType getCMDIType(CorpusNode node) {
        if (COLLECTION_PROFILE_ID.equals(profileIdentifier.getProfile(node.getNodeURI()))) {
            return new CMDICollectionType();
        } else {
            return new CMDIMetadataType();
        }
    }

    /**
     * Determines type for collection (according to {@link CorpusNode#getType()
     * }) metadata node
     *
     * @param node
     * @return
     * @see CorpusNodeType#COLLECTION
     */
    private NodeType getCollectionType(CorpusNode node) {
        if (node.getFormat().equals(IMDI_MIME_TYPE)) {
            return new IMDICorpusType();
        } else {
            return new CMDICollectionType();
        }
    }

    /**
     * Determines type for annotation resources (according to {@link CorpusNode#getType()
     * })
     *
     * @param name, String as the name of a node
     * @return
     * @see CorpusNodeType#RESOURCE_ANNOTATION
     */
    private NodeType getResourceAnnotationType(String name) {
        if (name.endsWith(".txt") || name.endsWith(".pdf") || name.endsWith(".eaf") || name.endsWith(".html") || name.endsWith(".csv") || name.endsWith(".lmf") || name.endsWith(".xml") || name.endsWith(".xsd") || name.endsWith("tbt") || name.endsWith(".cha")) {
            return new IMDIResourceWrittenType();
        } else {
            return new CMDIResourceTxtType();
        }
    }

    /**
     * Determines type for video resources (according to {@link CorpusNode#getType()
     * })
     *
     * @param name, String as the name of a node
     * @return
     * @see CorpusNodeType#RESOURCE_VIDEO
     */
    private NodeType getResourceVideoType(String name) {
        if (name.endsWith(".jpg") || name.endsWith(".html") || name.endsWith(".gif") || name.endsWith(".tif") || name.endsWith(".tiff") || name.endsWith(".png")) {
            return new IMDIResourcePictureType();
        } else if (name.endsWith(".wav") || name.endsWith(".mp3") || name.endsWith(".m4a") || name.endsWith(".aif")) {
            return new IMDIResourceAudioType();
        } else if (name.endsWith(".mpeg") || name.endsWith(".mpg") || name.endsWith(".mov") || name.endsWith(".mpv") || name.endsWith(".mp4") || name.endsWith(".smil")) {
            return new IMDIResourceVideoType();
        } else {
            return new CMDIResourceType();
        }
    }
}

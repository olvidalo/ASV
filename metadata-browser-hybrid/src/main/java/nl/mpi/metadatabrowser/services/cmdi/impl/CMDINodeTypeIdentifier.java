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

import java.io.File;
import java.net.URI;
import java.util.regex.Pattern;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDICollectionType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDIInfoType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceAudioType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourcePictureType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceVideoType;
import nl.mpi.metadatabrowser.model.cmdi.type.ResourceWrittenType;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import static nl.mpi.metadatabrowser.services.NodeTypeIdentifier.UNKNOWN_NODE_TYPE;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import nl.mpi.metadatabrowser.services.cmdi.ProfileIdentifier;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service that provides information about the node type of some corpus node
 *
 * TODO: Add (thread or session local?) caching of node URI -> type mapping (not
 * likely to change very often) - com.google.common.cache.CacheBuilder might be
 * a good basis
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CMDINodeTypeIdentifier implements NodeTypeIdentifier {

    private final static Pattern CMDI_FILE_PATTERN = Pattern.compile(".*\\.cmdi$", Pattern.CASE_INSENSITIVE);
    public static final String IMDI_MIME_TYPE = "application/imdi+xml";
    public static final URI COLLECTION_PROFILE_ID = URI.create("profile"); //TODO: have a list of profileID or add correct profileID
    private final ProfileIdentifier profileIdentifier;
    private final NodeResolver nodeResolver;

    /**
     *
     * @param profileIdentifier profile identifier to use for CMDI metadata
     * @param nodeResolver resolver for looking up node locations
     */
    @Autowired
    public CMDINodeTypeIdentifier(ProfileIdentifier profileIdentifier, NodeResolver nodeResolver) {
        this.profileIdentifier = profileIdentifier;
        this.nodeResolver = nodeResolver;
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

        // We have a type in the corpus node, determine CorpusNodeType
        switch (corpusNodeType) {
            case RESOURCE_VIDEO:
                return new ResourceVideoType();
            case RESOURCE_AUDIO:
                return new ResourceAudioType(); // previous new CMDIResourceType();
            case RESOURCE_IMAGE:
                return new ResourcePictureType();
            case RESOURCE_OTHER:
                return new CMDIResourceType(); // default icon Media
            case RESOURCE_ANNOTATION:
                return new ResourceWrittenType();
            case RESOURCE_LEXICAL:
                return new ResourceWrittenType();
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
        // Determine node file name, which is needed to determine the metadata
        // type (IMDI or CMDI) - see https://trac.mpi.nl/ticket/4511
        final String name = getFileName(node);
        
        if (node.getFormat().equals(IMDI_MIME_TYPE)) {
            // can still be transformed CMDI, check
            if (CMDI_FILE_PATTERN.matcher(name).matches()) {
                return new CMDIMetadataType();
            } else {
                return new IMDISessionType();
            }
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
        // Determine node file name, which is needed to determine the metadata
        // type (IMDI or CMDI) - see https://trac.mpi.nl/ticket/4511
        final String name = getFileName(node);
        
        if (node.getFormat().equals(IMDI_MIME_TYPE)) {
            // can still be transformed CMDI, check
            if (CMDI_FILE_PATTERN.matcher(name).matches()) {
                return new CMDICollectionType();
            } else {
                return new IMDICorpusType();
            }
        } else {
            return new CMDICollectionType();
        }
    }

    private String getFileName(CorpusNode node) {
        // the following step can be expensive when it needs a handle lookup
        // (depending on the implementation) - consider using a caching
        // implementation
        final File localFile = nodeResolver.getLocalFile(node);
        if (localFile == null) {
            return nodeResolver.getUrl(node).getPath();
        } else {
            return localFile.getName();
        }
    }

}

/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.metadatabrowser.model.cmdi.wicket.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.CorpusNodeType;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.corpusstructure.UnknownNodeException;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICatalogueType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifierException;
import nl.mpi.metadatabrowser.services.cmdi.impl.CMDINodePresentationProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class MetadataTransformingModel extends AbstractReadOnlyModel<String> {

    private final static Logger logger = LoggerFactory.getLogger(CMDINodePresentationProvider.class);
    private final String content;

    public MetadataTransformingModel(NodeResolver nodeResolver, TypedCorpusNode node, Templates templates, CorpusStructureProvider csp, NodeTypeIdentifier nodeTypeIdentifier) throws NodePresentationException, nl.mpi.archiving.corpusstructure.core.UnknownNodeException, NodeTypeIdentifierException {
        try {
            logger.debug("Transforming node {} using templates {}", node, templates);
            final InputStream in = nodeResolver.getInputStream(node);	// get the file
            StringWriter strWriter = new StringWriter();
            try {
                final Transformer transformer = templates.newTransformer();
                // set transformer options
                transformer.setOutputProperty(OutputKeys.METHOD, "html");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setParameter("CORPUS_LINKING", "false");
                transformer.setParameter("DOCUMENT_ID", node);
                transformNodeContent(strWriter, in, transformer);
                if (node.getNodeType() instanceof IMDICorpusType) {
                    strWriter = addCatalogueContentToCorpusView(node, transformer, strWriter, nodeResolver, csp, nodeTypeIdentifier);
                }
                // Transform, outputting to string
//                final Source source = new StreamSource(in);
//                transformer.transform(source, new StreamResult(strWriter));
            } finally {
                in.close();
            }

            // write to wicket the result of the parsing - not escaping model string so as to pass through the verbatim HTML 
            content = strWriter.toString();
        } catch (IOException ex) {
            throw new NodePresentationException("Could not read metadata for transformation", ex);       
	} catch (TransformerException ex) {
	    throw new NodePresentationException("Could not transform metadata", ex);
    }catch(NodeTypeIdentifierException ex){
        throw new NodeTypeIdentifierException("could not match node type in transformation", ex);
    }
    }

    @Override
    public String getObject() {
        return content;
    }

    private List<CorpusNode> getCatalogueNodesUnderCorpus(TypedCorpusNode corpusNode, CorpusStructureProvider csp, NodeResolver nodeResolver, NodeTypeIdentifier nodeTypeIdentifier) throws nl.mpi.archiving.corpusstructure.core.UnknownNodeException, NodeTypeIdentifierException {
        List<CorpusNode> result = new ArrayList<CorpusNode>();
        List<CorpusNode> children = csp.getChildNodes(corpusNode.getNodeURI());
        for (CorpusNode childNode : children) {
            try {
                if (nodeTypeIdentifier.getNodeType(childNode) instanceof IMDICatalogueType) {
                    result.add(childNode);
                }
            } catch (UnknownNodeException e) {
                logger.error("Node not found: " + e);
            }
        }
        return result;
    }

    private StringWriter addCatalogueContentToCorpusView(TypedCorpusNode node, Transformer transformer, StringWriter strWriter, NodeResolver nodeResolver, CorpusStructureProvider csp, NodeTypeIdentifier nodeTypeIdentifier) throws nl.mpi.archiving.corpusstructure.core.UnknownNodeException, IOException, NodePresentationException, NodeTypeIdentifierException {
        StringWriter result = strWriter;

        List<CorpusNode> catalogueNodeURLs = getCatalogueNodesUnderCorpus(node, csp, nodeResolver, nodeTypeIdentifier);
        // int indexOfBodyClose = -1;
        if (!catalogueNodeURLs.isEmpty()) {
            transformer.setParameter("DISPLAY_ONLY_BODY", "true");
            //String currentContent = content.toString();
            //indexOfBodyClose = currentContent.lastIndexOf("</body>");//Strip body close and add it later so we have pretty HTML again.
            //if (indexOfBodyClose != -1) {
            //    result= new StringWriter();
            //    result.append(currentContent.substring(0, indexOfBodyClose));
            // }
        }
        for (CorpusNode catalogueNodeUrl : catalogueNodeURLs) {
            InputStream in = nodeResolver.getInputStream(catalogueNodeUrl);
            transformNodeContent(strWriter, in, transformer);
            in.close();
        }
        return result;
    }

    private void transformNodeContent(StringWriter strWriter, InputStream in, Transformer transformer) throws NodePresentationException {
        final Source source = new StreamSource(in);
        try{
        transformer.transform(source, new StreamResult(strWriter));
        } catch (TransformerException ex) {
            throw new NodePresentationException("Could not transform metadata", ex);
        }
    }
}

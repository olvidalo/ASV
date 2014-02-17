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
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
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
 * Model class that handles the logic for node transformation, In other words,
 * handle the display of node presentation depending on type.
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class MetadataTransformingModel extends AbstractReadOnlyModel<String> {

    private final static Logger logger = LoggerFactory.getLogger(CMDINodePresentationProvider.class);
    private final String content;

    /**
     * model Constructor, set parameters and call for transformation
     *
     * @param nodeResolver NodeResolver, get inputStream for a given node
     * @param node TypedCorusNode, node to be transformed
     * @param templates, Templates, template to be use for transformation
     * (either cmdi or imdi)
     * @param csp CorpusStructureProvider, instanceof to be passed on for usage
     * in getCatalogueNodesUnderCorpus
     * @param nodeTypeIdentifier NodeTypeIdenfier, instanceof to be passed on
     * for usage in getCatalogueNodesUnderCorpus
     * @throws NodePresentationException
     * @throws NodeTypeIdentifierException
     */
    public MetadataTransformingModel(NodeResolver nodeResolver, TypedCorpusNode node, Templates templates, CorpusStructureProvider csp, NodeTypeIdentifier nodeTypeIdentifier) throws NodePresentationException, NodeTypeIdentifierException {
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
		transformer.setParameter("DOCUMENT_ID", node.toString());
		transformNodeContent(strWriter, in, transformer);
		if (node.getNodeType() instanceof IMDICorpusType) {
		    strWriter = addCatalogueContentToCorpusView(node, transformer, strWriter, nodeResolver, csp, nodeTypeIdentifier);
		}
	    } finally {
		in.close();
	    }

	    // write to wicket the result of the parsing - not escaping model string so as to pass through the verbatim HTML 
	    content = strWriter.toString();
	} catch (IOException ex) {
	    throw new NodePresentationException("Could not read metadata for transformation", ex);
	} catch (TransformerException ex) {
	    throw new NodePresentationException("Could not transform metadata", ex);
	} catch (NodeTypeIdentifierException ex) {
	    throw new NodeTypeIdentifierException("could not match node type in transformation", ex);
	}
    }

    @Override
    public String getObject() {
	return content;
    }

    /**
     * Method that set specific parameter to transformer and if call for
     * catalogue node return a list with at least one element. Makes call to
     * transformation for catalogue nodes to be added to corpus view
     *
     * @param node TypedCorpusNode, node under which existing catalogue nodes
     * will be added to view
     * @param transformer Transformer, transformer will set specific parameter
     * if at least one catalogue node exist
     * @param strWriter StringWriter, StringWriter use for transformation
     * @param nodeResolver NodeResolver, retrieve the input stram for catalogue
     * node
     * @param csp CorpusStructureProvider, passes on provider to retrieve
     * children in getCatalogueNodesUnderCorpus
     * @param nodeTypeIdentifier NodeTypeIdentifier, passes on identifier types
     * to check for catalogue typein getCatalogueNodesUnderCorpus
     * @return StringWriter to be tranformed
     * @throws IOException
     * @throws NodePresentationException
     * @throws NodeTypeIdentifierException
     */
    private StringWriter addCatalogueContentToCorpusView(TypedCorpusNode node, Transformer transformer, StringWriter strWriter, NodeResolver nodeResolver, CorpusStructureProvider csp, NodeTypeIdentifier nodeTypeIdentifier) throws IOException, NodePresentationException, NodeTypeIdentifierException {
	StringWriter result = strWriter;

	List<CorpusNode> catalogueNodeURLs = getCatalogueNodesUnderCorpus(node, csp, nodeTypeIdentifier);
	if (!catalogueNodeURLs.isEmpty()) {
	    transformer.setParameter("DISPLAY_ONLY_BODY", "true");
	}
	for (CorpusNode catalogueNodeUrl : catalogueNodeURLs) {
	    InputStream in = nodeResolver.getInputStream(catalogueNodeUrl);
	    transformNodeContent(strWriter, in, transformer);
	    in.close();
	}
	return result;
    }

    /**
     * Method that make a list of existing catalogue nodes to be added to a
     * given node's presentation
     *
     * @param corpusNode TypedCorpusNode, node that needs to add catalogue
     * information if available
     * @param csp CorpusStructureProvider, get children for specific node
     * @param nodeTypeIdentifier NodeTypeIdentifer, check if selected node is of
     * type catalogue
     * @return List of Catalogue Node as CorpusNode
     * @throws NodeTypeIdentifierException
     */
    private List<CorpusNode> getCatalogueNodesUnderCorpus(TypedCorpusNode corpusNode, CorpusStructureProvider csp, NodeTypeIdentifier nodeTypeIdentifier) throws NodeTypeIdentifierException {
	List<CorpusNode> result = new ArrayList<CorpusNode>();
	List<CorpusNode> children = csp.getChildNodes(corpusNode.getNodeURI());
	for (CorpusNode childNode : children) {
	    if (nodeTypeIdentifier.getNodeType(childNode) instanceof IMDICatalogueType) {
		result.add(childNode);
	    }
	}
	return result;
    }

    /**
     * Method that will transform the node content for page presentation display
     *
     * @param strWriter StringWriter, to be transformed
     * @param in InputStream, contains the node input
     * @param transformer Transformer
     * @throws NodePresentationException
     */
    private void transformNodeContent(StringWriter strWriter, InputStream in, Transformer transformer) throws NodePresentationException {
	// Transform, outputting to string
	final Source source = new StreamSource(in);
	try {
	    transformer.transform(source, new StreamResult(strWriter));
	} catch (TransformerException ex) {
	    throw new NodePresentationException("Could not transform metadata", ex);
	}
    }
}

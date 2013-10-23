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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class MetadataTransformingModel extends AbstractReadOnlyModel<String> {

    private final String content;

    public MetadataTransformingModel(NodeResolver nodeResolver, TypedCorpusNode node, Templates templates) throws NodePresentationException {
	try {
	    final InputStream in = nodeResolver.getInputStream(node);	// get the file
	    final StringWriter strWriter = new StringWriter();
	    try {
		final Transformer transformer = templates.newTransformer();
		// set transformer options
		transformer.setOutputProperty(OutputKeys.METHOD, "html");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setParameter("DISPLAY_ONLY_BODY", "true"); // for IMDI

		// Transform, outputting to string
		final Source source = new StreamSource(in);
		transformer.transform(source, new StreamResult(strWriter));
	    } finally {
		in.close();
	    }

	    // write to wicket the result of the parsing - not escaping model string so as to pass through the verbatim HTML 
	    content = strWriter.toString();
	} catch (IOException ex) {
	    throw new NodePresentationException("Could not read metadata for transformation", ex);
	} catch (TransformerException ex) {
	    throw new NodePresentationException("Could not transform metadata", ex);
	}
    }

    @Override
    public String getObject() {
	return content;
    }
}

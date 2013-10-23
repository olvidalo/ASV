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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.NodeType;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDICorpusType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class PanelViewNodeShowComponent extends Panel {

    public static final String IMDI_XSL = "/imdi-viewer.xsl";
    public static final String CMDI_XSL = "/cmdi2xhtml.xsl";

    public PanelViewNodeShowComponent(String id, NodeResolver nodeResolver, TypedCorpusNode node) throws NodePresentationException {
	super(id);
	final NodeType nodeType = node.getNodeType();

	final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	try {
	    final InputStream in = nodeResolver.getInputStream(node);	// get the file
	    final StringWriter strWriter = new StringWriter();
	    try {
		final Transformer transformer;
		if (nodeType instanceof IMDICorpusType || nodeType instanceof IMDISessionType) {
		    transformer = transformerFactory.newTransformer(new StreamSource(getClass().getResourceAsStream(IMDI_XSL)));
		} else {
		    transformer = transformerFactory.newTransformer(new StreamSource(getClass().getResourceAsStream(CMDI_XSL)));
		}
		// set transformer options
		transformer.setOutputProperty(OutputKeys.METHOD, "html");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		// Transform, outputting to string
		final Source source = new StreamSource(in);
		transformer.transform(source, new StreamResult(strWriter));
	    } finally {
		in.close();
	    }

	    // write to wicket the result of the parsing - not escaping model string so as to pass through the verbatim HTML 
	    final Label cmdiLabel = new Label("cmdiView", strWriter.toString());
	    //TODO: make this output valid HTML, then escape model strings!
	    //cmdiLabel.setEscapeModelStrings(false);
	    add(cmdiLabel);
	} catch (IOException ex) {
	    throw new NodePresentationException("Could not read metadata for transformation", ex);
	} catch (TransformerException ex) {
	    throw new NodePresentationException("Could not transform metadata", ex);
	}
    }
}

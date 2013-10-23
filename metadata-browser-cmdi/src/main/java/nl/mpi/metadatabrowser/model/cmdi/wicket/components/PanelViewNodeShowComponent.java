package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodePresentationException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

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
/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class PanelViewNodeShowComponent extends Panel {

    private final URL xslFile = getClass().getResource("/cmdi2xhtml.xsl");
    private final URL imdiXSLFile = getClass().getResource("/imdi-viewer.xsl");

    public PanelViewNodeShowComponent(String id, NodeResolver nodeResolver, TypedCorpusNode node) throws NodePresentationException {
	super(id);

	final Processor proc = new Processor(false);
	final XsltCompiler comp = proc.newXsltCompiler();

	try {
	    final InputStream in = nodeResolver.getInputStream(node);	// get the file
	    final XsltExecutable exp;
	    if (nodeResolver.getUrl(node).toString().endsWith(".imdi")) {
		exp = comp.compile(new StreamSource(imdiXSLFile.getFile())); // compile the xslt imdi file
	    } else {
		exp = comp.compile(new StreamSource(xslFile.getFile())); // compile the xslt file
	    }
	    final XdmNode source = proc.newDocumentBuilder().build(
		    new StreamSource(new InputStreamReader(in)));
	    final Serializer out = new Serializer();
	    final StringWriter strWriter = new StringWriter();
	    out.setOutputWriter(strWriter);
	    out.setOutputProperty(Serializer.Property.METHOD, "html");
	    out.setOutputProperty(Serializer.Property.INDENT, "yes");
	    out.setOutputProperty(Serializer.Property.ENCODING, "UTF-8");
	    final XsltTransformer trans = exp.load();

	    trans.setInitialContextNode(source);// parse xml via xslt
	    trans.setDestination(out);
	    trans.transform();

	    // write to wicket the result of the parsing - not escaping model string so as to pass through the verbatim HTML 
	    final Label cmdiLabel = new Label("cmdiView", strWriter.toString());
	    cmdiLabel.setEscapeModelStrings(false);
	    add(cmdiLabel);
	} catch (IOException ex) {
	    throw new NodePresentationException("Could not read metadata for transformation", ex);
	} catch (SaxonApiException ex) {
	    throw new NodePresentationException("Could not transform metadata", ex);
	}
    }
}

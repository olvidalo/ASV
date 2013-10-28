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

import java.util.Arrays;
import java.util.Collection;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.authorization.AuthorizationService;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import org.apache.wicket.Component;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import static org.jmock.Expectations.any;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodePresentationProviderTest {

    private final Mockery context = new JUnit4Mockery() {
	{
	    // Adds ability to mock abstract classes (in this case javax.xml.transform.Transformer)
	    setImposteriser(ClassImposteriser.INSTANCE);
	}
    };
    private TypedCorpusNode cmdiNode = new MockTypedCorpusNode(new CMDIMetadataType(), "node:1", "MockNode 1");
    private final Collection<TypedCorpusNode> collectionCorpus = Arrays.<TypedCorpusNode>asList(cmdiNode);

    /**
     * Test of getNodePresentation method, of class CMDINodePresentationProvider.
     */
    @Test
    public void testGetNodePresentation() throws Exception {
	System.out.println("getNodePresentation");
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);
	final AuthorizationService authSrv = context.mock(AuthorizationService.class);
	final LicenseService licSrv = context.mock(LicenseService.class);
	final Templates imdiTemplates = context.mock(Templates.class, "imdiTemplates");
	final Templates cmdiTemplates = context.mock(Templates.class, "cmdiTemplates");
	final Transformer transformer = context.mock(Transformer.class);

	context.checking(new Expectations() {
	    {
		// transformer will be created from templates (CMDI in this case)
		oneOf(cmdiTemplates).newTransformer();
		will(returnValue(transformer));

		never(imdiTemplates).newTransformer();

		// transformer will be configured...
		allowing(transformer).setOutputProperty(with(any(String.class)), with(any(String.class)));
		allowing(transformer).setParameter(with(any(String.class)), with(any(String.class)));

		// transformable content will be read
		oneOf(nodeResolver).getInputStream(cmdiNode);
		will(returnValue(getClass().getResourceAsStream("/IPROSLA_Nijmegen.cmdi")));

		// transformer will be requested to transform content
		oneOf(transformer).transform(with(any(StreamSource.class)), with(any(Result.class)));
	    }
	});

	CMDINodePresentationProvider instance = new CMDINodePresentationProvider(nodeResolver, authSrv, licSrv, imdiTemplates, cmdiTemplates);
	// Wicket tester is required to provide a wicket application context
	WicketTester tester = new WicketTester();
	// This call will trigger the transformation and wrap it in a component
	Component result = instance.getNodePresentation("test", collectionCorpus);
	tester.startComponentInPage(result);

	assertNotNull(result);
	assertEquals(result.getId(), "test");
    }
    //TODO: Add cases for IMDI, resource and fallback presentation
}

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
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.lat.ams.service.LicenseService;
import nl.mpi.lat.auth.authorization.AuthorizationService;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import org.apache.wicket.Component;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDINodePresentationProviderTest {
    
    private final Mockery context = new JUnit4Mockery();
    private TypedCorpusNode corpType = new MockTypedCorpusNode("node:1", "MockNode 1");

    /**
     * Test of getNodePresentation method, of class CMDINodePresentationProvider.
     */
    @Test
    public void testGetNodePresentation() throws Exception {
	System.out.println("getNodePresentation");
	final Collection<TypedCorpusNode> collectionCorpus = Arrays.<TypedCorpusNode>asList(corpType);
	final CorpusStructureProvider cs = context.mock(CorpusStructureProvider.class);
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);
	final AuthorizationService authSrv = context.mock(AuthorizationService.class);
	final LicenseService licSrv = context.mock(LicenseService.class);
	CMDINodePresentationProvider instance = new CMDINodePresentationProvider(cs, nodeResolver, authSrv, licSrv);
	
	context.checking(new Expectations() {
	    {
		oneOf(nodeResolver).getInputStream(corpType);
		will(returnValue(getClass().getResourceAsStream("/IPROSLA_Nijmegen.cmdi")));
	    }
	});

	// Wicket tester is required to provide a wicket application context
	WicketTester tester = new WicketTester();
	Component result = instance.getNodePresentation("test", collectionCorpus);
	tester.startComponentInPage(result);
	
	assertNotNull(result);
	assertEquals(result.getId(), "test");
    }
}

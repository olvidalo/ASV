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
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import nl.mpi.archiving.corpusstructure.core.AccessInfo;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import static org.hamcrest.Matchers.instanceOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ZipServiceImplTest {

    public static final URI NODE1_ID = URI.create("node:1");
    public static final URI NODE2_ID = URI.create("node:2");
    private final Mockery context = new JUnit4Mockery();

    public ZipServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createZipFileForNodes method, of class ZipServiceImpl.
     */
    @Test
    public void testCreateZipFileForNodes() throws Exception {
	System.out.println("createZipFileForNodes");
	final CorpusStructureProvider csdb = context.mock(CorpusStructureProvider.class);
	final NodeResolver nodeResolver = context.mock(NodeResolver.class);
	final TypedCorpusNode child1 = context.mock(TypedCorpusNode.class, "child1");
	final TypedCorpusNode child2 = context.mock(TypedCorpusNode.class, "child2");
	final List<TypedCorpusNode> childrenNodes = Arrays.asList(child1, child2);
	final AccessInfo ai = context.mock(AccessInfo.class);
	final String userId = "corpman";

	context.checking(new Expectations() {
	    {
		allowing(child1).getNodeURI();
		will(returnValue(NODE1_ID));
		allowing(child2).getNodeURI();
		will(returnValue(NODE2_ID));

		allowing(nodeResolver).getUrl(child1);
		will(returnValue(getClass().getClassLoader().getResource("IPROSLA_Nijmegen.cmdi")));
		allowing(nodeResolver).getUrl(child2);
		will(returnValue(getClass().getClassLoader().getResource("IPROSLA_Corpora.cmdi")));

		allowing(csdb).getObjectAccessInfo(NODE1_ID);
		will(returnValue(ai));

		allowing(csdb).getObjectAccessInfo(NODE2_ID);
		will(returnValue(ai));

		allowing(ai).hasReadAccess(userId);
		will(returnValue(true));
	    }
	});

	ZipServiceImpl instance = new ZipServiceImpl(csdb, nodeResolver);
	File result = instance.createZipFileForNodes(childrenNodes, userId);
	ZipFile zip = new ZipFile(result);
	assertThat(result, instanceOf(File.class));
	assertNotNull(result.getPath());
	assertNotNull(result.length());
	assertEquals(2, zip.size());
    }
}

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
package nl.mpi.metadatabrowser.services.impl.cmdi;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
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
        final CmdiCorpusStructureDB csdb = context.mock(CmdiCorpusStructureDB.class);
        final TypedCorpusNode child1 = context.mock(TypedCorpusNode.class, "child1");
        final TypedCorpusNode child2 = context.mock(TypedCorpusNode.class, "child2");
        final List<TypedCorpusNode> childrenNodes = Arrays.asList(child1, child2);

        context.checking(new Expectations() {

            {
                oneOf(child1).getNodeId();
                will(returnValue(1));
                oneOf(child2).getNodeId();
                will(returnValue(2));
                oneOf(csdb).getObjectURI(1);
                will(returnValue(getClass().getClassLoader().getResource("IPROSLA_Nijmegen.cmdi").toURI()));
                oneOf(csdb).getObjectURI(2);
                will(returnValue(getClass().getClassLoader().getResource("IPROSLA_Corpora.cmdi").toURI()));
            }
        });

        ZipServiceImpl instance = new ZipServiceImpl(csdb);
        File result = instance.createZipFileForNodes(childrenNodes);
        ZipFile zip = new ZipFile(result);
        assertThat(result, instanceOf(File.class));
        assertNotNull(result.getPath());
        assertNotNull(result.length());
        assertEquals(2, zip.size());
    }
}

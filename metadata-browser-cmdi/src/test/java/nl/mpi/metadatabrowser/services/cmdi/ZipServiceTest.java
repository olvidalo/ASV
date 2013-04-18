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
package nl.mpi.metadatabrowser.services.cmdi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import nl.mpi.corpusstructure.UnknownNodeException;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ZipServiceTest {
        private final Mockery context = new JUnit4Mockery();
        
    public ZipServiceTest() {
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
     * Test of createZipFileForNodes method, of class ZipService.
     */
    @Test
    public void testCreateZipFileForNodes() throws Exception {
        System.out.println("createZipFileForNodes");
        final CmdiCorpusStructureDB csdb = context.mock(CmdiCorpusStructureDB.class);
        final ZipService zipService = context.mock(ZipService.class);
        final TypedCorpusNode node = context.mock(TypedCorpusNode.class, "parent");
        final TypedCorpusNode child1 = context.mock(TypedCorpusNode.class, "child1");
        final TypedCorpusNode child2 = context.mock(TypedCorpusNode.class, "child2");
        final List<TypedCorpusNode> childrenNodes = Arrays.asList(child1,child2);
        
        ZipService instance = new ZipServiceImpl();
        File expResult = null;
        File result = instance.createZipFileForNodes(childrenNodes);
        assertEquals(expResult, result);

    }

    public class ZipServiceImpl implements ZipService {

        public File createZipFileForNodes(List<TypedCorpusNode> childrenNodes) throws IOException, UnknownNodeException, FileNotFoundException {
            return null;
        }
    }
}

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

import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.jmock.Expectations.returnValue;
import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CmdiCorpusStructureProviderFactoryImplTest {

    private final Mockery context = new JUnit4Mockery();

    public CmdiCorpusStructureProviderFactoryImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of createCorpusStructureDB method, of class
     * CmdiCorpusStructureProviderFactoryImpl.
     */
    @Test
    public void testCreateCorpusStructureDB() {
        System.out.println("createCorpusStructureDB");
        final CorpusStructureProvider cmdicsdb = context.mock(CorpusStructureProvider.class);
        CmdiCorpusStructureProviderFactoryImpl instance = new CmdiCorpusStructureProviderFactoryImpl(cmdicsdb);
        final CorpusStructureProvider result = instance.createCorpusStructureDB();
        context.checking(new Expectations() {
            {
                oneOf(result).getStatus();
                will(returnValue(true));

            }
        });
        assertNotNull(result);
        assertEquals(true, result.getStatus());
    }
}
/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class URISchemeRewriteFilterTest {

    /**
     * Test of filterURI method, of class URISchemeRewriteFilter.
     * @throws java.lang.Exception
     */
    @Test
    public void testFilterURI() throws Exception {
        URISchemeRewriteFilter instance = new URISchemeRewriteFilter("https?:\\/\\/mpi.nl.*", "target");
        assertFilterOutput(instance, "http://clarin.eu", "http://clarin.eu");
        assertFilterOutput(instance, "target://mpi.nl", "http://mpi.nl");
        assertFilterOutput(instance, "target://mpi.nl", "https://mpi.nl");
        assertFilterOutput(instance, "./somethingelse", "./somethingelse");
    }
    
    /**
     * 
     * Test of filterURI method, of class URISchemeRewriteFilter.
     * @throws java.lang.Exception
     */
    @Test
    public void testFilterURINoPattern() throws Exception {
        URISchemeRewriteFilter instance = new URISchemeRewriteFilter(null, null);
        assertFilterOutput(instance, "http://clarin.eu", "http://clarin.eu");
        assertFilterOutput(instance, "./somethingelse", "./somethingelse");
    }
    
    private static void assertFilterOutput(URISchemeRewriteFilter instance, String expected, String in) throws URISyntaxException {
        assertURIEquals(expected, instance.filterURI(URI.create(in)));
    }
    
    private static void assertURIEquals(String expected, URI actual) {
        assertEquals(URI.create(expected), actual);
    }
    
}

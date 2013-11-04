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
package nl.mpi.metadatabrowser.model.cmdi;

import java.net.URISyntaxException;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class DownloadActionRequestTest {

    private Mockery context = new JUnit4Mockery();

    /**
     * Test of getDownloadStream method, of class DownloadActionRequest.
     */
    @Test
    public void testGetDownloadStream() throws ResourceStreamNotFoundException, URISyntaxException {
	final IResourceStream resStream = context.mock(IResourceStream.class);
	DownloadActionRequest instance = new DownloadActionRequest("1.cmdi", resStream);
	assertSame(resStream, instance.getDownloadStream());
    }

    /**
     * Test of getFileName method, of class DownloadActionRequest.
     */
    @Test
    public void testGetFileName() {
	final IResourceStream resStream = context.mock(IResourceStream.class);
	DownloadActionRequest instance = new DownloadActionRequest("1.cmdi", resStream);
	assertEquals("1.cmdi", instance.getFileName());
    }
}

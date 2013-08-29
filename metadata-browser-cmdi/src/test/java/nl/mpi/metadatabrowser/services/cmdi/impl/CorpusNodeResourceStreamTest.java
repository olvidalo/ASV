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

import java.io.InputStream;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import static org.jmock.Expectations.returnValue;
import static org.junit.Assert.*;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CorpusNodeResourceStreamTest {

    private final Mockery context = new JUnit4Mockery() {
	{
	    // allows us to mock the abstract InputStream class
	    setImposteriser(ClassImposteriser.INSTANCE);
	}
    };
    private NodeResolver nodeResolver;
    private CorpusNode corpusNode;
    private InputStream inputStream;
    private CorpusNodeResourceStream instance;

    @Before
    public void setUp() {
	nodeResolver = context.mock(NodeResolver.class);
	corpusNode = context.mock(CorpusNode.class);
	inputStream = context.mock(InputStream.class);
	instance = new CorpusNodeResourceStream(nodeResolver, corpusNode);
    }

    /**
     * Test of getInputStream method, of class CorpusNodeResourceStream.
     */
    @Test
    public void testGetInputStream() throws Exception {
	context.checking(new Expectations() {
	    {
		oneOf(nodeResolver).getInputStream(corpusNode);
		will(returnValue(inputStream));
	    }
	});

	final InputStream result = instance.getInputStream();
	assertSame(inputStream, result);
    }

    /**
     * Test of close method, of class CorpusNodeResourceStream.
     */
    @Test
    public void testClose() throws Exception {

	context.checking(new Expectations() {
	    {
		allowing(nodeResolver).getInputStream(corpusNode);
		will(returnValue(inputStream));
		// input stream has been retrieved so should actually be closed in the end
		oneOf(inputStream).close();
	    }
	});
	instance.getInputStream();
	instance.close();
    }

    /**
     * Test of close method, of class CorpusNodeResourceStream.
     */
    @Test
    public void testFalseClose() throws Exception {

	context.checking(new Expectations() {
	    {
		// no input stream has been retrieved, so close should never be called
		never(inputStream).close();
	    }
	});
	instance.close();

    }
}
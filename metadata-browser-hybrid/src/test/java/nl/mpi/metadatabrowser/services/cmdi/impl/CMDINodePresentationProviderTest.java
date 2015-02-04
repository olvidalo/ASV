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
import java.io.IOException;
import java.net.URISyntaxException;
import nl.mpi.metadatabrowser.services.cmdi.mock.MockTypedCorpusNode;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;
import nl.mpi.archiving.corpusstructure.core.AccessLevel;
import nl.mpi.archiving.corpusstructure.core.FileInfo;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.AccessInfoProvider;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIMetadataType;
import nl.mpi.metadatabrowser.model.cmdi.type.CMDIResourceTxtType;
import nl.mpi.metadatabrowser.model.cmdi.type.IMDISessionType;
import nl.mpi.metadatabrowser.model.cmdi.wicket.components.ResourcePresentation;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.TemplatesStore;
import nl.mpi.metadatabrowser.wicket.MetadataBrowserServicesLocator;
import org.apache.wicket.Component;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.jmock.Expectations.returnValue;
import org.junit.After;
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
    private final TypedCorpusNode cmdiNode = new MockTypedCorpusNode(new CMDIMetadataType(), "node:1", "CMDI MockNode");
    private final TypedCorpusNode imdiNode = new MockTypedCorpusNode(new IMDISessionType(), "node:2", "IMDI MockNode");
    private final MockTypedCorpusNode resourceNode = new MockTypedCorpusNode(new CMDIResourceTxtType(), "node:2", "IMDI MockNode");
    private NodeResolver nodeResolver;
    private Templates imdiTemplates;
    private Templates cmdiTemplates;
    private Transformer transformer;
    private CorpusStructureProvider csProvider;
    private NodeTypeIdentifier nodeTypeIdentifier;
    private CMDINodePresentationProvider instance;

    @Before
    public void setUp() {
        nodeResolver = context.mock(NodeResolver.class);
        imdiTemplates = context.mock(Templates.class, "imdiTemplates");
        cmdiTemplates = context.mock(Templates.class, "cmdiTemplates");
        nodeTypeIdentifier = context.mock(NodeTypeIdentifier.class);
        csProvider = context.mock(CorpusStructureProvider.class);
        transformer = context.mock(Transformer.class);

        MetadataBrowserServicesLocator.Instance.set(new MetadataBrowserServicesLocator() {

            @Override
            public NodeResolver getNodeResolver() {
                return nodeResolver;
            }

            @Override
            public NodeTypeIdentifier getNodeTypeIdentifier() {
                return nodeTypeIdentifier;
            }

            @Override
            public CorpusStructureProvider getCorpusStructureProvider() {
                return csProvider;
            }

            @Override
            public TemplatesStore getTemplatesProvider() {
                final Map<String, Templates> templates = new HashMap<>();
                templates.put(TemplateKeys.IMDI, imdiTemplates);
                templates.put(TemplateKeys.CMDI, cmdiTemplates);
                return new TemplatesStore(templates);
            }

        });

        instance = new CMDINodePresentationProvider(nodeResolver);
    }

    @After
    public void cleanUp() {
        MetadataBrowserServicesLocator.Instance.set(null);
    }

    /**
     * Test of getNodePresentation method, of class
     * CMDINodePresentationProvider.
     */
    @Test
    public void testGetNodePresentationForCmdi() throws Exception {
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

        // Wicket tester is required to provide a wicket application context
        WicketTester tester = new WicketTester();
        // This call will trigger the transformation and wrap it in a component
        Component result = instance.getNodePresentation("test", Arrays.asList(cmdiNode));
        tester.startComponentInPage(result);

        assertNotNull(result);
        assertEquals(result.getId(), "test");
    }

    /**
     * Test of getNodePresentation method, of class
     * CMDINodePresentationProvider.
     */
    @Test
    public void testGetNodePresentationForImdi() throws Exception {
        context.checking(new Expectations() {
            {
                // transformer will be created from templates (IMDI in this case)
                oneOf(imdiTemplates).newTransformer();
                will(returnValue(transformer));

                never(cmdiTemplates).newTransformer();

                // transformer will be configured...
                allowing(transformer).setOutputProperty(with(any(String.class)), with(any(String.class)));
                allowing(transformer).setParameter(with(any(String.class)), with(any(String.class)));

                // transformable content will be read
                oneOf(nodeResolver).getInputStream(imdiNode);
                will(returnValue(getClass().getResourceAsStream("/IPROSLA_Nijmegen.cmdi")));

                // transformer will be requested to transform content
                oneOf(transformer).transform(with(any(StreamSource.class)), with(any(Result.class)));
            }
        });

        // Wicket tester is required to provide a wicket application context
        WicketTester tester = new WicketTester();
        // This call will trigger the transformation and wrap it in a component
        Component result = instance.getNodePresentation("test", Arrays.asList(imdiNode));
        tester.startComponentInPage(result);

        assertNotNull(result);
        assertEquals(result.getId(), "test");
    }

    /**
     * Test of getNodePresentation method, of class
     * CMDINodePresentationProvider.
     */
    @Test
    public void testGetNodePresentationForMultiple() throws Exception {
        context.checking(new Expectations() {
            {
                // transformer will be created from templates (IMDI in this case)
                never(imdiTemplates).newTransformer();
                never(cmdiTemplates).newTransformer();
            }
        });

        // Wicket tester is required to provide a wicket application context
        WicketTester tester = new WicketTester();
        // This call will create a component but perform no transformation
        Component result = instance.getNodePresentation("test", Arrays.asList(imdiNode, cmdiNode));
        tester.startComponentInPage(result);

        assertNotNull(result);
        assertEquals(result.getId(), "test");
    }

    /**
     * Test of getNodePresentation method, of class
     * CMDINodePresentationProvider.
     */
    @Ignore("fix resourcepresentation implementation")
    @Test
    public void testGetNodePresentationForResource() throws Exception {
        final AccessInfoProvider accessInfoProvider = context.mock(AccessInfoProvider.class);
        final FileInfo fileInfo = context.mock(FileInfo.class);
        resourceNode.setFileInfo(fileInfo);

        context.checking(new Expectations() {
            {
                // transformer will be created from templates (IMDI in this case)
                never(imdiTemplates).newTransformer();
                never(cmdiTemplates).newTransformer();

                allowing(nodeResolver).getUrl(resourceNode);
                will(returnValue(new URL("http://myresource")));

                allowing(accessInfoProvider).hasReadAccess(with(equalTo(resourceNode.getNodeURI())), with(any(String.class)));
                will(returnValue(true));
                allowing(accessInfoProvider).getAccessLevel(with(equalTo(resourceNode.getNodeURI())));
                will(returnValue(AccessLevel.ACCESS_LEVEL_OPEN_EVERYBODY));

                allowing(fileInfo).getChecksum();
                will(returnValue(""));
                allowing(fileInfo).getSize();
                will(returnValue(123l));
                allowing(fileInfo).getFileTime();
                will(returnValue(new Date()));
            }
        });

        // Wicket tester is required to provide a wicket application context
        WicketTester tester = new WicketTester();
        // This call will trigger the transformation and wrap it in a component
        Component result = instance.getNodePresentation("test", Arrays.<TypedCorpusNode>asList(resourceNode));
        tester.startComponentInPage(result);

        assertThat(result, instanceOf(ResourcePresentation.class));
        assertEquals(result.getId(), "test");
    }
    
    @Test
    public void testStylesheetsCmdi() throws Exception {
        createTemplates("/xslt/cmdi2xhtml.xsl");
    }

    @Test
    public void testStylesheetsImdi() throws Exception {
        createTemplates("/xslt/imdi-viewer.xsl");
    }

    private void createTemplates(final String xsltResource) throws IOException, TransformerFactoryConfigurationError, TransformerConfigurationException, URISyntaxException {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final StreamSource streamSource = new StreamSource(new File(getClass().getResource(xsltResource).toURI()));
        transformerFactory.newTemplates(streamSource);
    }
}

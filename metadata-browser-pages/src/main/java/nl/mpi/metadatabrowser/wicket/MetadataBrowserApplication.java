package nl.mpi.metadatabrowser.wicket;

import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.TemplatesStore;
import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Application object the Metadata Browser
 *
 * @see nl.mpi.metadatabrowser.wicket.Start#main(String[])
 */
public class MetadataBrowserApplication extends WebApplication implements MetadataBrowserServicesLocator {

    @Autowired
    private NodeResolver nodeResolver;

    @Autowired
    private CorpusStructureProvider corpusStructureProvider;

    @Autowired
    private NodeTypeIdentifier nodeTypeIdentifier;
    
    @Autowired
    private TemplatesStore templatesProvider;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        // This is required for the SpringBean annotations to work
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        // Helps model classes find the central services without having to store
        // a local reference
        MetadataBrowserServicesLocator.Instance.set(this);
    }

    public static MetadataBrowserApplication get() {
        return (MetadataBrowserApplication) Application.get();
    }

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
        return corpusStructureProvider;
    }

    @Override
    public TemplatesStore getTemplatesProvider() {
        return templatesProvider;
    }
}

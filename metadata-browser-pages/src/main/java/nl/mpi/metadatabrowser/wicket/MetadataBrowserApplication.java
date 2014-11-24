package nl.mpi.metadatabrowser.wicket;

import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import nl.mpi.metadatabrowser.services.TemplatesStore;
import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Application object the Metadata Browser
 *
 * @see nl.mpi.metadatabrowser.wicket.Start#main(String[])
 */
public class MetadataBrowserApplication extends WebApplication implements MetadataBrowserServicesLocator {

    private final static Logger logger = LoggerFactory.getLogger(MetadataBrowserApplication.class);

    @Autowired
    private NodeResolver nodeResolver;

    @Autowired
    private CorpusStructureProvider corpusStructureProvider;

    @Autowired
    private NodeTypeIdentifier nodeTypeIdentifier;

    @Autowired
    private TemplatesStore templatesProvider;

    @Autowired
    private Settings settings;

    /**
     * @return @see org.apache.wicket.Application#getHomePage()
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

        setCacheOptions();
    }

    private void setCacheOptions() throws StringValueConversionException {
        final String maxSizePerSession = settings.getMaxSizePerSession();
        final int inmemoryCacheSize = settings.getInmemoryCacheSize();
        
        if (!Strings.isEmpty(maxSizePerSession)) {
            final Bytes maxSizePerSessionBytes = Bytes.valueOf(maxSizePerSession);
            logger.info("Setting Max Size Per Session to {}", maxSizePerSessionBytes);
            getStoreSettings().setMaxSizePerSession(maxSizePerSessionBytes);
        }

        if (inmemoryCacheSize >= 0) {
            logger.info("Setting In Memory Cache Size to {} pages", inmemoryCacheSize);
            getStoreSettings().setInmemoryCacheSize(inmemoryCacheSize);
        }
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

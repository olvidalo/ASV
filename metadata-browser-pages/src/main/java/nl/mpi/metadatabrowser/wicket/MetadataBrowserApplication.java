package nl.mpi.metadatabrowser.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Application object the Metadata Browser
 *
 * @see nl.mpi.metadatabrowser.wicket.Start#main(String[])
 */
public class MetadataBrowserApplication extends WebApplication {

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
    }
}

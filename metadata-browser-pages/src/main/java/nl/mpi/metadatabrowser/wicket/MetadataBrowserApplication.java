package nl.mpi.metadatabrowser.wicket;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class in the test
 * packages.
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

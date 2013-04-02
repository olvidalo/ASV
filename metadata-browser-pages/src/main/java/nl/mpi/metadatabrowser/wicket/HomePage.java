package nl.mpi.metadatabrowser.wicket;

import nl.mpi.metadatabrowser.services.NodeActionsProvider;
import nl.mpi.metadatabrowser.services.NodePresentationProvider;
import nl.mpi.metadatabrowser.services.NodeTypeIdentifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private NodeTypeIdentifier nodeTypeIdentifier;
    @SpringBean
    private NodeActionsProvider nodeActionsProvider;
    @SpringBean
    private NodePresentationProvider nodePresentationProvider;

    public HomePage(final PageParameters parameters) {
	add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
	// TODO Add your page's components here
    }
}

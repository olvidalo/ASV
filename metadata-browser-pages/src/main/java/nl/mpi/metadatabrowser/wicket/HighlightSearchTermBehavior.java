package nl.mpi.metadatabrowser.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.string.Strings;

/**
 * A behavior that adds JavaScript, executed on DOM ready, to highlight
 * occurrences of the current searchword (retrieved from the query parameter by
 * the client) in the page
 *
 * @author twagoo
 */
public class HighlightSearchTermBehavior extends Behavior {

    private final static JavaScriptResourceReference HIGHLIGHT_JS = new JavaScriptResourceReference(HomePage.class, "res/jquery.highlight.js");
    private final HighlightSearchTermScriptFactory scriptFactory = new HighlightSearchTermScriptFactory();

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        // include highlight script
        response.render(JavaScriptHeaderItem.forReference(HIGHLIGHT_JS));

        final String words = getWordList(component);
        if (!Strings.isEmpty(words)) {
            String selector = getComponentSelector(component.getMarkupId());
            // after load, highlight 
            response.render(OnDomReadyHeaderItem.forScript(scriptFactory.createScript(selector, words)
                    + ";"
                    + "if (typeof expand_highlighted_imdi === 'function') { "
                    + " expand_highlighted_imdi();"
                    + "}"
                    + "if (typeof expand_highlighted_cmdi === 'function') { "
                    + " expand_highlighted_cmdi();"
                    + "}"            
            ));
        }
    }

    protected String getComponentSelector(String componentId) {
        return "#" + componentId;
    }

    protected String getWordList(Component component) {
        Request request = component.getPage().getRequestCycle().getRequest();
        return request.getQueryParameters().getParameterValue(getQueryParam()).toString();
    }

    protected String getQueryParam() {
        return "q";
    }

}

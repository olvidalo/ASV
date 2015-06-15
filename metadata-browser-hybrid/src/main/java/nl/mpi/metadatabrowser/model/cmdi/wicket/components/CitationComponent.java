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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.net.URI;
import java.net.URL;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.NodeIdFilter;
import nl.mpi.metadatabrowser.wicket.Settings;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class CitationComponent extends Panel {

    @SpringBean
    private NodeIdFilter nodeIdFilter;
    @SpringBean
    private CorpusStructureProvider csdb;
    @SpringBean
    private NodeResolver nodeResolver;
    @SpringBean
    private Settings appSettings;

    public CitationComponent(String id, IModel<TypedCorpusNode> nodeModel) {
        super(id);
        final TypedCorpusNode node = nodeModel.getObject();

        final URI nodeId = node.getNodeURI();

        final String nodeName = node.getName();
        add(new Label("name", nodeName));

        final URI parent = csdb.getCanonicalParent(nodeId);
        final String title;
        if (parent == null) {
            title = String.format("Resource \"%s\" is root node", node.getName());
        } else {
            title = String.format("Resource \"%s\" from \"%s\"", node.getName(), csdb.getNode(parent).getName());
        }

        final URL url = nodeResolver.getUrl(node);
        
        final URI handle;
        if (appSettings.isHandleDisplayAllowed()) {
            handle = nodeResolver.getPID(node); // can be null
        } else {
            handle = null;
        }
        
        if (handle != null) {
            final String wrapHandle = handle.getSchemeSpecificPart();
            final String resolver = csdb.getHandleResolverURI().toString();
            final String viewHandle = resolver.concat(wrapHandle) + "@view";
            add(new ExternalLink("handleLink", viewHandle, viewHandle));
        } else {
            add(new ExternalLink("handleLink", url.toString(), url.toString()));
        }

        add(createDetails(nodeId, title, url));
    }

    private Component createDetails(final URI nodeId, String title, final URL url) {

        final Form formDetails = new Form("nodeInfoDetails") {
            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
            }
        };
        // details
        formDetails.add(new Label("nodeId", nodeIdFilter.getURIParam(nodeId)));
        formDetails.add(new Label("title", title));
        formDetails.add(new ExternalLink("nodeLink", url.toString(), url.toString()));
        formDetails.setVisible(false);

        //TODO (?) enable csdb.getArchiveRoots().getArchiveName();
//        String archive_name = "archive";
//        if (archive_name == null) {
//            archive_name = "unknown";
//        }
        // Put details/submit form in container for refresh through AJAX
        final MarkupContainer formDetailsContainer = new WebMarkupContainer("formDetailsContainer");
        formDetailsContainer.setOutputMarkupId(true);
        final Link showDetails = new AjaxFallbackLink("showDetails") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                formDetails.setVisible(true);
                if (target != null) {
                    target.add(formDetailsContainer);
                }
            }

            @Override
            protected void onConfigure() {
                setVisible(!formDetails.isVisible());
            }

        };
        formDetailsContainer.add(showDetails);
        formDetailsContainer.add(formDetails);
        return formDetailsContainer;
    }
}

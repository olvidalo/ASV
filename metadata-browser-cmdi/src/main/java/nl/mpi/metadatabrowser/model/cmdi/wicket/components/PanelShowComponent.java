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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.FilterNodeIds;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class PanelShowComponent extends Panel {

    private final static Logger logger = LoggerFactory.getLogger(PanelShowComponent.class);
    @SpringBean
    private FilterNodeIds filterNodeId;

    public PanelShowComponent(String id, TypedCorpusNode node, CorpusStructureProvider csdb, NodeResolver nodeResolver) throws UnsupportedEncodingException {
        super(id);
        String title;
        final Form form = new Form("nodeInfoForm");
        final Form formDetails = new Form("nodeInfoDetails") {
            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
            }
        };
        String nodeName = node.getName();
        URI nodeId = node.getNodeURI();
        URI parent = csdb.getCanonicalParent(nodeId);
        if (parent == null) {
            title = String.format("Resource \"%s\" is root node", node.getName());
        } else {
            title = String.format("Resource \"%s\" from \"%s\"", node.getName(), csdb.getNode(parent).getName());
        }
        Date objectFileTime = csdb.getNode(nodeId).getFileInfo().getFileTime();
        String lastModified = "";
        if (objectFileTime != null) {
            lastModified = new Date(objectFileTime.getTime()).toString();
        }

        String resolver = csdb.getHandleResolverURI().toString();
        String archive_name = "archive"; //TODO enable csdb.getArchiveRoots().getArchiveName();
        if (archive_name == null) {
            archive_name = "unknown";
        }

        String handle = node.getPID().toString();
        String wrapHandle = handle;
        if (handle.contains(":")) {
            wrapHandle = handle.split(":")[1];
        }
        final String resolvedHandle = resolver.concat(wrapHandle);
        final String archiveName = archive_name;
        URL url = nodeResolver.getUrl(node);

        form.add(new Label("name", nodeName));
        ExternalLink handleLink = new ExternalLink("handleLink", resolvedHandle.concat("@view"), resolvedHandle.concat("@view"));
        form.add(handleLink);


        //embeded citation down the page
        ExternalLink openpath = new ExternalLink("openpath", "?openpath=" + node.getNodeURI(), nodeName);
        formDetails.add(openpath);
        formDetails.add(new Label("nodeId", filterNodeId.getURIParam(nodeId)));
        formDetails.add(new Label("title", title));

        formDetails.add(new Label("cite_title", title));
        formDetails.add(new Label("author", "unknwon"));
        formDetails.add(new Label("archive_name", archiveName));
        formDetails.add(new Label("format", node.getFormat()));
        formDetails.add(new Label("last_modified", lastModified));
        formDetails.add(new Label("cite_handle", wrapHandle));
        formDetails.add(new Label("resolvedHandle", resolvedHandle));
        formDetails.add(new ExternalLink("nodeLink", url.toString(), url.toString()));
        formDetails.setVisible(false);

        Link showDetails = new Link("showDetails") {
            @Override
            public void onClick() {
                formDetails.setVisible(true);
            }
        };

        // Put details/submit form in container for refresh through AJAX
        final MarkupContainer formInfoContainer = new WebMarkupContainer("formInfoContainer");
        formInfoContainer.add(form);
        // Add container to page
        add(formInfoContainer);

        final MarkupContainer formDetailsContainer = new WebMarkupContainer("formDetailsContainer");
        formDetailsContainer.add(showDetails);
        formDetailsContainer.add(formDetails);
        add(formDetailsContainer);
    }
}

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
package nl.mpi.metadatabrowser.model.cmdi;

import java.net.MalformedURLException;
import java.net.URI;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public final class PanelShowComponent extends Panel {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);

    public PanelShowComponent(String id, TypedCorpusNode node, String archiveName, String resolver) {
        super(id);

        //if (form != null) {
        final Form form = new Form("nodeInfoForm");

        String nodeName = node.getName();
        String nodeId = Integer.toString(node.getNodeId());
        URI nodeUri = node.getUri();
        String url = null;
        try {
            url = nodeUri.toURL().toString();
        } catch (MalformedURLException ex) {
            logger.error("url error while getting URL", ex);
        }
        // HANDLE bookmark action here
        form.add(new Label("nodeId", nodeId));
        form.add(new Label("name", nodeName));
        form.add(new Label("uri", nodeUri.toString()));
        form.add(new Label("url", url));
        form.add(new Label("last_modified", "lastmodified"));
        form.add(new Label("title", "title"));
        form.add(new Label("format", "format"));
        form.add(new Label("archive_name", archiveName));
        form.add(new Label("resolver", resolver));

        // Put details/submit form in container for refresh through AJAX
        final MarkupContainer formContainer = new WebMarkupContainer("formContainer");
        formContainer.add(form);
        // Add container to page
        add(formContainer);
        //(Form) createNodeInfoForm("nodeInfoForm");
        // Put details/submit form in container for refresh through AJAX
//        final MarkupContainer formContainer = new WebMarkupContainer("formContainer");
//        formContainer.add(nodeInfoForm);
//        // Add container to page
//        formContainer.setMarkupId("formContainer");
//        add(formContainer);


//        tableContainer = new WebMarkupContainer("bookmark");
//        tableContainer.setOutputMarkupId(true);
//        tableContainer.setMarkupId("bookmark");
//        add(tableContainer);
        // }




    }

//    /**
//     * Creates and adds node id form
//     *
//     * @param id
//     * @return Form
//     */
//    private Form createNodeInfoForm(final String id, TypedCorpusNode node, String archive_name, String resolver) {
//        final Form form = new Form("nodeIdForm");
//
//        String nodeName = node.getName();
//        String nodeId = Integer.toString(node.getNodeId());
//        URI nodeUri = node.getUri();
//        String url = null;
//        try {
//            url = nodeUri.toURL().toString();
//        } catch (MalformedURLException ex) {
//            logger.error("url error while getting URL", ex);
//        }
//        // HANDLE bookmark action here
//        form.add(new Label("nodeId", nodeId));
//        form.add(new Label("name", nodeName));
//        form.add(new Label("uri", nodeUri.toString()));
//        form.add(new Label("url", url));
//        form.add(new Label("last_modified", "lastmodified"));
//        form.add(new Label("title", "title"));
//        form.add(new Label("format", "format"));
//        form.add(new Label("archive_name", archive_name));
//        form.add(new Label("resolver", resolver));
//
//        // Put details/submit form in container for refresh through AJAX
//        final MarkupContainer formContainer = new WebMarkupContainer("formContainer");
//        formContainer.add(form);
//        // Add container to page
//        add(formContainer);
//
//        return form;
//    }
}

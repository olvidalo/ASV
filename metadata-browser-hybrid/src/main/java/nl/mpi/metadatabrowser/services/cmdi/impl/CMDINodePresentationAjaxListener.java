/*
 * Copyright (C) 2015 Max Planck Institute for Psycholinguistics
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

import nl.mpi.metadatabrowser.wicket.NodePresentationAjaxListener;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Listener that gets called when the node presentation gets updated via Ajax
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CMDINodePresentationAjaxListener implements NodePresentationAjaxListener {

    @Override
    public void onNodeSelectionChanged(AjaxRequestTarget target) {
        // IMDI viewer page (stylesheet result) includes the definition of
        // an init method. If it exists, call it.
        target.appendJavaScript(""
                + "if (typeof init_viewer === 'function') { "
                + " init_viewer('imdi-viewer-open.png', 'imdi-viewer-closed.png'); "
                + "} "
                + "doLayout(); "
        );
    }

}

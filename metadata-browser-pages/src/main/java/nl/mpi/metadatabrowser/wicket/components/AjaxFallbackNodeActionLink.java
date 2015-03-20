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
package nl.mpi.metadatabrowser.wicket.components;

import java.util.Collection;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.wicket.services.ControllerActionRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * AJAX fallback button for offering and handling {@link NodeAction}s on a
 * collection of nodes
 *
 * @see NodeActionLink
 * @see NodeActionHandler
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
class AjaxFallbackNodeActionLink extends AjaxFallbackLink {

    @SpringBean
    private ControllerActionRequestHandler actionRequestHandler;
    private final NodeActionHandler nodeActionHandler;

    public AjaxFallbackNodeActionLink(String id, Collection<TypedCorpusNode> nodes, NodeAction action) {
        super(id, Model.of(action.getName()));
        nodeActionHandler = new NodeActionHandler(action, nodes);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        nodeActionHandler.handle(actionRequestHandler, this, target);
    }

}

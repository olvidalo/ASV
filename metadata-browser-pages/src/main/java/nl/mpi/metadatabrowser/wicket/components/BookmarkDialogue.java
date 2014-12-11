/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.metadatabrowser.wicket.NodeViewLinkModel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @param <SerializableCorpusNode>
 */
public class BookmarkDialogue<SerializableCorpusNode extends CorpusNode & Serializable> extends GenericPanel<Collection<SerializableCorpusNode>> implements Serializable {

    public BookmarkDialogue(String id, final IModel<Collection<SerializableCorpusNode>> model) {
        super(id, model);
        final IModel<String> linkModel = new NodeViewLinkModel(model);
        final AbstractReadOnlyModel<String> titleModel = new AbstractReadOnlyModel<String>() {
            
            @Override
            public String getObject() {
                final Iterator<SerializableCorpusNode> iterator = model.getObject().iterator();
                if (iterator.hasNext()) {
                    final SerializableCorpusNode node = iterator.next();
                    return node.getName();
                } else {
                    return "";
                }
            }
        };
        
        final ExternalLink iconLink = new ExternalLink("icon", linkModel);
        iconLink.add(new Label("title", titleModel));
        add(iconLink);
        
        final ExternalLink link = new ExternalLink("link", linkModel, linkModel);
        link.add(new AttributeModifier("title", titleModel));
        add(link);
    }

}

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
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import java.util.List;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MultiNodePanel extends GenericPanel<List<TypedCorpusNode>> {

    public MultiNodePanel(String id, IModel<List<TypedCorpusNode>> model) {
        super(id, model);
        add(new ListView<TypedCorpusNode>("node", model) {

            @Override
            protected void populateItem(ListItem<TypedCorpusNode> item) {
                item.add(new Label("name", item.getModelObject().getName()));
            }
        });
    }

}

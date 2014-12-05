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
package nl.mpi.metadatabrowser.wicket;

import java.util.Collection;
import java.util.Collections;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Read only wrapper model that provides a singleton collection of the object
 * provided by the inner model
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 * @param <T>
 */
public class ReadOnlySingletonCollectionModel<T> extends AbstractReadOnlyModel<Collection<T>> {

    private final IModel<T> inner;

    public ReadOnlySingletonCollectionModel(IModel<T> inner) {
        this.inner = inner;
    }

    @Override
    public Collection<T> getObject() {
        return Collections.singleton(inner.getObject());
    }

    @Override
    public void detach() {
        inner.detach();
    }

}

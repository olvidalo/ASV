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
package nl.mpi.metadatabrowser.wicket;

import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.string.Strings;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class UserModel extends AbstractReadOnlyModel<String> {

    private final AuthenticationHolder auth;

    public UserModel(AuthenticationHolder auth) {
        this.auth = auth;
    }

    @Override
    public String getObject() {
        final String user = auth.getPrincipalName();
        if (Strings.isEmpty(user)) {
            return AuthenticationHolder.ANONYMOUS_PRINCIPAL;
        } else {
            return user;
        }
    }

}

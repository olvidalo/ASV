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
package nl.mpi.metadatabrowser.services.authentication;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import org.apache.wicket.request.cycle.RequestCycle;
import org.springframework.stereotype.Component;

/**
 * Class that holds the user id of the current logged in user. value is
 * Anonymous if null
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
@Component
public class AuthenticationHolderImpl implements AuthenticationHolder, Serializable {
//private static String userid;

    @Override
    public String getPrincipalName() {
        HttpServletRequest request = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
        String userid = request.getRemoteUser();
        if (userid == null || userid.equals("")) {
            userid = "anonymous";
        }
        return userid;
    }

    @Override
    public void setPrincipalName(String user) {
        //      AuthenticationHolderImpl.userid = user;
    }
}

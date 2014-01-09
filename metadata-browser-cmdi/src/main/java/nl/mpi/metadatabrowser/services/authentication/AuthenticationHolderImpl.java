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
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class AuthenticationHolderImpl implements AuthenticationHolder {

    @Override
    public String getPrincipalName() {        
      String userid = SecurityContextHolder.getContext().getAuthentication().getName();
      String user[] = userid.split(";");
        for (int i = 0; i<user.length; i++) {
            if (user[i].contains("Username")) {
                int index = user[i].indexOf("Username");
                userid = user[i].substring(index + 9);
                break;
            }      
    }
        return userid;
    }
}

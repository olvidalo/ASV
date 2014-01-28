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

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import nl.mpi.metadatabrowser.services.AuthenticationHolder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */

public class AuthenticationHolderImpl implements AuthenticationHolder {
private static String userid;


    @Override
    public String getPrincipalName() {

        return userid;        // String userid = SecurityContextHolder.getContext().getAuthentication().getName();
        //String userid =securityContext.getUserPrincipal().getName();
//      String user[] = userid.split(";");
//        for (int i = 0; i<user.length; i++) {
//            if (user[i].contains("Username")) {
//                int index = user[i].indexOf("Username");
//                userid = user[i].substring(index + 9);
//                break;
//            }
//    }
//        HttpServletRequest request = ((ServletRequestAttributes)
//             RequestContextHolder.getRequestAttributes()).getRequest();
//        return request.getRemoteUser();
    }

    public void setPrincipal(String user){
        this.userid = user;
    }
}

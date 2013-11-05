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

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import nl.mpi.metadatabrowser.model.NavigationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class NavigationActionRequest implements NavigationRequest, Serializable {

    private NavigationTarget target;
//    private Map<String, URI> parameters;
    private String targetUrl;

    public NavigationActionRequest(NavigationTarget navigationTarget, String url) {
//        this.parameters = parameters;
        this.target = navigationTarget;
        this.targetUrl = url;
    }

    public void setTarget(NavigationTarget target) {
        this.target = target;
    }

    @Override
    public NavigationTarget getTarget() {
        return target;
    }

//    public void setParameters(Map<String, URI> parameters) {
//        this.parameters = parameters;
//    }
//
//    @Override
//    public Map<String, URI> getParameters() {
//        return parameters;
//    }
    
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public String getTargetURL() {
        return targetUrl;
    }
}

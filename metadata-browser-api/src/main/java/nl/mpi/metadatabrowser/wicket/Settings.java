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

import org.apache.wicket.util.lang.Bytes;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class Settings {

    @Value("${nl.mpi.metadatabrowser.wicket.maxSizePerSession:}")
    private String maxSizePerSession;

    @Value("${nl.mpi.metadatabrowser.wicket.inmemoryCacheSize:-1}")
    private int inmemoryCacheSize;

    @Value("${nl.mpi.metadatabrowser.allowHandleDisplay:true}")
    private boolean handleDisplayAllowed;

    /**
     *
     * @return the maximum number of page instances that will be saved into
     * application-scoped cache (default = 0 so disabled); if less that 0,
     * should be ignored
     *
     * @see http://wicket.apache.org/guide/guide/versioningCaching.html
     */
    public int getInmemoryCacheSize() {
        return inmemoryCacheSize;
    }

    /**
     *
     * Consist of a floating point value followed by K, M, G or T for kilobytes,
     * megabytes, gigabytes or terabytes, respectively.
     *
     * @return Representation of the size for page store file (cache on disk per
     * session); if null or empty, should be ignored
     * @see http://wicket.apache.org/guide/guide/versioningCaching.html
     * @see Bytes#valueOf(java.lang.String)
     */
    public String getMaxSizePerSession() {
        return maxSizePerSession;
    }

    /**
     * Determines whether the application should display handles for nodes in
     * bookmarks etc regardless of whether they are available (defaults to true)
     *
     * @return Whether the application should display handles
     */
    public boolean isHandleDisplayAllowed() {
        return handleDisplayAllowed;
    }

}

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
import nl.mpi.metadatabrowser.model.DownloadRequest;
import org.apache.wicket.util.resource.IResourceStream;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class DownloadActionRequest implements DownloadRequest, Serializable {

    private final String fileName;
    private final IResourceStream streamContent;

    public DownloadActionRequest(String fileName, IResourceStream resStream) {
	this.fileName = fileName;
	this.streamContent = resStream;
    }

    /**
     *
     * @return the stream that should be forwarded to the user (via the browser)
     */
    @Override
    public IResourceStream getDownloadStream() {
	return streamContent;
    }

    /**
     *
     * @return the file name that should be offered to the user
     */
    @Override
    public String getFileName() {
	return fileName;
    }
}

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
    
    private static String fileName;
    private static IResourceStream streamContent;

    public static void setFileName(String name) {
        DownloadActionRequest.fileName = name;
    }

    public DownloadActionRequest(String fileName, IResourceStream resStream) {
        this.fileName = fileName;
        this.streamContent = resStream;
    }

    public DownloadActionRequest() {
    }
    


    public static void setStreamContent(IResourceStream streamContent) {
	DownloadActionRequest.streamContent = streamContent;
    }
    
    @Override
    public IResourceStream getDownloadStream() {
        return streamContent;
    }

    
    @Override
    public String getFileName() {
        return fileName;
    }
    
}

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
package nl.mpi.metadatabrowser.model.mock;

import java.io.Serializable;
import nl.mpi.metadatabrowser.model.DownloadRequest;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class MockDownloadRequest implements DownloadRequest, Serializable {

    private String fileName;
    private String streamContent;

    public void setStreamContent(String streamContent) {
	this.streamContent = streamContent;
    }

    @Override
    public IResourceStream getDownloadStream() {
	return new StringResourceStream(streamContent);
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    @Override
    public String getFileName() {
	return fileName;
    }
}

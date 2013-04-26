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
package nl.mpi.metadatabrowser.services.impl.cmdi;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import nl.mpi.archiving.tree.CorpusNode;
import nl.mpi.corpusstructure.UnknownNodeException;
import nl.mpi.metadatabrowser.model.cmdi.CmdiCorpusStructureDB;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class ZipServiceImpl implements ZipService {
    private final CmdiCorpusStructureDB csdb;

    public ZipServiceImpl(CmdiCorpusStructureDB csdb) {
        this.csdb = csdb;
    }

    @Override
    public File createZipFileForNodes(List<? extends CorpusNode> childrenNodes) throws IOException, UnknownNodeException, FileNotFoundException {
        //create object of FileOutputStream
        File tmp = File.createTempFile("mdtbrowser", ".zip");
        FileOutputStream fout = new FileOutputStream(tmp);
        //create object of ZipOutputStream from FileOutputStream
        ZipOutputStream zout = new ZipOutputStream(fout);
        byte[] buffer = new byte[1024];
        // HANDLE multiple download action here
        if (childrenNodes.size() > 0) {
            for (CorpusNode childNode : childrenNodes) {
                URI childUri = csdb.getObjectURI(childNode.getNodeId());

                FileInputStream is;
                String fileName = childUri.toString().substring(childUri.toString().lastIndexOf('/') + 1, childUri.toString().length());
                //String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));


                File file = new File(childUri.getPath());
                is = new FileInputStream(file);
                ZipEntry ze = new ZipEntry(fileName);
                zout.putNextEntry(ze);

                int length;
                while ((length = is.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
                is.close();
            }
        }
        zout.close();
        fout.close();
        return tmp;
    }
}

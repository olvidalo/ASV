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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import nl.mpi.archiving.corpusstructure.core.CorpusNode;
import nl.mpi.archiving.corpusstructure.core.NodeNotFoundException;
import nl.mpi.archiving.corpusstructure.core.service.NodeResolver;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProvider;
import nl.mpi.metadatabrowser.model.TypedCorpusNode;
import nl.mpi.metadatabrowser.services.authentication.AccessChecker;
import nl.mpi.metadatabrowser.services.cmdi.ZipService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * All logic to download multiples files happens here. Parent node is passed on
 * and recursively get all children and add them to the zipFile.
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ZipServiceImpl implements ZipService, Serializable {
    
    private final static Logger logger = LoggerFactory.getLogger(ZipServiceImpl.class);
    private final static long MAX_LIMIT = FileUtils.ONE_GB * 2;
    private final CorpusStructureProvider csdb;
    private final NodeResolver nodeResolver;
    private final AccessChecker accessChecker;

    /**
     * Constructor
     *
     * @param csdb
     * @param nodeResolver
     * @param accessInfoProvider
     */
    @Autowired
    public ZipServiceImpl(CorpusStructureProvider csdb, NodeResolver nodeResolver, AccessChecker accessChecker) {
        this.csdb = csdb;
        this.nodeResolver = nodeResolver;
        this.accessChecker = accessChecker;
    }

    /**
     * create zip and logic to add file to the zip
     *
     * @param node parent node
     * @param userid user that request the download
     * @return return file which is basically the zip file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Override
    @Transactional
    public File createZipFileForNodes(TypedCorpusNode node, String userid) throws IOException, FileNotFoundException {
        final File zipFile = File.createTempFile("mdbrowser", ".zip");
        final FileOutputStream fileStream = new FileOutputStream(zipFile);
        
        final int filesCount;
        try (ZipOutputStream zipStream = new ZipOutputStream(fileStream)) {
            // don't compress
            zipStream.setLevel(Deflater.NO_COMPRESSION);
            // add PID to zip as comment
            zipStream.setComment(getZipComment(node));

            // add this node and its children
            filesCount = addFile(node, userid, zipStream);
        } catch (NodeNotFoundException ex) {
            logger.warn("Node not found while creating zip archive", ex);
            removeOnAbort(zipFile, node);
            return null;
        } catch (IOException ex) {
            removeOnAbort(zipFile, node);
            throw ex;
        }
        
        logger.debug("Added {} files to archive of node {}: {}", filesCount, node.getNodeURI(), zipFile.getAbsolutePath());

        // at least one child must be present, otherwise fail
        if (filesCount > 1) {
            return zipFile;
        } else {
            logger.info("Children of node could not be added to ZIP (either non-existent or not accessible by user {})", userid);
            logger.debug("Deleting abandoned ZIP file {}", zipFile.getAbsolutePath());
            if (!zipFile.delete()) {
                logger.warn("Could not remove abandoned ZIP file: {}", zipFile.getAbsolutePath());
            }
            return null;
        }
    }

    public void removeOnAbort(final File zipFile, TypedCorpusNode node) {
        if (zipFile.exists()) {
            logger.info("Zip creation aborted, removing incomplete archive {} for node {}", zipFile, node.getNodeURI());
            zipFile.delete();
        }
    }
    
    private int addFile(CorpusNode node, String userid, ZipOutputStream zipStream) throws NodeNotFoundException, IOException {
        // wrap a new long so that it can be updated while writing
        final ThreadLocal<Long> sizeCount = new ThreadLocal<>();
        sizeCount.set(0L);
        
        return addFile(node, userid, zipStream, sizeCount);
    }
    
    private int addFile(CorpusNode node, String userid, ZipOutputStream zipStream, ThreadLocal<Long> sizeCount) throws NodeNotFoundException, IOException {
        final URI nodeURI = node.getNodeURI();
        
        if (accessChecker.hasAccess(userid, nodeURI)) {
            logger.debug("Adding {} to ZIP", nodeURI);

            // start counting written items (only root initially)
            int count = 1;

            // copy contents of current node to zip stream
            final long written = writeToZipStream(node, zipStream);
            updateWriteSizeCount(sizeCount, written);

            // add children, add recursively
            for (CorpusNode child : csdb.getChildNodes(nodeURI)) {
                count += addFile(child, userid, zipStream, sizeCount);
            }
            return count;
        } else {
            logger.trace("Skipped adding {} to ZIP, user has no access", nodeURI);
            return 0;
        }
    }

    /**
     *
     * @param node node to write
     * @param zipStream stream to write to
     * @return number of bytes written
     * @throws IOException
     */
    private long writeToZipStream(CorpusNode node, ZipOutputStream zipStream) throws IOException {
        final File localFile = nodeResolver.getLocalFile(node);
        
        final String fileName;
        if (localFile == null) {
            final URL nodeUrl = nodeResolver.getUrl(node);
            fileName = new File(nodeUrl.getPath()).getName();
        } else {
            fileName = localFile.getName();
        }
        
        logger.trace("Writing {} to ZIP as {}", node.getNodeURI(), fileName);

        // prepare target stream for this entry
        final ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setComment(node.getName());
        zipStream.putNextEntry(zipEntry);

        // write node contents
        final long written;
        try (InputStream inputStream = nodeResolver.getInputStream(node)) {
            written = ByteStreams.copy(inputStream, zipStream);
        }

        // end of writing this entry
        zipStream.closeEntry();
        
        logger.trace("Done writing {} to ZIP", fileName);
        
        return written;
    }

    /**
     *
     * @param node node to get a comment text for
     * @return the PID, or as a fallback the URL, of the given node as a string
     */
    private String getZipComment(TypedCorpusNode node) {
        final URI pid = nodeResolver.getPID(node);
        if (pid != null) {
            return pid.toString();
        } else {
            final URL url = nodeResolver.getUrl(node);
            return String.valueOf(url);
        }
    }

    /**
     * Updates and checks the number of bytes written
     *
     * @param sizeCount wrapper for byte count
     * @param addedBytes number of bytes to add
     * @throws IOException if the total number of bytes (size + added) exceeds
     * {@link #MAX_LIMIT}
     */
    private void updateWriteSizeCount(ThreadLocal<Long> sizeCount, final long addedBytes) throws IOException {
        // update written byte count accumulator
        final long totalBytes = sizeCount.get() + addedBytes;
        
        logger.trace("Written {}, total {}", addedBytes, totalBytes);

        // check against limit
        if (totalBytes > MAX_LIMIT) {
            final long kbLimit = MAX_LIMIT / FileUtils.ONE_KB;
            logger.warn("Written {} bytes to ZIP archive, limit of {} kilobytes exceeded", totalBytes, kbLimit);
            throw new IOException("Size limit of " + kbLimit + " kilobytes for ZIP archive exceeded");
        } else {
            sizeCount.set(totalBytes);
        }
    }
}

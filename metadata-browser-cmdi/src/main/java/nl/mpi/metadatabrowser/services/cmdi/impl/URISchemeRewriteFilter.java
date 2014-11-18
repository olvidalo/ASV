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
package nl.mpi.metadatabrowser.services.cmdi.impl;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.mpi.metadatabrowser.services.URIFilter;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class URISchemeRewriteFilter implements URIFilter, Serializable {

    private final Pattern prefixPattern;
    private final String targetScheme;

    public URISchemeRewriteFilter(String prefixPattern, String targetScheme) {
        if (Strings.isNullOrEmpty(prefixPattern)) {
            this.prefixPattern = null;
        } else {
            this.prefixPattern = Pattern.compile(prefixPattern);
        }
        this.targetScheme = targetScheme;
    }

    @Override
    public URI filterURI(URI uri) throws URISyntaxException {
        if (prefixPattern == null || targetScheme == null) {
            return uri;
        } else {
            // check if URI matches pattern for replacement
            final Matcher matcher = prefixPattern.matcher(uri.toString());
            if (matcher.matches()) {
                // replace the scheme, keep the rest
                return new URI(targetScheme, uri.getSchemeSpecificPart(), uri.getFragment());
            } else {
                return uri;
            }
        }
    }

}

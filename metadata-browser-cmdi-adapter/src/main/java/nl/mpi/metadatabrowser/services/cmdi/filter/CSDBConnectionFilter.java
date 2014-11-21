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
package nl.mpi.metadatabrowser.services.cmdi.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import nl.mpi.archiving.corpusstructure.adapter.proxy.CSDBContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Filter that closes any CDSB connection at the end of the request. It is
 * important for the functioning of this filter to configure it at the
 * <strong>top of the filter chain!!</strong>
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class CSDBConnectionFilter implements Filter {
    
    private final static Logger logger = LoggerFactory.getLogger(CSDBConnectionFilter.class);
    
    @Autowired
    private CSDBContainer container;
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
        logger.info("CSDB connections are managed by the CSDB Connection Filter. Objects contained in {}", container);
    }
    
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        // assuming to be on top of (relevant part of) filter chain
        fc.doFilter(sr, sr1);
        // end of request - CSDB connection can be safely closed
        logger.trace("Closing CDSB");
        container.setCsdb(null); // assuming this will also close the connection (i.e. TheadLocalCSDBContainer)
        logger.trace("Closing AO");
        container.setAo(null); // assuming this will also close the connection (i.e. TheadLocalCSDBContainer)
    }
    
    @Override
    public void destroy() {
    }
    
}

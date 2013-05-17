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

import nl.mpi.metadatabrowser.model.cmdi.wicket.components.PanelViewNodeShowComponent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import nl.mpi.corpusstructure.ArchiveAccessContext;
import nl.mpi.metadatabrowser.model.*;
import nl.mpi.util.OurURL;
import org.apache.wicket.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
public class CMDIViewNodeAction extends SingleNodeAction implements NodeAction {

    private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
    private final String name = "view Node";
    private Map<String, String> parameters = new HashMap<String, String>();
    private final CmdiCorpusStructureDB csdb;
    private boolean navType = false;

    public CMDIViewNodeAction(CmdiCorpusStructureDB csdb) {
        this.csdb = csdb;
    }

    @Override
    protected NodeActionResult execute(TypedCorpusNode node) throws NodeActionException {

        URI nodeUri = node.getUri();
        String nodeId = Integer.toString(node.getNodeId());
        OurURL nodeURL = csdb.getObjectURL(nodeId, ArchiveAccessContext.HTTP_URL); // Get the XML file
        String xmlContent = null;      

        logger.info("Action [{}] invoked on {}", getName(), nodeUri);


        if ((nodeURL != null) && (node != null)) {
            if (node.getNodeType() instanceof CMDIMetadata
                    || node.getNodeType() instanceof CMDICollectionType) {
                InputStream in = null;
                try {
                    in = nodeURL.toURL().openStream();
                    StringBuffer sb = new StringBuffer();
                    byte [] buffer = new byte[256];
                    while(true){
                        int byteRead = in.read(buffer);
                        if(byteRead == -1)
                            break;
                        for(int i = 0; i < byteRead; i++){
                            sb.append((char)buffer[i]);
                        }
                    }
                    xmlContent = sb.toString();
                    //return sb.toString();
                                //      try {
                                //                            JAXBContext jc = JAXBContext.newInstance(String.class);
                                //
                                //            File xml = new File(nodeURL.toString());
                                //            Unmarshaller unmarshaller = jc.createUnmarshaller();
                                //            String recon = (String) unmarshaller.unmarshal(xml);
                                //
                                //            Marshaller marshaller = jc.createMarshaller();
                                //            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                                //            marshaller.marshal(recon, System.out);
                                //
                                //                    xmlContent = nodeURL.toString();
                                //                } //all formats that should be handled by annex
                                //                catch (JAXBException ex) {
                                //                    java.util.logging.Logger.getLogger(CMDIViewNodeAction.class.getName()).log(Level.SEVERE, null, ex);
                                //                }
                }
                //      try {
                catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CMDIViewNodeAction.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(CMDIViewNodeAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
         else if (node.getNodeType() instanceof CMDIResourceTxtType) {
            parameters.put("nodeId", nodeId);
            parameters.put("jsessionID", "jsessioID");
            navType = true;
        } else {
            xmlContent = nodeURL.toString();
        }}

        final String xmlText = xmlContent;
        
        if (navType
                == true) {
            final NavigationActionRequest request = new NavigationActionRequest(NavigationRequest.NavigationTarget.ANNEX, parameters);
            return new SimpleNodeActionResult(request);
        } else {
            final ShowComponentRequest request = new ShowComponentRequest() {
                
                @Override
                public Component getComponent(String id) {
                    return new PanelViewNodeShowComponent(id, xmlText);
                }
            };
            return new SimpleNodeActionResult(request);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

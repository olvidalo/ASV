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

/**
 *
 * @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
 */
package nl.mpi.metadatabrowser.model.cmdi.wicket.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
*
* @author Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
*/
public class PanelEmbedActionDisplay extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelEmbedActionDisplay(String id, String redirectURL){
		super(id);
		StringBuilder sb = new StringBuilder();
            // create label for resource
            sb.append("<iframe id=\"iframe\" width=\"100%\" height=\"100%\" align=\"center\" src=\"");
            sb.append(redirectURL);
            sb.append("\">");
            sb.append("</iframe>");
            Label resourcelabel = new Label("iframe", sb.toString());
            resourcelabel.setEscapeModelStrings(false);
            add(resourcelabel);

	}
	
}
    


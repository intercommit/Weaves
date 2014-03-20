/*  Copyright 2014 InterCommIT b.v.
*
*  This file is part of the "Weaves" project hosted on https://github.com/intercommit/Weaves
*
*  Weaves is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Lesser General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
*  Weaves is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with Weaves.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package nl.intercommit.weaves.components;


import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;

/**
 * See {@link Grid}  Again overwritten to be able to add a clientId to each row to identify it client side.
 * 
 * @tapestrydoc
 */
public class GridRows extends org.apache.tapestry5.corelib.components.GridRows implements ClientElement {

	@Inject
	private ComponentResources resources;
	
	@Inject
	private TypeCoercer coerer;
	
	private String clientId;
	
	
	@BeforeRenderTemplate
	private void generateClientId() {
		try {
			// i cannot access the internal row object directly
			final PagedGrid container = (PagedGrid)resources.getContainer().getComponentResources().getContainer();
			clientId = coerer.coerce(container.getRowIdentifier(),String.class);
		} catch(Exception e) {
    		clientId = ""+System.currentTimeMillis();
    	}
	}
	
	@Override
    public String getClientId() {
		if (((Grid)resources.getContainer()).getRow() != null) {
			return clientId;
		} else {
			return "";
		}
    }
    
}

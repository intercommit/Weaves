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

import nl.intercommit.weaves.base.BasicClientElement;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * A switch component for enum types or anything based on a object array
 * Items in the list should have a decent toString method
 * 
 * @tapestrydoc
 */
@SupportsInformalParameters
public class Switch extends BasicClientElement {

	@Parameter
	private String value;
	
	@Parameter
	private String defaultHTML;
	
	@Inject
	private ComponentResources resources;
	
	public Block getActiveBlock() {
		
		Block activeBlock = resources.getBlock("default");
		if (value != null) {
			activeBlock = resources.getBlockParameter(value); 
			if (activeBlock == null) {
				activeBlock = resources.getBlockParameter("default");
				if (activeBlock == null) {
					activeBlock = resources.getBlock("default");
				}
			}
		} 
		return activeBlock;
	}
	
	public String getDefault() {
		return defaultHTML;
	}
	

	
}

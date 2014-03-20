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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
/**
 * This class is needed because we needed to override the Pager component
 * 
 * @see org.apache.tapestry5.corelib.components.Grid
 * 
 * @tapestrydoc
 */
public class Grid extends org.apache.tapestry5.corelib.components.Grid implements ClientElement  {

	@Parameter(defaultPrefix = BindingConstants.LITERAL,allowNull=true)
	private String nonSortable;
	
	@Parameter(required=false)
	private PagedGridPager pagedpager;
	
	@Parameter(required=true,defaultPrefix=BindingConstants.LITERAL)
	private String clientId;
	
	@Mixins("ck/OnEvent")
	@Component(parameters = { 
			"columnIndex=inherit:columnIndex", 
			"rowsPerPage=rowsPerPage", 
			"currentPage=currentPage", 
			"row=row",
			"overrides=overrides",
	        "event=literal:click",
	        "onCompleteCallback=clickRow"
	        }, 
	        publishParameters = "rowIndex,rowClass,volatile,encoder,lean")
	private nl.intercommit.weaves.components.GridRows rows;
	
	public Object getPagerTop() {
		 Object o = super.getPagerTop();
		 if (o!=null) o= pagedpager;
		 return o;
	}

	public Object getPagerBottom() {
		 Object o = super.getPagerBottom();
		 if (o!=null) o= pagedpager;
		 return o;
	}
	
	@SetupRender
	private void updateSortingColumns() {
		if (nonSortable != null) {
			final String[] columns = nonSortable.split(",");
			for (final String column: columns) {
				if (getDataModel().getPropertyNames().contains(column)) {
					getDataModel().get(column).sortable(false);
				}
			}
		}
	}

	@Override
	public String getClientId() {
		return clientId;
	}

}

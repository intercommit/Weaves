/*  Copyright 2011 InterCommIT b.v.
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
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
/**
 * This class is needed because we needed to override the Pager component
 * 
 * @see org.apache.tapestry5.corelib.components.Grid
 *
 */
public class Grid extends org.apache.tapestry5.corelib.components.Grid {

	@Parameter(defaultPrefix = BindingConstants.LITERAL,allowNull=true)
	private String nonSortable;
	
	@Parameter(required=true)
	private PagedGridPager pagedpager;
	
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
				getDataModel().get(column).sortable(false);
			}
		}
	}

}

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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.intercommit.weaves.grid.CollectionPagedGridDataSource;
import nl.intercommit.weaves.grid.HibernatePagedGridDataSource;
import nl.intercommit.weaves.grid.PagedGridDataSource;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
/**
 * Custom component PagedGrid, embeds the usual tapestry grid plus extra paging row and eventhandlers for paging
 * Can also be extended with a checkable checkbox column ! 
 * 
 * Features:
 * 
 * 1. Each row gets highlighted when clicked on
 * 2. Does not query the database for all rows, only a subset with limit
 * 3. Emits a 'pagedgrid:selectrow' javascript event when a row has been selected
 * 
 * Caution:
 * 
 * TODO: rename row
 * Your gridmodel cannot already have a row with name 'row'
 * 
 * TODO: be able to exclude the 'row'
 * 
 * Also when using checkboxes, make sure the rowidentifier function returns a unique index for the datasource, in case
 * of hibernate this will be a primary key so not a problem. But in case of a Collection the source may have been altered in 
 * the meantime and thus give back the wrong rows.
 *
 */
@Events(value=PagedGrid.ROW_SELECTED_EVENT)
@Import(library={"PagedGridScript.js"},stylesheet="PagedGrid.css")
@SupportsInformalParameters
public class PagedGrid {
	
	public final static String ROW_SELECTED_EVENT = "pagedgrid:selectrow";
	
	@Parameter(required = true)
    private PagedGridDataSource pagedsource;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String reorder;
	
	@Parameter(value="[25,50,100]")
	private List<Long> pagination;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String add;
	
	/**
	 * Parameter which defines if we have a column with checkboxes
	 */
	@Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
	private boolean checkBoxes;
	
	@Property
	private boolean checkall;
	
	@Property
	private boolean checked;
	
	@Persist(PersistenceConstants.SESSION)
	private Class rowIdClass;
	
	//session persistence because it lives over multiple AJAX requests.
	@Persist(PersistenceConstants.SESSION)
	private Map<Object,Boolean> checkedItems;
	
	// the user can override the default rowsperpage, this will be persisted.
	@Persist(PersistenceConstants.SESSION)
	private int overriddenRowsPerPage;
	
	private int rowIndex;
	
	@Inject
    private ComponentResources resources;
	
	@Inject
    private JavaScriptSupport scriptSupport;
	
	@Inject
	private Block rowCell;
	
	@Inject
	private Block checkboxCell;
	
	@Inject
	private Block checkboxHeader;
	
	@Inject
	private TypeCoercer coerer;
	
	@Component(
			inheritInformalParameters=true,
			publishParameters="row,exclude,pagerposition,include,columnIndex,model,sortModel,nonSortable",
	        parameters = {
					"source=pagedsource",
                    "add=prop:addedRow",
                    "rowsPerPage=prop:selectedrowsperpage",
                    "rowIndex=rowIndex",
                    "reorder=prop:ordering",
                    "overrides=customoverrides",
                    "rowclass=rowclass",
                    "pagedpager=pagedpager"
                    }
            )
    private nl.intercommit.weaves.components.Grid grid;
	
	@Property
	@Component(parameters = {
			"pagination=pagination",
			"hasNextPage=hasNextPage",
            "rowsPerPage=grid.rowsperpage",
            "currentPage=grid.currentPage"})
    private PagedGridPager pagedpager;

	@SetupRender
	private void checkParameters() {
		if (pagination.size() == 0) { throw new TapestryException("Specify at least one pagination value!",null);}
		checkedItems = new HashMap<Object, Boolean>();
	}
	
	@AfterRender
    private void afterRender(MarkupWriter writer) {
		if (checkBoxes) {
			scriptSupport.addScript("listenToCheckAllBox();");
			rowIdClass = pagedsource.getRowIdClass();
		}
    	scriptSupport.addScript("observeGrid();");
    }

	public int getRowIndex() {
		return rowIndex + (grid.getCurrentPage() - 1) * getSelectedRowsPerPage()+1;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	public String getRowClass() {
		return (rowIndex % 2 == 0?"odd":"even");
	}

	public String getOrdering() {
		if (reorder != null) {
			return "row" +addCheckBoxRow() + ","+ reorder;
		} 
		return "row" +addCheckBoxRow();
	}
	
	public String getAddedRow() {
		if (add != null) {
			return "row" +addCheckBoxRow() + ","+ add;
		}
		return "row" +addCheckBoxRow();
	}
	
	public String addCheckBoxRow() {
		if (checkBoxes) {
			return ",checkbox";
		} else {
			return "";
		}
	}
	
	public int getSelectedRowsPerPage() {
		if (overriddenRowsPerPage == 0) {
			return pagination.get(0).intValue();
		}
		return overriddenRowsPerPage; // the user selected 
	}
	
	@OnEvent(value="pagesize")
	void onPageSizeFromPagedGrid(int rowsPerPage) {
		this.overriddenRowsPerPage = rowsPerPage;
		grid.setCurrentPage(1); // reset to page1
	}	
	
	public PropertyOverrides getCustomOverrides() {
		return new PagedGridOverrides();
	}
	
	public class PagedGridOverrides implements PropertyOverrides {

		public Block getOverrideBlock(String name) {
			if ("rowCell".equals(name)){
				return rowCell;
			}
			if (checkBoxes) {
				if ("checkboxCell".equals(name)) {
					return checkboxCell;
				}
				if ("checkboxHeader".equals(name)) {
					return checkboxHeader;
				}
			}
			return resources.getBlockParameter(name);
		}

		public Messages getOverrideMessages() {
			return resources.getContainerMessages();
		}
	}
	

	
	@OnEvent(value = "checkboxclicked")
	private void clickme(String context) {
		
		if ("true".equalsIgnoreCase(context) ||"false".equalsIgnoreCase(context)) {
			if (Boolean.parseBoolean(context)) {
				for (Object key: checkedItems.keySet()) {
					checkedItems.put(key, true);
				}
			} else {
				for (Object key: checkedItems.keySet()) {
					checkedItems.put(key, false);
				}
			}
		} else {
			// rowObject is the actual(real) type and value of the selected item
			final Object rowObject = coerer.coerce(context, rowIdClass);
			/*
			 * containsKey works with equal and equal does NOT work with Long, which is usually the class
			 * of a primary hibernate key!
			 */
			boolean found = false;
			for (Object key:checkedItems.keySet()) {
				if ((""+key).equals(""+rowObject)) {
					found = true;
					break;
				}
			}
			if (found) {
				checkedItems.put(rowObject, !checkedItems.get(rowObject));
			} else {
				throw new TapestryException("Could not add selected row , because it is not in the grid.",this,null);
			}
		}
	}


	/**
	 * To fetch a list of checked items in your page, get a hold of the resources on that page and fetch the pagedgrid component.
	 * 
	 * When dealing with a {@link HibernatePagedGridDataSource} the list contains the primary keys (Long) of the entities selected.
	 * 
	 * When dealing with a {@link CollectionPagedGridDataSource} the list contains the index (Long) of the item in the collection
	 * 
	 * @return a list with checked items as {@link Long}, or <code>null</code> if nothing has been selected
	 */
	public Collection<Object> getCheckedItems() {
		final Collection<Object> selectedItems = new LinkedList<Object>();
		for (Object key: checkedItems.keySet()) {
			if (checkedItems.get(key)) {
				selectedItems.add(key);
			}
		}
		return selectedItems;
	}
	
	/**
	 * Method to get to the underlying grid
	 */
	public Grid getGrid() {
		return grid;
	}
	
	public PagedGridDataSource getPagedSource() {
		return pagedsource;
	}
	
	/**
	 * Get the unique identifier for the currently rendered row
	 * 
	 * @return an object containing the unique identifier to fetch from the source later on
	 */
	public Object getRowIdentifier() {
		final Object rowIdentifier = pagedsource.getIdentifierForRowValue(grid.getRow());
		checkedItems.put(rowIdentifier,false);
		return rowIdentifier;
	}
	
	/**
	 * Internal method used by the {@link PagedGridPager} component to create pagination links
	 * @return
	 */
	public List<Long> getPagination() {
		return pagination;
	}
	
	public boolean getHasNextPage() {
		return (grid.getDataSource().getRowValue(grid.getCurrentPage()* grid.getRowsPerPage()) != null); 
	}

	public void reset() {
		grid.reset();
		checkedItems = null;
	}
	
}

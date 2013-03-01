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

import nl.intercommit.weaves.base.BasicClientElement;
import nl.intercommit.weaves.grid.CollectionPagedGridDataSource;
import nl.intercommit.weaves.grid.HibernatePagedGridDataSource;
import nl.intercommit.weaves.grid.PagedGridDataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.Asset2;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.components.AjaxCheckbox;
/**
 * Custom component PagedGrid, embeds the usual tapestry grid plus extra paging row and eventhandlers for paging
 * Can also be extended with a checkable checkbox column ! 
 * 
 * Features:
 * 
 * 1. Each row gets highlighted when clicked on
 * 2. Does not query the database for all rows, only a subset with limit
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
 *
 * @tapestrydoc
 */
@Import(library={"pagedgrid/PagedGridScript.js","pagedgrid/jquery.chromatable.js"},stylesheet={"pagedgrid/PagedGrid.css"},stack="jquery")
@SupportsInformalParameters
public class PagedGrid extends BasicClientElement {
	
	
	@Component
	private nl.intercommit.weaves.components.Grid childrenGrid;
	
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
	
	/**
	 * Parameter that tells that the parent rows have children
	 */
	@Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
	private boolean tree;
	
	@Parameter(value="500",required=false,defaultPrefix=BindingConstants.LITERAL)
	private String maxHeight;
	
	@Parameter(value="false",defaultPrefix=BindingConstants.LITERAL)
	private boolean hoverAnimation;
	
	@Component
	private AjaxCheckbox checkall;
	
	@Component
	private Zone expansionZone;
	
	@Persist(PersistenceConstants.SESSION)
	private Class<?> rowIdClass;
	
	//session persistence because it lives over multiple AJAX requests.
	@Persist(PersistenceConstants.SESSION)
	private Map<Object,Boolean> checkedItems;
	
	// the user can override the default rowsperpage, this will be persisted.
	@Persist(PersistenceConstants.SESSION)
	private int overriddenRowsPerPage;
	
	@Persist(PersistenceConstants.SESSION)
	private boolean checkedAll;
	
	private int rowIndex;
	
	@Inject
    private ComponentResources resources;
	
	@Inject
    private JavaScriptSupport scriptSupport;
	
	@Property
	@Inject @Path("pagedgrid/expand.png")
	private Asset2 expandImage;
	
	@Property
	@Inject	@Path("pagedgrid/collapse.png")
	private Asset2 collapseImage;
	
	@Property
	@Inject	@Path("pagedgrid/branch.png")
	private Asset2 branchImage;
	
	@Property
	private List<?> children;
	
	@Component(
			inheritInformalParameters=true,
			publishParameters="row,columnIndex,include,exclude,model,sortModel,nonSortable,pagerposition",
			parameters = {
					"source=pagedsource",
                    "add=prop:addedRow",
                    "rowsPerPage=prop:selectedrowsperpage",
                    "rowIndex=rowIndex",
                    "reorder=prop:ordering",
                    "overrides=customoverrides",
                    "rowclass=rowclass",
                    "pagedpager=pagedpager",
                    "pagerposition=literal:bottom",
                    "clientId=clientId"}
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
		if (pagedsource.getAvailableRows() != 0) {
			if (tree) {
				scriptSupport.addScript("observeExpansionZone();");
			}
			scriptSupport.addScript("initializeGrid('"+getClientId()+"',"+maxHeight+");");
		}
		if (hoverAnimation) {
			scriptSupport.addScript("enableHovering(true);");
		}
		if (checkBoxes) {
			scriptSupport.addScript(InitializationPriority.LATE,"listenToCheckAllBox('"+checkall.getClientId()+"');");
			rowIdClass = pagedsource.getRowIdClass();
		}
		checkedAll = false;
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
			return addExtraRows() + ","+ reorder;
		} 
		return addExtraRows();
	}
	
	public String getAddedRow() {
		if (add != null) {
			return addExtraRows() + ","+ add;
		}
		return addExtraRows();
	}
	
	private String addExtraRows() {
		String extraRows = "row";
		if (checkBoxes) {
			extraRows = extraRows + ",checkbox";
		}
		if (tree) {
			extraRows = extraRows + ",expander";
		}
		return extraRows; // hmm ok, this works
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
	
	@OnEvent(value="fetchChildren")
	Block fetchChildren(long rowId) {
		children = pagedsource.fetchChildren(rowId);
		return expansionZone.getBody();
	}
			
	public PropertyOverrides getCustomOverrides() {
		return new PagedGridOverrides();
	}
	
	public PropertyOverrides getChildOverrides() {
		return new ChildGridOverrides();
	}
	
	@OnEvent(value = "checkboxclicked")
	private void clickme(EventContext context) {
		if (!(rowIdClass == null || checkedItems == null)) {
			if (context.toStrings().length ==1) {
				checkedAll = !checkedAll;
				for (Object key: checkedItems.keySet()) {
					checkedItems.put(key, checkedAll);
				}
			} else {
				// rowObject is the actual(real) type and value of the selected item
				final Object rowObject = context.get(rowIdClass, 0);
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
	
	public BeanModel<?> getChildModel() {
		return grid.getDataModel();
	}
	
	/*
	 * Overrides for parent and child grid
	 */
	private class PagedGridOverrides implements PropertyOverrides {

		public Block getOverrideBlock(String name) {
			try {
				return resources.getBlock(name);
			} catch (Exception e) {
				return resources.getBlockParameter(name);
			}
		}

		public Messages getOverrideMessages() {
			return resources.getContainerMessages();
		}
	}
	
	private class ChildGridOverrides implements PropertyOverrides {

		public Block getOverrideBlock(String name) {
			if (name.endsWith("Header")) {
				return null; // child grid does not have headers.
			}
			try {
				return resources.getBlock(name+"Child");
			} catch (Exception e) {
				return resources.getBlockParameter(name+"Child");
			}
		}

		public Messages getOverrideMessages() {
			return resources.getContainerMessages();
		}
	}

	
}

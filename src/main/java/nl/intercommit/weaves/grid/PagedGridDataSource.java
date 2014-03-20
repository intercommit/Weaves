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
package nl.intercommit.weaves.grid;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

/**
 * Paged implementation of a GridDataSource,
 * 
 * Warning: the preparedResults are internally (Tapestry) cached, each request for a row will return a new implementation of THIS class.
 * Meaning that you cannot store any values in this class!
 * 
 * 
 *
 */
public abstract class PagedGridDataSource<T> implements GridDataSource {

	@Persist
	private int startIndex;
	
	private List<T> preparedResults;
	
	private final Class<T> entityType;
	
	public PagedGridDataSource(final Class<T> entityType) {
		this.entityType = entityType;
	}
	
	public int getAvailableRows() {
		if (preparedResults == null) {
			return Integer.MAX_VALUE;
		}
		return preparedResults.size();
	}

	public Class<T> getRowType() {
		return entityType;
	}
	
	public Object getRowValue(int index) {
    	if ( (index - startIndex) < preparedResults.size() ) {
    		return preparedResults.get(index - startIndex);	
    	}
    	return null;
	}

	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {
		assert sortConstraints != null;
		this.startIndex = startIndex;
		final int endIndexPlusOne = endIndex + 1;// always try to retrieve on record more
		final List<SortConstraint> constraints = ListUtils.sum(sortConstraints, addAdditionalSorting());
		preparedResults = (List<T>) fetchResult(startIndex, endIndexPlusOne, constraints);
	}
	
	public abstract List<T> fetchResult(int startIndex,int endIndexPlusOne,List<SortConstraint> constraints);
	
	/**
	 * OVerride if extra sorting is needed.
	 * 
	 * @param constraints
	 */
	public List<SortConstraint> addAdditionalSorting() {
		return Collections.emptyList();
	};
	
	/**
	 * Override if extra filtering is needed, for a menu for example
	 */
	public void applyFiltering() {};
	
	/**
	 * Should return a value identifying the current checked row
	 * 
	 * For instance: A Hibernate grid may return the primary key of the checked object.
	 * Any other implementation may return the object as is or something else to identify the unique row within the given pagedsource
	 * 
	 * @param rowObject
	 * @return 
	 */
	public abstract Object getIdentifierForRowValue(final Object rowObject);
	
	public abstract Class<?> getRowIdClass();
	
}

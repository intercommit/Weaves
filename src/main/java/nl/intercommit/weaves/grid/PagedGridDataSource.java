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
package nl.intercommit.weaves.grid;

import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

/**
 * Paged implementation of a GridDataSource,
 * 
 * Warning: the preparedResults are internally (Tapestry) cached, each request for a row will return a new implementation of THIS class.
 * Meaning that you cannot store any values in this class!
 *
 */
public abstract class PagedGridDataSource implements GridDataSource {

	protected ChildrenFetcher fetcher;
	
	@Persist
	private int startIndex;
	
	private List<?> preparedResults;
	
	private final Class<?> entityType;
	
	public PagedGridDataSource(final Class<?> entityType) {
		this.entityType = entityType;
	}
	
	public int getAvailableRows() {
		if (preparedResults == null) {
			return Integer.MAX_VALUE;
		}
		return preparedResults.size();
	}

	public Class<?> getRowType() {
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
		preparedResults = fetchResult(startIndex, endIndexPlusOne, sortConstraints);
	}
	
	public abstract List<?> fetchResult(int startIndex,int endIndexPlusOne,List<SortConstraint> sortConstraints);
	
	public List<?> fetchChildren(final long rowId) {
		if (fetcher != null) {
			return fetcher.fetchChildren(rowId);
		}
		throw new TapestryException("Could not fetch children for 'PagedGridDataSource', no fetcher specified!",this,null);
	}

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

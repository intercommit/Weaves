package nl.intercommit.weaves.grid;

import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

/**
 * Paged implementation of a GridDataSource,
 * 
 * Warning: the preparedResults are internally (Tapestry) cached, each request for a row will return a new implementation of THIS class.
 * Meaning that you cannot store any values in this class!
 * 
 * @author antalk
 *
 */
public abstract class PagedGridDataSource implements GridDataSource {

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
	
	public abstract Class getRowIdClass();
	
}

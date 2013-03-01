package nl.intercommit.weaves.grid;

import java.util.List;
/**
 * An interface used by PagedGridDataSource for fetching children based on a row identifier.
 * The implementation itself is responsible for returning a list of related children
 * 
 * @author antalk
 *
 */
public interface ChildrenFetcher {

	public List<?> fetchChildren(long rowId);
}


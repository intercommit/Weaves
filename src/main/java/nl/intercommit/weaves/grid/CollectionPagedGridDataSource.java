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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public class CollectionPagedGridDataSource<T> extends PagedGridDataSource<T> {

	// a filtered subset of the collection
	private final List<T> list;
	
	// an Map with hashed object keys for fast searching.
	private final Map<Integer,Integer> indexedList;

    public CollectionPagedGridDataSource(final Collection<T> collection,final Class<T> entityType) {
    	super(entityType);
    	
    	assert collection != null;
    	
    	// Copy the collection so that we can sort it without disturbing the original
    	this.list = CollectionFactory.newList(collection);
    	
    	indexedList = new HashMap<Integer, Integer>();
    	for (int i=0;i<list.size(); i++) {
    		indexedList.put(list.get(i).hashCode(), i);
    	}
    }
    
    public List<T> fetchResult(int startIndex, int endIndexPlusOne,
		List<SortConstraint> sortConstraints) {
    	
    	// apply a filter first
    	doFilter();
    	
        for (SortConstraint constraint : sortConstraints)
        {
            final ColumnSort sort = constraint.getColumnSort();

            if (sort == ColumnSort.UNSORTED) continue;

            final PropertyConduit conduit = constraint.getPropertyModel().getConduit();

            final Comparator valueComparator = new Comparator<Comparable>()
            {
                public int compare(Comparable o1, Comparable o2)
                {
                    // Simplify comparison, and handle case where both are nulls.

                    if (o1 == o2) return 0;

                    if (o2 == null) return 1;

                    if (o1 == null) return -1;

                    return o1.compareTo(o2);
                }
            };

            final Comparator rowComparator = new Comparator()
            {
                public int compare(Object row1, Object row2)
                {
                    Comparable value1 = (Comparable) conduit.get(row1);
                    Comparable value2 = (Comparable) conduit.get(row2);

                    return valueComparator.compare(value1, value2);
                }
            };

            final Comparator reverseComparator = new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    int modifier = sort == ColumnSort.ASCENDING ? 1 : -1;

                    return modifier * rowComparator.compare(o1, o2);
                }
            };

            // We can freely sort this list because its just a copy.

            Collections.sort(list, reverseComparator);
        }
        
        //sorted, now do the paging or not..
        if (!list.isEmpty()) {
        	if (endIndexPlusOne >= list.size()) {
        		endIndexPlusOne = list.size();
        	} else {
        		endIndexPlusOne++; // sublist use endIndex as 'exclusive'!
        	}
        	if (startIndex <0) {
        		startIndex = 0;
        	}
        	if (startIndex > list.size()) {
        		startIndex = list.size();
        	}
        	return list.subList(startIndex, endIndexPlusOne);
        }
        return list;
        
    }
    
    private void doFilter() {
    	List<CollectionFilter> filter = createFilter();
    	if (filter == null) {
    		//some defensive actions
    		filter = new LinkedList<CollectionFilter>();
    	}
    	
    	for (final CollectionFilter activeFilter:filter) {
    		
    		try {
    			Method foundMethod = null;
    			
    			for (Method method: getRowType().getMethods()) {
    				if (method.getName().equalsIgnoreCase("get"+activeFilter.getName())) {
    					
    					if (method.getParameterTypes().length == 0) {
    						// get method with no params..
    						foundMethod = method;
    						break;
    					}
    				}
    			}
    			
    			
    			if (foundMethod != null) {
    			
	    			// if the method is not even available , dont bother going through the list
	    			for (final Object element : list.toArray()) {
	    				
	    				final Object realValue = foundMethod.invoke(element, null);
	    				
	    				switch (activeFilter.getOp()) {
	    					case EQ: {
	    						
	    						if (realValue instanceof String) {
	    							// String comparison
		    						if (!realValue.toString().equals((activeFilter.getValue().toString()))) {
		    	    					// filter it out
		    	    					list.remove(element);
		    	    				}
	    						} else {
	    							// object compare..
	    							// TODO: workout other instances
	    							if (!(realValue.equals(activeFilter.getValue()))) {
	    								list.remove(element);	
	    							}
	    						}
	    						break;
	    					}
	    					case GT: {
	    						if (realValue instanceof Date) {
	    							if ( ((Date)realValue).before((Date)activeFilter.getValue())) {
	    								list.remove(element);
	    							}
	    						}
	    						break;
	    					}
	    					case LT: {
	    						if (realValue instanceof Date) {
	    							if ( ((Date)realValue).after((Date)activeFilter.getValue())) {
	    								list.remove(element);
	    							}
	    						}
	    						break;
	    					}
	    				}
	    				
	    				
	    			
	    			}
    			}
    		} catch(Exception e) {
    			// TODO: logging
    			System.err.println("[ERROR] during collection filtering:" + e.getMessage());
    		}
    	}
    	
    }
    
    // Override if needed
    public List<CollectionFilter> createFilter() {
    	return new LinkedList<CollectionFilter>();
    };

   @Override
   public Object getIdentifierForRowValue(final Object rowObject) {
	   if (indexedList.containsKey(rowObject.hashCode())) {
		   return indexedList.get(rowObject.hashCode()); // return the original index in teh given list
	   }
	   return null;
   }
   
   @Override
   public Class getRowIdClass() {
	   return Integer.class; // the index in the original list is the row identifier type
   }

}

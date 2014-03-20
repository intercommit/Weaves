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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;
import org.hibernate.criterion.Order;
/**
 * A paged hibernate grid source.
 * 
 * When you want to enable checkboxes on the grid, the only allowed entitytypes are the ones with a Long as primary key ! 
 * Otherwise you can also use composite keys
 *
 */
public class HibernatePagedGridDataSource<T> extends PagedGridDataSource<T> {

	private final Session hibernateSession;
	private Criteria crit = null;
	
	public HibernatePagedGridDataSource(final Session session,final Class<T> entityType) {
		super(entityType);
		hibernateSession = session;
		if (session.getSessionFactory().getClassMetadata(entityType) == null) {
			throw new TapestryException("This entity ["+entityType+"] is not managed by the given session", this, new EntityExistsException());
		}
	}
	
	public Criteria getCriteria() {
		if (crit == null) {
			crit = hibernateSession.createCriteria(getRowType());
			 
	        if (hibernateSession.getCacheMode().isGetEnabled()) {
				crit.setCacheable(true);
			}
		}
		return crit;
	}
	
	public List<T> fetchResult(int startIndex, int endIndexPlusOne,
			List<SortConstraint> sortConstraints) {
		
        // We just assume that the property names in the SortContraint match the Hibernate
        // properties.
		
        applyFiltering();
        
        final List<String> orderedFields = new ArrayList<String>();
        
        for (final SortConstraint constraint : sortConstraints) {
        	
        	final String propertyName = constraint.getPropertyModel().getPropertyName();
            if (!orderedFields.contains(propertyName)) {
            	orderedFields.add(propertyName);
	            switch (constraint.getColumnSort()) {
	
	                case ASCENDING:
	
	                	getCriteria().addOrder(Order.asc(propertyName));
	                    break;
	
	                case DESCENDING:
	                	getCriteria().addOrder(Order.desc(propertyName));
	                    break;
	
	                default:
	            }
            }
        }
        getCriteria().setFirstResult(startIndex).setMaxResults(endIndexPlusOne - startIndex + 1);
        return getCriteria().list();
	}
	
	@Override
	public Object getIdentifierForRowValue(final Object rowObject) {
		try {
			return hibernateSession.getIdentifier(rowObject);
		} catch (TransientObjectException toe) {
			return null;
		}
		
	}

	@Override
	public Class<?> getRowIdClass() {
		return Long.class;
	}
}

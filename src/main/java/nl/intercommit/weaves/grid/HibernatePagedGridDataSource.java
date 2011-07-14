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

import org.apache.tapestry5.grid.SortConstraint;
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
 * @author antalk
 *
 */
public class HibernatePagedGridDataSource extends PagedGridDataSource {

	private final Session hibernateSession; 
	
	public HibernatePagedGridDataSource(final Session session,final Class<?> entityType) {
		super(entityType);
		hibernateSession = session;
	}
	
	public List<?> fetchResult(int startIndex, int endIndexPlusOne,
			List<SortConstraint> sortConstraints) {
		
        // We just assume that the property names in the SortContraint match the Hibernate
        // properties.

        final Criteria crit = hibernateSession.createCriteria(getRowType());
        
        if (hibernateSession.getCacheMode().isGetEnabled()) {
			crit.setCacheable(true);
		}
        
        applyFiltering(crit);
        
        crit.setFirstResult(startIndex).setMaxResults(endIndexPlusOne - startIndex + 1);

        for (final SortConstraint constraint : sortConstraints)
        {

            final String propertyName = constraint.getPropertyModel().getPropertyName();

            switch (constraint.getColumnSort())
            {

                case ASCENDING:

                    crit.addOrder(Order.asc(propertyName));
                    break;

                case DESCENDING:
                    crit.addOrder(Order.desc(propertyName));
                    break;

                default:
            }
        }
        
        applyAdditionalSorting(crit);

        return crit.list();
	}

	/**
	 * Overrule if you want to apply filtering
	 */
	public void applyFiltering(final Criteria criteria) {};

	/**
	 * Overrule if you want to apply additional sorting besides the user selected one
	 */
	public void applyAdditionalSorting(final Criteria criteria) {};
	
	
	@Override
	public Object getIdentifierForRowValue(final Object rowObject) {
		try {
			return hibernateSession.getIdentifier(rowObject);
		} catch (TransientObjectException toe) {
			return null;
		}
		
	}
	
	@Override
	public Class getRowIdClass() {
		return Long.class;
	}
}

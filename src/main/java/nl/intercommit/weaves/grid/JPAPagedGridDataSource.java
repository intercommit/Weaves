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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.internal.util.TapestryException;


public class JPAPagedGridDataSource<T> extends PagedGridDataSource<T> {

	private final EntityManager em;
	
	/*
	 * These fields are required later on to filter on fields.
	 */
	private CriteriaBuilder cb = null;
	private Root<T> root = null;
	private List<Predicate> predicates = null;
	
	
	public JPAPagedGridDataSource(final EntityManager em,final Class<T> entityType) {
		super(entityType);
		this.em = em;
		try { 
			em.getEntityManagerFactory().getMetamodel().managedType(entityType);
		} catch (IllegalArgumentException e) {
			throw new TapestryException("This entity ["+entityType+"] is not managed by the given entitymanager", this, e);
		}
	}
	
	
	@Override
	public List<T> fetchResult(int startIndex, int endIndexPlusOne,
			List<SortConstraint> sortConstraints) {
		
		 // We just assume that the property names in the SortContraint match the Hibernate
        // properties.
		
		cb = em.getCriteriaBuilder();
		final CriteriaQuery<T> cq = cb.createQuery(getRowType());
		root = cq.from(getRowType()); // the FROM clause
		
		predicates = new ArrayList<Predicate>();
		//Constructing list of parameters
		applyFiltering(); // use the predicates, cb and root fields to fill up the filters
        
        final List<String> orderedFields = new ArrayList<String>();
        
        for (final SortConstraint constraint : sortConstraints) {
        	
        	final String propertyName = constraint.getPropertyModel().getPropertyName();
            if (!orderedFields.contains(propertyName)) {
            	orderedFields.add(propertyName);
	            switch (constraint.getColumnSort()) {
	
	                case ASCENDING:
	                    cq.orderBy(cb.asc(root.get(propertyName)));
	                    break;
	
	                case DESCENDING:
	                	cq.orderBy(cb.desc(root.get(propertyName)));
	                    break;
	
	                default:
	            }
            }
        }
        cq.select(root).where(predicates.toArray(new Predicate[]{}));
        final TypedQuery<T> q = em.createQuery(cq);
		q.setFirstResult(startIndex);
		q.setMaxResults(endIndexPlusOne - startIndex + 1);
        return q.getResultList();
	}
	
	@Override
	public Object getIdentifierForRowValue(final Object rowObject) {
		try {
			return em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(rowObject);
		} catch (Exception toe) {
			toe.printStackTrace();
			return null;
		}
	}

	@Override
	public Class<?> getRowIdClass() {
		return em.getEntityManagerFactory().getMetamodel().entity(getRowType()).getIdType().getJavaType();
	}
	

	/**
	 * Can be used when the applyFiltering function is called, to add extra filtering.
	 * 
	 * @return
	 */
	protected List<Predicate> getPredicates() {
		return predicates;
	}
	
	protected CriteriaBuilder getCb() {
		return cb;
	}
	
	protected Root<T> getRoot() {
		return root;
	}
}

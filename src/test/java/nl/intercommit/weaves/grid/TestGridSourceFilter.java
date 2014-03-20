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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import nl.intercommit.weaves.grid.CollectionFilter.OPERATOR;
import nl.intercommit.weaves.test.entities.Person;

import org.apache.tapestry5.grid.SortConstraint;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import util.HibernateUtil;

public class TestGridSourceFilter extends TestCase {

	HibernateUtil util = null;
	Session session = null;
	Collection<Person> persons = new LinkedList<Person>();
	
	@Override
	protected void setUp() throws Exception {
		util = new HibernateUtil();
		
		session = util.create();
		
		Person p1 = new Person();
		p1.setName("fred");
		session.persist(p1);
		persons.add(p1);
		
		Person p2 = new Person();
		p2.setName("wilma");
		session.persist(p2);
		persons.add(p2);
		
		session.flush();
		
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		util.shutdown();
	}
	
	
	public void testHibernateGridSource() {
		HibernatePagedGridDataSource hpds = new HibernatePagedGridDataSource(session,Person.class) {
			
			@Override
			public void applyFiltering() {
				getCriteria().add(Restrictions.eq("name", "fred"));
			}
		};
		
		hpds.prepare(0, 10, new LinkedList<SortConstraint>());
		
		Person p1 = (Person)(hpds.getRowValue(0));
		assertNotNull(p1);
		assertEquals("fred", p1.getName());
		
		assertNotNull(hpds.getIdentifierForRowValue(p1));
	}
	
	public void testCollectionGridSource() {
		CollectionPagedGridDataSource cpds = new CollectionPagedGridDataSource(persons,Person.class) {
			
			@Override
			public List<CollectionFilter> createFilter() {
				List<CollectionFilter> list = new LinkedList<CollectionFilter>();
				
				CollectionFilter cf = new CollectionFilter("name","fred",OPERATOR.EQ);
				list.add(cf);
				return list;
			}
		};
		
		cpds.prepare(0,10,new LinkedList<SortConstraint>());
		
		Person p1 = (Person)(cpds.getRowValue(1));
		
		assertNull(p1); // it only contains fred..
		
		p1 = (Person)(cpds.getRowValue(0));
		
		assertEquals("fred",p1.getName());
		
		
		
	}
	
	
}



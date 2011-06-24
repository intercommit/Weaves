package nl.intercommit.tapestry.grid;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.internal.beaneditor.PropertyModelImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import nl.intercommit.tapestry.test.entities.Person;
import nl.intercommit.weaves.grid.CollectionFilter;
import nl.intercommit.weaves.grid.CollectionPagedGridDataSource;
import nl.intercommit.weaves.grid.HibernatePagedGridDataSource;
import nl.intercommit.weaves.grid.CollectionFilter.OPERATOR;
import util.HibernateUtil;
import junit.framework.TestCase;

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
			public void applyFiltering(Criteria criteria) {
				criteria.add(Restrictions.eq("name", "fred"));
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



package nl.intercommit.tapestry.test.pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import nl.intercommit.tapestry.test.beans.Box;
import nl.intercommit.tapestry.test.entities.Person;
import nl.intercommit.weaves.grid.CollectionPagedGridDataSource;
import nl.intercommit.weaves.grid.HibernatePagedGridDataSource;
import nl.intercommit.weaves.hibernate.HibernateMultiSessionManager;

public class PagedGridPage {
	
	@Inject
	private HibernateMultiSessionManager manager;
	

	public CollectionPagedGridDataSource getCollectionPagedSource() {
		List<Box> boxes = new LinkedList<Box>();
		
		Box box = new Box();
		box.setId(Long.valueOf(1));
		box.setType("type 1");
		boxes.add(box);
		
		box = new Box();
		box.setId(Long.valueOf(2));
		box.setType("type 2");
		boxes.add(box);
		
		return new CollectionPagedGridDataSource(boxes,Box.class);
	}
	
	public HibernatePagedGridDataSource getHibernatePagedSource() {
		return new HibernatePagedGridDataSource(manager.getSession("Factory1"),Person.class);
	}
	
	public List<Long> getPagination() {
		ArrayList<Long> list = new ArrayList<Long>();
		list.add(new Long(1));
		list.add(new Long(2));
		list.add(new Long(10));
		return list;
	}
	
	
	
}

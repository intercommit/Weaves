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
package nl.intercommit.weaves.test.pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.intercommit.weaves.grid.CollectionPagedGridDataSource;
import nl.intercommit.weaves.grid.HibernatePagedGridDataSource;
import nl.intercommit.weaves.hibernate.HibernateMultiSessionManager;
import nl.intercommit.weaves.test.beans.Box;
import nl.intercommit.weaves.test.entities.Person;

import org.apache.tapestry5.ioc.annotations.Inject;

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

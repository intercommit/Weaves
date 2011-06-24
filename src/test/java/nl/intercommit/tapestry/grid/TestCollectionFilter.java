package nl.intercommit.tapestry.grid;

import nl.intercommit.weaves.grid.CollectionFilter;
import nl.intercommit.weaves.grid.CollectionFilter.OPERATOR;
import junit.framework.TestCase;

public class TestCollectionFilter extends TestCase {
	
	public void testFilter() {
		CollectionFilter cf = new CollectionFilter("name","Smith",OPERATOR.EQ);
		
		assertTrue(cf.getValue().equals("Smith"));
		assertTrue(cf.getOp().equals(OPERATOR.EQ));
		assertTrue(cf.getName().equals("name"));
	}
}

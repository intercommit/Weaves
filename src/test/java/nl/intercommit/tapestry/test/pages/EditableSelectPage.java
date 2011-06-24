package nl.intercommit.tapestry.test.pages;

import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.annotations.Property;

public class EditableSelectPage {

	@Property
	private String item;
	
	public List<String> getList() {
		List<String> items = new LinkedList<String>();
		items.add("psp");
		items.add("ipad");
		items.add("3ds");
		return items;
		
	}
}

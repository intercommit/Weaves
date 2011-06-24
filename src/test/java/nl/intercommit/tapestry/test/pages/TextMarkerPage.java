package nl.intercommit.tapestry.test.pages;

import org.apache.tapestry5.annotations.Property;

public class TextMarkerPage {

	@Property
	private int maxLength;
	
	public String getMessage() {
		return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt Ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	}
	
	public String[] getWords() {
		return new String[] {"ipsum","Ut","exercitation"};
	}
	
	private void onActivate(String param) {
		maxLength = Integer.parseInt(param);
	}
}

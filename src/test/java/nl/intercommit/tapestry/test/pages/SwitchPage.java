package nl.intercommit.tapestry.test.pages;

public class SwitchPage {

	private String testValue;
	
	public String getTestValue() {
		return testValue;
	}
	
	public String getHtml() {
		return "<h1>default</h1>";
	}
	
	private void onActivate(String param) {
		testValue = param;
	}
}

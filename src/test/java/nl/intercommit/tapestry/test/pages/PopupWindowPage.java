package nl.intercommit.tapestry.test.pages;

import nl.intercommit.weaves.components.PopupWindow;

import org.apache.tapestry5.annotations.Component;

public class PopupWindowPage {
	
	@Component(parameters={"width=200","height=200","windowStyle=darkX"})
	private PopupWindow popupWindow;

}

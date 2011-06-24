package nl.intercommit.tapestry.test.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ModalBoxPage {

	@Inject
	private Block messageBlock;

	public Object onEvent() {
		return messageBlock;
	}

}

/*  Copyright 2014 InterCommIT b.v.
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
package nl.intercommit.weaves.mixins;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
/**
 * 
 * @tapestrydoc
 *
 */
public class SubmitOnChange {

	@Inject
	private JavaScriptSupport js;
	
	@InjectContainer
	private Field field;
	
	@AfterRender
	public void afterRender(final MarkupWriter writer) {
		js.addScript("Event.observe($('%s'), 'change', function(event) {"+
					"$('%s').submit();\n\r"+
			"});", field.getClientId(),writer.getElement().getAttribute("id"));
	}
	   
}

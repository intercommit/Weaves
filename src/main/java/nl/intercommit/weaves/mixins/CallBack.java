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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * 
 * @tapestrydoc
 *
 */
public class CallBack {
	
	@Parameter(allowNull=true)
	private Object clickContext;
	
	@Parameter(allowNull=false,defaultPrefix=BindingConstants.LITERAL,required=true)
	private String callback;
	
	@Inject
    private JavaScriptSupport js;

    @InjectContainer
    private ClientElement element;

    @AfterRender
    public void afterRender() {
    	    js.addScript(String.format("$('%s').observe('click', function(event) {"+ 
    	    		callback + "(event,"+clickContext+");	});",
                    element.getClientId()));
    }
}

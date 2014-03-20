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
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Copied from http://wiki.apache.org/tapestry/Tapestry5AndJavaScriptExplained
 * A simple mixin for attaching a javascript confirmation box to the onclick
 * event of any component that implements ClientElement.
 *
 * @author <a href="mailto:chris@thegodcode.net">Chris Lewis</a> Apr 18, 2008
 * 
 * @tapestrydoc
 */
@Import(library={"Confirm.js"})
public class Confirm {

    @Parameter(value = "Are you sure?", defaultPrefix = BindingConstants.LITERAL)
    private String message;

    @Inject
    private JavaScriptSupport scriptSupport;

    @InjectContainer
    private ClientElement element;

    @AfterRender
    public void afterRender() {
    	scriptSupport.addScript(String.format("new Confirm('%s', '%s');",
                    element.getClientId(), message));
    }

}

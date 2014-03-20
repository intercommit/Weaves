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
package nl.intercommit.weaves.components;

import nl.intercommit.weaves.base.BasicClientElement;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.components.Window;

/**
 * Ajax popup window based on chenillekit window {@link Window} 
 * 
 * Must have a zoneName parameter, this will allow the window to listen to zone update events and show this window.
 *
 * @tapestrydoc
 */
@Import(library={"PopupWindow.js"})
@SupportsInformalParameters
public class PopupWindow extends BasicClientElement {

	@Parameter(required=true,defaultPrefix="literal")
	private String width;

	@Parameter(required=true,defaultPrefix="literal")
	private String height;
	
	@Parameter
	private String title;
	
	@Parameter(value="true")
	private boolean resize;
	
	@Parameter(allowNull=true,required=false)
	private Asset additionalScript;
	
	@Parameter(required=false,value="greenlighting",defaultPrefix="literal")
	private String windowStyle;
	
	@Parameter(required=true,defaultPrefix="literal")
	private String zoneName;
	
	@Inject
    private JavaScriptSupport scriptSupport;
	
	@Component(
			inheritInformalParameters = true,
			publishParameters = "title",
			parameters = {
					"width=inherit:width",
					"height=inherit:height",
					"style=prop:windowStyle",
					"show=false",
					"clientId=prop:windowId"
					}
            )
    private Window window;
	
	@Component(parameters= {"id=prop:zoneName","update=show"})
	private Zone zone;
	
	@AfterRender
	void afterRender(MarkupWriter writer) {
		// LATE because otherwise the popup window object is not available
		scriptSupport.addScript(InitializationPriority.LATE,"listenToZoneEvent('"+zoneName+"','"+getWindowId()+"');",new Object[] {});

		if (resize) {
			scriptSupport.addScript(InitializationPriority.LATE, "$('"+getWindowId()+"').win.options.autoResize=true;", new Object[] {});
		}
		
		if (additionalScript != null) {
			scriptSupport.importJavaScriptLibrary(additionalScript);
		}
	}
	
	public String getWindowId() {
		return "pw_" + getClientId();
	}
	
	public String getWindowStyle() {
		return windowStyle;
	}
	
	public String getZoneName() {
		return zoneName;
	}
	/**
	 * 
	 * @return the zone contained in this window, so that you can reference it in the (TML) page
	 */
	public Zone getWindowZone() {
		return zone;
	}
	
}

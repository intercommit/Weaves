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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import nl.intercommit.weaves.menu.MenuItem;

import org.apache.tapestry5.Asset2;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * @tapestrydoc
 */
public class DropDownMenu {

	// linkedhashmaps to preserve ordering!
	@Parameter(required=true,allowNull=false)
	private LinkedHashMap<MenuItem,LinkedHashMap<MenuItem, List<MenuItem>>> menu;
	
	@Inject
	private JavaScriptSupport js;
	
	@Inject @Path("dropdown/DropDownMenu.js")
	private Asset2 dropDownJS;
	
	@Inject @Path("dropdown/DropDownMenu-T5.css")
	private Asset2 dropDownCSST5;
	
	@Inject @Path("dropdown/DropDownMenu-BS.css")
	private Asset2 dropDownCSSBS;
	
	@Inject
	private ComponentResources cr;
	
	@Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
	
	@Property
	private MenuItem level1;
	
	@Property
	private MenuItem level2;
	
	@Property
	private MenuItem level3;
	
	private MenuItem selectedLevel;

	@SetupRender
	private void initJavaScript() {
		if (!bootstrap) {
			js.importJavaScriptLibrary(dropDownJS);
			js.importStylesheet(dropDownCSST5);
			js.addScript("initMenu();", "");
		} else {
			js.importStylesheet(dropDownCSSBS);
		}
	} 

	public List<MenuItem> getMenuBar() {
		if (getHasLevel1()) {
			return Arrays.asList((MenuItem[])menu.keySet().toArray(new MenuItem[0]));
		} else {
			return new ArrayList<MenuItem>();	
		}
	}
	
	public List<MenuItem> getMenuItems() {
		if (getHasLevel2()) {
			return Arrays.asList((MenuItem[])menu.get(level1).keySet().toArray(new MenuItem[0]));
		} else {
			return new ArrayList<MenuItem>();
		}
	}
	
	public List<MenuItem> getSubMenus() {
		if (getHasLevel3()) {
			return Arrays.asList((MenuItem[])menu.get(level1).get(level2).toArray(new MenuItem[0]));	
		} else {
			return new ArrayList<MenuItem>();
		}
	}
	
	public boolean getHasLevel1() {
		return menu.keySet() != null;
	}
	
	public boolean getHasLevel2() {
		if (getHasLevel1()) {
			return menu.get(level1) != null;
		} 
		return false;
	}

	public boolean getHasLevel3() {
		if (getHasLevel2()) {
			return menu.get(level1).get(level2) != null ;	
		}
		return false;
	}
	
	@BeginRender
	public void determineSelectedLevel() {
		if (menu.keySet().size() > 0) {
			String reqPath = cr.getPage().getComponentResources().getPageName();
			if (reqPath.endsWith("/Index")) {
				// special case, default page reached (dont know where this is configured..) so it could break.
				reqPath = reqPath.substring(0,reqPath.indexOf("/Index"));
			}
			
			selectedLevel = (MenuItem) menu.keySet().toArray()[0]; // select 1st menu
			
			for (MenuItem rootLevel:menu.keySet()) {
				List<MenuItem> allItems = new LinkedList<MenuItem>();	
				
				allItems.add(rootLevel); // me
				if (menu.get(rootLevel) != null) {
					allItems.addAll(menu.get(rootLevel).keySet()); // all level2's
					for (MenuItem level3: menu.get(rootLevel).keySet()) {
						final List<MenuItem> subs = menu.get(rootLevel).get(level3);
						if (subs != null) {
							allItems.addAll(subs);	
						}
					}
				}
				
				for (MenuItem item: allItems) {
					final String basePath = item.getUrl().getBasePath().substring(1);
					if (basePath.length() > 0) {
						if (basePath.contains(reqPath.toLowerCase())) {
							selectedLevel = rootLevel;
							break;
						}
					}
				}
			}
		}
	}
	
	public String getSelectedclass() {
		if (level1.equals(selectedLevel)) {
			if (!bootstrap) {
				return "toplevelselected";
			} else {
				return "active";
			}
		}
		return "none";
	}
	
	public Block getMenuBlock() {
		if (bootstrap) {
			return cr.getBlock("bootstrap");
		} else {
			return cr.getBlock("classic");
		}
	}
}
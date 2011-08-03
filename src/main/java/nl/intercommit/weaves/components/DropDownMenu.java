/*  Copyright 2011 InterCommIT b.v.
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
import java.util.List;

import nl.intercommit.weaves.menu.MenuItem;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library="DropDownMenu.js",stylesheet="DropDownMenu.css")
public class DropDownMenu {

	public final static String MENU_ITEM_SELECTED = "selectmenuitem";
	
	// linkedhashmaps to preserve ordering!
	@Parameter(required=true,allowNull=false)
	private LinkedHashMap<MenuItem,LinkedHashMap<MenuItem, List<MenuItem>>> menu;
	
	@Inject
	private JavaScriptSupport js;
	
	@Inject
	private Request request;
	
	@Property
	private MenuItem level1;
	
	@Property
	private MenuItem level2;
	
	@Property
	private MenuItem level3;
	
	private boolean matched = false;
	
	@SetupRender
	private void initJavaScript() {
		js.addScript("initMenu();", "");
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
	
	public String getSelectedClass() {
		String className= "none";
		for (MenuItem topLevel: menu.keySet()) {
			if (matchesRequest(topLevel,topLevel)) {
				return "toplevelselected";
			}
			if (menu.get(topLevel) != null) {
				for (MenuItem level2: menu.get(topLevel).keySet()) {
					if (matchesRequest(level2,topLevel)) {
						return "toplevelselected";
					}
					if (menu.get(topLevel).get(level2) !=null ) {
					
						for (Object level3: menu.get(topLevel).get(level2).toArray()) {
							if (matchesRequest((MenuItem)level3,topLevel)) {
								return "toplevelselected";
							}
						}
					}
				}
			}
		}
		return className;
	}

	private boolean matchesRequest(final MenuItem menu,final MenuItem currentLevel) {
		if (matched) {
			return false;
		}
		String basePath = menu.getUrl().getBasePath();
		String reqPath = request.getPath();
		if (!request.getContextPath().equals("")) {
			basePath = basePath.substring(request.getContextPath().length());
		}
		matched = (basePath.startsWith(reqPath) &&
					currentLevel == level1 &&
					reqPath.length() > 1) ;
		return matched;
	}
	
}

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

import java.util.List;

import nl.intercommit.weaves.base.BasicClientElement;
import nl.intercommit.weaves.tabs.TabPanel;

import org.apache.tapestry5.Asset2;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
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
public class Tabs extends BasicClientElement {
	
	@Inject
    private JavaScriptSupport scriptSupport;
	
	@Inject
    private ComponentResources resources;
	
	@Property
	@Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
	
	@Inject @Path("tabs/tabs.js")
	private Asset2 tabJS;
	
	@Inject @Path("tabs/tabs.css")
	private Asset2 tabCSS;
	
	@Property
	@Parameter(required=true)
	private List<TabPanel> tabs;
	
	@Parameter
	private String selected;
	
	@Property
	@Parameter(value="literal:ic_t5-panel")
	private String panelClass;
	
	private TabPanel currenttab;
	
	@SetupRender
	private void initJavaScript() {
		if (!bootstrap) {
			scriptSupport.importJavaScriptLibrary(tabJS);
			scriptSupport.importStylesheet(tabCSS);
		}
	}
	
	@AfterRender
	private void createTabs() {
		if (selected == null) {
			selected = tabs.get(0).getId();
		}
		if (!bootstrap) {
			scriptSupport.addScript("initializeTabs('%s','%s')",new Object[] {getClientId(),selected});
		}
	}
	
	public Block getPanelBlock() {
		return resources.getContainer().getComponentResources().getBlock(currenttab.getBlockid());
	}

	public TabPanel getCurrenttab() {
		return currenttab;
	}

	public void setCurrenttab(TabPanel currenttab) {
		this.currenttab = currenttab;
	}
}


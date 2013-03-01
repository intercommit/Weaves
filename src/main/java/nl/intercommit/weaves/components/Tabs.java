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

import java.util.List;

import nl.intercommit.weaves.base.BasicClientElement;
import nl.intercommit.weaves.tabs.TabPanel;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * @tapestrydoc
 */
@Import(library={"tabs/tabs.js"},stylesheet={"tabs/tabs.css"})
public class Tabs extends BasicClientElement {
	
	@Inject
    private JavaScriptSupport scriptSupport;
	
	@Inject
    private ComponentResources resources;
	
	@Property
	@Parameter(required=true)
	private List<TabPanel> tabs;
	
	@Parameter
	private String selected;
	
	@Property
	@Parameter(value="literal:panel")
	private String panelClass;
	
	private TabPanel currenttab;
	
	@AfterRender
	private void initialize() {
		if (selected == null) {
			selected = tabs.get(0).getId();
		}
		scriptSupport.addScript("initializeTabs('%s','%s')",new Object[] {getClientId(),selected});
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

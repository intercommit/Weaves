package nl.intercommit.tapestry.test.pages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.intercommit.weaves.menu.MenuItem;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

public class DropDownMenuPage {

	private String menutype;
	
	@Inject
	private PageRenderLinkSource prls;
	
	private void onActivate(String menutype) {
		this.menutype = menutype;
	}
	
	
	
	
	
	public Map<MenuItem,Map<MenuItem, List<MenuItem>>> getMenu() {
		
		Map menu = new LinkedHashMap<MenuItem,Map<MenuItem, List<MenuItem>>>();		
		
		if ("full".equals(menutype)) {
			
	
			
			Map<MenuItem,List<MenuItem>> level1 = new LinkedHashMap<MenuItem, List<MenuItem>>();
			
			level1.put(new MenuItem("item1",prls.createPageRenderLink("DropDownMenuPage")), null);
			level1.put(new MenuItem("item2",prls.createPageRenderLink("DropDownMenuPage")), null);
			
			menu.put(new MenuItem("/images/menu1.png","top1",prls.createPageRenderLink("PagedGridPage")),level1);
			
			List<MenuItem> submenu = new ArrayList<MenuItem>();
			submenu.add(new MenuItem("sub1",prls.createPageRenderLink("DropDownMenuPage")));
			submenu.add(new MenuItem("sub2",prls.createPageRenderLink("DropDownMenuPage")));
			submenu.add(new MenuItem("sub3",prls.createPageRenderLink("DropDownMenuPage")));
			
			Map<MenuItem,List<MenuItem>> level2 = new LinkedHashMap<MenuItem, List<MenuItem>>();
			level2.put(new MenuItem("item1",prls.createPageRenderLink("DropDownMenuPage")), null);
			level2.put(new MenuItem("item2",prls.createPageRenderLink("DropDownMenuPage")), submenu);
			
			menu.put(new MenuItem("top2",prls.createPageRenderLink("SwitchPage")),level2);
		}
		return menu;
	}
}

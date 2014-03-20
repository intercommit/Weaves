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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;

/**
 * Generates a series of links used to jump to a particular page index within the overall data set.
 * 
 * TODO: support for zones
 * TODO: should be able to remove the 'row' column ?
 * 
 * @tapestrydoc
 */
@Events({InternalConstants.GRID_INPLACE_UPDATE + " (internal event)","pagesize"})
public class PagedGridPager
{
    @Parameter(required = true)
    private boolean hasNextPage;
    
    /**
     * The number of rows displayed per page.
     */
    @Parameter(required = true)
    private int rowsPerPage;

    /**
     * The current page number (indexed from 1).
     */
    @Parameter(required = true)
    private int currentPage;
    
    @Parameter(required = true)
    private List<Long> pagination;
  
    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;
    
    @Inject
    private AssetSource as;
    
    @Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
    
    void beginRender(final MarkupWriter writer)
    {
    	if (bootstrap) {
    		writeBootStrappedPager(writer);
    	} else {
    		writeTapestryPager(writer);
    	}
    }
    
    /*
     * http://getbootstrap.com/components/#pagination
     */
    private void writeBootStrappedPager(final MarkupWriter writer) {
    	writer.element("ul","class","pagination");
    	
    	 if (currentPage> 1) {
    		 writer.element("li");
    		 writer.element("a", "href",resources.createEventLink(EventConstants.ACTION, currentPage-1));
    		 writer.write("<<");
    		 writer.end();
    		 writer.end();
    	 } else {
    		 writer.element("li","class","disabled");
    		 writer.element("a", "href","#");
    		 writer.write("<<");
    		 writer.end();
    		 writer.end();
    	 }
    	 if (hasNextPage) {
    		 writer.element("li");
    		 writer.element("a", "href",resources.createEventLink(EventConstants.ACTION, currentPage+1));
    		 writer.write(">>");
    		 writer.end();
    		 writer.end();
    	 } else {
    		 writer.element("li","class","disabled");
    		 writer.element("a", "href","#");
    		 writer.write(">>");
    		 writer.end();
    		 writer.end();
    	 }
    	 writer.element("li");
		 writer.element("a", "href",resources.createEventLink(EventConstants.ACTION, currentPage));
		 writer.element("span","class","glyphicon glyphicon-refresh");
		 writer.end();
		 writer.write(" refresh");
		 writer.end();
		 writer.end();
    	 
		 // dropdown pagesize selector..// fuckina
		 writer.element("li","class","dropdown");
		 writer.writeRaw("<a href=\"#\" data-toggle=\"dropdown\" class=\"dropdown-toggle\"><b class=\"caret\"></b> pagesize <span class=\"badge\">"+rowsPerPage+"</span></a>" +
				 "<ul class=\"dropdown-menu\">");
		 for (final Long pagesize: pagination) {
			 final Link link = resources.getContainer().getComponentResources().createEventLink("pagesize",pagesize);
			 writer.writeRaw("<li><a href=\""+link+"\">"+pagesize+"</a></li>");
		 }
		 writer.writeRaw("</ul>");
		 writer.end(); // li
    	 writer.end(); // ul
    }
    
    
    private void writeTapestryPager(final MarkupWriter writer) {
    	
    	writer.element("div", "class", "t-data-grid-pager");

        final Element divElement = writer.element("div", 
        							"class","paged_navigation",
        							"style","float:left");
        
        if (currentPage> 1) {
        	writePageLink(divElement,currentPage-1,"prev_page");
        } else {
        	writePageLink(divElement,-1,"prev_page_disabled");
        }
        
		if (hasNextPage) {
			writePageLink(divElement,currentPage+1,"next_page");
		} else {
			writePageLink(divElement,-1,"next_page_disabled");
		}
		
		writePageLink(divElement,currentPage,"refresh");
		writer.end();
		
		final Element divPagerRows = writer.element("div", "class","paged_rows");
		divPagerRows.raw(messages.get("records_per_page")+" : ");
		
		final Link link = resources.getContainer().getComponentResources().createEventLink("pagesize","$pg$");
		Element select = writer.element("select","class","t-data-grid-pageselect","onchange","changePageSize('"+link.toURI()+"',this);");
		
		for (final Long size: pagination) {
			/*
			 * Generate an eventlink for the container which normally is the org.apache.tapestry5.corelib.components.Grid component 
			 * and generate an event for it, the PagedGrid for example catches this event..
			 */
			if (size.intValue() == rowsPerPage) {
				select = select.element("option","value",""+size,"selected","selected");
			} else {
				select = select.element("option","value", ""+size);
			}
			select.text(""+size);
		}
		writer.end(); // selectbox
		
		writer.end(); // paged_rows div
        writer.end(); // t-data-grid-pager div

	}

    private void writePageLink(final Element element, final int pageIndex,final String display)
    {
    	String hrefLink = "";
    	Link link = null;
    	if (pageIndex != -1) {
    	    link = resources.createEventLink(EventConstants.ACTION, pageIndex);
            hrefLink= link.toString();
    	} else {
    		hrefLink = "#";
    	}
        final Element aElement = element.element("a",
                                     "href", hrefLink,
                                     "title", messages.get(display),
                                     "class",display);
        
        aElement.element("img", "src",as.getClasspathAsset(resources.getBaseResource().getFolder()+"/"+display+".png").toClientURL());
    }

    /**
     * Normal, non-Ajax event handler.
     */
    void onAction(final int newPage) {
        currentPage = newPage;
    }
    
}

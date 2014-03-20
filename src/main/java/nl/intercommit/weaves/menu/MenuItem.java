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
package nl.intercommit.weaves.menu;

import org.apache.tapestry5.Link;

public class MenuItem {

	private String image;
	private String name;
	private Link url;
	

	public MenuItem(String name,Link url) {
		this.image = null;
		this.name = name;
		this.url = url;
	}
	
	public MenuItem(String image,String name,Link url) {
		this.image = image;
		this.name = name;
		this.url = url;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Link getUrl() {
		return url;
	}

	public void setUrl(Link url) {
		this.url = url;
	}
	
	
	
}

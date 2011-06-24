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

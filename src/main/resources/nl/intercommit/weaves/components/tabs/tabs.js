/*  Prototabs
 *  (c) 2007 James Starmer
 *
 *  Prototabs is freely distributable under the terms of an MIT-style license.
 *  For details, see the web site: http://www.jamesstarmer.com/prototabs
 *
/*--------------------------------------------------------------------------*/

function initializeTabs(id,def) {
	new ProtoTabs($(id),{defaultPanel: def});
}


var ProtoTabs = Class.create();
ProtoTabs.prototype = {
	
	initialize: function(element, options) {
		this.options = Object.extend({
			defaultPanel: '',
			ajaxUrls: 			{},
			ajaxLoadingText: 	'Loading...'	
		}, options || {});
		
		this.currentTab = '';
		
		this.element = $(element);
		this.listElements = $A(this.element.getElementsByTagName('LI'));

		//loop over each list element
		for(i = 0; i < this.listElements.length; i++) {	
			
			//get the tabs
			tabLI = this.listElements[i];
			var itemLinks = tabLI.getElementsByTagName('A');
			var id = itemLinks[0].href.split("#")[1];
			
			tabLI.linkedPanel = $(id);
			tabLI.linkedPanel.style.clear = "both";		//make tabs appear below buttons

			//check for the intially active tab
			if((this.options.defaultPanel != '') && (this.options.defaultPanel == id)){
				this.openPanel(tabLI);
			}else{
				$($(tabLI).linkedPanel).hide();
			}
			
			// watch for clicked
			$(itemLinks[0]).observe('click', function(event){
					element = Event.findElement(event, 'LI');
					this.openPanel(element);					
					Event.stop(event); // like return false;
			}.bind(this));
		}
		
	},
	
	openPanel: function(tab){
		tab = $(tab); // ie hack
		
		if(this.currentTab != ''){
			this.currentTab.linkedPanel.hide();
			this.currentTab.removeClassName('weaves-tab-selected');
		}
		
		//set the currently open panel to the new panel
		this.currentTab = tab;
		
		tab.linkedPanel.show();
		tab.addClassName('weaves-tab-selected');
		var url = this.options.ajaxUrls[tab.itemId];
		
		// if there is an ajax url defined update the panel with ajax
		if(url != undefined){
			tab.linkedPanel.update(this.options.ajaxLoadingText);
			new Ajax.Request(url,{
				onComplete: function(transport) {
					tab.linkedPanel.update(transport.responseText);
				}
			});
		}
		
	}
};
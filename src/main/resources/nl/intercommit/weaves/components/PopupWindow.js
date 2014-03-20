
function listenToZoneEvent(zoneName,id) {
	Event.observe($(zoneName),Tapestry.ZONE_UPDATED_EVENT,showWindow.bindAsEventListener(this,zoneName,id));
}

function showWindow(event) {
	
	var popupWindow;
	if (event.target.id == $A(arguments)[1]) {
		popupWindow = $($A(arguments)[2]).win; // find actual window object based on given id
	} else {
		return;
	}
	if (!popupWindow.visible) {
		
		// use prototype functions to figure out the max viewport! and then -20px to be sure..
		popupWindow.options.maxHeight = document.viewport.getHeight() -10;
		popupWindow.options.maxWidth = document.viewport.getWidth() - 20;
		popupWindow.options.showEffect = Element.show;
		popupWindow.options.hideEffect = Element.hide;
		popupWindow.options.effectOptions= { duration: 0.0 };
		popupWindow.oldStyle = 'auto'; // this is a (possible) fix for some weird bug in chenillikit where it takes the overflow style of the previous windows.. which is kinda dumb..
		popupWindow.showCenter(true);
	}
	if (popupWindow.options.autoResize) {
		// resize height only
		// get the zone div
		var contentHeight = popupWindow.getContent().childElements()[0].offsetHeight + popupWindow.heightN + popupWindow.heightS;
		
		if (contentHeight < popupWindow.options.maxHeight &&
				contentHeight != popupWindow.height &&
				popupWindow.getContent().offsetHeight < contentHeight) {
			
			popupWindow.setSize(popupWindow.width,contentHeight,true);	
		}
	}
}




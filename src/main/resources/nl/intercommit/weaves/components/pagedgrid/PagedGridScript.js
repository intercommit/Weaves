var prevClickedRow;
var prevSelectedRow;
var prevSelectedTD;
var hoverAnimation = false;

function listenToCheckAllBox(clientid) {
	
	var checkallButton = $(clientid);
	Element.getStorage(checkallButton); // Fix /hack to re-generate a _prototypeUID to this element after Jquery has messed up when rebuilding the grid
	Event.observe(checkallButton,'click',selectAllCheckBoxes);
	checkallButton.checked = false;
}

function initializeGrid(clientid,maxHeight) {
	
	Event.observe($(clientid),'mouseover',highlightrow);
	
	var $jQ = jQuery.noConflict();
	
	var grid = $jQ("#"+clientid);
	if (grid.height()>maxHeight) {
		grid.chromatable({
			width: grid.width(),
			height: maxHeight,
			scrolling: "yes"
        	});
	}
}

function enableHovering(enabled) {
	hoverAnimation = enabled;
}


function observeExpansionZone() {
	Event.observe($('expansionZone'),Tapestry.ZONE_UPDATED_EVENT,showChildren);
}

function selectAllCheckBoxes(event) {
	
	var checkallBox = Event.element(event);
	var grid = checkallBox.offsetParent.offsetParent;
	if (grid.id == "") {
		// we have the fixed no scrollable header from the Jquery chromatable. select its sibbling
		grid = grid.next();
	}
	var checkboxes = grid.select('.weaves-checkbox');
	
	var value = checkallBox.checked;
	
	for (var index = 0; index < checkboxes.length; ++index) {
		  var checkbox = checkboxes[index];
		  checkbox.checked = value;
	}
}

function highlightrow(event) {
	highlightRowElement(Event.element(event));
}

function highlightRowElement(element) {
	
	/*
	 * maybe for later
	 * 
	if (element.nodeName == 'TD') {
		if (prevSelectedTD !=null) {
			prevSelectedTD.removeClassName('tdSelect');
		}
		prevSelectedTD = element;
		element.addClassName('tdSelect');
	}
	*/
	
	if (element != null) {
		
		while (	!element.hasClassName('weaves-pagedgrid') && 
				!(element.hasClassName('odd') ||  element.hasClassName('even') ||  element.hasClassName('child'))) {
			element = element.up();
		}
		if (element.nodeName == 'TR') {
		
			if (element.rowIndex > 0) {
				if (prevSelectedRow != null) {
					if (hoverAnimation) prevSelectedRow.removeClassName('weaves-rowSelect');
				}
				prevSelectedRow = element;
				if (hoverAnimation) element.addClassName('weaves-rowSelect')
			}
		}
	}
}

function showChildren(event) {
	
	if ($('childrenGrid') != null) {
		var rowArray = $('childrenGrid').select('tbody')[0].childElements();
		var len=rowArray.length;
		for(var i=len; i>=0; i--) {
			var value = rowArray[i];
			prevSelectedRow.insert({after: value});	
		}
		prevSelectedRow.select('div.expander')[0].addClassName('weaves-pagedgrid-hidden');
		prevSelectedRow.select('div.contracter')[0].removeClassName('weaves-pagedgrid-hidden');
	}
}

function closeChildren(event,context) {
	
	var childArray = new Array();
	var row = prevSelectedRow.next('TR');
	var i = 0;
	while (row != null && row.hasClassName('child')) {
		childArray[i++] = row;
		row = row.next('TR');
	}
	// remove child items
	childArray.each(function(item) {
		item.remove();
	});
	
	prevSelectedRow.select('div.expander')[0].removeClassName('weaves-pagedgrid-hidden');
	prevSelectedRow.select('div.contracter')[0].addClassName('weaves-pagedgrid-hidden');
}
/*
 * This is the changepage size implementation of the PagedPager
 */
function changePageSize(uri,selectElement) {
	this.location = uri.sub("$$pg$$",selectElement.value);
}

function clickRow(response) {
	if (prevClickedRow != null) {
		prevClickedRow.removeClassName('rowClicked');
	}
	prevClickedRow = prevSelectedRow;
	prevClickedRow.addClassName('rowClicked');
	
	if(typeof afterRowClick == 'function') {
		afterRowClick(response);
	}
}
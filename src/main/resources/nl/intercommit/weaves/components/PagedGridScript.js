var prevRowColor;
var prevSelectedRow;  

function listenToCheckAllBox() {
	Event.observe($('checkall'),'click',selectAllCheckBoxes);
	$('checkall').checked = false;
}

function observeGrid() {
	Event.observe($('pagedgrid'),'click',highlightrow);
}

function selectAllCheckBoxes(event) {
	
	if (!$('checkall').checked) {
		
		if ($('checkBox') != null) {
			$('checkBox').checked = false;
			var checkboxNumber = '0';
		
			while ($('checkBox_'+checkboxNumber) != undefined) {
				$('checkBox_'+checkboxNumber).checked = false;
				checkboxNumber++;
			}
		}
	} else {
		if ($('checkBox') != null) {
			$('checkBox').checked = true;
			var checkboxNumber = '0';
		
			while ($('checkBox_'+checkboxNumber) != undefined) {
				$('checkBox_'+checkboxNumber).checked = true;
				checkboxNumber++;
			}
		}
	}
}

function highlightrow(event) {
	highlightRowElement(event.target);
}

function highlightRowElement(element) {
	
	if (element != null) {
		
		while (element.offsetParent.hasClassName('t-data-grid') && 
				!(element.hasClassName('odd') ||  element.hasClassName('even'))) {
			element = element.up();
		}
		if (element.nodeName == 'TR') {
		
			if (element.rowIndex > 0) {
				if (prevSelectedRow != null) {
					prevSelectedRow.style.backgroundColor = prevRowColor;
				}
				
				prevRowColor = getstyle(element,'backgroundColor');
				prevSelectedRow = element;
				
				element.style.backgroundColor='#bbbbbb';
				// fire a custom event to indicate to the outside world that this row was clicked
				$(element).fire("pagedgrid:selectrow");
			}
		}
	}
}

/*
 * Override tapestry's ajax call to update the zone, after the selected row has been highlighted..
 */
Tapestry.findZoneManager = function(element)
{
	highlightRowElement(element);
	
    var zoneId = $T(element).zoneId;

    return Tapestry.findZoneManagerForZone(zoneId);
}

function getstyle(obj, cAttribute) {
	if (obj.currentStyle) {
	  this.getstyle = function (obj, cAttribute) {return obj.currentStyle[cAttribute];};
	} else {
	  this.getstyle = function (obj, cAttribute) {return window.getComputedStyle(obj, null)[cAttribute];};
	}
	return getstyle(obj, cAttribute);
}

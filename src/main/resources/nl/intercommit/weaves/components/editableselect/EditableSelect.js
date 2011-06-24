if (!window.EditableSelect)
	var EditableSelect = new Object();

EditableSelect.init = function(inputId,options)
{
  var input = $(inputId);

  var offset = input.getLayout().get('left');
  
  var longestInput = "";
  var dropDownHtml = '<ul id="'+ inputId +'-select" class="select-list" style="left:'+ offset +'px;">';
  var length = options.data.length
  for (var i=0; i<length; i++)
  {
    dropDownHtml += '<li id="'+ inputId +'-option-'+ i  +'" name="'+ inputId +'-options" >'+ options.data[i] +'</li>';
  }
  dropDownHtml += '</ul>';

  var dropDownHeight = 0;
  var dropDownWidth = 0;
  var dropdownMargin = 0;
  
  
  if (options.dropDownImg != null)
  {
    input.insert({after : '<img id="'+ inputId +'-dropdown" src="'+ options.dropDownImg.src +'" class="select-dropdown" />'});
    var inputDropdown = $(inputId + "-dropdown");
    dropDownHeight = input.getHeight();
    dropDownWidth = options.dropDownImg.width * dropDownHeight / options.dropDownImg.height;
    dropDownHeight -= 2;
    dropDownWidth -= 0;
    
    inputDropdown.setStyle( { height : dropDownHeight + "px", width : dropDownWidth + "px", margin : "1px 0 1px -" + dropDownWidth + "px" } );
    inputDropdown.insert({after : dropDownHtml });
    inputDropdown.observe("click",EditableSelect.toggleSelect);
  }
  else
  {
    input.insert({after : dropDownHtml});
  }

  var inputSelect = $(inputId + "-select");
  
  for (var i=0; i<length; i++)
  {
    var option = $(inputId + '-option-' + i);

    option.observe("mouseover",EditableSelect.highlightOption);
    option.observe("mouseout",EditableSelect.unhighlightOption);
    option.observe("mousedown",EditableSelect.selectOption);
  }
  
  var inputWidth = options.width == null ? input.getWidth() : options.width;
  if (options.width || dropDownWidth > 0)
  {
    input.setStyle({ width : (inputWidth + 2*dropDownWidth) + "px" });
  }

  inputSelect.setStyle({ width : (inputWidth + 2*dropDownWidth) + "px" })
  input.observe("focus",EditableSelect.showSelect);
  input.observe("blur",EditableSelect.hideSelect);
  inputSelect.hide();  
}

EditableSelect.selectOption = function(e)
{
  var inputListItem = e.element();
  var fullId = inputListItem.id;
  var input = $(fullId.substr(0,fullId.indexOf("-option-")))
  input.value = inputListItem.innerHTML;
}

EditableSelect.highlightOption = function(e)
{
  var inputListItem = e.element();
  inputListItem.addClassName("highlight");
}

EditableSelect.unhighlightOption = function(e)
{
  var inputListItem = e.element();
  inputListItem.removeClassName("highlight");
}


EditableSelect.toggleSelect = function(e)
{
  var inputDropdown = e.element();
  var length = inputDropdown.id.length - 9; //"-dropdown".length;
  var inputId = inputDropdown.id.substr(0,length);
  EditableSelect.prepareShow(inputId).toggle();

  var input = $(inputId);
  input.focus();
}

EditableSelect.showSelect = function(e)
{
  var input = e.element();
  EditableSelect.prepareShow(input.id).show();
}

EditableSelect.hideSelect = function(e)
{
  var input = e.element();
  var inputId = input.id;
  var inputSelect = $(inputId + "-select");
  inputSelect.hide();
}

EditableSelect.prepareShow = function(inputId)
{
  var inputSelect = $(inputId + '-select');
  return inputSelect;
}


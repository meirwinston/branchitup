function UnitForm(args){
	var _contentPane;
	var _organizationNameInput;
	var _descriptionInput;
	var _addressInput;
	var _phoneNumberInput;
	var _faxNumberInput;
	
	this.getWidget = function(){
		return _contentPane;
	};
	
	this.populate = function(o){
//		console.log("populate Unit");
//		console.log(o);
		if(o.description){
			_descriptionInput.setValue(o.description);
		}
		if(o.ou){
			_organizationNameInput.setValue(o.ou);
		}
		if(o.telephoneNumber){
			_phoneNumberInput.setValue(o.telephoneNumber);
		}
		if(o.address){
			_addressInput.setValue(o.address);
		}
		if(o.facsimileTelephoneNumber){
			_addressInput.setValue(o.facsimileTelephoneNumber);
		}
	};
	
	this.toJson = function(){
		var o = {
			ou: _organizationNameInput.getValue(),
			description: _descriptionInput.getValue(),
			postalAddress: _addressInput.getValue(),
			telephoneNumber: _phoneNumberInput.getValue(),
			facsimileTelephoneNumber: _phoneNumberInput.getValue()
		};
		return o;
	};
	
	function buildHtml(){
		var tr,cell;
		var table = $("<table cellPadding='0' cellSpacing='0' border='0px' ></table>");
		table.css("font-size","14px");
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td></td>").text("Name"));
		tr.append($("<td></td>").append(_organizationNameInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		tr = $("<tr></tr>").css("height","30px");;
		tr.append($("<td></td>").text("Description"));
		tr.append($("<td></td>").append(_descriptionInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		
		tr = $("<tr></tr>").css("height","30px");;
		tr.append($("<td></td>").text("Address"));
		tr.append($("<td></td>").append(_addressInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td></td>").text("Phone Number"));
		tr.append($("<td></td>").append(_phoneNumberInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td></td>").text("FAX Number"));
		tr.append($("<td></td>").append(_faxNumberInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		return table;
	}
	
	function init(){
		dojo.require("dijit.layout.ContentPane");
		dojo.require("dojoui.ValidationTextBox");
//		dojo.require("dojoui.Button");

		_organizationNameInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Name"
		});
		
		_descriptionInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Description"
		});
		
		_addressInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Address"
		});
		
		_phoneNumberInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Phone Number"
		});
		_phoneNumberInput.getWidget().attr("type","password");
		
		_faxNumberInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid FAX Number"
		});
		
		var content = $("<span></span>").append(buildHtml());
		_contentPane = new dijit.layout.ContentPane({
		    id : generateId(),
			region : 'center',
			content : content,
			style : "border: 1px solid #DDDDFF;"
	    });
	}
	init();
}
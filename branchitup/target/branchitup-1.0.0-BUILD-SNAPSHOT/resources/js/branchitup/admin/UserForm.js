function UserForm(args){
	var _contentPane;
	var _givenNameInput;
	var _snInput;
	var _uidInput;
	var _userPasswordInput;
	var _createUserButton;
	
	this.getWidget = function(){
		return _contentPane;
	};
	
	this.populate = function(o){
		console.log("populate");
		console.log(o);
		if(o.sn){
			_snInput.setValue(o.sn);
		}
		if(o.givenName){
			_givenNameInput.setValue(o.givenName);
		}
		if(o.userPassword){
			_userPasswordInput.setValue(o.userPassword);
		}
		if(o.uid){
			_uidInput.setValue(o.uid);
		}
	};
	
	this.toJson = function(){
		var o = {
			givenName: _givenNameInput.getValue(),
			sn: _snInput.getValue(),
			uid: _uidInput.getValue(),
			userPassword: _userPasswordInput.getValue()
		};
		return o;
	};
	
	function buildSitesHtml(){
		var tr,cell;
		var table = $("<table cellPadding='0' cellSpacing='0' border='0px' ></table>");
		table.css("font-size","14px");
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td><input type='checkbox' /></td>"));
		tr.append($("<td><img src='resources/images/logic_or32x32.png' /></td>"));
		tr.append($("<td><a href='http://somesite.domain.com'>http://somesite1.domain.com</a></td>"));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='3'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td><input type='checkbox' /></td>"));
		tr.append($("<td><img src='resources/images/google_g32x35.png' /></td>"));
		tr.append($("<td><a href='http://somesite.domain.com'>http://www.google.com</a></td>"));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='3'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td><input type='checkbox' /></td>"));
		tr.append($("<td><img src='resources/images/Yahoo_Y32x32.png' /></td>"));
		tr.append($("<td><a href='http://somesite.domain.com'>http://www.yahoo.com</a></td>"));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='3'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		return table;
	}
	
	function buildHtml(){
		var tr,cell;
		var table = $("<table cellPadding='0' cellSpacing='0' border='0px' ></table>");
		table.css("font-size","14px");
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td colspan='2'></td>").text("User").css("font-size","18px").css("font-weight","bold"));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td></td>").text("First Name"));
		tr.append($("<td></td>").append(_givenNameInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		tr = $("<tr></tr>").css("height","30px");;
		tr.append($("<td></td>").text("Last Name"));
		tr.append($("<td></td>").append(_snInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		
		tr = $("<tr></tr>").css("height","30px");;
		tr.append($("<td></td>").text("Username"));
		tr.append($("<td></td>").append(_uidInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		
		tr = $("<tr></tr>").css("height","30px");
		tr.append($("<td></td>").text("Password"));
		tr.append($("<td></td>").append(_userPasswordInput.domNode));
		table.append(tr);
		
		tr = $("<tr></tr>");
		cell = $("<td colspan='2'></td>");
		cell.css("height","3px");
		cell.css("width","500px");
		cell.css("background-image","url('resources/images/gradiantLine500x3.png')");
		tr.append(cell);
		table.append(tr);
		
		
//		tr = $("<tr></tr>");
//		tr.append($("<td></td>"));
//		tr.append($("<td></td>").append(_createUserButton.domNode));
//		table.append(tr);
		
		return table;
	}
	
	function init(){
		dojo.require("dijit.layout.ContentPane");
		dojo.require("dojoui.ValidationTextBox");
		dojo.require("dojoui.Button");

		_givenNameInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid First Name"
		});
		
		_snInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Last Name"
		});
		
		_uidInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Username"
		});
		
		_userPasswordInput = new ValidationTextBoxWrapper({
			id: generateId(),
			value: "",
			required: true,
			style: "width: 150px",
//			regExp: "[a-z]+",
			invalidMessage: "Invalid Username"
		});
		_userPasswordInput.getWidget().attr("type","password");
		
		_createUserButton = new dojoui.Button({
			id : generateId(),
			label : "Create New User",
			onClick : function(e) {
				console.log("UserForm.createNewUser");
			}
		});
		
		var content = $("<span></span>").append(buildHtml()).append(buildSitesHtml());
		_contentPane = new dijit.layout.ContentPane({
		    id : generateId(),
			region : 'center',
			content : content,
			style : "border: 1px solid #DDDDFF;"
	    });
	}
	init();
}
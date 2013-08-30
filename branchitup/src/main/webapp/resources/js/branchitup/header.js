dojo.require("dojoui.MenuDropDownChild");

function Header(args){
	this.headerInProcessImgContainer = dojo.byId("headerInProcessImgContainer");
};

Header.prototype.showWaitIcon = function(){
	this.headerInProcessImgContainer.style.visibility = "visible";
}; 

Header.prototype.hideWaitIcon = function(){
	this.headerInProcessImgContainer.style.visibility = "hidden";
};

Header.prototype.register = function(){
	var self = this;
	dojo.connect(branchitup.jsonServlet, "onRequestBegin", function(e){
		//console.log("header.tag onRequestBegin");
		self.showWaitIcon();
	});
	
	dojo.connect(branchitup.jsonServlet, "onRequestComplete", function(e){
		//console.log("header.tag onRequestComplete");
		self.hideWaitIcon();
	});
	
	dojo.connect(branchitup.jsonServlet, "onRequestError", function(e){
		//console.log("header.tag onRequestError");
		self.hideWaitIcon();
	});
};

Header.prototype.showInviteFriendDialog = function(){
	dojo.require("dijit.form.Textarea");
	dojo.require("dijit.form.Form");
	var emailInput = new dijit.form.ValidationTextBox({
		name: 'email',
		required: true,
		trim: true, 
		placeHolder: 'Email Address', 
		maxLength: 150, 
		minLength: 1, 
		style: "width: 400px",
		regExp: Constants.RegularExpression.EMAIL, 
		invalidMessage: 'Invalid email address',
		onChange: function(e){
		}
	});
	var bodyInput = new dijit.form.Textarea({
		name: 'body',
		required: true, 
		trim: true,
		style: "width: 400px",
		placeHolder: 'type in a message', 
		maxLength: 256, 
		minLength: 1,
		value: "I've been using 'Branch It Up' and thought you might like to try it out. Here's an invitation to create an account.",
		'class': 'textarea'
	});
	var t = dojo.create("table");
	var tr,td;
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	
	td.appendChild(dojo.create("label",{innerHTML: "Email Address:"}));
	td.appendChild(dojo.create("br"));
	td.appendChild(emailInput.domNode);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.appendChild(dojo.create("br"));
	td.appendChild(dojo.create("label",{innerHTML: "Your Message:"}));
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.appendChild(bodyInput.domNode);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.style.paddingTop = "10px";
	td.style.color = "red";
//	td.appendChild(bodyInput.domNode);
	var errorCell = td;
	
	var form = new dijit.form.Form({
		method: "get"
	});
	form.domNode.appendChild(t);
	
	function submitInviteFriend(){
		if(form.isValid()){
			form.reset();
			branchitup.jsonServlet.doGet("InviteFriend",form.value,
			function(response){
//				console.log(form);
				dialogManager.closeDialog();
			},
			function(response){
//				console.log(response);
				errorCell.innerHTML = response.message;
			});
		}
		else{
			errorCell.innerHTML = "Invalid Input!";
		}
	}
	
	dialogManager.openDialog({
		title: "Invite A Friend",
		content: form,
		onOK: submitInviteFriend
//		dismiss: true
	});
};

var header = new Header({});

function showPromotionDialog(){
	dialogManager.openDialog({
		title: "Branch It Up message",
		content: "<img src='resources/images/branch_47x50.png' style='float: left; margin-right: 5px; margin-bottom: 5px; ' /><b style='font-size: 14px;'>Welcome to Branch It Up!</b><br/><br/>This is a work in progress site and we are still in the process of populating it with content and establishing a community. We would be delighted to have you be among the first to publish your work!<br/><br/><br/><br/>Thank You<br/>The Branch It Up team",
		style: "width: 400px; height: 300px;",
		acknowledge: true
	});
}

if(user){
	var toolsDropdown = new dojoui.MenuDropDownChild({
		parentDom: dojo.byId("header.toolsContainer")
	});
	toolsDropdown.createMenuItem({
		label: "<img src='resources/images/co-writing_21x20.png' style='margin-right: 5px;' />Invite a friend",
		onClick: header.showInviteFriendDialog
	});
	toolsDropdown.createMenuItem({
		label: "<img src='resources/images/login-icon_17x20.png' style='margin-right: 5px;' />Log Out",
		onClick: function(){
			window.location.href = 'j_spring_security_logout';
		}
	});
	toolsDropdown.createMenuItem({
		label: "<img src='resources/images/user_21x20.png' style='margin-right: 5px;' />Your Account",
		onClick: function(){
			window.location.href = 'edituserprofile';
		}
	});
	
	toolsDropdown.createMenuItem({
		label: "<img src='resources/images/branch_23x24.png' style='margin-right: 5px;' />What Is BranchItUp?",
		onClick: function(){
			window.location.href = 'help';
		}
	});
	
}
else{
	
	
	/*toolsDropdown.createMenuItem({
	label: "<img src='resources/images/user_21x20.png' style='margin-right: 5px;' />Sign Up",
	onClick: function(){
		window.location.href = 'signup';
	}
});
toolsDropdown.createMenuItem({
	label: "<img src='resources/images/login-icon_17x20.png' style='margin-right: 5px;' />Log In",
	onClick: function(){
		window.location.href = 'login';
	}
});*/
}


function onLoad(){
}
dojo.addOnLoad(onLoad);
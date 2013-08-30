dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.HorizontalSlider");
dojo.require("dijit.form.Form");
dojo.require("dojoui.Button");
dojo.require("dojox.form.PasswordValidator");
//var token = null;

var newPasswordInput = new dijit.form.ValidationTextBox({
	name: "newPassword",
	value: "",
	type: "password",
	required: true,
//	style: "width: 300px",
	maxLength: 50,
	minLength: 6,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please type up to 50 characters password!"
});
var confirmPasswordInput = new dijit.form.ValidationTextBox({
	name: "retypePassword",
	value: "",
	type: "password",
	required: true,
//	style: "width: 300px",
	maxLength: 50,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please re-type your new password"
});
function validateForm(){
	var messages = [];
	
	if(newPasswordInput.get("value").length < 5){
		messages.push("Password must contain at least 5 characters");
	}
	if(newPasswordInput.get("value") != confirmPasswordInput.get("value")){
		messages.push("Passwords don't match each other");
	}
	if(messages.length > 0){
		throw messages;
	}
}

function toJson(){
	var o = {
		newPassword: newPasswordInput.get("value"), 
		confirmPassword: confirmPasswordInput.get("value"),
		token: dojo.byId("tokenInput").value
	};
	return o;
}

function submit(){
	try{
		validateForm();
	}
	catch(messages){
		var d = dojo.create("ul");
		for(var i in messages){
			d.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({title: "Invalid Input", content: d, style: "color: red; width: 500px; height: 300px; vertical-align: top;"});
		return;
	}
	var o = toJson();
//	console.log("reset password");
//	console.log(o);
	
	
	branchitup.jsonServlet.doGet("ResetPassword",o,function(response){
//		console.log("reset response");
//		console.log(response);
		document.body.innerHTML = "Your password has been set successfully, to log back in click <a href='login'>here</a>";
	}, function(response){
		if(response['class'] == 'UserPasswordTokenExpiresException'){
			dialogManager.openDialog({
				title: "Request timed out", 
				content: "Your request to reset password has timed out, please submit a new request!", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
		else if(response['class'] == 'NoSuchItemException'){
			dialogManager.openDialog({
				title: "Account Not Found!", 
				content: "The specified account was not found!", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
		else if(response['class'] == 'NonUniqueResultException'){
			dialogManager.openDialog({
				title: "Problem reseting password", 
				content: "There was an internal problem reseting your password, please try again!", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
		else{
			dialogManager.openDialog({
				title: "Problem reseting password", 
				content: "There was an internal problem reseting your password, please try again!", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
	});
}

function onLoad(){
	
	dojo.byId("newPasswordCell").appendChild(newPasswordInput.domNode);
	dojo.byId("confirmPasswordCell").appendChild(confirmPasswordInput.domNode);
}
dojo.addOnLoad(onLoad);
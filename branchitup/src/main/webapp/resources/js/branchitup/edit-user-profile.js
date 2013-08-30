dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.CheckBox");
dojo.require("dijit.form.Textarea");

var imageUploader = new ImageUploader({
	uploadUrl: "service/FileUpload?params={\"album\":\"Default\", \"target\":\"PROFILE\"}",
	onComplete: function(params){
		console.log("edit-user-profile.js - after upload image ");
		console.log(params);
		if(params.length > 0){
			var info = params[0];
			dojo.byId("userprofile.profileImage").src = info.urls[0];
		}
	}
});

//var visibilityInput = new dijit.form.CheckBox({
//	checked: true,
//	name: "visibility"
//});

var firstNameInput = new dijit.form.ValidationTextBox({
	name: "firstName",
	value: "",
	required: true,
	style: "width: 300px",
	maxLength: 50,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please type up to 50 characters name!"
});

var middleNameInput = new dijit.form.ValidationTextBox({
	name: "middleName",
	value: "",
	required: false,
	style: "width: 300px",
	maxLength: 50,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please type up to 50 characters name!"
});

var lastNameInput = new dijit.form.ValidationTextBox({
	name: "lastName",
	value: "",
	required: true,
	style: "width: 300px",
	maxLength: 50,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please type up to 50 characters name!"
});

var genderInput = new dijit.form.FilteringSelect({
	style: "width: 300px;",
	required: true,
	store : new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : [{value: 1.0, name: "Male"}, {value: 0.0, name: "Female"}, {value: 0.5, name: "Unspecified"}]
		}
	})//,
	//value: 0.5
});

var emailInput = new dijit.form.ValidationTextBox({
	name: "email",
	value: "",
	required: true,
	style: "width: 300px",
	maxLength: 200,
	regExp: Constants.RegularExpression.EMAIL,
	invalidMessage: "Please type a valid email address"
});

var newPasswordInput = new dijit.form.ValidationTextBox({
	name: "newPassword",
	value: "",
	type: "password",
	required: true,
	style: "width: 300px",
	maxLength: 50,
	minLength: 6,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please type up to 50 characters password!"
});

var currentPasswordInput = new dijit.form.ValidationTextBox({
	name: "currentPassword",
	value: "",
	type: "password",
	required: true,
	style: "width: 300px",
	maxLength: 50,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please type your current password"
});

var retypePasswordInput = new dijit.form.ValidationTextBox({
	name: "retypePassword",
	value: "",
	type: "password",
	required: true,
	style: "width: 300px",
	maxLength: 50,
	regExp: Constants.RegularExpression.USER_NAME,
	invalidMessage: "Please re-type your new password"
});

var aboutMeInput = new dijit.form.Textarea({
	name: 'aboutMe',
	required: true, 
	trim: true,  
	style: "width: 800px; height: 200px;",
	placeHolder: 'Few words about myself', 
	maxLength: 30000, 
	minLength: 1,
	'class': 'textarea'
});

function submitUserPersonalInfo(){
	var messages = [];
	if(firstNameInput.get("state") != ""){
		messages.push("Invalid First Name");
	}
	if(middleNameInput.get("state") != ""){
		messages.push("Invalid Middle Name");
	}
	if(lastNameInput.get("state") != ""){
		messages.push("Invalid Last Name");
	}
	if(genderInput.get("state") != ""){
		messages.push("Invalid Gender Input");
	}
	if(messages.length == 0){
		var o = {
			firstName: firstNameInput.get("value"),
			middleName: middleNameInput.get("value"),
			lastName: lastNameInput.get("value"),
			gender: genderInput.get("value")
		};
		//console.log(o);
		branchitup.jsonServlet.doGet("UpdateUserProfileInfo",o,function(response){
//			console.log("SubmitUserProfileInfo response");
//			console.log(response);
			
			if(response.totalUpdates > 0){
				dialogManager.openDialog({
					title: "Success",
					content: "Your profile has been updated successfully!",
					acknowledge: true
				});
			}
		});
		
	}
	else{
		var d = dojo.create("ul");
		for(var i in messages){
			d.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Invalid User Information",
			content: d,
			acknowledge: true
		});
	}
}

function submitClearImage(){
//	console.log("submitClearImage");
//	return;
	branchitup.jsonServlet.doGet("ClearUserProfileImage",{},function(response){
		console.log("submitClearImage");
		console.log(response);
		if(response.updatesCount > 0){
			dojo.byId("userprofile.profileImage").src = "resources/images/no-image-branchitup_200x200.png";
		}
		else{
			dialogManager.openDialog({
				title: "No Image to remove",
				content: "The server could not find your profile image, it may have been removed already!",
				acknowledge: true
			});
		}
	},
	function(response){
		dialogManager.openDialog({
			title: "Problem",
			content: "There was a problem clearing your image, your image has not cleared.<br/>Please try again!",
			acknowledge: true
		});
	});
}

function submitNewEmail(){
	var messages = [];
	if(emailInput.get("state") != ""){
		messages.push("Invalid Email Address");
	}
	if(emailInput.get("value") == ""){
		messages.push("Email Address cannot be empty");
	}
	if(messages.length == 0){
		var o = {
			email: emailInput.get("value")
		};
//		console.log("update email");
//		console.log(o);
		branchitup.jsonServlet.doGet("UpdateProfileEmailAddress",o,function(response){
//			console.log("UpdateEmailAddress response");
//			console.log(response);
			if(response.totalUpdates > 0){
				dialogManager.openDialog({
					title: "Success",
					content: "Your profile has been updated successfully!",
					acknowledge: true
				});
			}
		});
	}
	else{
		var d = dojo.create("ul");
		for(var i in messages){
			d.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Invalid User Information",
			content: d,
			acknowledge: true
		});
	}
}

function submitAboutMe(){
	var o = {
		aboutMe: aboutMeInput.get("value")
	};
//	console.log("---submitAboutMe");
//	console.log(o);
//	return ;
//	
	branchitup.jsonServlet.doGet("UpdateProfileAboutMe",o,function(response){
		if(response.totalUpdates > 0){
			dialogManager.openDialog({
				title: "Success",
				content: "Your profile has been updated successfully!",
				acknowledge: true,
				style: "width: 400px; height: 200px;"
			});
		}
	}, function(response){
		dialogManager.openDialog({
			title: response.title,
			content: response.message,
			acknowledge: true
		});
	});
}

function submitPassword(){
	var messages = [];
	if(newPasswordInput.get("state") != ""){
		messages.push("Invalid Password input");
	}
	if(retypePasswordInput.get("state") != ""){
		messages.push("Re-type password input is invalid");
	}
	if(currentPasswordInput.get("state") != ""){
		messages.push("Current password input is invalid");
	}
	if(newPasswordInput.get("value") == ""){
		messages.push("New password cannot be empty");
	}
	if(newPasswordInput.get("value").length < 6){
		messages.push("New password must contain at least 6 characters");
	}
	if(retypePasswordInput.get("value") != newPasswordInput.get("value")){
		messages.push("Password and Re-type passwords do not match");
	}
	if(messages.length == 0){
		var o = {
			newPassword: newPasswordInput.get("value"),
			retypePassword: retypePasswordInput.get("value"),
			currentPassword: currentPasswordInput.get("value")
		};
//		console.log("update password");
//		console.log(o);
		branchitup.jsonServlet.doGet("UpdateProfilePassword",o,function(response){
//			console.log("UpdateProfilePassword response");
//			console.log(response);
			newPasswordInput.reset();
			currentPasswordInput.reset();
			retypePasswordInput.reset();
			dialogManager.openDialog({
				title: "Success",
				content: "Your profile has been updated successfully!",
				acknowledge: true
			});
		}, function(response){
			dialogManager.openDialog({
				title: response.title,
				content: response.message,
				acknowledge: true
			});
		});
	}
	else{
		newPasswordInput.set("value",null);
		currentPasswordInput.set("value",null);
		retypePasswordInput.set("value",null);
		var d = dojo.create("ul");
		for(var i in messages){
			d.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Invalid Password Inputs",
			content: d,
			acknowledge: true
		});
	}
}

function onVisibilityChange(select){
//	console.log("onVisibilityChange");
	console.log(select.value);
	
	branchitup.jsonServlet.doGet("UpdateProfileVisibility",{visibility: select.value},function(response){
//		console.log("UpdateProfileVisibility response");
//		console.log(response);
	},function(response){
		dialogManager.openDialog({
			title: "Problem",
			content: "Your visibility input could not be updated due to connectivity issues. Please try again!",
			acknowledge: true
		});
	});
}

function onLoad(){
//	dojo.byId("userprofile.visibilityInputContainer").appendChild(visibilityInput.domNode);
	dojo.byId("userprofile.firstNameInputContainer").appendChild(firstNameInput.domNode);
	dojo.byId("userprofile.middleNameInputContainer").appendChild(middleNameInput.domNode);
	dojo.byId("userprofile.lastNameInputContainer").appendChild(lastNameInput.domNode);
	dojo.byId("userprofile.genderInputContainer").appendChild(genderInput.domNode);
	dojo.byId("userprofile.emailInputContainer").appendChild(emailInput.domNode);
	dojo.byId("userprofile.currentPasswordInputContainer").appendChild(currentPasswordInput.domNode);
	dojo.byId("userprofile.newPasswordInputContainer").appendChild(newPasswordInput.domNode);
	dojo.byId("userprofile.retypePasswordInputContainer").appendChild(retypePasswordInput.domNode);
	dojo.byId("userprofile.imageUploaderContainer").appendChild(imageUploader.domNode);
	dojo.byId("userprofile.aboutMeInputContainer").appendChild(aboutMeInput.domNode);
	
	//
	genderInput.attr("value",0.5);
}

dojo.addOnLoad(onLoad);
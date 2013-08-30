dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.HorizontalSlider");
dojo.require("dijit.form.Form");
dojo.require("dojoui.Button");
dojo.require("dojox.form.PasswordValidator");
	
function validateForm(){
	var form = signupForm.get("value");
	var messages = [];
	var agreeCheckbox = dojo.byId("signup.agreeCheckbox");
	
	if(!agreeCheckbox.checked){
		messages.push("You must accept the terms of agreement");
	}
//	console.log("--ee-> " + form.email.length + ", " + form.email.state);
	if(form.email.length < 1){
		messages.push("Email Address must be a valid email address");
	}
	if(form.password.length < 6 || form.password.length > 32){
		messages.push("password must be greater than 6 characters and less than 32 characters");
	}
	else if(form.password != form.passwordConfirm){
		messages.push("Passwords do not match");
		valid = false;
	}
	
	if(form.firstName.length < 1 || form.firstName.length > 50){
		messages.push("First name must not be empty and less than 50 characters");
	}
	if(form.lastName.length < 1 || form.lastName.length > 50){
		messages.push("Last name must not be empty and less than 50 characters");
	}
	
	if(messages.length > 0){
		throw messages;
	}
}

function singupUser(){
//	var form = signupForm.get("value");
//	dojo.byId("genderInput").setAttribute("value", genderSlider.get("value"));
	console.log("--->signupUser");
	try{
		validateForm();
	}
	catch(messages){
		var d = dojo.create("ul");
		for(var i in messages){
			d.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({title: "Invalid Form", content: d, style: "color: red; width: 500px; height: 300px; vertical-align: top;"});
		return;
	}
	if(signupForm.isValid()){
		var formValue = signupForm.get("value");
//		console.log("submit:::1");
		dialogManager.openDialog({title: "Creating Account...", content: "Please Wait...<br /><img src='resources/images/animated-wait.gif' />", style: "width: 200px; height: 100px;"});
		
		branchitup.jsonServlet.doGet("SignupUser",formValue,function(response){
//			console.log("submit:::2");
//			console.log(response);
			dojo.byId("j_username").value = formValue.email; 
			dojo.byId("j_password").value = formValue.password;
			dojo.byId("signup.loginForm").submit();
			
		}, function(response){
//			console.log("this is error");
//			console.log(response);
//			return;
			if(response['class'] == "UserAccountException"){
				var ul = dojo.create("ul");
				for(var i in response.message){
					ul.appendChild(dojo.create("li",{innerHTML: response.message[i]}));
				}
				
				dialogManager.openDialog({
					title: "Please fix your inputs",
					content: ul,
					style: "width: 400px; height: 300px;",
					acknowledge: function(){
						dojo.byId("signup.captchaImg").src = "imageservice?target=CAPTCHA&random=" + Math.random(); //to avoid cache
					}
				});
			}
			else{
				dialogManager.openDialog({
					title: "Please fix your inputs",
					content: response.message,
					style: "width: 400px; height: 300px;",
					acknowledge: function(){
						dojo.byId("signup.captchaImg").src = "imageservice?target=CAPTCHA&random=" + Math.random(); //to aviod cache
					}
				});
			}
		});
	}
	else{
		dialogManager.openDialog({title: "Invalid Form", content: dojo.create("span",{innerHTML: "Form contains invalid characters!"}), style: "color: red; width: 300px; height: 200px; vertical-align: top;"});
	}
}

function onLoad(){
}
dojo.addOnLoad(onLoad);
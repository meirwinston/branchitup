//function isFormValid(){
//	if(signupForm.isValid()){
//		
//	}
//}
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.HorizontalSlider");
dojo.require("dijit.form.Form");
dojo.require("dojoui.Button");
dojo.require("dojox.form.PasswordValidator");
	
function validateForm(){
	var valid = true;
	var form = signupForm.get("value");
	
	var userName = dojo.byId("login.userNameErrorNode");
	var firstName = dojo.byId("login.firstNameErrorNode");
	var lastName = dojo.byId("login.lastNameErrorNode");
	var password = dojo.byId("login.passwordErrorNode");
	var repassword = dojo.byId("login.repasswordErrorNode");
	var email = dojo.byId("login.emailErrorNode");
	
	userName.innerHTML = "";
	password.innerHTML = "";
	repassword.innerHTML = "";
	firstName.innerHTML = "";
	lastName.innerHTML = "";
	email.innerHTML = "";
	
	if(form.userName.length < 6 || form.userName.length > 32){
		userName.innerHTML = "username must be greater than 6 characters and less than 32 characters";
		valid = false;
	}
	if(form.password.length < 6 || form.password.length > 32){
		password.innerHTML = "password must be greater than 6 characters and less than 32 characters";
		repassword.innerHTML = "";
		valid = false;
	}
	else if(form.password != form.passwordConfirm){
		password.innerHTML = "passwords do not match";
		repassword.innerHTML = "please re-type passwords";
		valid = false;
	}
	
	if(form.firstName.length < 1 || form.firstName.length > 32){
		firstName.innerHTML = "First name must not be empty and less than 32 characters";
		valid = false;
	}
	if(form.lastName.length < 1 || form.lastName.length > 32){
		lastName.innerHTML = "Last name must not be empty and less than 32 characters";
		valid = false;
	}
	
	if(form.email.length < 1 || emailAddressInput.state != ""){
		email.innerHTML = "invalid Email address";
		valid = false;
	}
	
	if(!valid){
		throw "Invalid Form";
	}
}

function singupUser(){
//	dojo.byId("genderInput").setAttribute("value", genderSlider.get("value"));
	validateForm();
	if(signupForm.isValid()){
		var formValue = signupForm.get("value");
		formValue.gender = genderSlider.get("value");
		
		branchitup.jsonServlet.doGet("SignupUser",formValue,function(response){
			if(!response.success){
				var m = dojo.byId("messageCell");
				m.innerHTML = "";
				m.style.display = "";
				for(var k in response.messages){
					m.innerHTML += response.messages[k] + "<br/>";
				}
			}
			else{
				window.location.href = "home";
			}
		});
	}
}

function onLoad(){
}
dojo.addOnLoad(onLoad);
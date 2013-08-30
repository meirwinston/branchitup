dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.HorizontalSlider");
dojo.require("dijit.form.Form");
dojo.require("dojoui.Button");
dojo.require("dojox.form.PasswordValidator");

function validateForm(){
	var messages = [];
	
	if(emailInput.get("value").length < 1){
		messages.push("Email Address must be a valid email address");
	}
	if(messages.length > 0){
		throw messages;
	}
}

function toJson(){
	var o = {
		email: emailInput.get("value")
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
//	console.log("restore password");
//	console.log(o);
	
	
	branchitup.jsonServlet.doGet("RestorePassword",o,function(response){
//		console.log("restore response");
//		console.log(response);
		
		window.location.href = "passwordack";
		
//		dojo.byId("restorepassword.messagesCell").innerHTML = "<label style='color: blue; text-align: left;'>Your password has been sent to your email, please check your email</label>";
//		dojo.byId("restorepassword.submitButton").className = "disabledButtonImg";
//		dojo.byId("restorepassword.submitButton").onclick = null;
	}, function(response){
		dialogManager.openDialog({
			title: response.message.title, 
			content: response.message.body, 
			style: "width: 400px; height: 200px;",
			acknowledge: true
		});
	});
}

function onLoad(){
}
dojo.addOnLoad(onLoad);
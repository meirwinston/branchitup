dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.Form");

var commentsInput = new dijit.form.Textarea({
	name : 'comments',
	required : true,
	trim : true,
	style: "width: 600px",
	placeHolder : 'Type in comments up to 1024 characters',
	maxLength : 1024,
	minLength : 1,
	class : 'textarea'
});

var challengeInput = new dijit.form.ValidationTextBox({
	name: 'challenge',
	required: true,
	trim: true,
	placeHolder: 'Enter challenge',
	style: "width: 300px",
	maxLength: 8,
	minLength: 1,
	regExp: Constants.RegularExpression.ENGLISH_NAME,
	invalidMessage: 'Please enter the correct challenge'
});


var subjectInput = new dijit.form.ValidationTextBox({
	name: 'subject',
	required: true,
	trim: true,
	placeHolder: 'Subject',
	style: "width: 300px",
	maxLength: 50,
	minLength: 1,
	regExp: Constants.RegularExpression.ENGLISH_NAME,
	invalidMessage: 'Name must begin with a letter and contain alphanumeric and numeric characters only'
});

var emailInput = new dijit.form.ValidationTextBox({
	name: "email",
	value: "",
	required: true,
	autocomplete: 'on',
	placeHolder: 'Your Email Address',
	style: "width: 300px",
	maxLength: 200,
	regExp: Constants.RegularExpression.EMAIL,
	invalidMessage: "Please type a valid email address"
});

function validateForm() {
	var messages = [];
	if(subjectInput.get("state") != ""){
		messages.push("Invalid subject");
	}
	if(emailInput.get("state") != ""){
		messages.push("Invalid email address");
	}
	if(challengeInput.get("state") != "" || challengeInput.get("value").length <= 0){
		messages.push("Invalid challenge");
	}
	if(commentsInput.get("value").length <= 0){
		messages.push("Comments field cannot be empty");
	}
	if (messages.length > 0) {
		throw messages;
	}
};

function toJson() {
	
	var o = {
		subject : subjectInput.getValue(),
		comments : commentsInput.get("value"),
		email: emailInput.get("value"),
		challenge: challengeInput.get("value")
	};
	return o;
}


function submitComments() {
//	console.log("contactus.submitComments:::");
	try{
		validateForm();
		var o = toJson();
		branchitup.jsonServlet.doGet("ContactUs",o,
		function(response) {
			dialogManager.openDialog({
				title: "Message Sent",
				content: "Thank you for your message",
				acknowledge: function(){
					window.location.href = "home";
				}
			});
		},
		function(response) {
			if(response['class'] == "InvalidInputException"){
				var ul = dojo.create("ul");
				for(var i in response.message){
					ul.appendChild(dojo.create("li",{innerHTML: response.message[i]}));
				}
//				console.log("contact-us.response ");
//				console.log(response);
				dialogManager.openDialog({
					title: "Problem processing your request",
					content: ul,
					style: "width: 400px; height: 300px;",
					acknowledge: function(){
						dojo.byId("contactus.captchaImg").src = "imageservice?target=CAPTCHA&random=" + Math.random(); //to avoid cache
					}
				});
			}
			
		});
		
		dialogManager.openDialog({
			title: "Message Sent",
			content: "Thank you, you may press OK to go back to homepage",
			acknowledge: function(){
				window.location.href = "home";
			}
		});
	}
	catch(messages){
//		console.log(messages);
		var ul = dojo.create("ul");
		for ( var i in messages) {
			ul.appendChild(dojo.create("li", {
				innerHTML : messages[i]
			}));
		}
		dialogManager.openDialog({
			title : "Invalid Input",
			content : ul,
			acknowledge: true
		});
	}
}

function onLoad() {
	dojo.byId("contactus.subjectInputContainer").appendChild(subjectInput.domNode);
	dojo.byId("contactus.commentsInputContainer").appendChild(commentsInput.domNode);
	dojo.byId("contactus.emailInputContainer").appendChild(emailInput.domNode);
	dojo.byId("contactus.challengeCell").appendChild(challengeInput.domNode);
	
}
dojo.addOnLoad(onLoad);

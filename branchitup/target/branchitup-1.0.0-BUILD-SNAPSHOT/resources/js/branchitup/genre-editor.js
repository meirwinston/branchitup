dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.Form");

var genreImage = dojo.byId("genreeditor.genreImage");
//var genreId = null;

var uploadButton = new ImageUploader(
{
	uploadUrl : "service/FileUpload?params={\"method\":\"html5\",\"album\":\"Default\", \"target\":\"SESSION\"}",
	onComplete : function(params) {

		var url = "imageservice?target=SESSION&id=" + params[0].id;
		genreImage.src = url;
		// genreImage.setAttribute("uploadId",params[0].id);
		fileItemIdInput.set("value", params[0].id);
	}
});

var descriptionInput = new dijit.form.Textarea({
	name : 'description',
	required : true,
	trim : true,
	placeHolder : 'Type in genre description',
	maxLength : 1024,
	minLength : 1,
	class : 'textarea'
});

// var nameInput = new dijit.form.ValidationTextBox({
// name: 'name',
// required: true,
// trim: true,
// placeHolder: 'type in genre name',
// maxLength: 32,
// minLength: 1,
// regExp: Constants.RegularExpression.ENGLISH_NAME,
// invalidMessage: 'Name must begin with a letter and contain alphanumeric and
// numeric characters'
// });

var nameInput = new GenresDropdown({
	onChange: function(o){
//		console.log("GenreDropdown, this is my onchange function");
//		console.log(o);
		if(o && o.item && o.item.id){
//			window.location.href = "genre?genreId=" + o.item.id;
//			genreId = o.item.id;
			descriptionInput.set("disabled",true);
			dojo.byId("genereeditor.submitButton").onclick = null;
			
			branchitup.jsonServlet.doGet("GetGenreDetails",{genreId: o.item.id},
				function(response) {
//					console.log("genre description response");
//					console.log(response);
					descriptionInput.set("value",response.description);
					dojo.byId("genereeditor.submitButton");
					dojo.byId("genreeditor.genreImage").setAttribute("src",response.iconImageUrl);
					
					descriptionInput.set("disabled",false);
					dojo.byId("genereeditor.submitButton").onclick = submitGenre;
				},
				function(response){
					dialogManager.openDialog({
						title : "Problem Accessing Genre",
						content : "Please refresh the page and try again"
					});
				}
			);
		}
		
	},
	onTextChange: function(value){
//		console.log("by phrase---** " + value);
	}
});

function validateForm() {
	// console.log("validateForm");
	// console.log(genreForm);
	var messages = [];
	if (nameInput.getItem() == null) {
		var val = nameInput.getValue();
		if(val == ""){
			messages.push("Genre name cannot be empty");
		}
		if(val.length > 50){
			messages.push("Length exceeds limit, please type a genre name with a maximum 50 of alphanumeric characters.");
		}
		var regexp = new RegExp(Constants.RegularExpression.ENGLISH_NAME);
		if(!regexp.test(val)){
			messages.push("The name must be entered in English");
		}
	}
	if (descriptionInput.get("value").length < descriptionInput.get("minLength")) {
		messages.push("Description is invalid, please type a description with a maximum of 1024 alphanumeric characters.");
	}
	console.log("999");
	console.log(messages);
	if (messages.length > 0) {
		throw messages;
	}
};

function toJson() {
	var o = {
		name : nameInput.getValue(),
		description : descriptionInput.get("value")
	};
	if (fileItemIdInput.get("value") != null
			&& fileItemIdInput.get("value") != "") {
		o.fileItemId = fileItemIdInput.get("value");
	}
	if (nameInput.getItem() != null) {
		o.genreId = nameInput.getItem().id;
	}
	return o;
}

function updateGenre() {
	if (!nameInput.getItem())
		return;
	try {
		validateForm(); 
		var o = toJson();
		o.genreId = nameInput.getItem().id;
		
//		console.log("GenreEditor.updateGenreForm:::");
//		console.log(o);
//		return;
		
		branchitup.jsonServlet.doGet("UpdateGenre",o,
		function(response) {
			// console.log("UpdateGenre response: ");
			// console.log(response);
			if (response.updatesCount > 0) {
				window.location.href = ("genre?genreId=" + response.genreId);
			} else {
				dialogManager.openDialog({
					title : "Problem Saving Genre",
					content : "The changes you made did not complete successfully. Please try again!"
				});
			}
		});
	} catch (messages) {
//		console.log("genre-editor. exp: messages: ");
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

function test() {
	console.log("GenreEditor.test:::");
	return;

}

function submitGenre() {
	// console.log("GenreEditor.submitForm:::");
	// return;
	
	if (nameInput.getItem()) {
		updateGenre();
	} else {
		newGenre();
	}
}

function newGenre() {
	try {
		validateForm();
		var o = toJson();
		
//		 console.log("GenreEditor.submitNewGenreForm:::");
//		 console.log(o);
//		 return;
		branchitup.jsonServlet.doGet("NewGenre", o, function(response) {
			// console.log("NewGenre response: ");
			// console.log(response);
			if (response.genreId) {
				window.location.href = ("genre?genreId=" + response.genreId);
			} else {

			}
		});
	} catch (messages) {
		var a = {
			title : "Invalid Input",
			content : "Genre must have an alphanumeric name and description.<br/>Image is optional."
		};
		dialogManager.openDialog(a);
	}
};

// try{
// uploadButton = new dojoui.UploadButton(uploaderArgs);
// dojo.byId("genreeditor.uploadButton").parentNode.replaceChild(uploadButton.domNode,dojo.byId("genreeditor.uploadButton"));
// }
// catch(e){
// console.log("Exception in creating flash plugin.");
// console.log(uploadButton);
// console.log(e);
// }

function onLoad() {
	// console.log("this is genreID " + genreId);
	// if(!genreId){
	dojo.byId("genreeditor.uploadButton").parentNode.replaceChild(
			uploadButton.domNode, dojo.byId("genreeditor.uploadButton"));
	// }

	dojo.byId("genreeditor.nameInputContainer").appendChild(nameInput.domNode);
	dojo.byId("genreeditor.descriptionInputContainer").appendChild(
			descriptionInput.domNode);
}
dojo.addOnLoad(onLoad);

dojo.require("dijit.form.Textarea");
dojo.require("dojo.data.ItemFileReadStore");

var bookId = null;
var languageInput;

//---FROM book editor
var imageUploader = new ImageUploader({
	uploadUrl: "service/FileUpload?params={\"method\":\"html5\",\"album\":\"Default\", \"target\":\"SESSION\"}",
	onComplete: function(params){
//		console.log("onComplete");
//		console.log(params);//[[""]]
		var url = "imageservice?target=SESSION&id=" + params[0].id;
//		console.log(url);
		
		dojo.byId("publishbook.coverImage").src = url;
		dojo.byId("publishbook.coverImage").setAttribute("uploadId",params[0].id);
	}
});

//-------

var primaryGenreDropdown = new ItemsDropdown({
	inputArgs: {
		required: true,
		trim: true, 
//		placeHolder: 'type in genre', 
		maxLength: 50, 
		minLength: 1, 
//		style: "width: 300px",
		regExp: Constants.RegularExpression.ENGLISH_NAME, 
		invalidMessage: 'Name must be in English'
	},
	onChange: function(o){
//		console.log("GenreDropdown, this is my onchange function");
//		console.log(o);
		if(o && o.item && o.item.id){
//			window.location.href = "genre?genreId=" + o.item.id;
		}
		
	}
});

primaryGenreDropdown.select = function(){
//	console.log("GenresDropdown:select:1:");
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["GENRE"], offset: 0, maxResults: 4},function(response){
		self.closeMenu();
		self._repositionMenu();
//		console.log("GenresDropdown:select:1:response");
//		console.log(response);
		
		self.populate(response.items);
	});
};


var secondaryGenreDropdown = new ItemsDropdown({
	inputArgs: {
		required: false,
		trim: true, 
//		placeHolder: 'type in genre', 
		maxLength: 50, 
		minLength: 1, 
//		style: "width: 300px",
		regExp: Constants.RegularExpression.ENGLISH_NAME, 
		invalidMessage: 'Name must be in English'
	},
	onChange: function(o){
//		console.log("GenreDropdown, this is my onchange function");
//		console.log(o);
		if(o && o.item && o.item.id){
//			window.location.href = "genre?genreId=" + o.item.id;
		}
		
	}
});

secondaryGenreDropdown.select = function(){
//	console.log("GenresDropdown:select:1:");
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["GENRE"], offset: 0, maxResults: 4},function(response){
		self.closeMenu();
		self._repositionMenu();
//		console.log("GenresDropdown:select:1:response");
//		console.log(response);
		
		self.populate(response.items);
	});
};

var publisherCommentInput = new dijit.form.Textarea({
	name: 'publisherComment',
	required: true, 
	trim: true, 
	placeHolder: 'type in a more about your role', 
	maxLength: 512, 
	minLength: 0, 
	"class": 'textarea'
});

function initLanguageInput(languages){
	languageInput = new dijit.form.FilteringSelect({
		style: "width: 150px;",
		required: true,
		store : new dojo.data.ItemFileReadStore( {
			data : {
				identifier : "value",
				items : languages
			}
		}),
		value: "ENG"
	});
	
//	dojo.byId("publishbook.languageCell").appendChild(languageInput.domNode);
};
function onAllowBranchingChange(){
	var b = !dojo.byId("publishbook.allowBranching").checked;
	
//	dojo.byId("deficiency.illustrating").disabled = b;
//	dojo.byId("deficiency.translating").disabled = b;
//	dojo.byId("deficiency.editing").disabled = b;
//	dojo.byId("deficiency.coauthoring").disabled = b;
//	dojo.byId("deficiency.proofreading").disabled = b;
	var value = b ? "hidden" : "visible";
	
	dojo.byId("deficiency.listTitleRow").style.visibility = value;
	dojo.byId("deficiency.illustratingRow").style.visibility = value;
	dojo.byId("deficiency.translatingRow").style.visibility = value;
	dojo.byId("deficiency.editingRow").style.visibility = value;
	dojo.byId("deficiency.coauthoringRow").style.visibility = value;
	dojo.byId("deficiency.proofreadingRow").style.visibility = value;
}

function validateRoles(messages){
	if(!dojo.byId("role.author").checked && 
	!dojo.byId("role.illustrator").checked && 
	!dojo.byId("role.translator").checked && 
	!dojo.byId("role.editor").checked && 
	!dojo.byId("role.proofreader").checked){
		messages.push("You did not specify your role in this publication.");
		return false;
	}
	
	return true;
}
function validateGenres(messages){
	var success = true;
	if(!primaryGenreDropdown.getItem() && (!primaryGenreDropdown.getValue() || primaryGenreDropdown.getValue() == "")){
		messages.push("You must specify a genre, you can search for existing genres <a href='genres'>here</a>, or create a new one by typing in a genre name");
		success = false;
	}
	else if(secondaryGenreDropdown.getItem()){
//		console.log("SECONDARY::: ");
//		console.log(secondaryGenreDropdown);
		if(primaryGenreDropdown.getItem().id == secondaryGenreDropdown.getItem().id){
			messages.push("Your secondary and primary genres cannot be the same");
			success = false;
		}
		else if(primaryGenreDropdown.getValue() == secondaryGenreDropdown.getValue()){
			messages.push("Your secondary and primary genres cannot be the same");
			success = false;
		}
	}
	else if(primaryGenreDropdown.textbox.state != "" || secondaryGenreDropdown.textbox.state){
		success = false;
	}
	return success;
}
function validate(){
	var messages = [];
	if(!bookId){
		messages.push("There is no book associated with this page, please refresh the page again!");
	}
	if(!languageInput.get("value")){
		messages.push("Language must be specified");
	}
	validateGenres(messages);
	
//	if(!primaryGenreDropdown.getItem()  && (!primaryGenreDropdown.getValue() || primaryGenreDropdown.getValue() == "")){
//		messages.push("You must specify a genre, if you found no appropriate genre to your book you can create one: <a href='genreeditor'>New Genre</a>");
//	}
//	else if(secondaryGenreDropdown.getItem()){
////		console.log("SECONDARY::: ");
////		console.log(secondaryGenreDropdown);
//		if(primaryGenreDropdown.getItem().id == secondaryGenreDropdown.getItem().id){
//			messages.push("Your secondary and primary genres cannot be the same");
//		}
//	}
//	if(!dojo.byId("role.author").checked && 
//	!dojo.byId("role.illustrator").checked && 
//	!dojo.byId("role.translator").checked && 
//	!dojo.byId("role.editor").checked && 
//	!dojo.byId("role.proofreader").checked){
//		messages.push("You did not specify your role in this publication.");
//	}
	validateRoles(messages);
	if(messages.length > 0){
		throw messages;
	}
}

function toJson(){
	if(!bookId) return null;
	
	var o = {
		bookId: bookId,
		genreIds: [],
		genreNewValues: []
	};
	
	//from book-editor
	var coverUploadId = dojo.byId("publishbook.coverImage").getAttribute("uploadId");
	if(coverUploadId){
		o['coverUploadId'] = coverUploadId;
	}
	
	var coverImageFileId = dojo.byId("publishbook.coverImage").getAttribute("coverImageFileId");
	if(coverImageFileId){
		o['coverImageFileId'] = coverImageFileId;
	}
	
	o['bookLanguage'] = languageInput.get("value");
	if(primaryGenreDropdown.getItem()){
		o.genreIds.push(primaryGenreDropdown.getItem().id);
	}
	else{
		o.genreNewValues.push(primaryGenreDropdown.getValue());
	}
	if(secondaryGenreDropdown.getItem()){
		o.genreIds.push(secondaryGenreDropdown.getItem().id);
	}
	else{
		o.genreNewValues.push(secondaryGenreDropdown.getValue());
	}
	if(publisherCommentInput.attr("value")){
		o.publisherComment = publisherCommentInput.attr("value");
	}
	o.allowBranching = dojo.byId("publishbook.allowBranching").checked;
	var deficiency = {
		illustrating: dojo.byId("deficiency.illustrating").checked,
		translating: dojo.byId("deficiency.translating").checked,
		coauthoring: dojo.byId("deficiency.editing").checked,
		editing: dojo.byId("deficiency.coauthoring").checked,
		proofreading: dojo.byId("deficiency.proofreading").checked,
	};
	o.deficiency = deficiency;
	var publisherRoles = {
		author: dojo.byId("role.author").checked,
		illustrator: dojo.byId("role.illustrator").checked,
		translator: dojo.byId("role.translator").checked,
		editor: dojo.byId("role.editor").checked,
		proofreader: dojo.byId("role.proofreader").checked
	};
	o.publisherRoles = publisherRoles;
	
	return o;
}
function buildSummaryNode(){
//	console.log("buildSummaryNode");
//	console.log(item);
	//"You are about to publish your book!<br/><br/>Please make sure that all the information is correct before proceeding.<br/><br/>If it is all correct, please click “Publish” to publish your book now to BranchitUp.com",
	var t = dojo.create("table", {border: "0", style: "padding: 5px;"});
	var tr,td;
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "Language of the book";
	td = tr.insertCell(tr.cells.length);
	td.innerHTML = languageInput.get("displayedValue");
	
	//--
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "Genre";
	td = tr.insertCell(tr.cells.length);
	td.innerHTML = primaryGenreDropdown.getValue("value");
	
	if(secondaryGenreDropdown.getValue("value").length > 0){
		td.innerHTML += (", " + secondaryGenreDropdown.getValue("value"));
	}
	//--
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "Your role/s";
	td = tr.insertCell(tr.cells.length);
	if(dojo.byId("role.author").checked){
		td.innerHTML = "Writer";
	}
	if(dojo.byId("role.illustrator").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML += "Illustrator";
	}
	if(dojo.byId("role.translator").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML += "Translator";
	}
	if(dojo.byId("role.editor").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
			td.innerHTML += "Editor";
		}
	}
	if(dojo.byId("role.proofreader").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML += "Proof-reader";
	}
	
	//--

	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "Allow users to branch this book";
	td = tr.insertCell(tr.cells.length);
	if(dojo.byId("publishbook.allowBranching").checked){
		td.innerHTML = "Yes";
	}
	else{
		td.innerHTML = "No";
	}
	
	//--
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.className = "blueHeading4";
	td.innerHTML = "This book requires";
	td = tr.insertCell(tr.cells.length);
	
	if(dojo.byId("deficiency.illustrating").checked){
		td.innerHTML = "Illustration";
	}
	if(dojo.byId("deficiency.translating").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML += "Translation to other language";
	}
	if(dojo.byId("deficiency.editing").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML += "Editing";
	}
	if(dojo.byId("deficiency.coauthoring").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML += "Writer/Co-author";
	}
	if(dojo.byId("deficiency.proofreading").checked){
		if(td.innerHTML.length > 0){
			td.innerHTML += ", ";
		}
		td.innerHTML = "Proof reading";
	}
	
	//--
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.colSpan = 2;
	td.innerHTML = "Please make sure that all the information is correct before proceeding.<br/>If it is all correct, please click “Publish” to publish your book now to BranchitUp.com.  Once you have published your work, you will not be able to make any changes without branching it into another version. You may only delete the publication as long as it has not been branched.";
	
	return t;
}

function submit(){
//	console.log("publish-book submit");
	dialogManager.openDialog({title: "Publishing...", content: "Please Wait...<br /><img src='resources/images/animated-wait.gif' />", style: "width: 200px; height: 100px;"});
	var o = toJson();
//	console.log(o);
//	return;
	
	branchitup.jsonServlet.doGet("PublishBook",o,function(response){
		console.log("PublishBook:response::");
		console.log(response);
//		window.location.href = "home";
//		return;
		dialogManager.openDialog({
			title: "Success!", 
			content: "Thank you for sharing your book with us!<br/>Your book will be available shortly. You may share this link to your book on facebook:<br/><a href='publishedbook?bookId=" + response.bookId + "'>" + response.url + "</a>", 
			style: "width: 450px; height: 200px;",
			acknowledge: function(){
				window.location.href = "home";
			}
		});
	},function(response){
		dialogManager.openDialog({
			title: "Publish Book did not succeed",
			content: "There was a problem during the publishing process. Please try again!",
			acknowledge: true
		});
	});
}

function showSubmitDialog(){
	try{
		validate();
//		var o = toJson();
//		console.log("submit");
//		console.log(o);
		dialogManager.openDialog({
			title: "Ready to Publish?",
			style: "width: 600px; height: 300px;",
			content: buildSummaryNode(),
			inProcess: false,
			onOK: function(){
				if(!this.inProcess){
					this.inProcess = true;
					submit();
				}
			},
			okLabel: "Publish",
			onCancel: function(){
				publishStep6();
			}
		});
	}
	catch(messages){
		var d = dojo.create("ul");
		for(var i in messages){
			d.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Please correct your input",
			content: d
		});
	}
}

function openNewGenreDialog(){
	window.open("genreeditor");
}

//--- WIZARD

function createStepsNavigation(step){
	var d = dojo.create("div",{style: "padding-bottom: 20px;"});
	for(var i = 0 ; i < 5 ; i++){
		if(step != (i+1)){
			d.appendChild(dojo.create("label",{innerHTML: "Step " + (i+1), "class": "blueHeading4"}));
		}
		else{
			d.appendChild(dojo.create("label",{innerHTML: "Step " + (i+1), "class": "blueHeading4", style: "color: orange;"}));
		}
		if(i < 4){
			d.appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;&nbsp;>&nbsp;&nbsp;&nbsp;", "class": "blueHeading4"}));
		}
		
	}
	return d;
}
function publishStep6(){
	var c = dojo.byId("publishbook.wizardContainer");
	var dom = dojo.create("span");
	dom.appendChild(createStepsNavigation(5));
	dom.appendChild(dojo.create("br"));
	
	dom.appendChild(dojo.create("label",{innerHTML: "Permissions", "class": "heading3"}));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.byId("publishbook.definiencyList"));
	
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/back-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/back-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/back-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(dom);
			publishStep5();
		}
	}));
	dom.appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;&nbsp;"}));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/publish-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/publish-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/publish-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(dom);
			showSubmitDialog();
			c.innerHTML = "";
		}
	}));
	
	c.innerHTML = "";
	c.appendChild(dom);
}
function publishStep5(){
	var c = dojo.byId("publishbook.wizardContainer");
	var dom = dojo.create("span");
	dom.appendChild(createStepsNavigation(4));
	dom.appendChild(dojo.create("br"));
	
	dom.appendChild(dojo.create("label",{innerHTML: "Your role in this publication:&nbsp;&nbsp;&nbsp;", "class": "heading3"}));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.byId("publishbook.userRole"));
	

	dom.appendChild(dojo.create("br"));
	var messageCell = dojo.create("label",{style: "color: red;"});
	dom.appendChild(messageCell);
	
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/back-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/back-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/back-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(dom);
			publishStep4();
		}
	}));
	dom.appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;&nbsp;"}));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/next-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/next-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/next-button_115x25.png'",
		onclick: function(){
			var messages = [];
			if(validateRoles(messages)){
				dojo.byId("publishbook.hidden").appendChild(dom);
				publishStep6();
			}
			else{
				if(messages.length > 0){
					messageCell.innerHTML = messages[0];
				}
				else{
					messageCell.innerHTML = "At least one role has to be specified";
				}
			}
		}
	}));
	
	c.innerHTML = "";
	c.appendChild(dom);
}

function publishStep4(){
	var c = dojo.byId("publishbook.wizardContainer");
	var t = dojo.create("table");
	var tr,td;
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.colSpan = 2;
	td.appendChild(createStepsNavigation(3));
	td = tr.insertCell(tr.cells.length);
	
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.appendChild(dojo.create("label",{innerHTML: "Genres:&nbsp;&nbsp;&nbsp;", "class": "heading3"}));
	td.appendChild(branchitup.createTooltipLabel("<b>?</b>","You have the option of attributing this publication to two genres.  The primary genre is mandatory.  The secondary genre is optional.  Start typing the genre name and select the most appropriate name from the dropdown list.  If the genre you are selecting does not appear in the dropdown list, it means it has not yet been created.  The genre you create will automatically be added as a genre option in the system once your work has been published.   You have the option of returning to the list of genres to provide a description of this new genre after you complete the publication process."));
	td = tr.insertCell(tr.cells.length);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.appendChild(dojo.create("label",{innerHTML: "Primary Genre:&nbsp;&nbsp;&nbsp;"}));
	td = tr.insertCell(tr.cells.length);
	td.appendChild(primaryGenreDropdown.domNode);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.appendChild(dojo.create("label",{innerHTML: "Secondary Genre:&nbsp;&nbsp;&nbsp;"}));
	td = tr.insertCell(tr.cells.length);
	td.appendChild(secondaryGenreDropdown.domNode);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.style.color = "red";
	td.colSpan = 2;
	var messageCell = td;
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.colSpan = 2;
	
	td.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/back-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/back-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/back-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(t);
			publishStep3();
		}
	}));
	td.appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;&nbsp;"}));
	td.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/next-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/next-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/next-button_115x25.png'",
		onclick: function(){
			var messages = [];
			if(validateGenres(messages)){
				dojo.byId("publishbook.hidden").appendChild(t);
				publishStep5();
			}
			else{
				if(messages.length > 0){
					messageCell.innerHTML = messages[0];//"Primary genre must be specified with a valid value";
				}
				else{
					messageCell.innerHTML = "Primary genre must be specified with a valid value";
				}
			}
		}
	}));
	
	
	c.innerHTML = "";
	c.appendChild(t);
}

function publishStep3(){
	var c = dojo.byId("publishbook.wizardContainer");
	var dom = dojo.create("span");
	dom.appendChild(createStepsNavigation(2));
	dom.appendChild(dojo.create("br"));
	
	dom.appendChild(dojo.create("label",{innerHTML: "Language Of The Book:&nbsp;&nbsp;&nbsp;", "class": "heading3"}));
	dom.appendChild(languageInput.domNode);
	dom.appendChild(branchitup.createTooltipLabel("<b style='margin-left: 10px;'>?</b>","The language in which this publication has been written."));
	
	dom.appendChild(dojo.create("br"));
	var messageCell = dojo.create("label",{style: "color: red;"});
	dom.appendChild(messageCell);
	
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/back-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/back-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/back-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(dom);
			publishStep2();
		}
	}));
	dom.appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;&nbsp;"}));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/next-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/next-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/next-button_115x25.png'",
		onclick: function(){
			if(!languageInput.get("value")){
				messageCell.innerHTML = "You must specify a supported language";
			}
			else{
				dojo.byId("publishbook.hidden").appendChild(dom);
				publishStep4();
			}
			
		}
	}));
	
	c.innerHTML = "";
	c.appendChild(dom);
}
function publishStep2(){
	var c = dojo.byId("publishbook.wizardContainer");
	var dom = dojo.create("span");
	dom.appendChild(createStepsNavigation(1));
	dom.appendChild(dojo.create("br"));
	
	dom.appendChild(dojo.create("label",{innerHTML: "Upload Cover Image:&nbsp;&nbsp;&nbsp;", "class": "heading3"}));
	dom.appendChild(imageUploader.domNode);
	dom.appendChild(branchitup.createTooltipLabel("<b style='margin-left: 10px;'>?</b>","This is the image that will appear on the cover of the book.  A cover image is optional."));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/back-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/back-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/back-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(dom);
			publishStep1();
		}
	}));
	dom.appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;&nbsp;"}));
	dom.appendChild(dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/next-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/next-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/next-button_115x25.png'",
		onclick: function(){
			dojo.byId("publishbook.hidden").appendChild(dom);
			publishStep3();
		}
	}));
	
	c.innerHTML = "";
	c.appendChild(dom);
}
function publishStep1(){
	var c = dojo.byId("publishbook.wizardContainer");
	
	var dom = dojo.create("span");
	dom.appendChild(createStepsNavigation(0));
	dom.appendChild(dojo.create("br"));
	dom.appendChild(dojo.create("br"));
	
	var img = dojo.create("img",{
		id: "publishbook.submitButton",
		src: "resources/images/buttons/publish-wizard-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/publish-wizard-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/publish-wizard-button_115x25.png'",
		onclick: function(){
			publishStep2();
		}
	});
	
	dom.appendChild(img);
	c.innerHTML = "";
	c.appendChild(dom);
}
//-----

function init(){
	dojo.byId("publishbook.hidden").appendChild(imageUploader.domNode);
	publishStep1();
	
	dojo.byId("publishbook.publisherCommentCell").appendChild(publisherCommentInput.domNode);
	dojo.byId("publishbook.hidden").style.display = "none";
}

dojo.addOnLoad(init);
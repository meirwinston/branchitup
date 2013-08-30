dojo.require("dojoui.Pagination");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Textarea");

dojo.require("dojox.form.Uploader");
if(dojo.isIE){
	dojo.require("dojox.form.uploader.plugins.Flash");
}
else{
	dojo.require("dojox.form.uploader.plugins.HTML5");
}

//var audioUploader = new dojox.form.Uploader({
//	label: "Select File",
//	url: "service/UploadAudio",
//	multiple: false,
//	type: "file",
//	id: "audioUploader"
//});

var bottomPane = dojo.byId("publishedbook.bottomPane");
var commentForm = new dijit.form.Form({
	method: "get"
});

//var itemsDropdown = new PublishedBooksDropdown({
var itemsDropdown = new ItemsDropdown({
	id: "publishedbook.itemsDropdown",
	onChange: function(obj){
//			console.log("--->this is my onchange function");
//			console.log(obj);
		if(obj && obj.item && obj.item.type == "BOOK" && obj.item.id > 0){
			window.location.href = ("publishedbook?bookId=" + obj.item.id);
		}
	},
	onTextChange: function(value){
	}
});

ItemsDropdown.prototype.select = function(){
	//console.log("PublishedBooksDropdown:select:1:");
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["PBOOK"], offset:0, maxResults: 4},function(response){
		self.closeMenu();
		self._repositionMenu();
		//console.log("PublishedBooksDropdown:select:1:response");
		//console.log(response);
		
		self.populate(response.items);
	});
};


var pagination = new dojoui.Pagination({
	pageSize: 8,
	id: "commentsPagination",
//		onBrowse: dojo.hitch(_self,_self.browsePublicationRecords),
	onBrowse: function(offset){browseComments(offset);},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

var attachmentsPagination = new dojoui.Pagination({
	pageSize: 8,
	id: "attachmentsPagination",
//		onBrowse: dojo.hitch(_self,_self.browsePublicationRecords),
	onBrowse: function(offset){scrollAudioAttachments(offset);},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

var audioDescriptionInput = new dijit.form.Textarea({
	id: "publishedbook.audioDescriptionInput",
	name: "comment",
	placeholder: "Describe your audio file",
	maxLength: 512,
	trim : true,
	class : 'textarea'
});

function SubmitAudioFileRating(audioFileId, rating){
//	console.log("submitRating " + rating);
	branchitup.jsonServlet.doGet("SubmitAudioFileRating",{audioFileId: audioFileId, rate: rating},function(response){
//		console.log("SubmitAudioFileRating.response " + dojo.byId("rate_" + audioFileId));
//		console.log(response);
		setAverageRatingStars(dojo.byId("rate_" + audioFileId), response.ratingAverage, true);
	},
	function(response){
//		console.log("FAIL:::");
//		console.log(response);
		dialogManager.openDialog({
			title: "Problem submitting your request", 
			content: response.message, 
			style: "width: 300px; height: 200px;",
			acknowledge: true
		});
	});
}


function submitRating(rating){
//	console.log("submitRating " + rating);
	branchitup.jsonServlet.doGet("SubmitBookRating",{bookId: params.bookId[0], rate: rating},function(response){
//		console.log("submitRating.response " + dojo.byId("publishedbook.ratingAverage"));
//		console.log(response);
		setAverageRatingStars(dojo.byId("publishedbook.ratingAverage"), response.ratingAverage, false);
		setRatingCount(response.ratingCount);
		
		if(userAccountId){
			var arr = document.getElementsByName("commentRate_" + userAccountId);
			for(var i in arr){
				if(arr[i].appendChild){ //check if this is a dom element
					setAverageRatingStars(arr[i], rating, true);
				}
			}
		}
		
	},
	function(response){
//		console.log("FAIL:::");
//		console.log(response);
		dialogManager.openDialog({
			title: "Problem submitting your request", 
			content: response.message, 
			style: "width: 300px; height: 200px;",
			acknowledge: true
		});
	});
}

function submitComment(){
	var value = commentForm.get("value");
	branchitup.jsonServlet.doGet("SubmitBookComment",{bookId: params.bookId[0], comment: value.comment},function(response){
//			console.log("submitComment.response");
//			console.log(response);
//		if(response['javaClass'] == "UnauthorizedException"){
//			console.log("here");
//		}
		commentForm.reset();
		browseComments(0);
		
	},function(response){
		if(response['javaClass'] == "UnauthorizedException"){
			dialogManager.openDialog({
				title: "Please Log In", 
				content: "To add a comment you must be logged in, if you do not have an account yet you can sign up for a new account <a href=\"signup\">here.</a>", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
	});
}

function createRateInput(callback,userRating, atts){
	var dom = dojo.create("span");
	var rateitLabel = dojo.create("label", {"name":"rateitAction", innerHTML: "rate it!&nbsp;", onclick: function(){
		if(starsPanel.style.display == "none"){
			starsPanel.style.display = "";
		}
		else{
			starsPanel.style.display = "none";
		}
		
	}}); 
	var starsPanel = dojo.create("span", {style: "display: none;"});
	dom.appendChild(rateitLabel);
	dom.appendChild(starsPanel);
	var arr = [];
	
	var calculateRating = function(){
		var count = 0;
		for(var i = 0 ; i < arr.length ; i++){
			if(arr[i].src.indexOf("star-yellow_16x16") != -1){
				count += 0.2;
			}
		}
		//float numbers will give inaccurate results like: 0.4 + 0.2 = 0.600000000001
		return parseFloat(count.toPrecision(6));
	};
	
	var onRateImgClicked = function(e){
		var index = e.target.getAttribute("index");
		
		for(var i = 0 ; i <= index ; i++){
			arr[i].src = "resources/images/star-yellow_16x16.png";
		}
		for(var i = parseInt(index)+1 ; i < arr.length ; i++){
			arr[i].src = "resources/images/star-grey_16x16.png";
		}
		
		if(callback){
			callback(calculateRating(), atts);
		}
	};
	if(userRating){ //Rate It!
		var userStars = (userRating * 5);
		for(var i = 0 ; i < 5 ; i++){
			if(i < userStars){
				arr.push(dojo.create("img",{index: i, src: "resources/images/star-yellow_16x16.png", onclick: onRateImgClicked}));
			}
			else{
				arr.push(dojo.create("img",{index: i, src: "resources/images/star-grey_16x16.png", onclick: onRateImgClicked}));
			}
		}
	}
	else{
		for(var i = 0 ; i < 5 ; i++){
			arr.push(dojo.create("img",{index: i, src: "resources/images/star-grey_16x16.png", onclick: onRateImgClicked}));
		}
	}
	for(var i in arr){
		arr[i].style.cursor = "pointer";
		starsPanel.appendChild(arr[i]);
	}
	
	return dom;
}

//require(['dojox/form/Uploader', 'dojox/form/uploader/plugins/Flash'], function(Uploader){
//    var audioUploader = new dojox.form.Uploader({
//    	type: "file",
//    	multiple: false,
//    	label: "Select File",
//    	url: "/tests/UploadFile.php",
//    	uploadOnSelect: true
//    },"publishedbook.audioUploaderContainer");
//});

function storeAudioUpload(sessionAttributeId){
	var o ={
		bookId: params.bookId[0],
		sessionAttributeId: sessionAttributeId,
		description: audioDescriptionInput.get("value")
	};
	branchitup.jsonServlet.doGet("StoreAudioUpload",o,function(response){
//		console.log("StoreAudioUpload");
//		console.log(response);
		scrollAudioAttachments(0);
		
		var dialog = dialogManager.getDialog();
		if(dialog){
			dialog.onCancel();
		}
	},function(response){
		var dialog = dialogManager.getDialog();
		if(dialog){
//			console.log("Lets see the dialog");
//			console.log(dialog._contentSetter.content);
			
			var t = dialog._contentSetter.content;
			var tr = t.rows[t.rows.length-1];
			
			tr.firstChild.innerHTML = ("<b>Problem uploading your audio file: </b> " + response.message);
//			console.log(dialog.get("content"));
		}
	});
}

/*
FIREFOX:

fieldName "uploadedfile"
file "Hurts- Wonderful Life.mp3"
name "Hurts- Wonderful Life.mp3"
resourceType "audio/mpeg"
sessionAttributeId "bcdf5704-1513-44fc-982a-157100047c74"
size 10173083


 */
function showCompleteAudioUploadDialog(fileInfo){
//	console.log("showCompleteAudioUploadDialog");
//	console.log(fileInfo);
	
	var t = dojo.create("table");
	var td,tr;
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.innerHTML = "In order to complete the upload please provide description below and hit the submit button.<br>Your audio will be published after a validation check.<br><br>";
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.appendChild(audioDescriptionInput.domNode);
	
	tr = t.insertRow(t.rows.length);
	td = tr.insertCell(tr.cells.length);
	td.style.paddingTop = "10px";
	td.style.color = "red";
	td.innerHTML = "&nbsp;";
	
	dialogManager.openDialog({
		title: "Complete Your Upload", 
		content: t, 
		attribute: t,
		style: "width: 500px; height: 300px; vertical-align: top;",
		onOK: function(){
			storeAudioUpload(fileInfo['sessionAttributeId']);
		},
		okLabel: "Submit Your Audio Book",
//		dismiss: true
	});
}

function initAudioForm(){
//	console.log("initAudioForm");
//	console.log(audioUploader);
	if(dojo.isIE){
		audioUploader.set("url","service/UploadAudioFlash");
	}
	else{
		audioUploader.set("url","service/UploadAudioHTML5");
	}
	
	//onAbort,onBegin,onCancel, onChange, onClose, onShow
	dojo.connect(audioUploader,"onComplete",function(response){
		var uploaderContainer = dojo.byId("publishedbook.audioUploaderContainer");
//		console.log("on complete.....");
//		console.log(arguments);
		uploaderContainer.removeChild(dojo.byId("publishedbook.imgUpload"));
		uploaderContainer.removeChild(dojo.byId("publishedbook.labelUpload"));
		dojo.query('[widgetId]', uploaderContainer)[0].style.display = "";
		if(response.type != "exception"){
			showCompleteAudioUploadDialog(response[0]);
		}
		else{
			dialogManager.openDialog({
				title: "Problem Uploading Your File",
				content: response.message,	//16309TB
				acknowledge: true
			});	
		}
		
	});
	
	dojo.connect(audioUploader,"onChange",function(){
//		console.log("onChange.....");
//		console.log(arguments);
	});
	
	dojo.connect(audioUploader,"onBegin",function(){
//		console.log("onBegin.....");
		var uploaderContainer = dojo.byId("publishedbook.audioUploaderContainer");
		dojo.query('[widgetId]', uploaderContainer)[0].style.display = "none";
		uploaderContainer.appendChild(dojo.create("img",{id: "publishedbook.imgUpload", src: "resources/images/animated-wait.gif"}));
		uploaderContainer.appendChild(dojo.create("label",{id: "publishedbook.labelUpload", innerHTML: "&nbsp;Uploading File..."}));
		
	});
}

function initCommentForm(){
	var commentInput = new dijit.form.Textarea({
		id: "publishedbook.commentTextarea",
		name: "comment",
		placeholder: "Tell the world what you think about this book",
		maxLength: 1024,
		trim : true,
		class : 'textarea'
	});
	// src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitGenre();" /></td>
	var submitCommentButton = dojo.create("img",{
		id: "publishedbook.submitButton",
		onclick: submitComment, 
		src: "resources/images/buttons/submit-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/submit-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/submit-button_115x25.png'"
	});
	//dont allow the publisher to rate its own book
	if(userAccountId){
		if(publication.publisherAccountId != userAccountId){
			commentForm.domNode.appendChild(createRateInput(submitRating,userRating,null));
		}		
	}
	
	commentForm.domNode.appendChild(dojo.create("br"));
	commentForm.domNode.appendChild(commentInput.domNode);
	commentForm.domNode.appendChild(dojo.create("div",{"id":"publishedbook.textareaSubscript", innerHTML: "(Maximum 1024 characters)"}));
	commentForm.domNode.appendChild(dojo.create("br"));
	commentForm.domNode.appendChild(submitCommentButton);
	
}

function initToolbar(){
	var d; 
	
	d = dojo.byId("header.searchbarContainer"); 
	d.appendChild(itemsDropdown.domNode);
}

function createRatingsDom(rate){ //shows up on every comment
	var dom = dojo.create("span",{name: "rate"});
	if(!rate) rate = 0;
	var userStars = (rate * 5);
	for(var i = 0 ; i < 5 ; i++){
		var img = dojo.create("img");
		if(i < userStars){
			img.src = "resources/images/star-yellow_16x16.png";
		}
		else{
			img.src = "resources/images/star-grey_16x16.png";
		}
		dom.appendChild(img);
	}
	return dom;
};

function setRatingCount(count){
	var dom = dojo.byId("publishedbook.ratingCount");
	if(count){
		dom.innerHTML = ("&nbsp;Rated:" + count);
	}
}

function setAverageRatingStars(dom, rate, smallStars){
	if(!dom){
		console.log("setAverageRatingStars is undefined, " + rate + ", " + smallStars);
		return;
	}
//	var dom = dojo.byId("publishedbook.ratingAverage");
	dom.innerHTML = "";
//	console.log("setAverageRatingStars " + rate);
	if(!rate) rate = 0;
	var averageStars = rate * 5;
	if(smallStars){
		for(var i = 0 ; i < 5 ; i++){
			var img = dojo.create("img");
			if(i < averageStars){
				img.src = "resources/images/star-yellow_16x16.png";
			}
			else{
				img.src = "resources/images/star-grey_16x16.png";
			}
			dom.appendChild(img);
		}
	}
	else{
		for(var i = 0 ; i < 5 ; i++){
			var img = dojo.create("img");
			if(i < averageStars){
				img.src = "resources/images/star_yellow20x20.png";
			}
			else{
				img.src = "resources/images/star_grey20x20.png";
			}
//			console.log("GGGGG::");
//			console.log(dom);
			dom.appendChild(img);
		}
	}
	
}

function populateDeficiencyList(deficiencyList){
	var d = dojo.byId("publishedbook.deficiencyList");
//	console.log("populateDeficiencyList");
//	console.log(deficiencyList);
	if(deficiencyList){
		for(var i in deficiencyList){
			var item = Constants.Publication.BookRole[deficiencyList[i]];
			var img = dojo.create("img",{
				title: item.deficiencyLabel,
				src: item.symbolIcon,
				style: "height: 15px; width: auto;"
			});
			img.style.marginLeft = "5px";
			d.appendChild(img);
		}			
	}
}

function populateContributors(map){
	var c = dojo.byId("publishedbook.contributorsPane");
//	console.log("populateContributors");
//	console.log(map);
	var arr;
	
	arr = map['WRITING'];
	if(arr){
		c.appendChild(dojo.create("h3",{"class": "blueText", innerHTML: "Author / Writer(s)"}));
		for(var i in arr){
			var s = arr[i].firstName + " " + arr[i].lastName;
			if(i < (arr.length-1)){
				s += ", ";
			}
			c.appendChild(dojo.create("span",{innerHTML: s}));
		}
	}
	
	arr = map['ILLUSTRATING'];
	if(arr){
		c.appendChild(dojo.create("h3",{"class": "blueText", innerHTML: "Illustrator / Designer(s)"}));
		for(var i in arr){
			var s = arr[i].firstName + " " + arr[i].lastName;
			if(i < (arr.length-1)){
				s += ", ";
			}
			c.appendChild(dojo.create("span",{innerHTML: s}));
		}
	}
	
	arr = map['TRANSLATING'];
	if(arr){
		c.appendChild(dojo.create("h3",{"class": "blueText", innerHTML: "Translator(s)"}));
		for(var i in arr){
			var s = arr[i].firstName + " " + arr[i].lastName;
			if(i < (arr.length-1)){
				s += ", ";
			}
			c.appendChild(dojo.create("span",{innerHTML: s}));
		}
	}
	
	arr = map['EDITING'];
	if(arr){
		c.appendChild(dojo.create("h3",{"class": "blueText", innerHTML: "Editor(s)"}));
		for(var i in arr){
			var s = arr[i].firstName + " " + arr[i].lastName;
			if(i < (arr.length-1)){
				s += ", ";
			}
			c.appendChild(dojo.create("span",{innerHTML: s}));
		}
	}
	
	arr = map['PROOF_READING'];
	if(arr){
		c.appendChild(dojo.create("h3",{"class": "blueText", innerHTML: "Proof Reader(s)"}));
		for(var i in arr){
			var s = arr[i].firstName + " " + arr[i].lastName;
			if(i < (arr.length-1)){
				s += ", ";
			}
			c.appendChild(dojo.create("span",{innerHTML: s}));
		}
	}
}

/*
bookId
	1985
	
firstName
	"Adam"
	
lastName
	"Johnson"
	
roleMask
	2
	
title
	"test versions"
	
userAccountId
	1
	
version
	"1.1"
 */
function renderPathOfBranch(arr){
	var pane = dojo.byId("publishedbook.pathOfBranch");
	for(var i in arr){
		var d = dojo.create("span",{"name": "branchItem",onclick: "javascript:window.location.href='publishedbook?bookId=" + arr[i].bookId + "'"});
		d.appendChild(dojo.create("span",{innerHTML: arr[i].title}));
		d.appendChild(dojo.create("span",{innerHTML: " by " + arr[i].firstName + " " + arr[i].lastName}));
		d.appendChild(dojo.create("br"));
		d.appendChild(dojo.create("span",{innerHTML: "version: " + arr[i].version}));
		
		pane.appendChild(d);
		
		pane.appendChild(dojo.create("br"));
		pane.appendChild(dojo.create("span",{name: "branchItemSeperator"}));
		pane.appendChild(dojo.create("br"));
	}
}
function showPathOfBranch(){
	branchitup.jsonServlet.doGet("GetPathOfBranch",{bookId: params.bookId[0]},function(response){
//		console.log("showPathOfBranch");
//		console.log(response);
		renderPathOfBranch(response.branchList);
	});
}

function showContributors(){
	branchitup.jsonServlet.doGet("GetPublicationContributors",{bookId: params.bookId[0]},function(response){
//		console.log("fetchContributors");
//		console.log(response);
//		var publishers = response.publishers;
//		for(var i in publishers){
//			console.log("publisher");
//			console.log(publishers[i]);
//		}
		populateContributors(response.contributorsByRole);
	});
}

//--
function showBranchConfirm(){
	var self = this;
	
	var t = dojo.create("div");
	var d = dojo.create("img",{src: "resources/images/branch_47x50.png"});
	d.style.float = "left";
	t.appendChild(d);
	t.appendChild(dojo.create("span",{innerHTML: ("You are about to branch: '" + publication.title + "'.</br><br/>Branching this publication will allow you to illustrate, translate, edit, or otherwise enhance this work. When you are done working on the book, you may publish your version to make it available to all readers.<br/></br>After clicking \"branch\" below, you will find a copy of \"" + publication.title + "\" and its associated sheets in your workdesk. This is your copy to work on as you like!</br></br>Your branch will automatically be numbered by the BranchItUp numbering system.</br></br></br>Good Luck!</br>")}));

	dialogManager.openDialog({
		title: "Branch A Book", 
		content: t, 
		style: "width: 500px; height: 400px; vertical-align: top;",
		onOK: dojo.hitch(self,branch),
		okLabel: "Branch This Book"
	});
};


function branch(){
	dialogManager.openDialog({title: "Branching...", content: "Please Wait...<br /><img src='resources/images/animated-wait.gif' />", style: "width: 200px; height: 100px;"});
	branchitup.jsonServlet.doGet("BranchPublishedBook",{bookId: publication.bookId},function(response){
		window.location.href = ("bookeditor?bookId=" + response.bookId);
	},
	function(m){
		if(m.javaClass == "UnauthorizedException"){
			dialogManager.openDialog({
				title: "Problem during branching",
				content: "You are not logged in, please first login or if you do not have an account sign up <a href='signup'>here</a>",
				acknowledge: true
			});
		}
		else{
			dialogManager.openDialog({
				title: "Problem during branching",
				content: "There was an unexpected problem during the process of branching, please try again!",
				acknowledge: true
			});
		}
	});
};

//--

function populateComments(scrollResults){
//		pagination.set("totalResults",scrollResults.count);
//		pagination.render();
//		console.log("RENDER pagination with count " + scrollResults.count);
	
	var commentsPane = dojo.byId("publishedbook.commentsPane");
	commentsPane.innerHTML = "";
	
	var arr = scrollResults.list;
	for(var i = arr.length-1 ; i >= 0 ; i--){
		var div = dojo.create("div", {name: "comment"});
		var d;
		var h = dojo.create("h4",{"class": "title"});
		
		d = createRatingsDom(arr[i].rate);
		d.setAttribute("name",("commentRate_" + arr[i].userAccountId));
		h.appendChild(d);
		h.appendChild(dojo.create("span",{innerHTML: "&nbsp;&nbsp;&nbsp;"}));
		
		d = dojo.create("label",{name: "commentatorName", innerHTML: "Posted by: " + arr[i].firstName + " " + arr[i].lastName});
		h.appendChild(d);
		d = dojo.create("label",{name: "commentPostedOn", innerHTML: "Posted on: " + branchitup.formatMillisToDate(arr[i].createdOn)});
		h.appendChild(d);
		
		div.appendChild(h);
		div.appendChild(dojo.create("p",{innerHTML: arr[i].comment}));
		commentsPane.appendChild(div);
	}
}

function downloadAttachment(audioFileId){ 
	if(audioFileId){
		branchitup.jsonServlet.downloadAttachement(audioFileId,{octetstream: true, audio: true});
//		branchitup.jsonServlet.downloadAttachement(audioFileId);
	}
	else{
		dialogManager.openDialog({
			title: "No Attachment Available",
			content: "There is no attachment associated with this book!",
			acknowledge: true
		});
	}
};

function streamAttachment(audioFileId){ 
	if(audioFileId){
		branchitup.jsonServlet.downloadAttachement(audioFileId,{audio: true});
	}
	else{
		dialogManager.openDialog({
			title: "No Attachment Available",
			content: "There is no attachment associated with this book!",
			acknowledge: true
		});
	}
};


function populateAudioAttachments(scrollResults){
//	console.log("populateAudioAttachments: ");
//	console.log(scrollResults);
	var commentsPane = dojo.byId("publishedbook.audioAttachments");
	commentsPane.innerHTML = "";
	
	var arr = scrollResults.list;
	if(arr.length <= 0){
		commentsPane.innerHTML = "<p style='color: grey;'>No audio book attachments are available!</p>";
	}
	else{
		for(var i = arr.length-1 ; i >= 0 ; i--){
			var div = dojo.create("div", {name: "comment"});
			var d;
			var h = dojo.create("h4",{"class": "title"});
			h.appendChild(dojo.create("img",{name: "downloadImg", src: "resources/images/audio_18x20.png", onclick: "streamAttachment(" + arr[i].audioFileId + ")"}));
			
			d = createRatingsDom(arr[i].rate);
			d.id = ("rate_" + arr[i].audioFileId);
			h.appendChild(d);
			
			d = dojo.create("label",{name: "commentatorName", innerHTML: "Uploaded by: " + arr[i].ownerFirstName + " " + arr[i].ownerLastName});
			h.appendChild(d);
			d = dojo.create("label",{name: "commentPostedOn", innerHTML: "On: " + branchitup.formatMillisToDate(arr[i].createdOn)});
			h.appendChild(d);
			d = dojo.create("a",{name: "download", innerHTML: "download", href: "javascript:downloadAttachment(" + arr[i].audioFileId + ")"});
			h.appendChild(d);
			
			div.appendChild(h);
			
			if(arr[i].description){
				div.appendChild(dojo.create("p",{innerHTML: arr[i].description}));
			}
			
			
			var actionsDiv = dojo.create("div");
//			actionsDiv.appendChild(dojo.create("label",{innerHTML: "rate it"}));
			
			actionsDiv.appendChild(createRateInput(function(rate,atts){
//				console.log("rate attachment " + rate +", ID: " + atts);
				SubmitAudioFileRating(atts, rate);
			},(arr[i].userRate ? arr[i].userRate : 0.0) ,arr[i].audioFileId));
			div.appendChild(actionsDiv);
			commentsPane.appendChild(div);
		}
	}
	
}


function setTab(tabName){
//	console.log("setTab ");
//	console.log(this);
	
	switch(tabName){
	case "COMMENTS":
		bottomPane.innerHTML = "<h2 class='titleBlack'>Comments</h2><div id='publishedbook.commentFormPane'></div><div id='publishedbook.commentsPane'></div>";
		bottomPane.appendChild(pagination.domNode);
		dojo.byId("publishedbook.commentFormPane").appendChild(commentForm.domNode);
		
		dojo.byId("publishedbook.commentsTab").className = "tabToolbarActionSelected";
		dojo.byId("publishedbook.contributorsTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.pathOfBranchTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.attachmentsTab").className = "tabToolbarAction";
//			browseComments(pagination.currentPageNumber);
		browseComments(0);
		break;
	case "CONTRIBUTORS":
		bottomPane.innerHTML = "<h2 class='titleBlack'>Contributors</h2><div id='publishedbook.contributorsPane'></div>";
		showContributors();
		
		dojo.byId("publishedbook.commentsTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.contributorsTab").className = "tabToolbarActionSelected";
		dojo.byId("publishedbook.pathOfBranchTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.attachmentsTab").className = "tabToolbarAction";
		
		break;
	case "PATH_OF_BRANCH":
		bottomPane.innerHTML = "<h2 class='titleBlack'>Path of Branch</h2><div id='publishedbook.pathOfBranch'></div>";
		showPathOfBranch();
		
		dojo.byId("publishedbook.commentsTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.contributorsTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.pathOfBranchTab").className = "tabToolbarActionSelected";
		dojo.byId("publishedbook.attachmentsTab").className = "tabToolbarAction";
		break;
	case "ATTACHMENTS":
		bottomPane.innerHTML = "<h2 class='titleBlack'>Audio</h2><div id='publishedbook.audioAttachments'></div>";
		bottomPane.appendChild(attachmentsPagination.domNode);
		scrollAudioAttachments(0);
		
		dojo.byId("publishedbook.commentsTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.contributorsTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.pathOfBranchTab").className = "tabToolbarAction";
		dojo.byId("publishedbook.attachmentsTab").className = "tabToolbarActionSelected";
	
		if(audioUploader.domNode.style.visibility == "hidden"){
			var d = dojo.create("span",{id: "publishedbook.audioUploaderContainer"});
			d.appendChild(dojo.create("label",{innerHTML: "Upload your own audio to this book: "}));
			d.appendChild(audioUploader.domNode);
			audioUploader.domNode.style.visibility = "";
			
			bottomPane.appendChild(d);
		}
		
		break;
	}
}

function browseComments(offset, callback){
	var args = {offset: offset, maxResults: pagination.pageSize, bookId: params.bookId[0]};
//		console.log("--->browseComments: ");
//		console.log(args);
	branchitup.jsonServlet.doGet("ScrollReviews",args,function(response){
		populateComments(response);
		pagination.set("totalResults",response.count);
		pagination.refresh();
	});
}

function scrollAudioAttachments(offset, callback){
	var args = {offset: offset, maxResults: attachmentsPagination.pageSize, bookId: params.bookId[0]};
//	console.log("--->scrollAudioAttachments: ");
//	console.log(args);
	branchitup.jsonServlet.doGet("ScrollBookAudioAttachments",args,function(response){
//		console.log("scrollAudioAttachments response");
//		console.log(response);
		populateAudioAttachments(response);
		attachmentsPagination.set("totalResults",response.count);
		attachmentsPagination.refresh();
	});
}

function formatPublisherRoles(publisherRoleList){
	//publisher roles
	var s = "";
	for(var i in publisherRoleList){
		s += Constants.Publication.BookRole[publisherRoleList[i]].publisherRoleLabel;
		if(i < publisherRoleList.length-1){
			s += " • ";
		}
	}
	return s;
};

function reload(callback){
	setTab("COMMENTS");
//	console.log("published-book reload setTab to comments");
}
function onLoad(){
//	console.log("onLoad");
//	console.log(exception);
	initToolbar();
	initCommentForm();
	initAudioForm();
	dojo.byId("publishedbook.publisherRolesLabel").innerHTML = formatPublisherRoles(publication.publisherRoleList);
	
	if(exception){
		dialogManager.openDialog({
			style: "width: 300px; height: 200px",
			content: exception.message,
			title: "Book Not Found",
			acknowledge: function(){
				window.location.href = "home";
			}
		});
	}
	else{
		reload();
	}
}
dojo.addOnLoad(onLoad);

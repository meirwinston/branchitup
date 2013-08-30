dojo.require("dojoui.Pagination");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Textarea");

var articleId = null;

var itemsDropdown = new ItemsDropdown({
	id: "article.itemsDropdown",
	onChange: function(obj){
		if(obj.item.type == "SHEET"){
			//console.log("bookeditor-insert sheet here");
			appendSelectedSheet(itemsDropdown.getItem());
		}
	},
	onTextChange: function(value){
	}
});
itemsDropdown.select = function(){
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["ARTICLE"], offset: 0, maxResults: 4},function(response){
		self.closeMenu();
		self._repositionMenu();
		self.populate(response.items);
	});
};
itemsDropdown.menuItemSelected = function(item){
	this.setArticle(item);
};
itemsDropdown.setArticle = function(item){
	window.location.href = ("article?articleId=" + item.id);
};
var commentForm = new dijit.form.Form({
	method: "get"
});

var pagination = new dojoui.Pagination({
	pageSize: 10,
	id: "commentsPagination",
//		onBrowse: dojo.hitch(_self,_self.browsePublicationRecords),
	onBrowse: function(offset){browseComments(offset);},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

function populateComments(scrollResults){
	var commentsPane = dojo.byId("article.commentsPane");
	commentsPane.innerHTML = "";
	
	var arr = scrollResults.list;
	for(var i = 0 ; i < arr.length ; i++){
		var div = dojo.create("div", {name: "comment"});
		var d;
		var h = dojo.create("h4",{"class": "title"});
		
		d = dojo.create("label",{name: "commentatorName", innerHTML: "Posted by: " + arr[i].fullName});
		h.appendChild(d);
		d = dojo.create("label",{name: "commentPostedOn", innerHTML: "Posted on: " + branchitup.formatMillisToDate(arr[i].createdOn)});
		h.appendChild(d);
		
		div.appendChild(h);
		div.appendChild(dojo.create("p",{innerHTML: arr[i].comment}));
		commentsPane.appendChild(div);
	}
}

function browseComments(offset, callback){
	var args = {offset: offset, maxResults: pagination.pageSize, sheetId: articleId};
	branchitup.jsonServlet.doGet("ScrollArticleComments",args,function(response){
		populateComments(response);
		pagination.set("totalResults",response.count);
		pagination.refresh();
	});
}

function submitComment(){
	var value = commentForm.get("value");
	branchitup.jsonServlet.doGet("SubmitArticleComment",{
		sheetId: articleId, 
		comment: value.comment
	},function(response){
		commentForm.reset();
		browseComments(0);
		
	},function(response){
		if(response['javaClass'] == "UnauthorizedException"){
			dialogManager.openDialog({
				title: "Please Log In", 
				content: "To add a comment you must be logged in, please <a href=\"login\">log in here.</a> If you do not have an account yet you can sign up for a new account <a href=\"signup\">here.</a>", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
	});
}

function initCommentForm(){
	var commentInput = new dijit.form.Textarea({
		id: "article.commentTextarea",
		name: "comment",
		placeholder: "Add your comments here (must be logged in)",
		maxLength: 4000,
		trim : true,
		class : 'textarea'
	});
	var submitCommentButton = dojo.create("img",{
		id: "publishedbook.submitButton",
		onclick: submitComment, 
		src: "resources/images/buttons/submit-button_115x25.png",
		onmouseover: "this.src = 'resources/images/buttons/submit-button-hover_115x25.png'",
		onmouseout: "this.src = 'resources/images/buttons/submit-button_115x25.png'"
	});
	
	commentForm.domNode.appendChild(dojo.create("br"));
	commentForm.domNode.appendChild(commentInput.domNode);
	commentForm.domNode.appendChild(dojo.create("div",{"id":"article.textareaSubscript", innerHTML: "(Maximum 4000 characters)"}));
	commentForm.domNode.appendChild(dojo.create("br"));
	commentForm.domNode.appendChild(submitCommentButton);
	
	dojo.byId("article.paginationPane").appendChild(pagination.domNode);
	dojo.byId("article.commentFormPane").appendChild(commentForm.domNode);
}
function initToolbar(){
	var d; 
	
	d = dojo.byId("header.searchbarContainer"); 
	d.appendChild(itemsDropdown.domNode);
}

function onLoad(){
	console.log("Article.onload");
	initToolbar();
	initCommentForm();
	
	browseComments(0);
}
dojo.addOnLoad(onLoad);
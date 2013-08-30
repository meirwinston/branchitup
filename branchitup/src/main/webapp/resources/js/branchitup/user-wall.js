dojo.require("dojoui.Pagination");
dojo.require("dijit.form.Button");

var itemsDropdown = new ItemsDropdown({
	id: "userwall.itemsDropdown",
	onChange: function(obj){
		pagination.currentPageNumber = 0;
		if(obj.item.type == "USER"){
			window.location.href = ("userwall?userAccountId=" + obj.item.id);
		}
	},
	onTextChange: function(value){
	}
});

ItemsDropdown.prototype.select = function(){
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["USER"], offset:0, maxResults: 3},function(response){
//		console.log("SELECT response ");
//		console.log(response);
		self.closeMenu();
		self._repositionMenu();
		self.populate(response.items);
	});
};


var pagination = new dojoui.Pagination({
	pageSize: 6,
	id: "userwall.pagination",
	onBrowse: function(offset){browseWallRecords(offset);},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

function initToolbar(){
	var d; 
	
	d = dojo.byId("header.searchbarContainer"); 
	d.appendChild(itemsDropdown.domNode);
	
//	var d; 
//	d = dojo.byId("toolbar.searchBox");
//	d.appendChild(itemsDropdown.domNode);
//	
//	dojo.byId("userwall.paginationCell").appendChild(pagination.domNode);
//	
//	d = dojo.byId("toolbar.searchButton");
//	var b = new dijit.form.Button({label: "Search", onClick: function(){}});
//	d.appendChild(b.domNode);
}

var userMessageInput = new dijit.form.Textarea({
	name: 'message',
	required: true, 
	trim: true, 
	placeHolder: 'type in your message', 
	maxLength: 512, 
	minLength: 1,
	'class': 'textarea'
});
function submitMessage(){
	var args = messageForm.get("value");
	if(userAccountId){
		args.userAccountId = userAccountId;
	}
	branchitup.jsonServlet.doGet("PostUserMessage",args,function(response){
//		console.log("ScrollUserWallRecords:response:");
//		console.log(response);
		messageForm.reset();
//		pagination.calculateIndexes(pagination.currentPageNumber,pagination.totalResults+1);
//		pagination.pageToLastPage();
		
		browseWallRecords(0);
	});
}
var submitMessageButton = new dijit.form.Button({
	label: "Post",
	onClick: submitMessage
});
function addItem(item){
	var centerPane = dojo.byId("userwall.messagesPane");
	var d = dojo.create("div",{name: "wallRecord"});
	if(item.senderProfileImageUrl){
		d.appendChild(dojo.create("img",{name: "profileImage", src: item.senderProfileImageUrl}));
	}
	else{
		d.appendChild(dojo.create("img",{name: "profileImage", src: "resources/images/no-image-branchitup_200x200.png"}));
	}
	
	d.appendChild(dojo.create("a",{"class":"blueTitle", href: "userprofile?userAccountId=" + item.senderAccountId, innerHTML: item.senderFirstName + " " + item.senderLastName + "<br/>"}));
	if(item.createdOn){
		d.appendChild(dojo.create("span",{name: "createdOn", innerHTML: branchitup.formatMillisToDate(item.createdOn)}));
	}
	
	d.appendChild(dojo.create("p",{innerHTML: item.message}));
	centerPane.appendChild(d);
}

function setItems(items){
//	console.log("userwall--->setItems");
//	console.log(items);
	dojo.byId("userwall.messagesPane").innerHTML = "";
	for(var i in items){
		addItem(items[i]);
	}

}

function browseWallRecords(offset, callback){
	var args = {offset: offset, maxResults: pagination.pageSize, userAccountId: userAccountId};
//	console.log("browseWallRecords");
//	console.log(args);
	
	branchitup.jsonServlet.doGet("ScrollUserWallRecords",args,function(response){
//		console.log("ScrollUserWallRecords:response:");
//		console.log(response);
		pagination.set("totalResults",response.count);
		pagination.refresh();
		if(response.count > 0){
			setItems(response.list);			
		}
//		else{
//			showNoItemsMessage();
//		}
//		
//		if(callback){
//			callback(response);
//		}
	});
};
function initializeForm(){
	dojo.byId("userwall.messageInputContainer").appendChild(userMessageInput.domNode);
	dojo.byId("userwall.submitButtonContainer").appendChild(submitMessageButton.domNode);
}
function reload(){
	browseWallRecords(0);
}
function onLoad(){
	initToolbar();
	initializeForm();
	reload();
}
dojo.addOnLoad(onLoad);
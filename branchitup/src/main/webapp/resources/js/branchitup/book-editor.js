dojo.require("dojoui.DNDSource");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dojoui.Pagination");

var sheetsContainer = dojo.byId("bookeditor.sheetsContainer");
var sheetsHtmlTable = dojo.create("table",{"class":"listTable"});
sheetsContainer.appendChild(sheetsHtmlTable);
var bookId = null;

var titleInput = new dijit.form.ValidationTextBox({
	name: 'title',
	required: true,
	trim: true, 
//	placeHolder: 'type in book name', 
	maxLength: 50, 
	minLength: 1, 
	style: "width: 400px",
	regExp: Constants.RegularExpression.MULTILINGUAL_NAME, 
	invalidMessage: 'Invalid book title',
	onChange: function(e){
		dojo.byId("bookeditor.title").innerHTML = e;
	}
});
var bookSummaryInput = new dijit.form.Textarea({
	name: 'bookSummary',
	required: true, 
	trim: true, 
	placeHolder: 'type in a short summary', 
	maxLength: 1024, 
	minLength: 1,
	'class': 'textarea'
});
//var imageUploader = new ImageUploader({
//	uploadUrl: "service/FileUpload?params={\"method\":\"html5\",\"album\":\"Default\", \"target\":\"SESSION\"}",
//	onComplete: function(params){
////		console.log("onComplete");
////		console.log(params);//[[""]]
//		var url = "imageservice?target=SESSION&id=" + params[0].id;
////		console.log(url);
//		
//		dojo.byId("bookditor.coverImage").src = url;
//		dojo.byId("bookditor.coverImage").setAttribute("uploadId",params[0].id);
//	}
//});

var itemsDropdown = new ItemsDropdown({
	id: "bookeditor.itemsDropdown",
	onChange: function(obj){
		if(obj.item.type == "SHEET" || obj.item.type == "ARTICLE"){
			//console.log("bookeditor-insert sheet here");
			appendSelectedSheet(itemsDropdown.getItem());
		}
	},
	onTextChange: function(value){
	}//,
//	placeHolder: "Type in sheet name to append to your book"
});

itemsDropdown.select = function(){
	var self = this;
	
	var o = {
		phrase: self.textbox.get("displayedValue"), 
		offset:0, maxResults: 4, 
		types: ["SHEET","PSHEET"]
	};
//	if(sheetVisibilityInput.get("value") == "PRIVATE"){
//		o.types = ["SHEET"];
//	}
	if(dojo.byId("bookeditor.visibilityInput").checked){
		o.types = ["SHEET"];
	}
//	console.log("book-editor.select matching items");
//	console.log(o);
	/*
	 * maxResults
	4
offset
	0
phrase
	"o"
types
	["SHEET"]
	 */
	branchitup.jsonServlet.doGet("SelectMatchingItems",o,function(response){
		self.closeMenu();
		self._repositionMenu();
		self.populate(response.items);
	});
};

//var sheetVisibilityInput = new dijit.form.FilteringSelect({
//	style: "width: 150px;",
//	required: true,
//	store : new dojo.data.ItemFileReadStore( {
//		data : {
//			identifier : "value",
//			items : [{value: "PUBLIC", name: "Public Sheets"}, {value: "PRIVATE", name: "My Sheets Only"}]
//		}
//	}),
//	value: "PRIVATE"
//});


var sheetsDndSource = new dojoui.DNDSource(sheetsHtmlTable, {
	id : "bookeditor.sheeetsDndSource",
	accept : [ "SHEET", "ARTICLE" ],
//	selfAccept: false,
	copyOnly: true,
	creator: sheetSourceNodeCreator,
	onChange : function(source, nodeArray, isCopy, target) {
		refreshSquenceIndex();
	}
});
function refreshSquenceIndex(){
	var arr = sheetsHtmlTable.rows;
	for(var i = 0 ; i < arr.length ; i++){
		if(arr[i].cells){
			
			arr[i].cells[1].innerHTML = (i+1);
		}
		
	}
}

function scrollSheets(offset){
//	var folder = sheetsFolderInput.get("value");
//	if(!folder) {
//		return;
//	}
	
//	var args = {offset: offset, maxResults: sheetsPagination.pageSize, folder: folder};
	var args = {offset: offset, maxResults: 10};
	branchitup.jsonServlet.doGet("ScrollSheetUserItems",args,function(response){
		console.log("BookEditor:response:");
		console.log(response);
		
//		setSheets({list: response.items, count: response.count});
		sheetsPagination.set("totalResults",response.count);
		sheetsPagination.refresh();
	});
	
}
function SheetBrowser(args){
//	var dom = dojo.create("div");
	var dialog = null;
	
	var pagination = new dojoui.Pagination({
		id: generateId(),
		pageSize: 10,
		classOfNode: "pagination",
		onBrowse: function(offset, pageSize){
			scrollSheets(offset);
		},
		classOfSelectedLabel: "paginationSelectedLabel",
		classOfUnselectedLabel: "paginationUnselectedLabel",
		classNextBack: "paginationNextBack"
	});
	
	function scrollSheets(offset){
		var args = {offset: offset, maxResults: pagination.pageSize};
		branchitup.jsonServlet.doGet("ScrollSheetUserItems",args,function(response){
//			console.log("BookEditor:response:");
//			console.log(response);
			var t = dojo.create("table",{id: "bookeditor.sheetsTable", style: "width: 100%;"});
			var td,tr;
			for(var i in response.items){
				tr = t.insertRow(t.rows.length);
				td = tr.insertCell(tr.cells.length);
				td.name = "sheetName";
				td.innerHTML = response.items[i].name;
				
				td = tr.insertCell(tr.cells.length);
				td.name = "addToBook";
				td.innerHTML = "<a href='javascript:appendSelectedSheet({id: " + response.items[i].id + "})'>Add To Book</a>";
			}
			
			dialog.set("content",t);
			
			var bottom = dojo.create("div");
			bottom.style.position = "absolute";
			bottom.style.bottom = 0;
			bottom.appendChild(pagination.domNode);
			bottom.appendChild(dojo.create("span",{style: "margin-left: 20px; color: blue; cursor: pointer; text-decoration: underline;", innerHTML: "done!", onclick: dialog.onCancel}));
			
			dialog.containerNode.appendChild(bottom);
			pagination.set("totalResults",response.count);
			pagination.refresh();
		});
			
	}
//	(window.screen.height - 200)
	this.open = function(){
		dialog = dialogManager.openDialog({
			title: "Browse Your Work Desk", 
			content: "Loading Sheets...", 
			style: "width: 500px; height: 400px; vertical-align: top;",
			acknowledge: true
		});
//		dialog.bottomPane.appendChild(pagination.domNode);
//		console.log(dialog.bottomPane);
		scrollSheets(0);
	};
	//--
//	return dom;
}
var sheetBrowser = new SheetBrowser({});
function openWorkdeskBrowser(){
	sheetBrowser.open();
}
function toJson(){
	var o = {
		title: titleInput.get("value"),
		bookSummary: bookSummaryInput.get("value")
	};
	if(bookId){
		o['bookId'] = bookId;
	}
//	var coverUploadId = dojo.byId("bookditor.coverImage").getAttribute("uploadId");
//	if(coverUploadId){
//		o['fileItemId'] = coverUploadId;
//	}
	var sheetIds = [];
	var sheets = sheetsDndSource.getItemsInOrder();
	for(var i in sheets){
		sheetIds.push(sheets[i].item.data.sheetId);
	}
	o.sheetIds = sheetIds;

	return o;
};
function validateForm(){
	var messages = [];
	if(!bookForm.isValid()){
		messages.push("Invalid inputs");
	}
	var val = titleInput.get("value");
	if(!val || val == ""){
		messages.push("Book title cannot be empty");
	}
	val = bookSummaryInput.get("value");
	if(!val || val == ""){
		messages.push("Book summary cannot be empty");
	}
	if(messages.length > 0){
		throw messages;
	}
}

function updateBook(){
	try{
		validateForm();
		var o = toJson();
//		console.log("updateBook");
//		console.log(o);
		branchitup.jsonServlet.doGet("UpdateBook",o,function(response){
		//	console.log("updateOpenBook response");
		//	console.log(response);
			dialogManager.openDialog({
				title: "<img src='resources/images/branchitup-logo_15x16.ico' />&nbsp;Update Book", 
				content: "Your book has been saved successfully<br/><br/>To publish this book now, click the “Publish Now” button below.<br/>To publish this book another time, click the “Just Save” button below. If you want to publish this book later on, find this item in your work desk and click the “Publish Book” link under the book item.<br/><br/>What would you like to do?", 
				style: "width: 500px; height: 300px; vertical-align: top;",
				onOK: function(){
					window.location.href = ("workdesk");
				},
				onCancel: function(){ //publish book
					window.location.href = ("publishbook?bookId=" + bookId);
				},
				okLabel: "Just Save",
				cancelLabel: "Publish Now"
			});
			
		}, function(response){
			dialogManager.openDialog({
				title: "Problem Updating Your Book", 
				content: "There was a problem updating your book, please try again!", 
				style: "width: 300px; height: 200px; vertical-align: top;",
				acknowledge: true
			});
		});
	}
	catch(messages){
		var ul = dojo.create("ul");
		for(var i in messages){
			ul.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Invalid Input",
			content: ul,
			acknowledge: true
		});
	}
		
};

function newBook(){
	try{
		validateForm();
		var o = toJson();
//		console.log("newBook");
//		console.log(o);
		branchitup.jsonServlet.doGet("NewBook",o,function(response){
//			console.log("NewBook response");
//			console.log(response);
			bookId = response.bookId;
			dialogManager.openDialog({
				title: "New Book", 
				content: "Your book has been saved successfully<br/><br/>To publish this book now, click the “Publish Now” button below.<br/>To publish this book another time, click the “Just Save” button below. If you want to publish this book later on, find this item in your work desk and click the “Publish Book” link next to the book.<br/><br/>What would you like to do?", 
				style: "width: 500px; height: 300px; vertical-align: top;",
				onOK: function(){
					window.location.href = ("publishbook?bookId=" + bookId);
				},
				onCancel: function(){
					window.location.href = ("workdesk");
				},
				okLabel: "Publish Now",
				cancelLabel: "Just Save"
			});
			
		}, function(response){
			dialogManager.openDialog({
				title: "Problem Creating Your Book", 
				content: "There was a problem creating your book, please try again!", 
				style: "width: 300px; height: 200px; vertical-align: top;",
				acknowledge: true
			});
		});
	}
	catch(messages){
		var ul = dojo.create("ul");
		for(var i in messages){
			ul.appendChild(dojo.create("li",{innerHTML: messages[i]}));
		}
		dialogManager.openDialog({
			title: "Please fix your form",
			content: ul,
			acknowledge: true
		});
	}
		
};

function submitBook(){
	if(bookId){
		updateBook();
	}
	else{
		newBook();
	}
}

function addSheets(sheets){
//	console.log("OpenBookEditor.addSheet " + sheetsDndSource.countItems());
//	console.log(this);
	if(sheetsDndSource.countItems() > 299){ //specify one less than the total amount
		dialogManager.openDialog({
			title: "Number Of Sheets Exceeds Limit",
			content: "The maximum number of sheets compiled in a book cannot exceed 300 sheets",
			acknowledge: true
		});
		return;
	}
	
	if(sheets && sheets.length > 0){
		sheetsDndSource.insertNodes(false,sheets);
	}
//	if(sheetsDndSource.countItems() <= 0){
//		dojo.byId("bookeditor.sheetsContainer").appendChild(dojo.create("label",{innerHTML: "NO SHEETS"}));
//	}
	
};

function deleteSheetItemsByIds(ids) {
	for ( var i = 0; i < ids.length; i++) {
		sheetsDndSource.forInItems( function(item, id) {
//			console.log("deleteSheetItemsByIds " + id);
			if (item.data.sheetId == ids[i]) {
//				console.log(item); //Object { data={...}, type=[1]}
//				console.log(id); //dojoUnique3
//				console.log(dojo.byId(id)); //<div id="dojoUnique3" class="dojoDndItem dojoDndItemOver dojoDndItemAnchor" name="sheetItem">
//				sheetsHtmlTable.removeChild(dojo.byId(id));
				var node = dojo.byId(id);
				if(node){
					node.parentNode.removeChild(node);
				}
				
				sheetsDndSource.delItem(id);
			}
		}, sheetsDndSource);
	}
	refreshSquenceIndex();
};

//function appendSelectedSheet(){
////	console.log("appendSelectedSheet" + this.sheetsDropdown.getValue());
//	var item = itemsDropdown.getItem();
//	
//	//do not allow duplicates
//	var items = sheetsDndSource.getItems();
////	console.log("compare to ");
////	console.log(item);
//	
//	for(var i in items){
//		if(items[i].data.sheetId == item.id){
//			dialogManager.openDialog({
//				title: "Duplicate Sheet",
//				content: "You have already inserted the specified sheet in the book, a single sheet can appear only once per book",
//				acknowledge: true
//			});
//			return;
//		}
//	}
//	if(item){
////		console.log("add sheet");
////		console.log(item);
//		
//		//not here
//		branchitup.jsonServlet.doGet("GetBookDetailSheet",{sheetId: item.id},function(response){
////			console.log("GetBookDetailSheet");
////			console.log(response);
//			
//			addSheets([response]);
//		},function(response){
//			dialogManager.openDialog({
//				title: response.title,
//				content: response.message,
//				acknowledge: true
//			});
//		});
////		addSheets([{sheetId: item.id, sequenceIndex: items.length, name: item.name}]);
//		
//		itemsDropdown.textbox.set("value",null);
//	}
//	else{
//		dialogManager.openDialog({
//			title: "Sheet Input is Invalid",
//			content: "Please select a valid sheet name prior to adding the sheet to the book!",
//			acknowledge: true
//		});
//	}
//};


function appendSelectedSheet(item){
//	console.log("appendSelectedSheet" + this.sheetsDropdown.getValue());
	
	//do not allow duplicates
	var items = sheetsDndSource.getItems();
//	console.log("compare to ");
//	console.log(item);
	
	for(var i in items){
		if(items[i].data.sheetId == item.id){
			dialogManager.openDialog({
				title: "Duplicate Sheet",
				style: "width: 400px; height: 200px;",
				content: "You have already inserted the specified sheet in the book, a single sheet can appear only once per book",
				acknowledge: true
			});
			return;
		}
	}
	if(item){
//		console.log("add sheet");
//		console.log(item);
		
		//not here
		branchitup.jsonServlet.doGet("GetBookDetailSheet",{sheetId: item.id},function(response){
//			console.log("GetBookDetailSheet");
//			console.log(response);
			
			addSheets([response]);
		},function(response){
			dialogManager.openDialog({
				title: response.title,
				content: response.message,
				acknowledge: true
			});
		});
//		addSheets([{sheetId: item.id, sequenceIndex: items.length, name: item.name}]);
		
		itemsDropdown.textbox.set("value",null);
	}
	else{
		dialogManager.openDialog({
			title: "Sheet Input is Invalid",
			content: "Please select a valid sheet name prior to adding the sheet to the book!",
			acknowledge: true
		});
	}
};

function openSheetPreview(sheetId){
//	console.log("openSheetPreview:::" + sheetId);
	if(sheetId){
		window.location.href = "sheetpreview?sheetId=" + sheetId;
	}
	
}

function sheetSourceNodeCreator(item, hint) {
//	console.log("sheetSourceNodeCreator");
//	console.log(item);
//	console.log(hint);
	var tr = sheetsHtmlTable.insertRow(sheetsHtmlTable.rows.length);
	var td,a; 
	//<td style="padding: 0" ><div style="border-bottom: 1px solid gray;"></div></td>
	td = tr.insertCell(tr.cells.length);
	td.style.padding = 0;
	a = dojo.create("div",{name: "sheetSlot"});
	td.appendChild(a);
	
	td = tr.insertCell(tr.cells.length);
	td.innerHTML = (sheetsHtmlTable.rows.length);
	
	td = tr.insertCell(tr.cells.length);
	a = dojo.create("img",{src: "resources/images/sheet_21x20.png"});
	td.appendChild(a);
	

	td = tr.insertCell(tr.cells.length);
	a = dojo.create("a",{innerHTML: item.name, "class": "anchorBold", href: "sheetpreview?sheetId=" + item.sheetId});
	td.appendChild(a);
	
	td.innerHTML += (" <small>by " + item.ownerFirstName + " " + item.ownerLastName + "</small>");
	
	td = tr.insertCell(tr.cells.length);
	if(item.allowRemove){
		a = dojo.create("span",{innerHTML: "Remove Sheet" , onclick: "deleteSheetItemsByIds([" + item.sheetId + "]);", "class": "anchor"});
		td.appendChild(a);	
	}
	
	td = tr.insertCell(tr.cells.length);
//	console.log("--++-ddd");
//	console.log(item);
	if(item.allowEdit){
		a = dojo.create("a",{innerHTML: "Edit Sheet", "class": "anchor", href: "sheeteditor?sheetId=" + item.sheetId});
		td.appendChild(a);
	}
	
	item.type = [ "SHEET","ARTICLE" ];
	item.nodeId = generateId();
	var struct = {
		node : tr,
		data : item,
		type : item.type
	};
	return struct;
};

function init(){
	var sheetsDropdownContainer = dojo.byId("bookeditor.sheetsDropdownContainer");
//	sheetsDropdownContainer.appendChild(dojo.create("span",{innerHTML: "Search: "}));
	sheetsDropdownContainer.appendChild(itemsDropdown.domNode);
	
//	var sheetsVisibilityContainer = dojo.byId("bookeditor.sheetsVisibilityContainer");
//	sheetsVisibilityContainer.appendChild(sheetVisibilityInput.domNode);
	
//	dojo.byId("bookeditor.imageUploaderContainer").appendChild(imageUploader.domNode);
	dojo.byId("bookeditor.titleInputContainer").appendChild(titleInput.domNode);
	dojo.byId("bookeditor.bookSummaryInputContainer").appendChild(bookSummaryInput.domNode);
};

dojo.addOnLoad(init);
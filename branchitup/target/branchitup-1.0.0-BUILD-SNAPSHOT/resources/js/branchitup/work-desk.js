dojo.require("dojoui.Pagination");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.FilteringSelect");

var sheetlist = dojo.byId("workdesk.sheetlist");
var booklist = dojo.byId("workdesk.booklist");
var centerPane = dojo.byId("workdesk.centerPane");
var itemsTable = dojo.byId("workdesk.itemsTable");
var _sheets = null;
var _books = null;
var _folders = null;
var _blogs = [];

var itemsDropdown = new ItemsDropdown({
	id: "workdesk.itemsDropdown",
	onChange: function(obj){
//		pagination.currentPageNumber = 0;
		sheetsPagination.currentPageNumber = 0;
		
		if(obj.item.type == "SHEET"){
			conditionKey = null;
			conditionValue = null;
//			console.log("sheeteditor?sheetId=" + obj.item.id);
			window.location.href = "sheeteditor?sheetId=" + obj.item.id; 
		}
		else if(obj.item.type == "BOOK"){
			conditionKey = null;
			conditionValue = null;
			window.location.href = "bookeditor?bookId=" + obj.item.id; 
		}
		else{
			conditionKey = null;
			conditionValue = null;
		}
		
	},
	onTextChange: function(value){
//		pagination.currentPageNumber = 0;
		sheetsPagination.currentPageNumber = 0;
//		console.log("Home.onTextChange++: " + value);
		conditionKey = "BY_PHRASE";
		conditionValue = value;
		
//		browseUserWorkdesk(pagination.currentPageNumber);
	}
});

/*
createdOn
	1326573110000
	
id
	4
	
modifiedOn
	null
	
name
	"Believing the Lie"
	
type
	"BOOK"
 */
ItemsDropdown.prototype.select = function(){
	var self = this;
	branchitup.jsonServlet.doGet("SelectMatchingItems",{phrase: self.textbox.get("displayedValue"), types: ["SHEET","BOOK"], offset:0, maxResults: 3},function(response){
//		console.log("SELECT response ");
//		console.log(response);
		self.closeMenu();
		self._repositionMenu();
		self.populate(response.items);
	});
};

var sheetsSortInput = new dijit.form.FilteringSelect({
	style: "width: 100px; float: right;",
	required: true,
	onChange: function(e){
		setSheets({list: _sheets});
	},
	store : new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : [{value: "DATE", name: "Date"}, {value: "NAME", name: "Name"}]
		}
	}),
	value: "NAME"
});

var sheetsFolderInput = new dijit.form.FilteringSelect({
	style: "width: 150px; float: right",
	required: true,
	onChange: function(){
		scrollSheets(0);
		if(this.value && this.value.length > 0){
			dojo.byId("workdesk.folderLabel").innerHTML = " in \"" + this.value + "\" folder";
		}
		else{
			dojo.byId("workdesk.folderLabel").innerHTML = "";
		}
		
	},
	store : new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : []
		}
	}),
	value: "NAME"
});

var booksSortInput = new dijit.form.FilteringSelect({
	style: "width: 100px; float: right;",
	required: true,
	onChange: function(e){
		setBooks({list: _books});
	},
	store : new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : [{value: "DATE", name: "Date"}, {value: "NAME", name: "Name"}]
		}
	}),
	value: "NAME"
});

var pagination = new dojoui.Pagination({
	id: generateId(),
	pageSize: 4,
	classOfNode: "pagination",
	onBrowse: function(offset, pageSize){
//		browseUserWorkdesk(offset);
	},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

var sheetsPagination = new dojoui.Pagination({
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

var publicationsPagination = new dojoui.Pagination({
	id: generateId(),
	pageSize: 10,
	classOfNode: "pagination",
	onBrowse: function(offset, pageSize){
		scrollPublications(offset);
	},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

var booksPagination = new dojoui.Pagination({
	id: generateId(),
	pageSize: 10,
	classOfNode: "pagination",
	onBrowse: function(offset, pageSize){
		scrollBooks(offset);
	},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});


function setFolders(folders){
	_folders = folders;
	var arr = [];
//	console.log("setFolders");
//	console.log(folders);
	for(var i in folders){
		if(folders[i]){
			arr.push({value: folders[i], name: folders[i]});
		}
	}
	if(arr.length == 0) return;
	var store = new dojo.data.ItemFileReadStore( {
		data : {
			identifier : "value",
			items : arr
		}
	});
	sheetsFolderInput.set("store",store);
	sheetsFolderInput.set("value",arr[0].value);
}

function publishAsArticleToBlog(sheetId,blogId){
	console.log("publishAsArticleToBlog " + sheetId + ", " + blogId);
	
	branchitup.jsonServlet.doGet("PublishSheetToBlog",{sheetId: sheetId, blogId: blogId},function(response){
//		console.log("publishAsArticleToBlog:response:");
//		console.log(response);
		
		scrollSheets(sheetsPagination.currentPageNumber);
	});	
}

function openPublishAsArticleDialog(sheetId, blogId){
	if(!sheetId || !blogId) return;
	
	dialogManager.openDialog({
		title: "Publish Article",
		content: "You are about to publish this sheet as an article. Once the sheet is published you will not be able to edit or delete it.",
		okLabel: "Publish",
		cancelLabel: "Cancel",
		onOK: function(){
			publishAsArticleToBlog(sheetId,blogId)
			dialogManager.closeDialog();
		}
	});
}

function setSheets(sheets){
//	console.log("setSheets");
//	console.log(sheets);
	var sortBy = sheetsSortInput.get("value");
	
	_sheets =  sheets.list;
	
	if(sortBy == "NAME"){
		_sheets = sheets.list.sort(function(a,b){
			if(a.name == b.name) return 0;
			return (a.name > b.name) ? -1 : 1;
		});
	}
	else if(sortBy == "DATE"){
		_sheets = sheets.list.sort(function(a,b){
			if(a.createdOn == b.createdOn) return 0;
			return (a.createdOn > b.createdOn) ? 1 : -1;
		});
	}
	
	var pane = dojo.byId("workDesk.sheetsPane");
	pane.innerHTML = "";
	var t = dojo.create("table",{id:"workdesk.sheetsTable", "class":"listTable"});
	pane.appendChild(t);
	var tr,td,a;
	if(_sheets.length > 0){
		for(var i in _sheets){
			var item = _sheets[i];
			
			tr = t.insertRow(t.rows.length, {});
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("img",{src: "resources/images/sheet_21x20.png"});
			td.appendChild(a);
			
			td = tr.insertCell(tr.cells.length);
			if(item.type == 'ARTICLE'){
				a = dojo.create("a",{innerHTML: item.name , href: "article?articleId=" + item.id, "class": "anchorBold"});
			}
			else{
				a = dojo.create("a",{innerHTML: item.name , href: "sheetpreview?sheetId="+item.id, "class": "anchorBold"});	
			}
			
			td.appendChild(a);
			
			td.innerHTML += (", created on " + branchitup.formatDate(new Date(item.createdOn)));
			
			
			td = tr.insertCell(tr.cells.length);
			if(item.type == 'SHEET'){
				a = dojo.create("a",{innerHTML: "Delete Sheet" , onclick: "openDeleteSheetDialog(" + item.id + ");", "class": "anchor"});
				td.appendChild(a);
			}
			else if(item.type == 'ARTICLE'){
				a = dojo.create("a",{innerHTML: "Published", href: "article?articleId=" + item.id});
				a.style.color = 'gray';
				a.style.textDecoration = "none";
				td.appendChild(a);
			}
			
			
			td = tr.insertCell(tr.cells.length);
			if(item.type == 'SHEET'){
				a = dojo.create("a",{innerHTML: "Edit Sheet" , href: "sheeteditor?sheetId="+item.id, "class": "anchor"});
				td.appendChild(a);
			}
			
			td = tr.insertCell(tr.cells.length);
			if(item.type == 'SHEET' && blogs.length > 0){
				for(var j = 0 ; j < blogs.length ; j++){
					a = dojo.create("a",{innerHTML: "Publish Article to '" + blogs[j].name + "'" , onclick: "openPublishAsArticleDialog(" + item.id + ", " + blogs[j].blogId + ");", "class": "anchor"});
					td.appendChild(a);
					
					if(j < blogs.length-2){
						td.appendChild(dojo.create("label",{innerHTML: ", "}));
					}
				}
			}
		}
	}
	else{
		pane.innerHTML = "NO ITEMS ARE AVAILABLE&nbsp;<a href='javascript:window.open(\"sheeteditor\")'>Create new Sheet</a>";
	}
	
}
function openSheetEditor(){
//	window.open("sheeteditor");
	window.location.href = "sheeteditor";
}
function openDeleteSheetDialog(sheetId){
//	console.log("openDeleteSheetDialog: " + sheetId);
	if(!sheetId) return;
	
	dialogManager.openDialog({
		title: "Confirm Delete Sheet",
		content: "You are about to permanently delete a sheet from your workdesk. Note that this sheet will not be removed from other books that are currently referencing this sheet<br/> Continue?",
		onOK: function(){
			deleteSheet(sheetId);
			dialogManager.closeDialog();
		}
	});
}
function deleteSheet(sheetId){
	if(sheetId){
		branchitup.jsonServlet.doGet("DeleteSheet",{sheetId: sheetId},function(response){
//			console.log("DeleteSheet:response:");
//			console.log(response);
			var d = dojo.byId("workdesk.sheet." + sheetId);
			if(d){
				var parentNode = d.parentNode;
				parentNode.removeChild(d);
			}
			scrollSheets(sheetsPagination.currentPageNumber);
		});	
	}
}

function openSheet(sheetId){
	if(sheetId){
		window.location.href="sheeteditor?sheetId=" + sheetId;
	}
}

function openSheetPreview(sheetId){
//	console.log("openSheetPreview:::" + sheetId);
	if(sheetId){
		window.location.href = "sheetpreview?sheetId=" + sheetId;
	}
}

function deleteBook(bookId){
	if(bookId){
		branchitup.jsonServlet.doGet("DeleteBook",{bookId: bookId},function(response){
//			console.log("DeleteBook:response:");
//			console.log(response);
			if(response.deletedCount > 0){
				scrollBooks(0);
				scrollSheets(0);
			}
//			dialogManager.closeDialog();
		},function(response){
			dialogManager.openDialog({
				title: "Problem Deleting Book",
				content: response.message,
				acknowledge: true
			});
		});	
	}
}

/*
 You are about to permanently delete the book from your workdesk.
All associated sheets that were created as part of this compilation will be deleted as well.
Sheets that have been attached separately will not be affected (will remain with no change) 
<br/><br/>Are you sure?

 */
function openDeleteBookDialog(bookId,title){
//	console.log("openDeleteBookDialog: ");
//	console.log(book);
//	return;
	
	if(!bookId) return;
	var s;
	if(title && title.length > 0){
		s = "You are about to permanently delete the book '" + title + "' from your workdesk.<br/><br/>Are you sure?";
	}
	else{
		s = "You are about to permanently delete the book from your workdesk.<br/><br/>Are you sure?";
	}
	
	dialogManager.openDialog({
		title: "Confirm Delete Book",
		content: s,
		onOK: function(){
			deleteBook(bookId);
		},
		dismiss: true
	});
}
function openBook(bookId){
	console.log("openBook: " + bookId);
	if(bookId){
		window.location.href="bookeditor?bookId=" + bookId;
	}
}

//-------BRANCH 
function createBranchConfirmDom(title){
	t = dojo.create("div");
	var d = dojo.create("img",{src: "resources/images/branch_47x50.png"});
	d.style.float = "left";
	t.appendChild(d);
	t.appendChild(dojo.create("span",{innerHTML: ("You are about to branch: '" + title + "'.</br><br/>Branching this publication will allow you to illustrate, translate, edit, or otherwise enhance your own work<br/></br></br>")}));

	return t;
};


function branch(bookId){
	branchitup.jsonServlet.doGet("BranchPublishedBook",{bookId: bookId},function(response){
		window.location.href = "workdesk";
	},
	function(){
		dialogManager.openDialog({
			title: "Problem during branching",
			content: "There was an unexpected problem during the process of branching, please try again!",
			acknowledge: true
		});
	});
};

function showBranchConfirm(bookId, title){
	dialogManager.openDialog({
		title: "Branch Your Own Book " + title, 
		content: createBranchConfirmDom(title), 
		style: "width: 500px; height: 400px; vertical-align: top;",
		onOK: function(){
			branch(bookId);
		},
		okLabel: "Branch This Book"
	});
};
//------

function setBooks(books){
//	console.log("setBooks");
//	console.log(books);
	
	var sortBy = booksSortInput.get("value");
	_books =  books.list;
	if(sortBy == "NAME"){
		_books = books.list.sort(function(a,b){
			if(a.name == b.name) return 0;
			return (a.name > b.name) ? -1 : 1;
		});
	}
	else if(sortBy == "DATE"){
		_books = books.list.sort(function(a,b){
			if(a.createdOn == b.createdOn) return 0;
			return (a.createdOn > b.createdOn) ? 1 : -1;
		});
	}
	
	var pane = dojo.byId("workDesk.booksPane");
	pane.innerHTML = "";
	if(_books.length > 0){
		var t = dojo.create("table",{id: "workdesk.booksTable", "class":"listTable"});
		var tr,td,a;
		for(var i in _books){
			var item = _books[i];
			tr = t.insertRow(t.rows.length);
			tr.setAttribute("id","bookNode." + item.id);
//			console.log("here is item::");
//			console.log(item);
			//
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("img",{src: "resources/images/book_20x20.png"});
			td.appendChild(a);
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("a",{innerHTML: item.name , href: "publishedbook?bookId="+item.id, "class": "anchorBold"});
			td.appendChild(a);
			
			td.innerHTML += (",&nbsp;created on " + branchitup.formatDate(new Date(item.createdOn)));
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("a",{innerHTML: "Publish Book", href: "publishbook?bookId="+item.id, "class": "anchor"});
			
			td.appendChild(a);
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("a",{innerHTML: "Edit Book", href: "bookeditor?bookId="+item.id, "class": "anchor"});
			td.appendChild(a);
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("label",{innerHTML: "Delete Book", onclick: "openDeleteBookDialog(" + item.id + ",'" + item.name + "');", "class": "anchor"});
			td.appendChild(a);
		}
		pane.appendChild(t);
	}
	else{
		pane.innerHTML = "NO ITEMS ARE AVAILABLE&nbsp;<a href='bookeditor'>Compile new Book</a>";
	}
}
//-----------BOOKS
function scrollBooks(offset){
	var args = {offset: offset, maxResults: booksPagination.pageSize};
	branchitup.jsonServlet.doGet("ScrollBookUserItems",args,function(response){
//		console.log("ScrollBookUserItems:response:");
//		console.log(response);
		
		setBooks({list: response.items, count: response.count});
		
		booksPagination.set("totalResults",response.count);
		booksPagination.refresh();
	});
	
}


//-----------SHEETS
function scrollSheets(offset){
	var folder = sheetsFolderInput.get("value");
	if(!folder) {
//		sheetsFolderInput.set("value",sheetsFolderInput.store.data.items[0].value);
		return;
	}
	
	var args = {offset: offset, maxResults: sheetsPagination.pageSize, folder: folder};
	branchitup.jsonServlet.doGet("ScrollSheetUserItems",args,function(response){
//		console.log("ScrollSheetUserItems:response:");
//		console.log(response);
		
		setSheets({list: response.items, count: response.count});
		
		sheetsPagination.set("totalResults",response.count);
		sheetsPagination.refresh();
	});
	
}

//----------------------PUBLICATIONS

function setPublications(publications){
//	console.log("setPublications");
//	console.log(arr);
	var sortBy = 'NAME';
	
	var arr =  publications.list;
	
	if(sortBy == "NAME"){
		arr = publications.list.sort(function(a,b){
			if(a.title == b.title) return 0;
			return (a.title > b.title) ? -1 : 1;
		});
	}
	else if(sortBy == "DATE"){
		arr = publications.list.sort(function(a,b){
			if(a.createdOn == b.createdOn) return 0;
			return (a.createdOn > b.createdOn) ? 1 : -1;
		});
	}
	
	var pane = dojo.byId("workDesk.publicationsPane");
	var t = dojo.create("table", {"id":"workdesk.publicationsList", "class":"listTable"});
	var tr, td, a;
	pane.innerHTML = "";
	pane.appendChild(t);
	if(arr.length > 0){
		for(var i in arr){
			var item = arr[i];
//			console.log("HERERe");
//			console.log(item);
			
			tr = t.insertRow(t.rows.length);
			tr.setAttribute("id","publicationNode." + item.bookId);
			
			td = tr.insertCell(tr.cells.length);
			if(item.coverImageUrl){
				a = dojo.create("img",{name: "publicationThumbnail", src: item.coverImageUrl});
			}
			else{
				a = dojo.create("img",{name: "publicationThumbnail", src: "resources/images/no-image-branchitup_200x200.png"});
			}
			td.appendChild(a);
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("a",{"class": "anchorBold", innerHTML: item.title, href: "publishedbook?bookId=" + item.bookId});
			td.appendChild(a);
			
			td.innerHTML += (",&nbsp;published on " + branchitup.formatDate(new Date(item.createdOn)));
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("span",{"class": "anchor", innerHTML: "Delete", onclick: "showDeleteConfirmation(" + item.bookId + ", \"" + item.title + "\");"});
			td.appendChild(a);
			
			td = tr.insertCell(tr.cells.length);
			a = dojo.create("span",{"class": "anchor", innerHTML: "Branch", onclick: "showBranchConfirm(" + item.bookId + ", \"" + item.title + "\");"});
			td.appendChild(a);
		}
	}
	else{
		pane.innerHTML = "NO ITEMS ARE AVAILABLE";
	}
	
}

function scrollPublications(offset){
	var args = {offset: offset, maxResults: publicationsPagination.pageSize};
	branchitup.jsonServlet.doGet("ScrollUserPublishedBookDetails",args,function(response){
//		console.log("ScrollPublishedBookDetails:response:");
//		console.log(response);
		
		setPublications({list: response.publications, count: response.count});
		
		publicationsPagination.set("totalResults",response.count);
		publicationsPagination.refresh();
	});
}

function showDeleteConfirmation(bookId, title){
//	console.log("showDeleteConfirmation: " + bookId);
	if(title && title.length > 0){
		dialogManager.openDialog({
			title: "Delete " + title, 
			content: "You are about to permanently delete the book '" + title + "'. <br/>Do you want to continue?", 
			style: "width: 300px; height: 200px;",
			onOK: function(){
				deletePublication(bookId);
			},
			dismiss: true,
			okLabel: "Delete"
		});
	}
	else{
		dialogManager.openDialog({
			title: "Delete Your Publication", 
			content: "You are about to permanently delete your publication.<br/>Do you want to continue?", 
			style: "width: 300px; height: 200px;",
			onOK: function(){
				deletePublication(bookId);
			},
			okLabel: "Delete"
		});
	}
}
function deletePublication(bookId){
//	console.log("deletePublication " + bookId);
	branchitup.jsonServlet.doGet("DeletePublication",{bookId: bookId},function(response){
//		console.log("deletePublication.response");
//		console.log(response);
		if(response.recordsCount > 0){
//			window.location.href = "workdesk";
//			"workdesk.li." + item.bookId
//			var d = dojo.byId("publicationNode." + bookId);
//			if(d){
//				d.parentNode.removeChild(d);
//			}
			
			scrollPublications(0);
		}
		else{
			dialogManager.openDialog({
				title: "No Record Was Deleted", 
				content: "There are other branches of this publication.  Therefore, the publication cannot be deleted.  Instead, the system will set its status to Hidden.", 
				style: "width: 400px; height: 200px;",
				acknowledge: true
			});
		}
	},
	function(response){
//		console.log("deletePublication.FAIL:::");
//		console.log(response);
		dialogManager.openDialog({
			title: "Delete Publication Failed...", 
			content: response.message, 
			style: "width: 300px; height: 200px;",
			acknowledge: true
		});
	});
}

function init(){
	var d; 
	d = dojo.byId("header.searchbarContainer");
	d.appendChild(itemsDropdown.domNode);
	
	dojo.byId("workDesk.sheetsHeader").appendChild(dojo.create("label",{innerHTML: "&nbsp;&nbsp;", "style":"padding-left: 20px;"}));
	dojo.byId("workDesk.sheetsHeader").appendChild(sheetsPagination.domNode);
	
	dojo.byId("workDesk.sheetsHeader").appendChild(sheetsSortInput.domNode);
	dojo.byId("workDesk.sheetsHeader").appendChild(dojo.create("label",{innerHTML: "Sort By: ", "style":"float: right"}));
	dojo.byId("workDesk.sheetsHeader").appendChild(sheetsFolderInput.domNode);
	dojo.byId("workDesk.sheetsHeader").appendChild(dojo.create("label",{innerHTML: "Folder: ", "style":"float: right"}));
	
	
	dojo.byId("workDesk.booksHeader").appendChild(booksSortInput.domNode);
	dojo.byId("workDesk.booksHeader").appendChild(dojo.create("label",{innerHTML: "Sort By: ", "style":"float: right"}));
//	dojo.byId("workdesk.paginationCell").appendChild(pagination.domNode);
	
	dojo.byId("workDesk.booksHeader").appendChild(booksPagination.domNode);
	dojo.byId("workDesk.publicationsHeader").appendChild(publicationsPagination.domNode);
}

function onLoad(){
	init();
	scrollPublications(publicationsPagination.currentPageNumber);
	scrollBooks(booksPagination.currentPageNumber);
}
dojo.addOnLoad(onLoad);


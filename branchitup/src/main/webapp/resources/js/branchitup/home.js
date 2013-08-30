dojo.require("dojoui.Pagination");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.ValidationTextBox"); //required to avoid dijit.showTooltip is not a function
dojo.require("dijit.TooltipDialog");

var centerPane = dojo.byId("centerPane");
var conditionKey = null;
var conditionValue = null;

var itemsDropdown = new ItemsDropdown({
	id: "home.itemsDropdown",
	onChange: function(obj){
//		console.log("--->this is my onchange function");
//		console.log(obj);
		pagination.currentPageNumber = 0;
		if(obj.item.type == "GENRE"){
			conditionKey = "BY_GENRE";
			conditionValue = obj.item.id;
			scrollPublishedBooks(pagination.currentPageNumber);
		}
		else if(obj.item.type == "USER"){
			conditionKey = "BY_PUBLISHER";
			conditionValue = obj.item.id;
			scrollPublishedBooks(pagination.currentPageNumber);
		}
		else if(obj.item.type == "BOOK"){
			conditionKey = null;
			conditionValue = null;
			window.location.href = "publishedbook?bookId=" + obj.item.id; 
		}
		else if(obj.item.type == "SHEET"){
			conditionKey = null;
			conditionValue = null;
			window.location.href = "sheetpreview?sheetId=" + obj.item.id; 
		}
		else{
			conditionKey = null;
			conditionValue = null;
			scrollPublishedBooks(pagination.currentPageNumber);
		}
		
	},
	onTextChange: function(value){
		pagination.currentPageNumber = 0;
//		console.log("Home.onTextChange++: " + value);
		conditionKey = "BY_PHRASE";
		conditionValue = value;
		
		scrollPublishedBooks(pagination.currentPageNumber);
	}
});
var pagination = new dojoui.Pagination({
	pageSize: 6,
	id: "home-pagination",
	onBrowse: function(offset){scrollPublishedBooks(offset);},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});
initToolbar();

var setPublications = function(arr){
	//280 depends on home.css
	var itemsPerRow = Math.floor((window.screen.width-450)/280); //screenWidth-450 (right page) / 280 (width of each item)
	centerPane.innerHTML = "";
	//"class":"publicationItem"
	var t = dojo.create("table",{"border": "0px","class":"publicationItems"});
	var tr,td,row,cell;
	
	for(var i in arr){
		//addPublicationRecord(arr[i]);
		row = parseInt(i/itemsPerRow);
		cell = i%itemsPerRow;
		if(t.rows.length <= row){
			tr = t.insertRow(t.rows.length);
		}
		else{
			tr = t.rows[t.rows.length-1];
		}
		td = tr.insertCell(cell);
		td.setAttribute("class","publicationItem");
		td.appendChild((new VPublicationItem({publication: arr[i]})).domNode);
	}
	centerPane.appendChild(t);
};

//var setPublications = function(arr){
//	centerPane.innerxHTML = "";
////	var div = dojo.create("div",{"border": "0px","class":"publicationItems"});
//	for(var i in arr){
//		var p = new VPublicationItem({publication: arr[i]});
//		p.domNode.className = "publicationItem";
////		var p = dojo.create("div",{innerHTML: "ITEM " + i, style: "width: 200px; height: 500px; display: inline-block;"});
//		
//		centerPane.appendChild(p.domNode);
//	}
////	centerPane.appendChild(div);
//};

function showNoItemsMessage(){
	centerPane.innerHTML = "<div class='noItemsHeading'>NO ITEMS FOUND</div>";
}

var scrollPublishedBooks = function(offset,callback){
	var args = {offset: offset, maxResults: pagination.pageSize};
	if(conditionKey){
		args['conditionKey'] = conditionKey;
		args['conditionValue'] = conditionValue;
	}
//	console.log("--->scrollPublishedBooks: ");
//	console.log(args);
	branchitup.jsonServlet.doGet("ScrollPublishedBookDetails",args,function(response){
		pagination.set("totalResults",response.count);
		pagination.refresh();
		
		if(response.count > 0){
			setPublications(response.publications);
		}
		else{
			showNoItemsMessage();
		}
//		console.log("home.scrollPublishedBooks:after render::, " + response.count);
//		console.log(response);
		if(callback){
			callback();
		}
	});
//	console.log("scrollPublishedBooks:::");
//	console.log(_pagination);
};

function initToolbar(){
	var d; 
	d = dojo.byId("home.paginationCell");
	d.appendChild(pagination.domNode);
	
	d = dojo.byId("header.searchbarContainer"); 
	d.appendChild(itemsDropdown.domNode);
}

function reload(callback){
	scrollPublishedBooks(pagination.currentPageNumber, callback);
}
function onLoad(){
	reload();
}
dojo.addOnLoad(onLoad);
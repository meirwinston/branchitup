dojo.require("dojoui.FilteringSelect");
dojo.require("dojoui.SimpleTextarea");
dojo.require("dojoui.UploadButton");
dojo.require("dojoui.Pagination");

var centerPane = dojo.byId("genres.centerPane");
//var toolbar = null; //dijit.byId("genres.toolbar");
var conditionKey = null;
var conditionValue = null;

var itemsDropdown = new GenresDropdown({
	onChange: function(o){
//		console.log("GenreDropdown, this is my onchange function");
//		console.log(o);
		if(o && o.item && o.item.id){
			window.location.href = "genre?genreId=" + o.item.id;
		}
		
	},
	onTextChange: function(value){
		pagination.currentPageNumber = 0;
		conditionKey = "BY_PHRASE";
		conditionValue = value;
		
		browseGenres(0);
	}
});

//this.newGenreButton = new dojoui.Button({
//	id: generateId(),
//	label: "<span style='color: blue; cursor: pointer; text-decoration: underline;'>New Genre</span>",
//	onClick: function(){
//		openGenreEditor({openFor: "new"});
//	}
//});

var pagination = new dojoui.Pagination({
	id: generateId(),
	pageSize: 10,
	classOfNode: "pagination",
	onBrowse: function(offset, pageSize){
		browseGenres(offset);
	},
	classOfSelectedLabel: "paginationSelectedLabel",
	classOfUnselectedLabel: "paginationUnselectedLabel",
	classNextBack: "paginationNextBack"
});

//var newGenre = new dojoui.Label({value: "New Genre", style: "color: white; cursor: pointer;"});
//newGenre.domNode.onclick = function(){window.location.href = "genreeditor"};

function initToolbar(){
	var d; 
	d = dojo.byId("genres.paginationCell");
	d.appendChild(pagination.domNode);
	
	d = dojo.byId("header.searchbarContainer"); 
	d.appendChild(itemsDropdown.domNode);
	
//	var d; 
//	d = dojo.byId("toolbar.searchBox");
//	d.appendChild(itemsDropdown.domNode);
//	
//	dojo.byId("genres.paginationCell").appendChild(pagination.domNode);
//	
//	d = dojo.byId("toolbar.searchButton");
//	var b = new dijit.form.Button({label: "Search", onClick: dojo.hitch(itemsDropdown,itemsDropdown.doSelectAction)});
//	d.appendChild(b.domNode);
}

function showNoItemsMessage(){
	centerPane.innerHTML = "<div class='noItemsHeading'>NO ITEMS FOUND</div>";
}

function browseGenres(offset, callback){
//	console.log("GenresBrowser.browseGenres::"+callback);
//	return;
	
//	var self = this;
	var args = {offset: offset, maxResults: pagination.pageSize};
	if(conditionKey){
		args['conditionKey'] = conditionKey;
		args['conditionValue'] = conditionValue;
	}
	
	branchitup.jsonServlet.doGet("ScrollGenreDetails",args,function(response){
//		console.log("browseGenres:response:");
//		console.log(response);
		pagination.set("totalResults",response.count);
		pagination.refresh();
		if(response.count > 0){
			setItems(response.genresList);			
		}
		else{
			showNoItemsMessage();
		}
		
		if(callback){
			callback(response);
		}
	});
};
//--

/*
 createdOn
1334116488000

description
"des update1"

genreId
116

iconImageFileId
22155

name
"my genre"

originatorAccountId
4
 */
function addItem(item){
	var d = dojo.create("div",{name: "genreItem"});
	if(item.iconImageFileId){
		//d.appendChild(dojo.create("img",{src: ("imageservice?id=" + item.iconImageFileId + "&thumbnail")}));
	}
	else{
		//d.appendChild(dojo.create("img",{src: "resources/images/no-image-branchitup_200x200.png"}));
	}
	
	d.appendChild(dojo.create("span",{onclick: "javascript:window.location.href='genre?genreId=" + item.genreId + "'", name:"genreTitle", innerHTML: item.name + "<br/>"}));
	if(item.modifiedOn){
		d.appendChild(dojo.create("span",{name: "genreDate", innerHTML: branchitup.formatMillisToDate(item.modifiedOn)}));
	}
	else{
		d.appendChild(dojo.create("span",{name: "genreDate", innerHTML: branchitup.formatMillisToDate(item.createdOn)}));
	}
	if(item.description){
		d.appendChild(dojo.create("p",{innerHTML: item.description}));
	}
	else{
		d.appendChild(dojo.create("p",{innerHTML: ""}));
	}
	
	d.appendChild(dojo.create("a",{innerHTML: "edit", href: "genreeditor?genreId=" + item.genreId}));
	centerPane.appendChild(d);
}

function setItems(items){
//	console.log("genres--->setItems");
	dojo.byId("genres.centerPane").innerHTML = "";
	for(var i in items){
		addItem(items[i]);
	}
};

function reload(callback){
	browseGenres(0);
}
function onLoad(){
	initToolbar();
	reload();
}
dojo.addOnLoad(onLoad);
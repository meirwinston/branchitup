dojo.require("dojoui.Pagination");
dojo.require("dojoui.Toolbar");
dojo.require("dijit.form.Button");
dojo.require("dijit.Toolbar");
dojo.require("dojoui.Label");
dojo.require("dojoui.SimpleTable");
dojo.require ( "dijit.ToolbarSeparator" );
dojo.require("dijit.form.ValidationTextBox"); //required to avoid dijit.showTooltip is not a function

(function(){
//	var _self = this;
	var centerPane = dojo.byId("centerPane");
	var paginationPane = dojo.byId("paginationPane");
//	var searchPane = dojo.byId("searchPane");
	
	
	var pagination = new dojoui.Pagination({
		pageSize: 10,
		onBrowse: function(offset){
			browseSheets(offset,null);
		},
		classOfSelectedLabel: "paginationSelectedLabel",
		classOfUnselectedLabel: "paginationUnselectedLabel",
		classNextBack: "paginationNextBack"
	});
	paginationPane.appendChild(pagination.domNode);
	
	var columnStructure = [
	    {name: "Name", field: 'name', width: '100px'},
		{name: "Owner", field: 'ownerFirstName', width: '250px'},
		{name: "Genre", field: 'genreName', width: '300px'}
	];
	
	var sheetsTable = new dojoui.SimpleTable({
		id: generateId(),
		style: "font-size: 12px; color: #5f6d81; font: 'normal 14px arial'",
		structure: { cells: columnStructure },
		selectionMode: "single"
	});
	
	var browseSheets = function(offset, callback){
		branchitup.jsonServlet.doGet("BrowseSheets",{offset: offset, maxResults: pagination.pageSize},function(response){
			console.log("browseSheets::2::response");
			console.log(response);
			
			pagination.set("totalResults",response.count);
			pagination.refresh();
			sheetsTable.setItems(response.sheets);
			if(callback){
				callback(response);
			}
		});
	};
	
	centerPane.appendChild(sheetsTable.domNode);
	                	
	function reload(callback){
		browseSheets(pagination.currentPageNumber);
	}
	function onLoad(){
		reload();
	}
	dojo.addOnLoad(onLoad);
}());

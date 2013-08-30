/* Dojo v1.4 */
if(!dojo._hasResource["dojoui.DataGrid"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.DataGrid"] = true;
dojo.provide("dojoui.DataGrid");

dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dojo.data.ItemFileReadStore");

dojo.declare("dojoui.DataGrid", dojox.grid.DataGrid, {
	identifier: "id", //default value
	autoWidth: false, //solves the problem with horizontal scrollbar, header does not sort and not clickable
	autoHeight: false, //solves the problem with vertical scroll not showing all rows
	clientSort: true,
	selectionMode: "multiple", //single,none,extended
	rowsPerPage: 40,
	comparatorMap: null,
	rowSelector: '20px',
	itemsMap: {},
	_gridData: {
		identifier: this.identifier,
		label: this.identifier,
		items: [] //not in presentation order
	},
	postCreate: function(){
		if(this.store == null){
//			this.store = new dojo.data.ItemFileWriteStore({data: this._gridData, typeMap: this.typeMap});
			this.store = new dojo.data.ItemFileWriteStore({data: this._gridData});
			if ( this.comparatorMap )
			{
				this.store.comparatorMap = this.comparatorMap;
			}
		}
		this.inherited(arguments);
	},
	sortByColumnIndex: function(colIndex, asc){
		if(asc == true){
			asc = 1;
		}
		else{
			asc = -1;
		}
		var cell = this.getCell(colIndex);
		
		if(this.dnd&&!this.dndRowConn){
			this.dndRowConn=dojo.connect(this.select,"startMoveRows",dojo.hitch(this,this.clearSort));
		}
		
		this.retainLastRowSelection();
		this.inSorting=true;
		this.sortAttrs = [{attr: cell.field, asc: asc, cell: cell, cellNode: null}];
		this._unarySortCell.cell = cell;
		if(this.canSort()){
			this.sort();
			this.edit.info={};
			this.update();
		}
		this.inSorting=false;
		
	},
	isFilterActivated: function(){
		return (typeof this.query != 'undefined') && (this.query != null);
	},
	_updateItemsMap: function(){
		var arr = this.store._arrayOfTopLevelItems;
		this.itemsMap = {};
		for(var j in arr){
			this.itemsMap[arr[j][this.identifier]] = arr[j];
		}
	},
	addItems: function(items){
		for(var i in items){
			this.itemsMap[items[i][this.identifier]] = this.store.newItem(items[i]);
		}
//		this.store.save();//this might cause problems, then move it back inside the loop
//		this.sort();
//		
//		if(this.isFilterActivated()){
//			this._refresh(true);
//		}
		setTimeout(dojo.hitch(this,this._refresh),0);

	},
//		startup: function(){
//			this.inherited(arguments);
//			var headerNodes = dojo.query("[role*='columnheader']", this.viewsHeaderNode);
//			dojo.forEach(headerNodes, function(node, index){
//				node.setAttribute("columnIndex",index);
//			}, this);
//		},
	getRowsPerPage: function(){
		return this.scroller.rowsPerPage;
	},
	connectOnMouseDown: function(f){
		return dojo.connect(this, "onMouseDown", f);
	},
	
	connectOnHeaderCellClick: function(f){
		return dojo.connect(this, "onHeaderCellClick", f);
	},
	
	connectOnStyleRow: function(f){ //the function declares one parameter n with(index,domNode);
		return dojo.connect(this, "onStyleRow", f);
	},
	
	connectOnRowSelected: function(f){
		return dojo.connect(this, "onRowClick", f);
	},
	
	connectOnCellClick: function(f){ //f(event)
		return dojo.connect(this, "onCellClick", f);
	},
	connectOnApplyCellEdit: function(f){//not checked
		return dojo.connect(this, "onApplyCellEdit", f);
		//dojo.connect(grid, "onApplyCellEdit", dojo.partial(onApplyCellEditHandler, grid));
//			 function onApplyCellEditHandler(inGrid, inValue, inRowIndex, inFieldIndex) {
//		            jsonStore.save();
//		            inGrid.updateRow(inRowIndex);
//		        } 
	},
	removeSelected: function(){
		var items = this.getSelected();
		this.removeSelectedRows();
		for(var i in items){
			delete this.itemsMap[items[i][this.identifier]];
		}
		return items;
	},
	removeItems: function(items){
		this.selection.clear();
		for ( var i = 0; i < items.length; i++) {
			delete this.itemsMap[items[i][this.identifier]];
			this.store.deleteItem(items[i]);
//			this.store.save();
		}
		
		this.store.save(); //this might cause problems, then move it back inside the loop
	},
	clear: function(){
		var items = this.getAllItems();
		for ( var i = 0; i < items.length; i++) {
			this.store.deleteItem(items[i]);
		}
		
		this.store.save();
	},
	removeByIds: function(ids){
		this.selection.clear();
		for(var i in ids){
			var item = this.itemsMap[ids[i]];
			if(typeof item != 'undefined' && item != null){
				this.store.deleteItem(item);
			}
			delete this.itemsMap[ids[i]];
		}
//		this.store.save(); //this might cause problems
		setTimeout(dojo.hitch(this,this._refresh),0);
	},
//	removeByIds: function(ids){
//		this.selection.clear();
//		for(var i in ids){
////				console.log("removeById:::::"+ids[i] + ", " + this.store.fetchItemByIdentity({identity: ids[i], onItem: function(e){console.log(e);}}));
//			
//			var items = this._gridData.items;
//			for(var j in items){
//				if(items[j][this.identifier] == ids[i]){
//					this.store.deleteItem(items[j]);
//					delete this.itemsMap[ids[i]];
//				}
//			}
//			this.store.save(); //this might cause problems, then move it back inside the loop
//		}
//	},
	getHeaderLabels: function(){
		var nodes = dojo.query("[id*='caption']", this.viewsHeaderNode);
		var arr = new Array(nodes.length);
		dojo.forEach(nodes, function(node, index){
			//console.log(index +", " + node.textContent +", " + node.innerHTML);
			arr[index] = node.textContent;
		}, this);
		return arr;
	},
	//not a good way to use for style, sorting the table or re-rendering will overwrite this style
	setCellStyle__: function(itemId,cellIndex,style){
		var rowIndex = this.getIndexById(itemId);
		var node = this.getCellNode(rowIndex,cellIndex);
		for(var k in style){
			node.style[k] = style[k];
		}
	},
	getRowNodes: function(){
		return this.views.views[1].rowNodes; //a Map Object of Div elements with indexes as keys
		//e.g. <table style="height: 24px;" class="dojoxGridRowTable" role="presentation" border="0" cellpadding="0" cellspacing="0"><tbody><tr><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="0" style="width: 250px;">...</td><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="1" style="width: 80px;">n1</td><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="2" style="width: 100px;">MINOR</td><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="3" style="width: 200px;">...</td><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="4" style="width: 200px;">...</td><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="5" style="width: 200px;">...</td><td tabindex="-1" role="gridcell" class="dojoxGridCell" idx="6" style="width: 200px;">...</td></tr></tbody></table>
	},
	getRowNode: function(index){
		return this.views.views[1].getRowNode(index);
	},
	getCellNode: function(rowIndex, cellIndex){
		return this.views.views[1].getCellNode(rowIndex,cellIndex);
	},
	unselectAll: function(){
		this.selection.clear();
	},
	addItem: function(newItem){
		this.addItems([newItem]);
	},
	filter: function(query){//query example: {inventoryState: 'ONSITE' }
		//sets this.query
		this.setQuery(query,{ignoreCase:true, deep: false, cache: true});
	},
	getAllItems: function(){ //ignore filter
		return this.store._arrayOfTopLevelItems;
	},
	getSelectedItems: function(){
		var items = this.selection.getSelected();
		return items;
	},
	_updateItemsMap: function(items){
		for(var i = 0 ; i < items.length ; i++){
			this.itemsMap[items[i][this.identifier]] = items[i]; 
		}
	},
	setItems: function(arr){
		this._gridData = { //identifier: _identifier,
			label: this.identifier,
			items: arr
		};
		this._updateItemsMap(arr);
//		this.store = new dojo.data.ItemFileWriteStore({data: this._gridData, typeMap: this.typeMap});
		this.store = new dojo.data.ItemFileWriteStore({data: this._gridData });
		if ( this.comparatorMap )
		{
			this.store.comparatorMap = this.comparatorMap;
		}
		this.setStore(this.store);
	},
	setReadOnlyItems: function(arr){
		this._gridData = { //identifier: _identifier,
			label: this.identifier,
			items: arr
		};
		this._updateItemsMap(arr);
		this.store = new dojo.data.ItemFileReadStore({data: this._gridData });
		if ( this.comparatorMap )
		{
			this.store.comparatorMap = this.comparatorMap;
		}
		this.setStore(this.store);
	},
	getRowData: function(index){
		return this.getItem(index);
	},
//		getColumnIndexes: function(){ 
////			printObject(this.structure[0].cells[0]);
////			var nodes = dojo.query("[role*='columnheader']", this.viewsHeaderNode);
////			dojo.forEach(nodes, function(node, index){
////				console.log(node);
////			}, this);
//			console.log(this.plugins);
//		},
	getHeaderNodes: function(){ //in order
		var nodes = dojo.query("[role*='columnheader']", this.viewsHeaderNode);
		var arr = new Array(nodes.length);
		dojo.forEach(nodes, function(node, index){
			//console.log(index +", " + node.textContent +", " + node.innerHTML);
			arr[index] = node;
		}, this);
		return arr;
	},
	getHeaderLabelNodes: function(){ //in order
		var nodes = dojo.query("[id*='caption']", this.viewsHeaderNode);
		var arr = new Array(nodes.length);
		dojo.forEach(nodes, function(node, index){
			//console.log(index +", " + node.textContent +", " + node.innerHTML);
			arr[index] = node;
		}, this);
		return arr;
	},
	loadAllItems: function(){
		this.store.fetch({
			start: 0,
			count: this.rowCount,
			query: this.query,
			sort: this.getSortProps(),
			queryOptions: this.queryOptions,
			isRender: this.isRender,
			onBegin: dojo.hitch(this, "_onFetchBegin"),
			onComplete: dojo.hitch(this, "_onFetchComplete"),
			onError: dojo.hitch(this, "_onFetchError")
		});
	},
	getDisplayedItems: function(){
		var items = [];
		var count = this.rowCount;
		for(var i = 0 ; i < count ; i++){			
			var item = this.getItem(i);
			if(item != null){
				items.push(item);
			}
		}
		return items;
	},
	getItemByIndex: function(index){
		return this.getItem(index);
	},
	getItems: function(){ //don't ignore filter
		var items = [];
		var count = this.rowCount;
		this.loadAllItems();
		
		for(var i = 0 ; i < count ; i++){
			var item = this.getItem(i);
			items.push(item);
		}
		return items;
	},
	getSortedItems: function(){ //don't ignore filter
		var items = this.getItems();
		if(this.getSortIndex() < 0){
			return items;
		}
		
		var sortProps = this.getSortProps();
		var sortAttributes = [];
		var i;
		for(i in sortProps){
			sortAttributes.push({attr: sortProps[i].attribute, asc: (sortProps[i].descending ? -1 : 1)});//{attr: "setTime", asc: -1} -1 or 1
		}
		for(i = (sortAttributes.length - 1) ; i >= 0 ; i--){			
			items.sort(function(a,b){
				if(sortAttributes[i].asc > 0){ //ascending
					if(a[sortAttributes[i].attr] > b[sortAttributes[i].attr]){
						return 1;
					}
					else if(a[sortAttributes[i].attr] < b[sortAttributes[i].attr]){
						return -1;
					}
					else{
						return 0;
					}
				}
				else{ //descending
					if(a[sortAttributes[i].attr] > b[sortAttributes[i].attr]){
						return -1;
					}
					else if(a[sortAttributes[i].attr] < b[sortAttributes[i].attr]){
						return 1;
					}
					else{
						return 0;
					}
				}
			});
		}
		return items;
	},
	getRowsContent: function(){ //don't ignore filter

		var items = [];
		var count = this.rowCount;
		var i,rowsMap,cells,j,viewIndex;
		this.loadAllItems();
		//each view represents a set of columns the 
		//first set is probably fixed with no scroll bar
		var views = this.views.views;
		
		//start with 1
		for(viewIndex = 1 ; viewIndex < views.length ; viewIndex++){
			rowsMap = this.views.views[viewIndex].rowNodes;
			i = 0;
			for(var k in rowsMap){
				cells = dojo.query("[idx]", rowsMap[k]);
				if(items.length <= i){
					items.push([]);
				}
				
				for(j = 0 ; j < cells.length ; j++){
					items[i].push(cells[j].textContent);
				}
				i++;
			}
		}
		return items;
	},
	getIndexById: function(id){
		var items = this._gridData.items;
		for(var i in items){
			if(items[i][this.identifier] == id){
				return i;
			}
		}
	},
	
	//TO DO: find efficient way to receive items by ID
	//This method is not efficient, store._itemsByIdentity for some reason
	//is not initilized and populated
	getItemById: function(id){
//		console.log("x.js: tems.Table: getItemById: " + this.store._getItemByIdentity(id) + ", if this is working change the code!");
//		console.log("getItemsById:" + id);
//		console.log(this.itemsMap);
		return this.itemsMap[id];
//		var items = this._gridData.items;
//		for(var i in items){
//			if(items[i][this.identifier] == id){
//				return items[i];
//			}
//		}
	},
	getItemsByIds: function(ids){ //NOT CHECKED YET
		var items = [],item;
		for(var i in ids){
			item = this.getItemById(ids[i]);
			if(item != null){
				items.push(item);
			}
		}
		return items;
	},
//	setData: function(arr){ //APPEARS ONLY ON EnhancedCheckboxGrid, why?
//		this._gridData = { //identifier: _identifier,
//			label: this.identifier,
//			items: arr
//		};
//		
//		this.store = new dojo.data.ItemFileWriteStore({data: this._gridData, typeMap: this.typeMap});
////			this.store = new tems.Store({data: this._gridData, typeMap: this.typeMap});
//		this.setStore(this.store);
//		this._updateItemsMap();
//	},
	updateItems: function(items){
		this.selection.clear();
		for ( var i = 0; i < items.length; i++) {
			this.store.deleteItem(this.itemsMap[items[i][this.identifier]]);
			this.itemsMap[items[i][this.identifier]] = this.store.newItem(items[i]);
		}

		if(this.isFilterActivated()){
			this._refresh(true);
		}
		
		this.store.save();

	},

	updateItem: function(item,newItem){ //DOES NOT WORK WELL
		for(var k in newItem){
			if(k != this.identifier){
				if(typeof newItem[k]._type != 'undefined'){
					var v = this.typeMap[newItem[k]._type].deserialize(newItem[k]._value);
					this.store.setValue(item,k,v);
				}
				else{
					this.store.setValue(item,k,newItem[k]);
				}
			}
		}
	},
	replaceItem: function(item,newItem){
		this.store.deleteItem(item);
		this.store.save();
		
		newItem[this.identifier] = item[this.identifier];
		
		this.store.newItem(newItem);
		this.store.save();
		//-map
		this.itemsMap[newItem[this.identifier]] = newItem;
	}
});

}
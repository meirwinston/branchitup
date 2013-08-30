/*version 1.4*/
if(!dojo._hasResource["dojoui.TreeGrid"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.TreeGrid"] = true;
dojo.provide("dojoui.TreeGrid");

dojo.require("dojox.grid.TreeGrid");
dojo.declare("dojoui.TreeGrid", dojox.grid.TreeGrid, {
//	jsId: this.id,
//	widgetId: this.id,
	defaultOpen: false,
	selectionMode: "single", //single,none,extended,multiple

	onSelectionChanged: function(){
		this.inherited(arguments);
		this.selectFocusedNode();
	},
//	postCreate: function(){
//		this.inherited(arguments);
//		this.selection.addToSelection = function (_2) { 
//			if (this.mode == "none") { 
//				return; 
//			} 
//			var _3 = null; 
//			if (typeof _2 == "number" || typeof _2 == "string") { 
//				_3 = _2; 
//			} 
//			else { 
//				_3 = this.grid.getItemIndex(_2); 
//			} 
//			if (this.selected[_3]) { 
//				this.selectedIndex = _3; 
//			} 
//			else { 
//				if (this.onCanSelect(_3) !== false) { 
//					this.selectedIndex = _3; 
//					var _4 = dojo.query("tr[dojoxTreeGridPath='" + _3 + "']", this.grid.domNode); 
//					if (_4.length) { dojo.attr(_4[0], "aria-selected", "true"); } 
//					this._beginUpdate(); 
//					this.selected[_3] = true; 
//					this._insertSortedSelection(_3); 
//					this.onSelected(_3); 
//					if(_4.length && this.grid.onRowSelected){
//						this.grid.onRowSelected(_4[0]); //_4 is an array of a single tr dom node
//					}
//					this._endUpdate(); 
//				} 
//			} 
//		};
//	},
	isSelected: function(inRowIndex){ //UNCHECKED YET
		return this.selectionMode.selected[inRowIndex];
	},
	onStyleRow : function(row) {
		//the class dojoxGridSubRowAlt overwrites the selection class
		//if the row is selected it would not be shown
		//this method removes this class from the node
		if (!this.layout._isCollapsable) {
			this.inherited(arguments);
			return;
		}
		var _73 = dojo.attr(row.node, "dojoxTreeGridBaseClasses");
		if (_73) {
			row.customClasses = _73;
		}
		var i = row;
		var _74 = i.node.tagName.toLowerCase();
		i.customClasses += (i.odd ? " dojoxGridRowOdd" : "")
			+ (i.selected && _74 == "tr" ? " dojoxGridRowSelected" : "")
			+ (i.over && _74 == "tr" ? " dojoxGridRowOver" : "");
		if (i.selected && _74 == "tr") {
			i.customClasses = i.customClasses.replace("dojoxGridSubRowAlt","");
		}

		this.focus.styleRow(i);
		this.edit.styleRow(i);
	},
	connectOnStyleRow: function(f){ //the function declares one parameter n with(index,domNode);
		return dojo.connect(this, "onStyleRow", f);
	},
	getItemByIndex: function(index){ //index e.g. 0/0/1
		return this.getItem(index);
	},
	getSelectedItems: function(){
		var items = this.selection.getSelected();
		return items;
	},
	getSelectedCell: function(){
		return this.focus.cell;
	},
	selectFocusedNode: function(){
//		console.log("node selected:::2");
		dojo.query ( ".dojoxGridCellFocus", this.domNode ).forEach(function(node){
			node.focus();
			var userSelection = window.getSelection();
			
//			console.log("node selected:::3");
			if(userSelection.rangeCount > 0){
//				console.log("node selected:::4");
				var range = userSelection.getRangeAt(0);
				range.selectNode(node);
//				console.log("node selected:::1");
			}
			else{
//				console.log("node selected:::5");
				var range = userSelection.createRange();
				range.selectNode(node);
//				console.log("node selected:::6");
			}
		});
	},
	getRootItemById: function(id){
		var i = this._by_idty[id]; //{item,idty} only roots
		if(i){
			return i.item; 
		}
		return null;
	},
	getItemById: function(id){
		return this.treeModel.store._itemsByIdentity[id]; 
	},
	getRootItemsMap: function(){ //only roots
		return this._by_idty;
	},
	getAllItems: function(){ //ignore filter
		return this.treeModel.store._arrayOfTopLevelItems;
	},
	connectOnSelectionChanged: function(f){
		return dojo.connect(this, "onSelectionChanged", f);
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
		this.loadAllItems();
		var self = this;
		var list = [];
		dojo.query ( "[dojoxtreegridpath]", this.domNode ).forEach(function(node){
			var attr = node.getAttribute("dojoxtreegridpath");
			if ( attr )
			{
				var item = self.getItem(attr);
				list.push(item);
			}
		});
		return list;
	},
//	_fetch: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid._fetch");
//	},
//	_onFetchBegin: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid._onFetchBegin");
//	},
//	_onFetchComplete: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid._onFetchComplete");
//	},
//	_refresh: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid._refresh");
//	},
//	_render: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid._render");
//	},
//	postrender: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid.postrender");
//	},
//	update: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid.update");
//	},
//	updateRows: function(){
//		this.inherited(arguments);
//		console.log("TreeGrid.updateRows");
//	},
	//I override this method since there is a bug in the original implementation
	//the method did not return items for 0/0/0 for example.
	getItem: function (idx) { 
		var isArray = dojo.isArray(idx);
		if (dojo.isString(idx) && idx.indexOf("/")) { 
			idx = idx.split("/"); 
			isArray = true;
		}
		if (isArray && idx.length == 1) { 
			idx = idx[0]; 
			isArray = false;
		} 
		if (!isArray) { 
			return dojox.grid.DataGrid.prototype.getItem.call(this, idx); 
		} 
		var s = this.store; 
		var itm = dojox.grid.DataGrid.prototype.getItem.call(this, idx[0]); 
		var cf; 
		if (this.aggregator) { 
			cf = this.aggregator.childFields || [];
		} 
		else { 
			if (this.treeModel) { 
				cf = this.treeModel.childrenAttrs || []; 
			} 
		} 
		if (cf.length > 0) { 
			for (var i = 0; i < idx.length - 1 && itm; i++) {
				if (cf[0]) { 
					itm = (s.getValues(itm, cf[0]) || [])[idx[i + 1]];
				} 
				else { 
					itm = null; 
				} 
			} 
		} 
		return itm || null; 
	}
	
});
}
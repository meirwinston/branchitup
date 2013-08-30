if(!dojo._hasResource["dojoui.EnhancedCheckboxGrid"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.EnhancedCheckboxGrid"] = true;
dojo.provide("dojoui.EnhancedCheckboxGrid");


//data grid -- KEEP THE ORDER
//dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.grid.EnhancedGrid");
//dojo.require("dojox.grid.enhanced.plugins.DnD");
//dojo.require("dojox.grid.enhanced.plugins.Menu");
dojo.require("dojox.grid.enhanced.plugins.NestedSorting");
//dojo.require("dojox.grid.enhanced.plugins.IndirectSelection");
//dojo.require("dojox.grid._CheckBoxSelector");
dojo.require("dojo.data.ItemFileWriteStore");
//----------------------
//this class 
var _1=dojox.grid.cells;
dojo.declare("dojox.grid.cells._Widget",_1._Base,{widgetClass:dijit.form.TextBox,constructor:function(_2){
	this.widget=null;
	if(typeof this.widgetClass=="string"){
		dojo.deprecated("Passing a string to widgetClass is deprecated","pass the widget class object instead","2.0");
		this.widgetClass=dojo.getObject(this.widgetClass);
	}
},formatEditing:function(_3,_4){
	this.needFormatNode(_3,_4);
	return "<div></div>";
},getValue:function(_5){
	return this.widget.attr("value");
},setValue:function(_6,_7){
	if(this.widget&&this.widget.attr){
		if(this.widget.onLoadDeferred){
			var _8=this;
			this.widget.onLoadDeferred.addCallback(function(){
				_8.widget.attr("value",_7===null?"":_7);
			});
		}else{
			this.widget.attr("value",_7);
		}
	}else{
		this.inherited(arguments);
	}
},getWidgetProps:function(_9){
	return dojo.mixin({},this.widgetProps||{},{constraints:dojo.mixin({},this.constraint)||{},value:_9});
},createWidget:function(_a,_b,_c){
	return new this.widgetClass(this.getWidgetProps(_b),_a);
},attachWidget:function(_d,_e,_f){
	_d.appendChild(this.widget.domNode);
	this.setValue(_f,_e);
},formatNode:function(_10,_11,_12){
	if(!this.widgetClass){
		return _11;
	}
	if(!this.widget){
		this.widget=this.createWidget.apply(this,arguments);
	}else{
		this.attachWidget.apply(this,arguments);
	}
	this.sizeWidget.apply(this,arguments);
	this.grid.rowHeightChanged(_12);
	this.focus();
	return undefined;
},sizeWidget:function(_13,_14,_15){
	var p=this.getNode(_15),box=dojo.contentBox(p);
	dojo.marginBox(this.widget.domNode,{w:box.w});
},focus:function(_16,_17){
	if(this.widget){
		setTimeout(dojo.hitch(this.widget,function(){
			dojox.grid.util.fire(this,"focus");
		}),0);
	}
},_finish:function(_18){
	this.inherited(arguments);
	dojox.grid.util.removeNode(this.widget.domNode);
}});


//####################
dojo.declare("dojoui.EnhancedCheckboxGrid",dojox.grid.EnhancedGrid,{
	checkboxMap: null, //^^
	createSelection: function(){
		this.selection = new dojox.grid.DataSelection(this);
		this.selection.addToSelection = function(inItemOrIndex){
			var rowSelector = this.grid.views.views[0]; //^^
			if(this.mode == 'none'){ return; }
			var idx = null;
			if(typeof inItemOrIndex == "number" || typeof inItemOrIndex == "string"){
				idx = inItemOrIndex;
			}else{
				idx = this.grid.getItemIndex(inItemOrIndex);
			}
			dojox.grid.Selection.prototype.addToSelection.call(this, idx);
			
//				console.log("DataSelection:addToSelection:" + rowSelector.checkboxMap[idx]);
			if(typeof this.grid.checkboxMap[idx] != 'undefined'){ //^^
				//without the drag plugin, this is being checked but immediatly unchecked
				this.grid.checkboxMap[idx].checked = true;
			}
		},

		this.selection.deselect = function(inItemOrIndex){
			var rowSelector = this.grid.views.views[0]; //^^
			
			if(this.mode == 'none'){ return; }
			var idx = null;
			if(typeof inItemOrIndex == "number" || typeof inItemOrIndex == "string"){
				idx = inItemOrIndex;
			}else{
				idx = this.grid.getItemIndex(inItemOrIndex);
			}
			dojox.grid.Selection.prototype.deselect.call(this, idx);
			
//				console.log("DataSelection:deselect:" + rowSelector.checkboxMap[idx]);
			if(typeof this.grid.checkboxMap[idx] != 'undefined'){ //^^
				this.grid.checkboxMap[idx].checked = false;
				this.grid.headerCheckbox.checked = false;
			}
		}
	},
	getRowsSelectionCheckbox: function(){ //^^
	//	var rowNodes = this.views.views[1].rowNodes;
		var rowNodes = this.views.views[0].rowNodes;
		var arr = new Array();
		for(var i in rowNodes){
			arr.push(rowNodes[i].firstChild.rows[0].cells[0].firstChild);
		}
		return arr;
	},
	headerCheckbox: null, //^^
	selectAll: function(){
		this.headerCheckbox.checked = true;
		this.grid.selection.selectRange(0,this.grid.rowCount);
	},
	createHeaderCheckbox: function(){ //^^
		var checkbox = document.createElement("input");
		checkbox.type = "checkbox";
		checkbox.grid = this;
		checkbox.addEventListener('click',function(e){
			var s = this.grid.selection;
	//		var checkArray = checkbox.grid.getRowsSelectionCheckbox();
	
			if(e.target.checked){
	//			for(var i in checkArray){
	//				checkArray[i].checked = true;
	//				s.addToSelection(checkArray[i].inRowIndex);
	//			}
	//			s.selectRange(0,checkArray[i].length);
//				console.log("createHeaderCheckbox:selectAll: " + this.grid.rowCount);
				s.selectRange(0,this.grid.rowCount);
			}
			else{
	//			for(var i in checkArray){
	//				checkArray[i].checked = false;
	//			}
				s.deselectAll();
			}
			//THIS IS A DUMMY CALL TO length, IT IS NECESSARY FOR THE SELECTION
			//MODEL WHEN WE WANT TO SELECT ALL RECORDS - WEIRD!!!
			var length = s.getSelected().length;
//			console.log("selected: " + s.getSelected().length);
			//printObject(s);
		},false);
		
		this.headerCheckbox = checkbox; 
		return checkbox;
	},
	postCreate: function(){
		this.checkboxMap = {};//^^
		this.inherited(arguments); 
		
		this.views.views[0].buildRowContent = function(inRowIndex, inRowNode){
			var w = this.contentWidth || 0;
			
			 //^^ my block
			var t = document.createElement("table");
			t.className = "dojoxGridRowbarTable";
			t.style.width = (w + "px");
			t.border = "0";
			t.cellSpacing = "0";
			t.cellPadding = "0";
			t.setAttribute("role",(dojo.isFF<3 ? "wairole:" : "")+"presentation");
			var row = t.insertRow(0);
			var cell = row.insertCell(0);
			cell.className = "dojoxGridRowbarInner";
			cell.align = "center";
			
			var checkbox = this.grid.checkboxMap[inRowIndex];
			
			if(typeof checkbox == 'undefined' || checkbox == null){
				checkbox = document.createElement("input");
				//uncomment this if the drag plugin is activated //***//
//					checkbox.onclick = function(e){
//						var evt= window.event || e;
//						if (evt.preventDefault){ //supports preventDefault?
//							evt.preventDefault();
//						}
//						else{ //IE browser
//							return false;
//						}
//					};
				
				this.grid.checkboxMap[inRowIndex] = checkbox;
				checkbox.type = "checkbox";
				checkbox.grid = this.grid;
				checkbox.inRowIndex = inRowIndex;
				
			}
			cell.appendChild(checkbox);
			
			if(this.grid.selection.isSelected(inRowIndex)){
				checkbox.checked = true;
			}
			else{
				checkbox.checked = false;
			}				
			inRowNode.innerHTML = "";
			inRowNode.appendChild(t);
			//inRowNode.innerHTML = '<table class="dojoxGridRowbarTable" style="width:' + w + 'px;" border="0" cellspacing="0" cellpadding="0" role="'+(dojo.isFF<3 ? "wairole:" : "")+'presentation"><tr><td class="dojoxGridRowbarInner" align="center"><input name="rowCheckboxSelector" type="checkbox" /></td></tr></table>'; //^^
			
//				this.grid.selection.select(inRowIndex);
			
		};
		//^^
		var con = this.domNode.childNodes[1];
		var div = document.createElement("div");
		div.style.paddingLeft = "4px";
		div.style.paddingTop = "4px";
		div.appendChild(this.createHeaderCheckbox());
		con.appendChild(div);
		
		//-------- Events
		
		this._events.onStyleRow = function(inRow){
			// summary:
			//		Perform row styling on a given row. Called whenever row styling is updated.
			//
			// inRow: Object
			// 		Object containing row state information: selected, true if the row is selcted; over:
			// 		true of the mouse is over the row; odd: true if the row is odd. Use customClasses and
			// 		customStyles to control row css classes and styles; both properties are strings.
			//
			// example: onStyleRow({ selected: true, over:true, odd:false })
			var i = inRow;
			i.customClasses += (i.odd?" dojoxGridRowOdd":"") + (i.over?" dojoxGridRowOver":""); //^^
			this.focus.styleRow(inRow);
			this.edit.styleRow(inRow);
		};
		this._events.onCellClick = function(e){
			// summary:
			//		Event fired when a cell is clicked.
			// e: Event
			//		Decorated event object which contains reference to grid, cell, and rowIndex
			this._click[0] = this._click[1];
			this._click[1] = e;
			if(!this.edit.isEditCell(e.rowIndex, e.cellIndex)){
				this.focus.setFocusCell(e.cell, e.rowIndex);
			}
//				this.onRowClick(e); //^^ eliminate select row on cell click
		};
	}
});
//##################
//##################
dojo.declare("dojoui.Table", dojoui.EnhancedCheckboxGrid, {
	typeMap: {
			"Date": {
			type: Number,
			deserialize: function(value){
				var date = new Date(value);
				date.toString = function(){
					var s = "";
					s += this.getDate();
					s += "-";
					s += ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"][this.getMonth()];
					s += "-";
					s += this.getFullYear();
					return s;
				};
				return date;
			}
		},
		"DayHour": {
			deserialize: function(value){
				var hours = parseInt(value / 3600000);
				var days = parseInt(hours / 24);
				hours = hours % 24;
				return (days + "d " + hours + "h");
			}
		},
		"Timestamp": {
			type: Number,
			deserialize: function(value){
				var date = new Date(value);
				
				date.toString = function(){
					var s = "";
					s += this.getDate();
					s += "-";
					s += ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"][this.getMonth()];
					s += "-";
					s += this.getFullYear();
					s += " ";
					s += this.getHours();
					s += ":";
					s += this.getMinutes();
					s += ":";
					s += this.getSeconds();
					return s;
				};
				return date;
//						return {toString: function(){return 'meir';}};
			}
		}
	},
	id: this.id, 
	jsId: this.id, 
	widgetId: this.id,
	identifier: "id", //default value
	autoWidth: false, //solves the problem with horizontal scrollbar, header does not sort and not clickable
	autoHeight: false, //solves the problem with vertical scroll not showing all rows
	clientSort: true,
	itemsMap: {},
	selectionMode: "multiple",
	rowsPerPage: 40,
	rowSelector: '20px',
//			_layout:  [{cells:this.columns}],
//			structure: [{cells:this.columns}],
	plugins: {nestedSorting: false, dnd: true}, //if dnd is activated search for //***// and uncomment as described
	_gridData: {
		identifier: this.identifier,
		label: this.identifier,
		items: [] //not in presentation order
	},
	postCreate: function(){
		if(this.store == null){
			this.store = new dojo.data.ItemFileWriteStore({data: this._gridData, typeMap: this.typeMap});
//				this.store = new tems.Store({data: this._gridData, typeMap: this.typeMap});
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
	
//			startup: function(){
//				this.inherited(arguments);
//				var headerNodes = dojo.query("[role*='columnheader']", this.viewsHeaderNode);
//				dojo.forEach(headerNodes, function(node, index){
//					node.setAttribute("columnIndex",index);
//				}, this);
//			},
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
//				 function onApplyCellEditHandler(inGrid, inValue, inRowIndex, inFieldIndex) {
//			            jsonStore.save();
//			            inGrid.updateRow(inRowIndex);
//			        } 
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
//				this.store.save();
		}
		
		this.store.save(); //this might cause problems, then move it back inside the loop
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
		this.headerCheckbox.checked = false;
	},
//	selectAll: function(){
//		this.selection.selectRange(0,this.rowCount);
//	},
	allSelected: function(){
		return this.headerCheckbox.checked;
	},
	clearCheckboxSelection: function(){
		this.checkboxSelection.clear();
	},
	addItem: function(newItem){
		this.addItems([newItem]);
	},
	filter: function(query){//query example: {inventoryState: 'ONSITE' }
//		console.log("EnhancedCheckboxGrid:filter:");
//		console.log(query);
//		console.log(this.store._arrayOfTopLevelItems[0]);
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
	setData: function(arr){
		this._gridData = { //identifier: _identifier,
			label: this.identifier,
			items: arr
		};
		
		this.store = new dojo.data.ItemFileWriteStore({data: this._gridData, typeMap: this.typeMap});
//			this.store = new tems.Store({data: this._gridData, typeMap: this.typeMap});
		this.setStore(this.store);
		this._updateItemsMap();
	},
	getRowData: function(index){
		return this.getItem(index);
	},
//			getColumnIndexes: function(){ 
////				printObject(this.structure[0].cells[0]);
////				var nodes = dojo.query("[role*='columnheader']", this.viewsHeaderNode);
////				dojo.forEach(nodes, function(node, index){
////					console.log(node);
////				}, this);
//				console.log(this.plugins);
//			},
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
		var self = this;
		
		this.store.fetch({
			start: 0,
			count: self.rowCount,
			query: self.query,
			sort: self.getSortProps(),
			queryOptions: self.queryOptions,
			isRender: self.isRender,
			onBegin: dojo.hitch(self, "_onFetchBegin"),
			onComplete: dojo.hitch(self, "_onFetchComplete"),
			onError: dojo.hitch(self, "_onFetchError")
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
//		console.log("EnhancedCheckboxGrid:getRowsContent: " + items.length);
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
//		console.log("x.js: tems.Table: getItemById: " + this.store._getItemByIdentity(id));

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
	addItems: function(items){
		for(var i in items){
			this.itemsMap[items[i][this.identifier]] = this.store.newItem(items[i]);
		}
//		this.store.save({onComplete: this._refresh});
		setTimeout(dojo.hitch(this,this._refresh),0);
	},
	updateItems: function(items){
		this.selection.clear();
		for ( var i = 0; i < items.length; i++) {
			this.store.deleteItem(this.itemsMap[items[i][this.identifier]]);
			this.itemsMap[items[i][this.identifier]] = this.store.newItem(items[i]);
		}
//		this.store.save({onComplete: this._refresh});
		setTimeout(dojo.hitch(this,this._refresh),0);
	},

	updateItem: function(item,newItem){ //DOESN'T WORK WELL
		for(var k in newItem){
			if(k != this.identifier){
				if(typeof newItem[k]._type != 'undefined'){
					var v = this.typeMap[newItem[k]._type].deserialize(newItem[k]._value);
					this.store.setValue(item,k,v);
				}
				else{
					try {
						this.store.setValue(item,k,newItem[k]);
						
					} catch (e) {
						console.log(e);
					}
					
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
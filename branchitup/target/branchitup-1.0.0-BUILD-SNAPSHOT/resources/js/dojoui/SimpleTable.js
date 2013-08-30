if(!dojo._hasResource["dojoui.SimpleTable"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.SimpleTable"] = true;
dojo.provide("dojoui.SimpleTable");
dojo.require("dijit._Widget");

dojo.declare("dojoui.SimpleTable", dijit._Widget, {
	identifier: null,
	idPrefix: "dojoui.SimpleTable",
	headerVisible : true,
	itemsMap: {},
	clear: function(){
		var t = this.domNode.tBodies[0];
		while(t.rows.length > 0){
			t.deleteRow(0);
		}
	},
	prependItems: function(items){
		for(var i in items){
			this.insertItem(items[i], 0);
		}
	},
	updateItem: function(item){
		var row,cell;
		var headerCells = this.structure.cells;
		row = dojo.byId(this.idPrefix + item[this.identifier]);
		this.itemsMap[item[this.identifier]] = item;
		for(var j = 0 ; j < row.cells.length ; j++){
			cell = row.cells[j];
			cell.innerHTML = ""; //clear field
			if(headerCells[j].formatter){
				cell.appendChild(headerCells[j].formatter(item[headerCells[j].field],row.rowIndex-1,item,cell));
			}
			else{
				cell.innerHTML = item[headerCells[j].field];
			}
		}
	},
	insertItem: function(item,index){
//		console.log("SimpleTable.insertItem");
		var t = this.domNode;
		var row,cell;
		var headerCells = this.structure.cells;
		row = t.tBodies[0].insertRow(index);
		if(this.identifier && item[this.identifier]){
			row.id = (this.idPrefix + item[this.identifier]);
			this.itemsMap[item[this.identifier]] = item;
//			console.log("SimpleTable.insertItem.id:");
//			console.log(row);
		}
		for(var j in headerCells){
			cell = row.insertCell(row.cells.length);
			if(headerCells[j].formatter){
				cell.appendChild(headerCells[j].formatter(item[headerCells[j].field],index,item,cell));
			}
			else{
				cell.innerHTML = item[headerCells[j].field];
			}
			if(headerCells[j].width){
				cell.style.width = headerCells[j].width; 
			}
			//-- style
			cell.style.borderBottom = "1px dotted #DDDDDD";
			cell.style.verticalAlign = "top";
			if(j < headerCells.length - 1){
				cell.style.borderRight = "1px solid #DDDDDD";
			}
		}
	},
	setItems: function(items){
		this.clear();
		for(var i in items){
			this.insertItem(items[i], i);
		}
	},
	hasItem: function(item){
		return (typeof this.itemsMap[item[this.identifier]] != 'undefined');
	},
	render: function(){
		var t = dojo.create("table");
		t.appendChild(dojo.create("tbody"));
		t.cellSpacing = 0;
		t.cellPadding = "5px";
		t.border = "0px";
		t.style.width = "100%";
		
		if(this.style){
			for(var k in this.style){
				t.style[k] = this.style[k];
			}
		}
		this.domNode = t;
		
		//-- HEADER
		if(this.headerVisible){
			var cell,row,header;
			header = t.createTHead();
			row = header.insertRow(0);
			row.className = "soria dijitToolbar";
			
			var headerCells = this.structure.cells;
			for(var i in headerCells){
				cell = row.insertCell(i);
				cell.style.fontWeight = "bold";
				
				if(headerCells[i].width){
					cell.style.width = headerCells[i].width; 
//					console.log("setting width to " + headerCells[i].width + ", " + i);
				}
				cell.innerHTML = headerCells[i].name;
				cell.setAttribute("field",headerCells[i].field);
			}
		}
		
		//-- BODY
	},
	postCreate: function(){
		this.render();
	},
	_setValueAttr: function(newValue){ //inherit this method as a connection to attr("value",newValue)
		this.domNode.innerHTML = newValue;
	}
});
}
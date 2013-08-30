if(!dojo._hasResource["dojoui.Dialog"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.Dialog"] = true;
dojo.provide("dojoui.Dialog");

dojo.require("dijit.Dialog");
dojo.require("dijit.layout.BorderContainer");
	
dojo.declare("dojoui.Dialog", dijit.Dialog, {
//	title: "My Title",
//	execute: "alert(arguments[0])",
//	style:"width: 450px; height: 350px",
//	doLayout: true,
//	parseOnLoad: true,
//	//_content: new dijit.layout.BorderContainer({design: "headline",gutters: 'false'}), //content is reserved from super class
//	_content: null,
//	contentPane: null,
//	postCreate: function(){
//		this._content = this._createLayout();
//		this.inherited(arguments);
//		this.set("content",this._content);
//	},
//	show: function(){ //remove from v1.6
//		this.inherited(arguments);
//		//dom height is being determined after show is called
//		this._content.style.height = (parseInt(this.domNode.clientHeight)-40) + "px";
//	},
//	setContent: function(content){
//		this.contentPane.attr("content", content);
//	},
//	getContent: function(){
//		return this.contentPane.attr("content");
//	},
//	_createLayout: function(){
//		var superClass = this;
//		//--------------------------------
//		var t = dojo.create("table");
//		t.cellPadding = 0;
//		t.cellSpacing = 0;
//		t.border = "0px";
//		t.style.width = "100%";
//		t.style.height = "100%";
//		
//		var row,cell;
//		
//		row = t.insertRow(t.rows.length);
//		cell = row.insertCell(row.cells.length);
//		cell.style.verticalAlign = "top";
//		cell.appendChild(this.dhtml);
//		
//		var tfoot = t.createTFoot();
//		row = tfoot.insertRow(0);
//		cell = row.insertCell(0);
//		cell.style.height = "30px";
//		
//		if(typeof this.actions != 'undefined'){
//			var b,i;
//			for(i in this.actions){
//				b = new dijit.form.Button({
//					_actionIndex: parseInt(i),
//					label: superClass.actions[i].name,
//					onClick: function(e){
//						superClass.actions[this._actionIndex].action();
//						if(superClass.actions[this._actionIndex].close == true){
//							superClass.onCancel();
//						}
//					}
//				});
//				cell.appendChild(b.domNode);
//			}
//		}
//		
//		return t;
//	},
//	close: function(){
//		this.onCancel();
//	}
});
}
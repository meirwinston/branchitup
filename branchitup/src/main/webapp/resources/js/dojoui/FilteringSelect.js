/* Dojo v1.4 */
if(!dojo._hasResource["dojoui.FilteringSelect"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.FilteringSelect"] = true;
dojo.provide("dojoui.FilteringSelect");

dojo.require("dijit.form.FilteringSelect");
dojo.require("dojo.data.ItemFileReadStore");
dojo.declare("dojoui.FilteringSelect", dijit.form.FilteringSelect, {	
	postCreate: function(){
	
	//for some reason data is null, but _jsonData contains the right data
		this.store.data = this.store._jsonData;
		this.inherited(arguments);
	},
	connectOnChange: function(f){
//			return dojo.connect(this, 'onChange', function(elementId){
//				for(var i = 0; i < this.store.data,items.length ; i++){
//					if(this.store.data.items[i][this.store.identifier] == elementId){
//						f.apply(this,[this.store.data.items[i]]);
//						break;
//					}
//				}
//			});
	},
	disconnect: function(event){
		dojo.disconnect(event);
	},
	
	setData: function(data){
		this.store = new dojo.data.ItemFileReadStore({data: data});
		this.startup();
	},
	getItems: function(){
		return this.store.data.items;
	},
	selectByIndex: function(index){
		var item = this.store.data.items[index];
		if(typeof item != 'undefined'){
			this.attr("value",item[this.store.data.identifier]);
		}
	},
	setDisabled: function(b){
		this.attr("disabled",b);
	},
	getItemByIndex: function(index){
		var item = null;
		if(index >= 0 && index < this.store.data.items.length){
			item = this.store.data.items[index];
		}
		return item;
	},
	setValue: function(value){
		this.attr("value",value);
	},
	getDomNode: function(){
		return this.domNode;
	},
	getValue: function(){
		var value = null;
		if(typeof this.get("value") != 'undefined'){
			value = this.get("value");
		}
		return value;
	},
//	getItem: function(){
//		var elementId = this.attr("value");
//		var item = null;
//		for(var i = 0; i < this.store.data.items.length ; i++){
//			if(this.store.data.items[i][this.store.data.identifier] == elementId){
//				item = this.store.data.items[i];
//				break;
//			}
//		}
//		return item;
//	},
	getItem: function(){
		var value = this.attr("value");
		return this.store._getItemByIdentity(value);
	},
	setWidth: function(width){
		this.domNode.style.width = width;
	}
});
}
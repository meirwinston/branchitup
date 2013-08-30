if(!dojo._hasResource["dojoui.DNDSource"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.DNDSource"] = true;
dojo.provide("dojoui.DNDSource");

dojo.require("dojo.dnd.Source");
dojo.declare("dojoui.DNDSource", dojo.dnd.Source, {
//		accept: ["text"],
//		copyOnly: false, //true: disallow items to be removed
//		horizontal: false,
//		isSource: true,
//		selfAccept: true,
//		selfCopy: false,
	singular: true,
	onFocusCallback: null,
	copyFromSource: false,
	postDropCallback: null,
	onChange: null,
	_tmpAccept:null,
	
	postCreate: function(){
		this.inherited(arguments);
	},
	connectOnMouseDown: function(f){
//		return dojo.connect(this, 'onMouseDown', function(e){e.src = _self; f(e);});
		return dojo.connect(this, 'onMouseDown', f);
	},
	onMouseDown:  function (e) { 
		this.inherited(arguments);
		if(this.onFocusCallback != null){
			this.onFocusCallback(e);
		}
	},
	onMouseUp:  function (e) { 
		this.inherited(arguments);
		if(this.onFocusCallback != null){
			this.onFocusCallback(e);
		}
	},
	onDndDrop: function (source, nodeArray, isCopy, target) {
		if(this == target && this != source){ //avoid self copy
			isCopy = this.copyFromSource;
		}
		this.inherited(arguments);

		if(this == target && this.postDropCallback != null){
			this.postDropCallback(source, nodeArray, isCopy);
		}
		if(this.onChange != null){
			this.onChange(source, nodeArray, isCopy, target);
		}
	},
	setAcceptArray: function(arr){
		this.accept = arr;
	},
	getAcceptArray: function(){
		return this.accept;
	},
	blockAccept: function(){
		//the original accept field is an object an not array
		//this if makes sure that the accept is what constructed by this object
		if(typeof this.accept.length == 'undefined'){ //if accept.constructor == Object
			this._tmpAccept = this.accept;
			this.accept = [];
		}
	},
	renewAccept: function(){
		if(this._tmpAccept != null){
			this.accept = this._tmpAccept;
		}
		
	},
	getSelectedItems: function(){
		var arr = [];
		this.forInSelectedItems( function(item, id) {
			item.nodeId = id;
			arr.push(item);
		}, this);
		return arr;
	},
	getItems: function(){
		var arr = [];
		this.forInItems( function(item, id) {
			item.nodeId = id;
			arr.push(item);
		}, this);
		return arr;
	},
	getItemsInOrder: function(){ //in order
		var indexes = {};
		var nodes = this.getAllNodes();
		for(var i = 0 ; i < nodes.length ; i++){
			indexes[nodes[i].id] = {index: i, node: nodes[i]};
		}
		var arr = new Array(nodes.length);
		this.forInItems( function(item, id) {
			arr[indexes[id].index] = ({item: item, id: id, node: indexes[id].node});
		}, this);
		
		return arr;
	},
	addItems: function(items){
		this.insertNodes(false, items);//addSelected: bool, items
	},
	countItems: function(){
		var count = 0;
		this.forInItems( function(item, id) {
			count++;
		}, this);
		return count;
	}
});
}
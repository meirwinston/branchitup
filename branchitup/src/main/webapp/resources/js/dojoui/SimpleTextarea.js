if(!dojo._hasResource["dojoui.SimpleTextarea"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.SimpleTextarea"] = true;
dojo.provide("dojoui.SimpleTextarea");
	
dojo.require("dijit.form.SimpleTextarea");

dojo.declare("dojoui.SimpleTextarea", dijit.form.SimpleTextarea, {
	postCreate: function() {
		this.inherited(arguments);
		this.connect(this.domNode,"onfocus", "gainedFocus" );
		this.connect(this.domNode,"onblur", "lostFocus" );
	},

	setMaxLength: function(maxLength){
		this.set("maxLength",maxLength);
	},
	getValue: function(){
		return this.get("value");
	},		
	setValue: function(val){
		this.set("value",val);
	},
	gainedFocus: function(){
		this._onInput(arguments);
	},
	_onInput: function(){
		this.inherited(arguments);
		if ( (this.getValue() == "") && this.promptMessage )
		{
			this.displayMessage(this.promptMessage);
		}
		else
		{
			this.displayMessage();
		}
	},
	lostFocus: function(){
		this.displayMessage();
	},
	displayMessage:function(_d){
		if(this._message==_d){
			return;
		}
		this._message=_d;
		dijit.hideTooltip(this.domNode);
		if(_d){
			dijit.showTooltip(_d,this.domNode,this.tooltipPosition);
		}
	}
});
}
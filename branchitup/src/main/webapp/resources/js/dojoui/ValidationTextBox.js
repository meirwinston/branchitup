if(!dojo._hasResource["dojoui.ValidationTextBox"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.ValidationTextBox"] = true;
dojo.provide("dojoui.ValidationTextBox");

dojo.require("dijit.form.ValidationTextBox");

dojo.declare("dojoui.ValidationTextBox", dijit.form.ValidationTextBox, {
	getValue: function(){
		return this.attr("value");
	},
	setValue: function(value){
		this.attr("value",value);
	},
	isValid: function(){
		return (this.state == "");
	},
	
	setDisabled: function(b){
		this.attr("disabled",b);
	},
	setInvalidMessage: function(message){
		this.attr("invalidMessage",message);
	},
	setRegularExpression: function(regExp){
		if(typeof regExp != 'undefined'){
			this.attr("regExp",regExp);////"[a-zA-Z ]+", //characters and space
		}
	}
});

function ValidationTextBoxWrapper(o,container){
	var _self = this;
	var _widget = null;
	
	this.getValue = function(){
		return _widget.attr("value");
	};
	
	this.setValue = function(value){
		_widget.attr("value",value);
	};
	
	this.getDomNode = function(){
		return _widget.domNode;
	};
	
	this.getWidget = function(){
		return _widget;
	};
	
	this.isValid = function(){
		return (_widget.state == "");
	};
	
	this.setDisabled = function(b){
		_widget.attr("disabled",b);
	};
	
	function ggg(constraints){
		alert(constraints);
	}
	
	function init(){
		
		var c = {
			id: o.id
		};
		
		if(typeof o.value != 'undefined'){
			c.value = o.value;
		}
		if(typeof o.maxLength != 'undefined'){
			c.maxLength = o.maxLength;
		}
		if(typeof o.style != 'undefined'){
			c.style = o.style;
		}
		if(typeof o.regExp != 'undefined'){
			c.regExp = o.regExp;//"[a-zA-Z ]+", //characters and space
		}
		if(typeof o.required != 'undefined'){
			c.required = o.required;
		}
		if(typeof o.invalidMessage != 'undefined'){
			c.invalidMessage = o.invalidMessage;
		}
		if(typeof o.constraints != 'undefined'){
			c.constraints = o.constraints;
		}
		if(typeof o.regExpGen != 'undefined'){
			c.regExpGen = o.regExpGen; //function to validate
		}
		if(typeof o.required != 'undefined'){
			c.required = o.required; 
		}
		if(typeof o.containerId != 'undefined'){
			var container = document.createElement("span");
			if(typeof dojo.byId(o.containerId) != 'undefined'){
				dojo.byId(o.containerId).appendChild(container);
			}
		}
		else{
			_widget = new dijit.form.ValidationTextBox(c,container);
			_self.domNode = _widget.domNode;
		}
	}
	init();
}
}
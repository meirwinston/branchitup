if(!dojo._hasResource["dojoui.TooltipDialog"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.TooltipDialog"] = true;
dojo.provide("dojoui.TooltipDialog");

dojo.require("dijit.TooltipDialog");
	
dojo.declare("dojoui.TooltipDialog", dijit.TooltipDialog, {
	content: "my content",
//		title: "my title",
	closable: true,
	parseOnLoad: true,
//		show: function(){
//			var _self = this;
//			console.log("show function " + _self);
//			dijit.popup.open({
//				parent: _self.domNode.parentNode,
//				popup: _self,
//				around: _self.domNode.parentNode
//			});
//		},
	execute: function(e){console.log("execute TooltipDialog!");}
});
}
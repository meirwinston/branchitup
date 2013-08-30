if(!dojo._hasResource["dojoui.FisheyeList"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.FisheyeList"] = true;
dojo.provide("dojoui.FisheyeList");

dojo.require("dojox.widget.FisheyeList");
//dojo.require("dojox.widget.FisheyeListItem");//label,iconSrc, onclick

dojo.declare("dojoui.FisheyeList", dojox.widget.FisheyeList, {
	itemWidth: "20", 
	itemHeight: "20",
	itemMaxWidth: "50",
	itemMaxHeight: "50",
	orientation: "horizontal",
	effectUnits: "2",
	itemPadding: "10",
	attachEdge: "top",
	labelEdge: "bottom",
	createItem: function(o){ //{label,iconSrc,onclick}
		var item = dojox.widget.FisheyeListItem(o);
		this.addChild(item);
		return item;
	}
});
}
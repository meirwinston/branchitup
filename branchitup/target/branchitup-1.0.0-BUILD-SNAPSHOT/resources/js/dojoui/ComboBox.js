if(!dojo._hasResource["dojoui.ComboBox"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.ComboBox"] = true;
dojo.provide("dojoui.ComboBox");

dojo.require("dijit.form.ComboBox");
dojo.require("dojo.data.ItemFileReadStore");

dojo.declare("dojoui.ComboBox", dijit.form.ComboBox, {
	postCreate: function(){
		this.inherited(arguments);
	},
	setItems: function(items,identifier){ //NOT TESTED
		var store = new dojo.data.ItemFileReadStore( {
			data : {
				identifier : identifier,
				items : items
			}
		});
		this.attr("store",store);
	}
});
}
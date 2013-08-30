if(!dojo._hasResource["dojoui.LanguageFilteringSelect"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.LanguageFilteringSelect"] = true;
dojo.provide("dojoui.LanguageFilteringSelect");

dojo.require("dijit.form.FilteringSelect");
dojo.require("dojo.data.ItemFileReadStore");

dojo.declare("dojoui.LanguageFilteringSelect", dijit.form.FilteringSelect, {
	store: new dojo.data.ItemFileReadStore({
		data: {
			identifier: "value",
			items: [
		        {name: "English", value: "ENGLISH"},
		        {name: "Hebrew", value: "HEBREW"}
			]
		}
	}),
	invalidMessage: "No valid language is selected",
	postCreate: function(){
//		
		this.inherited(arguments);
	}
});
}
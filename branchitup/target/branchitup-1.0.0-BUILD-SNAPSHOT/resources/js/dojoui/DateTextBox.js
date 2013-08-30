if(!dojo._hasResource["dojoui.DateTextBox"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojoui.DateTextBox"] = true;
dojo.provide("dojoui.DateTextBox");

/*
Pattern  	Result (in a particular locale)
yyyy.MM.dd G 'at' HH:mm:ss zzz 	1996.07.10 AD at 15:08:56 PDT
EEE, MMM d, ''yy 	Wed, July 10, '96
h:mm a 	12:08 PM
hh 'o''clock' a, zzzz 	12 o'clock PM, Pacific Daylight Time
K:mm a, z 	0:00 PM, PST
yyyyy.MMMM.dd GGG hh:mm aaa 	01996.July.10 AD 12:08 PM


 */
dojo.require("dijit.form.DateTextBox");

dojo.declare("dojoui.DateTextBox", dijit.form.DateTextBox, {
	required: false,
//		constraints: {datePattern: "dd-MMM-yyyy"}, //doesn't work here only as argument
	
//		postCreate: function(){
//			this.inherited(arguments);
////			this.attr("constraints",{datePattern: "dd-MMM-yyyy"});
//		},
	getValue: function(){
		return this.attr("displayedValue");
	},
	getDate: function(){
		return this.attr("value");
	},
	setDate: function(date){
		this.attr("value",date);
	},
	setValue: function(s){//not checked
		if(typeof s != 'undefined' && s != ""){
			this.attr("displayedValue",s);
		}
	},
	isValid: function(){ //not checked
		return (this.state == "");
	},
	setDisabled: function(b){//not checked
		this.attr("disabled",b);
	}
});
}
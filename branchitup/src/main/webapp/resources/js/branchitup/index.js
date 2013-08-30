
	
//dojo.connect(branchitup.startup,function(){
//	header.register();
//});
function onLoad(){	
	importJS(["resources/js/branchitup/MainTabContainer.js"],function(){
		branchitup.mainTabContainer = new MainTabContainer({});
		header.register();
	});
	
//	
//	if(dojo.isIE >= 7){ // only IE8 and above
//		importJS(["http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js"]);
//	}
//
//		if(dojo.isFF < 3){ // only Firefox 2.x and lower
//		  ...
//		}
//
//		if(dojo.isIE == 7){ // only IE7
//		  ...
//		}
}
dojo.addOnLoad(onLoad);
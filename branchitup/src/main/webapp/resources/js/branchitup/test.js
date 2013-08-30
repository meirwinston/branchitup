dojo.require("dijit.form.TextBox");
dojo.require("dojoui.Button");

function testUploader(){
	
	dojo.require("dojoui.UploadButton");
	dojo.require("dojox.form.FileUploader");
	var uploader = new dojoui.UploadButton({
		uploadType: "flash",
		url: "upload/FileUpload?params={method:'flash'}", //html5
		label: "U...",
		style: "width: 80px; height: 20px;",
//		force: "html",
		onBegin: function(){
			console.log("***testUploader.onBegin::");
//			this.url = "upload/FileUpload?method=flash"; //html5
		},
		onComplete: function(params){ //these params are determined in upload/FileUpload
			console.log("***testUploader.initUploader.onComplete::" + params);
//			console.log(); 
//			printObject(params);
			for(var i = 0 ; i < params.length ; i++){
				printObject(params[i]);
				
				if(params[i].additionalParams){
					console.log("params[i].additionalParams");
					printObject(params[i].additionalParams);
				}
				else{
					console.log("NO params[i].additionalParams");
				}
			}
		}
	},"testDiv");
console.log("login.js:::");
//console.log(uploader);



var uploaderx = new dojox.form.FileUploader({
	id: "__ee__ee__",
	button: new dijit.form.Button({label: "SAAAAA", style: "width: 200px; height: 25px;"}),
	uploadUrl: "upload/FileUpload?params={method: 'flash'}", //html5
	selectMultipleFiles: true,
	uploadOnChange: false,
	degradable: true,
	style: "width: 200px; height: 25px;",
	fileMask: ["All Images", "*.*"]
	
});

console.log(uploaderx);
dojo.byId("testDiv2").appendChild(uploaderx.domNode);

//	this.flashMovie.doUpload()
//	this.onFileChange(_6);
//	function __flash__toXML(value)
//	try { document.getElementById("dojox-embed-flash-0").SetReturnValue(__flash__toXML(dojo.publish("testDiv/filesProgress",[({bytesLoaded:561276,name:"Lighthouse.jpg",bytesTotal:561276}),"event"])) ); } catch (e) { document.getElementById("dojox-embed-flash-0").SetReturnValue("<undefined/>"); }
}

function onLoad(){
	testUploader();
}
dojo.addOnLoad(onLoad);

/*
lls=[].concat(ls)
for(i in lls)
if(!(i in ap))
lls[i].apply(this,arguments);
lls
return r
this.flashMovie.doUpload();
}
}
lls=[].concat(ls)
for(i in lls)
if(!(i in ap))
lls[i].apply(this,arguments)
lls
return r
this.onFileChange(_6)
}
lls
return r
}
var type = typeof(value)
if (type == "string")
if (type == "undefined")
return "<undefined/>"
try <***FORK***> return _67[_68].apply(_67,arguments||[])
*/
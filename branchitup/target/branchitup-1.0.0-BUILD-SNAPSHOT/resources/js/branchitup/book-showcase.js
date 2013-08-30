//console.log("BookShowcase:::");
//console.log(dijit.byId("readMoreButton"));
function BookShowcase(args){
	this.args = args;
	this.itemIndex = 0;
	this.modulusRotation = 6;
	
	this.coverImage = dojo.byId("bookShowcase.coverImage");
	this.bookDescription = dojo.byId("bookShowcase.bookDescription");
	this.navigateNext = dojo.byId("bookShowcase.navigateNext");
	this.navigatePrev = dojo.byId("bookShowcase.navigatePrev");
	this.header = dojo.byId("bookShowcase.header");
	
	var _self = this;
	dojo.connect(this.navigateNext,"click",function(){
		_self.itemIndex = ((_self.itemIndex+1) % _self.modulusRotation);
		_self.scroll();
	});
	
	dojo.connect(this.navigatePrev,"click",function(){
		if(_self.itemIndex <= 0) _self.itemIndex+=_self.modulusRotation;
		
		_self.itemIndex = ((_self.itemIndex-1) % _self.modulusRotation);
		_self.scroll();
	});
};
BookShowcase.prototype.onSelect = function(e){
	if(this.args && this.args.publication){
		window.location.href = ("publishedbook?bookId=" + this.args.publication.bookId); 
	}
};
BookShowcase.prototype.setPublication = function(publication){
	if(!publication) return;
	this.args.publication = publication;
	this.bookDescription.innerHTML = publication.bookSummary;
	this.header.style.backgroundImage = "url(resources/images/" + (this.itemIndex + 1) + "th-slide-selected_170x20.png)";
	
	if(publication.coverImageFileId){
		//thumbnail: "w100Border", src: ("imageservice?id=" + record.coverImageFileId + "&thumbnail")
		this.coverImage.setAttribute("src",("imageservice?id=" + publication.coverImageFileId + "&thumbnail"));
	}
	else{
		this.coverImage.setAttribute("src","resources/images/no-image-branchitup_200x200.png");
	}
};
	

BookShowcase.prototype.scroll = function(){
	var _self = this;
//	console.log("weRbookShowcaseroll.itemIndex " + this.itemIndex);
	var args = {offset: this.itemIndex, maxResults: 1};
	
	branchitup.jsonServlet.doGet("BrowseBookShowcase",args,function(response){
		_self.setPublication(response.publications[0]);
	});
};

var bookShowcase = new BookShowcase({});
bookShowcase.scroll();


function onLoad(){
	readMoreButton.set("onClick",dojo.hitch(bookShowcase,bookShowcase.onSelect));
}
dojo.addOnLoad(onLoad);
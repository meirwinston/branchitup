//dojo.require("dojoui.Toolbar");

var itemsDropdown = new GenresDropdown({
	onChange: function(o){
//		console.log("GenreDropdown, this is my onchange function");
//		console.log(o);
		if(o && o.item && o.item.id){
			window.location.href = "genre?genreId=" + o.item.id;
		}
	},
	onTextChange: function(value){
		if(value && value != ""){
//			params.searchPhrase = value;
		}
		else{
//			delete params.searchPhrase;
			window.location.href = "genres";
		}
//		console.log("GEnresBrowser.onTextChange " + value);
//		browseGenres(0);
	}
});

function initToolbar(){
	var d; 
	
	d = dojo.byId("header.searchbarContainer"); 
	d.appendChild(itemsDropdown.domNode);
}

function reload(callback){
}
function onLoad(){
	initToolbar();
	reload();
}
dojo.addOnLoad(onLoad);
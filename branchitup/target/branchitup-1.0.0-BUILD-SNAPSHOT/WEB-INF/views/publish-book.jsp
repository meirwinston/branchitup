<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
  	<meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
		@import "resources/css/publish-book.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Publish ${book.title}</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script type="text/javascript">
    	var params = ${params};
    	
    	dojo.require("dijit.form.FilteringSelect");
    	dojo.require("dijit.form.Form");
    	//dojo.require("dijit.form.Textarea");
    	dojo.addOnLoad(function(){
    		bookId = params["bookId"][0];
    		var d;
    		
    		d = dojo.byId("publishbook.title");
    		if(${not empty book.title}){
    			d.innerHTML = ${func:serialize(book.title)};
    		}
    		d = dojo.byId("publishbook.summary");
    		if(${not empty book.bookSummary}){
    			d.innerHTML = ${func:serialize(book.bookSummary)};
    		}
    		
    		d = dojo.byId("publishbook.coverImage");
    		
    		if(${not empty book.parentCoverImageUrl}){
    			d.src = "${book.parentCoverImageUrl}";
    			d.setAttribute("coverImageFileId","${book.parentCoverImageFileId}");
    		}
    		
    		var map = ${func:serialize(supportedLanguages)};
    		//console.log("init --- ");
    		//console.log(arr);
    		var languageItems = [];
    		for(var threeCharCode in map){
    			languageItems.push({value: threeCharCode, name: map[threeCharCode]});
    		}
    		initLanguageInput(languageItems);
    		//console.log("published-book.init ");
    		
    		var derivedGenres = ${func:serialize(derivedGenres)};
    		if(derivedGenres){
    			if(derivedGenres.length > 0){
    				derivedGenres[0].id = derivedGenres[0].genreId;
    				primaryGenreDropdown.setItem(derivedGenres[0]);
    			}
        	if(derivedGenres.length > 1){
        		derivedGenres[1].id = derivedGenres[1].genreId;
        		secondaryGenreDropdown.setItem(derivedGenres[1]);
    			}
    		}
    		
    	});
    </script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra publishbook">
  <st:header id="branchitup_header" />
  <div id="publishbook.centerPane">
		<h2>Publish Your Book</h2>
		<div id="publishbook.bookBlock">
			<img id="publishbook.coverImage" src="resources/images/no-image_200x201.png" /><label id="publishbook.title" class="heading3"></label>
			<br/><br/>
			<span id="publishbook.summary"></span>
		</div>
		<div style="border-bottom: 2px dashed grey; margin-bottom: 15px;"></div>
		<c:if test="${sheetsCount != null && sheetsCount <= 0}">
		<div><b style="color: red;">This is an empty book! </b> To add sheets to this book, first <a href="sheeteditor">create a sheet</a>, save it, and go to <a href="bookeditor?bookId=${book.bookId}">New Book</a> and add sheets to the book.<br><br></div>
		</c:if>
		
		<div id="publishbook.wizardContainer"></div>
	</div>
	<script src="resources/js/branchitup/widgets/ItemsDropdown.js" ></script>
	<script src="resources/js/branchitup/widgets/ImageUploader.js" type="text/javascript"></script>
  <script src="resources/js/branchitup/publish-book.js" ></script>
  <div id="publishbook.hidden" style="visibility: hidden;">
	  <table id="publishbook.userRole" >
	  	<tbody>
	  		<tr>
	  			<td style="width: 10px;"><img src="resources/images/writing_20x20.png" /></td>
	  			<td style="width: 10px;"><input checked="true" type="checkbox" id="role.author" /></td>
	  			<td>Writer</td>
	  		</tr>
	  		<tr>
	  			<td><img src="resources/images/illustrating_24x20.png" /></td>
	  			<td><input type="checkbox" id="role.illustrator" /></td>
	  			<td>Illustrator</td>
	  		</tr>
	  		<tr>
	  			<td><img src="resources/images/translating_26x20.png" /></td>
	  			<td><input type="checkbox" id="role.translator" /></td>
	  			<td>Translator</td>
	  		</tr>
	  		<tr>
	  			<td><img src="resources/images/editing_14x20.png" /></td>
	  			<td><input type="checkbox" id="role.editor" /></td>
	  			<td>Editor</td>
	  		</tr>
	  		<tr>
	  			<td><img src="resources/images/co-writing_21x20.png" /></td>
	  			<td><input type="checkbox" id="role.proofreader" /></td>
	  			<td>Proof Reader</td>
	  		</tr>
	  		<tr>
	  			<td id="publishbook.describeYourRoleLabel"  colspan="3"><label class="blueHeading4">Describe your role:</label>&nbsp;&nbsp;Briefly describe your role in this publication.  Be sure to specify any enhancements you made to the original work if this is a branch.</td>
	  		</tr>
	  		<tr>
	  			<td id="publishbook.publisherCommentCell" colspan="3"></td>
	  		</tr>
	  	</tbody>
	  </table>
 
   	<table id="publishbook.definiencyList">
  	<tbody>
  		<tr>
  			<td style="width: 10px;"><input checked="true" type="checkbox" id="publishbook.allowBranching" onclick="onAllowBranchingChange()" /></td>
  			<td colspan="2">Check here if you give permission to other users to branch this publication. (you will be credited on any new publication)</td>
  		</tr>
  		<tr id="deficiency.listTitleRow">
  			<td></td>
  			<td colspan="2"><b>Recommendations</b><br>Check the enhancements you would like to be made to your publication
  			</td>
  		</tr>
  		<tr id="deficiency.illustratingRow">
  			<td></td>
  			<td style="width: 10px;"><input type="checkbox" id="deficiency.illustrating" checked="true" /></td>
  			<td>Illustrate</td>
  		</tr>
  		<tr id="deficiency.translatingRow">
  			<td></td>
  			<td><input type="checkbox" id="deficiency.translating" checked="true" /></td>
  			<td>Translate</td>
  		</tr>
  		<tr id="deficiency.coauthoringRow">
  			<td></td>
  			<td><input type="checkbox" id="deficiency.coauthoring" /></td>
  			<td>Co-Writing</td>
  		</tr>
  		<tr id="deficiency.editingRow">
  			<td></td>
  			<td><input type="checkbox" id="deficiency.editing" /></td>
  			<td>Edit</td>
  		</tr>
  		<tr id="deficiency.proofreadingRow">
  			<td></td>
  			<td><input type="checkbox" id="deficiency.proofreading" /></td>
  			<td>Proof Read</td>
  		</tr>
  		<tr>
  			<td></td>
  			<td></td>
  			<td></td>
  		</tr>
  	</tbody>
  	</table>
  </div>
  </body>
</html>
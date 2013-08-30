<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
		@import "resources/css/genre-editor.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Genres</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script>
    //var params = ${params};
	dojo.addOnLoad(function(){
		if(${not empty genre.genreId}){
			nameInput.setItem({id: parseInt("${genre.genreId}"), name: ${func:serialize(genre.name)}});
			//console.log("A${genre.genreId}");
			//dojo.byId("genreeditor.uploadButton").parentNode.replaceChild(dojo.create("label"),dojo.byId("genreeditor.uploadButton"));
			
			//if(${not empty genre.name}){
			//	nameInput.set("value",${func:serialize(genre.name)});
			//}
			nameInput.setDisabled(true);
			
			if(${not empty genre.description}){
				descriptionInput.set("value",${func:serialize(genre.description)});
			}
    		if("${genre.iconImageUrl}" == ""){
    			//console.log("NO IMAGE---");
    			//dojo.byId("genreeditor.genreImage").setAttribute("src","resources/images/no-image_200x201.png");
    		}
    		else{
    			//console.log("YES IMAGE---${genre.iconImageUrl}");
    			dojo.byId("genreeditor.genreImage").setAttribute("src","${genre.iconImageUrl}");
    		}
    		
    		//uploadButton.domNode.parentNode.removeChild(uploadButton.domNode);
    		//delete uploadButton;
		}
		//else{
			//dojo.byId("genreeditor.uploadButton").parentNode.replaceChild(uploadButton.domNode,dojo.byId("genreeditor.uploadButton"));
		//}
	});
    </script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra genreeditor">
  	<c:if test="${not noHeader}">
  	<header><st:header id="branchitup_header" /></header>
  	</c:if>
  	<div id="genreeditor.toolbar"></div>
  	<div id="genreeditor.centerPane">
  		<h2>New Genre</h2>
  		<form jsId="genreForm" data-dojo-type="dijit.form.Form" data-dojo-props="method: 'post'">
  		<table name="layout">
  			<tr>
  				<td class="blueHeading3">Step 1:</td>
  				<td>Input the name of the genre (up to 50 characters): </td>
  			</tr>
  			<tr>
  				<td></td>
  				<td id="genreeditor.nameInputContainer"></td>
  			</tr>
  			<tr><td colspan="2" name="space"></td></tr>
  			<tr>
  				<td class="blueHeading3">Step 2:</td>
  				<td>Input a description for the genre below: </td>
  			</tr>
  			<tr>
  				<td></td>
  				<td id="genreeditor.descriptionInputContainer"></td>
  			</tr>
  			<tr><td colspan="2" name="space"></td></tr>
  			<tr>
  				<td class="blueHeading3">Step 3: </td>
  				<td>Optionally, upload an image to represent your genre:</td>
  			</tr>
  			<tr>
  				<td></td>
  				<td>
  					<img id="genreeditor.genreImage" name="genreImage" src="resources/images/no-image-branchitup_200x200.png" />
  					<span id="genreeditor.uploadButton" />
  				</td>
  			</tr>
  			<tr><td colspan="2" name="space"></td></tr>
  			<tr>
  				<td></td> <!-- dont call the method 'submit' safari reserves this operation to itself -->
  				<td><img id="genereeditor.submitButton" name="submitButton" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitGenre();" /></td>
  			</tr>
  		</table>
  		<input type="hidden" name="fileItemId" jsId="fileItemIdInput"  data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'fileItemId', type: 'hidden'" />
  		</form>
  	</div>
  	<script src="resources/js/branchitup/widgets/ItemsDropdown.js"></script>
  	<script src="resources/js/branchitup/widgets/ImageUploader.js" type="text/javascript"></script>
	  <script src="resources/js/branchitup/genre-editor.js" ></script>
  </body>
</html>
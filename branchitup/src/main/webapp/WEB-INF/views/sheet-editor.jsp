<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
		@import "resources/css/sheet-editor.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title><c:if test="${sheet.name != null}">${sheet.name}</c:if><c:if test="${sheet.name == null}">Write...</c:if></title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script type="text/javascript">
    	dojo.addOnLoad(function(){
    		//return;
    		<c:if test="${not empty sheet.sheetId}">
    			sheetId = ${sheet.sheetId};
    		</c:if>
    		
    		<c:if test="${not empty permissions}">
    			permissions = ${permissions};
    			visibilityInput.set("value",permissions.visibility);
	    		oldVisibility = permissions.visibility;
	    		allowEditingInput.set("checked",permissions.allowEditing);
				</c:if>
    		
    		if(${not empty sheet.name}){
    			sheetNameInput.set("value",${func:serialize(sheet.name)});
    		}
    		folderNameInput.set("value",${func:serialize(sheet.folderName)});
    		if(${not empty sheet.content}){
    			sheetEditor.set("value",${func:serialize(sheet.content)});
    		}
    		
    		imagesContainer.setAlbums(${albums});
    		
    		if(visibilityInput.get("value") == "PUBLIC"){
    			visibilityInput.set("disabled",true);
    			if(permissions.allowEditing){
    				allowEditingInput.set("disabled", true);
    			}
    			
    			folderNameInput.set("disabled",true);
    			//folderNameInput.set("value",null);
    			if(!permissions.ownedByUser && !permissions.allowEditing){
    				submitButton.set("disabled",true);
    			}
    		}
    		if(permissions && permissions.forcePrivate){
    			visibilityInput.set("value","PRIVATE");
    			visibilityInput.set("disabled",true);
    		}
    	});
    </script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
  <st:header id="branchitup_header" page="sheeteditor"/>
  <div id="sheeteditor.centerPane"></div>
  <div id="sheeteditor.footerPane">&nbsp;</div>
  <script src="resources/js/branchitup/widgets/ImageUploader.js"></script>
  <script src="resources/js/branchitup/widgets/ImagesContainer.js"></script>
  <script src="resources/js/branchitup/sheet-editor.js" ></script>
  </body>
</html>
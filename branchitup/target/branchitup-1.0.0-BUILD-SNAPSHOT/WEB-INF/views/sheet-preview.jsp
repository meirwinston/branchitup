<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
		@import "resources/css/sheet-preview.css"
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>${sheet.name}</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
  </head>
  <body class="tundra">
  	<c:if test="${not noHeader}">
  		<st:header id="branchitup_header" page="sheetpreview" />
  	</c:if>
		<div id="centerPane">${sheet.content}</div>
		<script src="resources/js/branchitup/sheet-preview.js"></script>
		<script src="resources/js/branchitup/google-analytics.js"></script>
  </body>
</html>
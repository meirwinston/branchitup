<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
		@import "resources/css/main.css";
		@import "resources/css/user-wall.css";
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>${userFullName} Wall</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script type="text/javascript">
    	dojo.require("dijit.form.Form");
    	dojo.require("dijit.form.Textarea");
    	if(${not empty userAccountId}){
    		var userAccountId = ${userAccountId};
    	}
    </script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
  	<header><st:header id="branchitup_header" /></header>
  	<div id="userwall.centerPane">
  		<div id="userwall.formPane">
  		<form jsId="messageForm" data-dojo-type="dijit.form.Form" data-dojo-props="method: 'get'">
			<span>Add message to ${userFullName} here: </span>
			<br/>
			<span id="userwall.messageInputContainer"></span><small>(maximum 512 characters)</small>
			<br/>
			<span id="userwall.submitButtonContainer"></span>
		</form>
  		</div>
  		<div id="userwall.messagesPane"></div>
  	</div>
  	<div id="userwall.paginationCell"></div>
    <script src="resources/js/branchitup/widgets/ItemsDropdown.js"></script>
	  <script src="resources/js/branchitup/user-wall.js" ></script>
  </body>
</html>
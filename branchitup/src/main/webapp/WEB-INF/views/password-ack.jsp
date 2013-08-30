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
	</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title></title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
		Your password has been sent to your email, to login click <a href="login">here</a> or go to <a href="home">homepage</a>.
  </body>
</html>
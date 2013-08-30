<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->

<html>
  <head>
    <meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.7.1/dijit/themes/branchitup/tundra.css";
			@import "resources/css/main.css";
		</style>
		<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
		<link rel="image_src" href="resources/images/branch_47x50.png" />
  	<title>Branch It Up</title>
  	<!-- 
  	<script src='http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js'></script>
  	 -->
  	<script src="resources/js/dojo-release-1.7.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra">
  	<span id="headerPane"><st:header /></span>
  	<span id="centerPane" style="width: 100%;"></span>
    <div id='tmpContainer' style='position: fixed;visibility: hidden;'></div>
    
    <div id="testContainer"></div>
    <script src="resources/js/branchitup/index.js" ></script>
    
    
    <div style="position: relative;bottom:0px; width: 100%; height: 240px;">
    	<div style="position: absolute;"><img src="resources/images/publication-no-image_239x240.png"></div>
			<div align="center" style="position: absolute; width: 250px; " name='genre'><img src="resources/images/branch_26x28.png"><span name='genreTitle'>Fiction</span><img src="resources/images/branch_26x28.png"></div>
			<div style="margin:0px auto; width: 250px;" name='version'><img src="resources/images/branch_26x28.png"><span name='versionNumber'>2.0.2.3.4</span><img src="resources/images/branch_26x28.png"></div>
		</div>
  </body>
  
  <!-- TEST -->
<!-- TEST 
<script type="text/javascript" src="resources/js/branchitup/widgets/ItemsDropdown.js">
///branchitup/src/main/webapp/resources/js/branchitup/widgets/ItemsDropdown.js
//var c = dojo.byId("testContainer");

//var d = new ByCategoryDropdown({});
//c.appendChild(c.domNode);
</script>
 -->
</html> 

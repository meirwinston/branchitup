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
			@import "resources/css/user-profile.css";
		</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Profile</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra userprofile">
  <st:header id="branchitup_header" page="userprofile" />
  <div id="userprofile.centerPane">
  	<div id="userprofile.aboutMe">
  		<label class="heading3">${userAccount.firstName} ${userAccount.lastName}</label>
			<br/><br/>
			<c:if test="${not empty userAccount.profileImageUrl}">
			<img id="userprofile.profileImage" name="profileImage" src="${userAccount.profileImageUrl}" />
			</c:if>
			<c:if test="${empty userAccount.profileImageUrl}">
			<img id="userprofile.profileImage" name="profileImage" src="resources/images/no-image_200x201.png" />
			</c:if>
			<c:out value="${userAccount.aboutMe }"></c:out>
			<br>
			<img src="resources/images/user-wall_19x20.png" />
			<a href='userwall?userAccountId=${userAccount.userAccountId}'>Leave a message</a>
  	</div>
  	<br/><br/>
  
	<div class="heading3">Contact Information</div>
  	<div>
  	<c:choose>
  	<c:when test="${userAccount.visibility == 'PUBLIC'}">
  		<label class="blueHeading4">Email Address:&nbsp;&nbsp;&nbsp;</label><label>${userAccount.email}</label>
  	</c:when>
  	<c:otherwise>
  		<label style="color: grey">Unavailable</label>
  	</c:otherwise>
  	</c:choose>
  	</div>
  </div>
  <script src="resources/js/branchitup/widgets/ImageUploader.js" type="text/javascript"></script>
  <script src="resources/js/branchitup/user-profile.js" ></script>
  </body>
</html>
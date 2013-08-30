<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
  	<meta charset="UTF-8"> <!-- HTML5 syntax -->
  	<meta http-equiv="cache-control" content="max-age=0" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="pragma" content="no-cache" />
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
			@import "resources/css/main.css";
			@import "resources/css/edit-user-profile.css";
		</style>
	<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>Your Profile</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script type="text/javascript">
    	var params = ${params};
    	dojo.require("dijit.form.ValidationTextBox");
    	dojo.require("dijit.form.Form");
    	dojo.addOnLoad(function(){
    		var val;
    		
    		val = ${func:serialize(userAccount.firstName)};
    		firstNameInput.set("value",val);
    		
    		val = ${func:serialize(userAccount.middleName)};
    		middleNameInput.set("value", val);
    		
    		val = ${func:serialize(userAccount.lastName)};
    		lastNameInput.set("value",val);
    		
    		val = ${func:serialize(userAccount.aboutMe)};
    		aboutMeInput.set("value",val);
    		//console.log("^^^^^^^^ ${func:serialize(userAccount.aboutMe)}");
    		if(${not empty userAccount.gender}){
    			genderInput.set("value","${userAccount.gender}");
    		}
    		
    		val = ${func:serialize(userAccount.email)};
    		emailInput.set("value",val);
    		
    		if("${userAccount.profileImageUrl}".length > 0){
       		dojo.byId("userprofile.profileImage").src = "${userAccount.profileImageUrl}"; 
       	}
    		
    		dojo.byId("userprofile.visibilityInput").value = "${userAccount.visibility}";
    		console.log("setting the selected value to ${userAccount.visibility}");
    	});
    </script>
    <script src="resources/js/branchitup/google-analytics.js"></script>
  </head>
  <body class="tundra userprofile">
  <st:header id="branchitup_header" page="edituserprofile" />
  <div id="userprofile.centerPane">
  	<div>
  		<label class="heading3">Profile Image</label>
			<br/><br/>
			<img id="userprofile.profileImage" name="profileImage" src="resources/images/no-image_200x201.png" />
  			<!-- <span id="genreeditor.uploadButton" /> -->
  		<span id="userprofile.imageUploaderContainer"></span>
  		<span><img  name="imageButton" src="resources/images/buttons/clear-image-button_115x25.png" onclick="submitClearImage();" onmouseover="this.src = 'resources/images/buttons/clear-image-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/clear-image-button_115x25.png'"/></span>
  	</div>
  	<br/><br/>
  	<table name="layoutTable">
  	<tr>
			<td class="heading3">Visibility</td>
			<td>
				<select id="userprofile.visibilityInput" onchange="onVisibilityChange(this)">
					<option value="HIDE_CONTACT_INFORMATION" selected="selected">HIDE CONTACT INFORMATION</option>
					<option value="PUBLIC">PUBLIC</option>
					<option value="PRIVATE">PRIVATE</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="heading3">Personal Information</td>
		</tr>
		<tr>
			<td class="blueHeading4">First Name:</td>
			<td id="userprofile.firstNameInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4">Middle Name</td>
			<td id="userprofile.middleNameInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4">Last Name</td>
			<td id="userprofile.lastNameInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4">Gender</td>
			<td id="userprofile.genderInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4"></td>
			<td><img name="imageButton" id="userprofile.submitPersonalInfoButton" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitUserPersonalInfo()"/></td>
		</tr>
		<!-- --------------------- -->
		<tr>
			<td colspan="2" class="heading3">Your Email</td>
		</tr>
		<tr>
			<td class="blueHeading4">Email Address:</td>
			<td id="userprofile.emailInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4"></td>
			<td><img name="imageButton" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitNewEmail();"/></td>
		</tr>
		<!-- --------------------- -->
		<tr>
			<td colspan="2" class="heading3">Password</td>
		</tr>
		<tr>
			<td class="blueHeading4">Current Password: </td>
			<td id="userprofile.currentPasswordInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4">New Password: </td>
			<td id="userprofile.newPasswordInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4">Re-type Password: </td>
			<td id="userprofile.retypePasswordInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4"></td>
			<td><img name="imageButton" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitPassword();" /></td>
		</tr>
		<!-- --------------------- -->
		
		<tr>
			<td colspan="2" class="heading3">About Me</td>
		</tr>
		<tr>
			<td class="blueHeading4">Few words about me:<br></td>
			<td id="userprofile.aboutMeInputContainer"></td>
		</tr>
		<tr>
			<td class="blueHeading4"></td>
			<td><img name="imageButton" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'" onclick="submitAboutMe();" /></td>
		</tr>
		<!-- --------------------- -->
	</table>
  </div>
  <script src="resources/js/branchitup/widgets/ImageUploader.js" type="text/javascript"></script>
  <script src="resources/js/branchitup/edit-user-profile.js" ></script>
  </body>
</html>
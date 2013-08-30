<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html>
<html>
<head>
<style>		 
	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
	@import "resources/css/signup.css";
</style>
<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
<meta charset="UTF-8">
<title>New Account</title>
<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
<script src="resources/js/branchitup/main.js"></script>
<script src="resources/js/branchitup/google-analytics.js"></script>
</head>
<body class="tundra signup" >
<div name="logo"><img id="signup.logo" onclick="window.location.href='home'" src="resources/images/branchitup-logo_190x50.png" /></div>
<form jsId="signupForm" data-dojo-type="dijit.form.Form"  data-dojo-props="onSubmit: singupUser, method: 'get', action: 'branchitup_realm'">
<table name="layoutTable" class="loginLayoutTable">
<tbody>
<tr>
	<td align="center" id="signup.loginTitleCell"><img src="resources/images/signup-for-account_286x24.png" /></td>
</tr>
<tr>
	<td name="descriptionCell">Create your free registered account</td>
</tr>
<tr>
	<td name="emailAddressCell"><input name="email" type="text" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="trim: true, placeHolder: 'Email', name: 'email', required: 'true', regExp: '[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}', invalidMessage: 'Invalid Email Address.'" /></td>
</tr>
<tr>
	<td name="firstNameCell"><input type="text" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'firstName', trim: true, placeHolder: 'First Name', maxLength: 32, propercase: true, regExp: '[a-zA-Z -]+'" /></td>
</tr>
<tr>
	<td name="lastNameCell"><input type="text" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'lastName', trim: true, placeHolder: 'Last Name', maxLength: 50, propercase: true, regExp: '[a-zA-Z ]+'" /></td>
</tr>
<tr>
	<td name="passwordCell"><input type="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'password', trim: true, placeHolder: 'Password', type: 'password', maxLength: 32" /></td>
</tr>
<tr>
	<td name="passwordCell"><input type="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'passwordConfirm', trim: true, placeHolder: 'Retype Password', type: 'password', maxLength: 32" /></td>
</tr>
<tr>
	<td name="imageCell"><img id="signup.captchaImg" src="imageservice?target=CAPTCHA" /></td>
</tr>
<tr>
	<td name="challengeCell"><input data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="name: 'challenge', trim: true, placeHolder: 'Enter Challenge', type: 'text', maxLength: 8" /></td>
</tr>
<tr>
	<td style="visibility: hidden;" name="agreeCell"><input type="checkbox" name="agree" id="signup.agreeCheckbox" checked="true" />&nbsp;I agree to the terms of service</td>
</tr>
<tr>
	<td name="submitCell"><img name="submitButton" onclick="singupUser()" src="resources/images/buttons/submit-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/submit-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/submit-button_115x25.png'"/></td>
</tr>
</tbody>
</table>
</form>

<form id="signup.loginForm" method="POST" action='j_spring_security_check'>
	<input type="hidden" name="j_username" id="j_username" />
	<input type="hidden" name="j_password" id="j_password" />
</form>
<script type="text/javascript" src="resources/js/branchitup/signup.js"></script>
<st:footer />
</body>
</html>

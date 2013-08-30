<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>
<!DOCTYPE html>
<html>
<head>
<style>		 
	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
	@import "resources/css/login.css";
</style>
<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
<meta charset="UTF-8">
<title>Login to Branch It Up</title>
<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
<script src="resources/js/branchitup/main.js"></script>
<script src="resources/js/branchitup/google-analytics.js"></script>
</head>
<body class="tundra login" >
<div name="titleCell"><img src="resources/images/branchitup-logo_190x50.png" /></div>
<form method="POST" data-dojo-type="dijit.form.Form"  data-dojo-props="action: '/branchitup/j_spring_security_check'">
<table name="layoutTable" class="loginLayoutTable">
<tbody>
<tr>
	<td align="center" name="loginTitleCell"><img src="resources/images/user-login-title_129x24.png" /></td>
</tr>
<tr>
	<td name="errorCell"><c:if test="${param['login_error'] == 'true'}">Failed to login</c:if><c:if test="${param['login_error'] != 'true'}"></c:if></td>
</tr>
<tr>
	<td name="descriptionCell">Login using your registered account</td>
</tr>
<tr>
	<td name="emailAddressCell"><input type="text" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="value: '', autocomplete: 'on', trim: true, placeHolder: 'Email Address', name: 'j_username', id: 'j_username'" /></td>
</tr>
<tr>
	<td name="passwordCell"><input type="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="value:'', trim: true, placeHolder: 'Password', name: 'j_password', id: 'j_password', type: 'password'" /></td>
</tr>
<tr>
	<td name="forgotPasswordCell"><img src="resources/images/info-icon_16x17.png"/>&nbsp;<a href="restorepassword">Forgot password?</a></td>
</tr>
<tr>
	<td name="submitCell"><input type="image" name="submitButton" onclick="javascript:submit()" src="resources/images/buttons/signin-button_115x25.png" onmouseover="this.src = 'resources/images/buttons/signin-button-hover_115x25.png'" onmouseout="this.src = 'resources/images/buttons/signin-button_115x25.png'"/><br/><span name="registerNow">Don't have an account? <a href="signup">Register Now</a></span></td>
</tr>

</tbody>
</table>
</form>
<script type="text/javascript" src="resources/js/branchitup/login.js"></script>
<st:footer />
</body>
</html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag import="org.springframework.beans.factory.annotation.Autowired"%>
<%@tag import="com.branchitup.service.ModelService"%>
<%@tag description="User Workdesk" pageEncoding="UTF-8"%>
<%@attribute name="id"%>
<style>		 
		@import "resources/css/user-workdesk.css";
</style>

<table id="userWorkdesk">
	<tr>
		<td name="headerLeft"></td>
		<td name="headerMiddle" onclick="javascript:window.location.href='workdesk'">Your Work Desk</td>
		<td name="headerRight"></td>
	</tr>
	<tr>
		<td name="2ndHeaderLeft"></td>
		<td name="2ndHeaderMiddle"></td>
		<td name="2ndHeaderRight"></td>
	</tr>
	<tr>
		<td id="userWorkdesk.workdeskContent" name="workdeskContent" colspan="3">No Items in you workdesk...</td>
	</tr>
</table>
<script src="resources/js/branchitup/user-workdesk.js"></script>
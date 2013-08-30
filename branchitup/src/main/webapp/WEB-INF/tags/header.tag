<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="BranchItUp header" pageEncoding="UTF-8"%>
<%@attribute name="id"%>
<%@attribute name="page"%>
<style>		 
		@import "resources/css/header.css";
</style>
<script type="text/javascript">
var user = null;
function openSheetEditor(){
	//window.open("sheeteditor");
	window.location.href = "sheeteditor";
}

</script>

<c:if test="${pageContext.request.userPrincipal != null}">
	<script>
		user = "${pageContext.request.userPrincipal}";
	</script>
</c:if>
	<table class="branchitupHeaderToolbar" border="0">
	<tbody>
	<tr>
		<td name="button" style="width: 150px;">
			<a href="home"><img src="resources/images/branchitup-logo-white_100x36.png" style="" /></a>
		</td>
		<td name="button" style="width: 110px;" onclick="javascript:window.location.href=dojo.query('a',this)">
			<c:choose>
		  	<c:when test="${page == 'sheetpreview' && allowEditing}">
		  		<a href="sheeteditor?sheetId=${sheet.sheetId}"><img src='resources/images/sheet_21x20.png'/>Edit Sheet</a>
		  	</c:when>
		  	<c:otherwise>
		  		<a href="sheeteditor"><img src='resources/images/sheet_21x20.png'/>New Sheet</a>
		  	</c:otherwise>
		  </c:choose>
			
		</td>
		<td name="button" style="width: 110px;" onclick="javascript:window.location.href=dojo.query('a',this)">
			<a href="bookeditor"><img src='resources/images/book_20x20.png'/>New Book</a>
		</td>
		<td name="button" style="width: 90px;" onclick="javascript:window.location.href=dojo.query('a',this)">
		<c:choose>
  	<c:when test="${page == 'genres'}"><a href="genreeditor"><img src='resources/images/genre_19x20.png'/>New Genre</a>
  	</c:when>
  	<c:when test="${page == 'genre'}"><a href="genreeditor?genreId=${genre.genreId}"><img src='resources/images/genre_19x20.png'/>Edit Genre</a>
  	</c:when>
  	<c:otherwise><a href="genres"><img src='resources/images/genre_19x20.png'/>Genres</a>
  	</c:otherwise>
  	</c:choose>
		</td>
		<td name="button" style="width: 140px;" onclick="javascript:window.location.href=dojo.query('a',this)">
			<c:choose>
		  	<c:when test="${pageContext.request.userPrincipal != null}">
		  		<a href="workdesk"><img src='resources/images/workdesk_25x20.png'/>Your Workdesk</a>
		  	</c:when>
		  	<c:otherwise>
		  		<a href="help"><img src='resources/images/branch_23x24.png' style='margin-right: 5px; border: 0;' />What Is BranchItUp?</a>
		  	</c:otherwise>
	  	</c:choose>
		</td>
		<c:if test="${page == 'sheeteditor'}">
		<td onclick="if(onHeaderUploadImageClick) onHeaderUploadImageClick();" name="button" style="width: 120px;" onclick="javascript:window.location.href=dojo.query('a',this)">
			<img src='resources/images/photo_scenery_20x20.png'/>Upload Image
		</td>
		</c:if>
		<td id="header.searchbarContainer">
			<c:choose>
		  	<c:when test="${page == 'sheeteditor'}">
		  		<label name="label">Here type a chapter, short story, recipe,... <br>When your done, SAVE the sheet!</label>
		  	</c:when>
		  	<c:when test="${page == 'edituserprofile'}"></c:when>
		  	<c:when test="${page == 'bookeditor'}"></c:when>
		  	<c:when test="${page == 'userprofile'}"></c:when>
		  	<c:otherwise>
		  		<img style="vertical-align: bottom;" src="resources/images/search-icon_24x20.png" />
		  	</c:otherwise>
		  </c:choose>
		</td>
		<c:choose>
  	<c:when test="${pageContext.request.userPrincipal != null}">
  		<td id="header.toolsContainer" name="button" style="width: 32px;">
  			<img src="resources/images/header-tools-wheel_32x20.png">
  		</td>
  	</c:when>
  	<c:otherwise>
  		<td id="header.singupContainer" name="button" style="width: 60px;" onclick="window.location.href='signup'">Sign Up</td>
			<td id="header.loginContainer" name="button" style="width: 60px;" onclick="window.location.href='login'">Log In</td>
  	</c:otherwise>
  	</c:choose>
	</tr>
	</tbody>
</table>
<div id="header.toolbarContainer"></div>
<script type="text/javascript" src="resources/js/branchitup/header.js"></script>
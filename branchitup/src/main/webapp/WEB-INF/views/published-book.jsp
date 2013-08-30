<%@page import="com.branchitup.json.JacksonUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="func" uri="/WEB-INF/tld/functions.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tld/simple-tags.tld" %>

<!DOCTYPE html> <!-- HTML5 syntax -->
<html>
  <head>
  	<meta charset="UTF-8"> <!-- HTML5 syntax -->
    <style>		 
    	@import "resources/js/dojo-release-1.8.1/dijit/themes/tundra/tundra.css";
			@import "resources/css/main.css";
			@import "resources/css/published-book.css";
		</style>
		<link rel="shortcut icon" href="resources/images/branchitup-logo_15x16.ico" type="image/x-icon" />
  	<title>${book.title}</title>
  	<script src="resources/js/dojo-release-1.8.1/dojo/dojo.js" djConfig="modulePaths: {dojoui: '../../dojoui', branchitup: '../../branchitup'}, parseOnLoad:true, isDebug:false" ></script>
    <script src="resources/js/branchitup/main.js"></script>
    <script type="text/javascript">
    	var userAccountId = null;
	    <c:if test="${exception != null}">
	    	var exception = {message: "${exception.message}"};
	    </c:if>
	    <c:if test="${exception == null}">
	    	var exception = null;
	    </c:if>
	    <c:if test="${userAccountId != null}">
	    	userAccountId = ${userAccountId};
	    </c:if>
    </script>
		<script src="resources/js/branchitup/google-analytics.js"></script>    
  </head>
  <body class="tundra publishedbook">
  <script type="text/javascript">
  	var params = ${params};
  	var publication = ${func:serialize(book)};
  	<c:if test="${not empty userRating}">var userRating = ${userRating};</c:if>
  	<c:if test="${empty userRating}">var userRating = null;</c:if>
  </script>
  <st:header id="branchitup_header" page="publishedbook"/>
  <div id="publishedbook.topPane"></div>
  <div id="publishedbook.centerPane">
  	<c:if test="${book.coverImageFileId != null}" >
  		<img name="coverImage" src="${book.coverImageUrl}" />
  	</c:if>
  	<c:if test="${book.coverImageFileId == null}" >
  		<img name="coverImage" src="resources/images/no-image-branchitup_200x200.png" />
  	</c:if>
  	<h3 class='title'><c:out value="${book.title}" />
  	<c:if test="${book.parentId != null }">
  		<img src='resources/images/branch_14x15.png'/>
  	</c:if></h3>
  	<span class='blueText'>Published by:&nbsp;<a href='userprofile?userAccountId=${book.publisherAccountId}'>${book.publisherFirstName} ${book.publisherLastName}</a></span>
  	<label style="color: #aaaaaa; " id="publishedbook.publisherRolesLabel"></label>
  	<c:if test="${book.parentId != null }">
  		<br />
  		<span class='blueText'>Originated From:&nbsp; <a href="/publishedbook?bookId=${book.parentId}" >${book.parentTitle}</a></span>
  	</c:if>
  	<br/>
  	<span class='blueText'>Genre/s:&nbsp;
	  	<c:forEach items="${book.genreTitles}" var="g" varStatus="loop">
	  		${g.name}
	  		${!loop.last ? ',' : ''}
	    </c:forEach>
  	</span>
  	<br/>
  	<span class='blueText'>Version:&nbsp;<c:out value="${book.version}" /></span>
  	<br/>
  	<span class='blueText'>Published On:&nbsp;<c:out value="${func:formatDate(book.createdOn)}" /></span>
  	<br/>
  	<span class='blueText'>BranchItUp ID:&nbsp;<c:out value="${book.bookId}" /></span>
  	<p name='bookSummary'><c:out value="${book.bookSummary}" /></p>
  	<p>
  	<br/><br/>
  	<span name='ratings' id="publishedbook.ratingAverage"></span>
  	<span name="ratingCount" id="publishedbook.ratingCount"></span>
  	<ul>
  		<li class="greySmall">Downloads:&nbsp;<c:out value="${book.downloadsCount}" /></li>
  		<li class="greySmall">Book requires:&nbsp;<label name='deficiencyList' id="publishedbook.deficiencyList" ></label></li>
  		<li class="greySmall">Download Book:&nbsp;<img style="cursor: pointer;" name="pdfAttachment" title="Download PDF Format" src="resources/images/pdf20x20.png" onclick="javascript:branchitup.jsonServlet.downloadAttachement(${book.pdfAttachmentId})"/>
  		<c:if test="${book.epubAttachmentId != null}">
  			&nbsp;<img style="cursor: pointer;" name="epubAttachment" title="Download EPUB Format" src="resources/images/epub_logo_20x20.png" onclick="javascript:branchitup.jsonServlet.downloadAttachement(${book.epubAttachmentId})"/>
  		</c:if>
  		</li>
  	</ul>
  	
  	<c:if test="${not empty publisherComment}">
  		<p class="greySmall" >Publisher Comments:&nbsp;${publisherComment}</p>
  	</c:if>
		<c:if test="${book.branchable}">
		<img name="branchBookImg" src="resources/images/buttons/branch-this-book-button_170x25.png" onmouseover="this.src = 'resources/images/buttons/branch-this-book-button-hover_170x25.png'" onmouseout="this.src = 'resources/images/buttons/branch-this-book-button_170x25.png'" onclick="showBranchConfirm();"/>
		</c:if>  	
  	
  </div>
  
  <div id="publishedbook.tabPane" class="tundra dijitToolbar">
  	<span id="publishedbook.commentsTab" class="tabToolbarAction" onclick="setTab('COMMENTS');">Comments</span>
  	<span class="tabToolbarDividor">|</span>
  	<span id="publishedbook.contributorsTab" class="tabToolbarAction" onclick="setTab('CONTRIBUTORS');">Contributors</span>
  	<span class="tabToolbarDividor">|</span>
  	<span id="publishedbook.pathOfBranchTab" class="tabToolbarAction" onclick="setTab('PATH_OF_BRANCH');">Path Of Branch</span>
  	<span class="tabToolbarDividor">|</span>
  	<span style="visibility: hidden;" id="publishedbook.attachmentsTab" class="tabToolbarAction" onclick="setTab('ATTACHMENTS');">Audio</span>
  </div>
  <div id="publishedbook.bottomPane"></div>
  
  <input style="visibility: hidden;" id='audioUploader' jsId='audioUploader' type='file' multiple='false' dojoType='dojox.form.Uploader' 
  			label='Select File' url='service/UploadAudio' 
  			uploadOnSelect='true' />
 			
  <script src="resources/js/branchitup/widgets/ItemsDropdown.js" ></script>
  <script src="resources/js/branchitup/published-book.js" ></script>
   
  
  <script type="text/javascript">
  	var avg = "${book.ratingAverage}";
  	if(avg == ""){
  		avg = 0.0;
  	}
  	else{
  		avg = parseFloat(avg);
  	}
  	setAverageRatingStars(dojo.byId("publishedbook.ratingAverage"),avg, false);
  	populateDeficiencyList(${func:serialize(book.deficiencyList)});
  	
  	if(${book.ratingCount != null && book.ratingCount > 0}){
  		dojo.byId("publishedbook.ratingCount").innerHTML = "&nbsp;Rated: ${book.ratingCount}";
  	}
  </script>
  </body>
</html>
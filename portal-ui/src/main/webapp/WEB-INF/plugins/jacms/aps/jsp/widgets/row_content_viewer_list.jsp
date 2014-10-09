<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jacms:rowContentList listName="contentInfoList" titleVar="titleVar"
	pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" />

<c:if test="${null != titleVar}">
	<h2 class="page-title"><span><c:out value="${titleVar}" /></span></h2>
</c:if>
<c:choose>
	<c:when test="${!(empty contentInfoList)}">
			<wp:pager listName="contentInfoList" objectName="groupContent" pagerIdFromFrame="true" advanced="true" offset="5">
				<c:set var="group" value="${groupContent}" scope="request" />
				<c:forEach var="contentInfoVar" items="${contentInfoList}" begin="${groupContent.begin}" end="${groupContent.end}">
					<c:choose>
						<c:when
							test="${contentInfoVar['modelId'] != null}">
								<jacms:content contentId="${contentInfoVar['contentId']}" modelId="${contentInfoVar['modelId']}" />
						</c:when>
						<c:otherwise>
							<jacms:content contentId="${contentInfoVar['contentId']}" />
						</c:otherwise>
					</c:choose>
				</c:forEach>
					<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
			</wp:pager>
	</c:when>
	<c:otherwise>
		<p class="alert alert-info"><wp:i18n key="LIST_VIEWER_EMPTY" /></p>
	</c:otherwise>
</c:choose>

<c:if test="${null != pageLinkVar && null != pageLinkDescriptionVar}">
	<p class="text-right"><a class="btn btn-primary" href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
</c:if>

<%-- Important: reset variables --%>
<c:set var="contentInfoList" value="${null}"  scope="request" />
<c:set var="group" value="${null}"  scope="request" />

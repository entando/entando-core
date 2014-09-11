<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1><wp:i18n key="SEARCH_RESULTS" /></h1>
<jacms:searcher listName="result" />
<p><wp:i18n key="SEARCHED_FOR" />: <em><strong><c:out value="${search}" /></strong></em></p>
<c:choose>

	<c:when test="${empty result}">
	<p class="alert alert-info"><wp:i18n key="SEARCH_NOTHING_FOUND" /></p>
	</c:when>
	<c:otherwise>

<wp:pager listName="result" objectName="groupContent" max="10" pagerIdFromFrame="true" advanced="true" offset="5">
	<c:set var="group" value="${groupContent}" scope="request" />

	<p><em><wp:i18n key="SEARCH_RESULTS_INTRO" /> <!-- infamous whitespace hack -->
	<c:out value="${groupContent.size}" /> <!-- infamous whitespace hack -->
	<wp:i18n key="SEARCH_RESULTS_OUTRO" /> [<c:out value="${groupContent.begin + 1}" /> &ndash; <c:out value="${groupContent.end + 1}" />]:</em></p>

	<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
	<c:forEach var="contentId" items="${result}" begin="${groupContent.begin}" end="${groupContent.end}">
		<jacms:content contentId="${contentId}" modelId="list" />
	</c:forEach>
	<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />

</wp:pager>

</c:otherwise>
</c:choose>
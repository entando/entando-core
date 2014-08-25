<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="currentCode"><wp:currentPage param="code" /></c:set>

<c:if test="${currentCode == 'notfound'}">
<div class="alert alert-error alert-block">
	<h1 class="alert-heading"><wp:i18n key="PAGE_NOT_FOUND" escapeXml="false" /></h1>
</div>
</c:if>
<c:if test="${currentCode == 'errorpage'}">
<div class="alert alert-error alert-block">
	<h1 class="alert-heading"><wp:i18n key="GENERIC_ERROR" escapeXml="false" /></h1>
</div>
</c:if>

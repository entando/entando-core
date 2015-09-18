<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${null != userFilterOptionsVar && !empty userFilterOptionsVar}">

<div class="row-fluid"><div class="span12 padding-medium-top">

<c:set var="hasUserFilterError" value="${false}" />
<c:forEach var="userFilterOptionVar" items="${userFilterOptionsVar}">
<c:if test="${null != userFilterOptionVar.formFieldErrors && !empty userFilterOptionVar.formFieldErrors}"><c:set var="hasUserFilterError" value="${true}" /></c:if>
</c:forEach>

<c:if test="${hasUserFilterError}">
<div class="alert alert-error">
	<a class="close" data-dismiss="alert" href="#"><i class="icon-remove"></i></a>
	<h2 class="alert-heading"><wp:i18n key="ERRORS" /></h2>
	<ul>
		<c:forEach var="userFilterOptionVar" items="${userFilterOptionsVar}">
			<c:if test="${null != userFilterOptionVar.formFieldErrors}">
			<c:forEach var="formFieldError" items="${userFilterOptionVar.formFieldErrors}">
			<li>
			<wp:i18n key="jacms_LIST_VIEWER_FIELD" />&#32;<em><c:out value="${formFieldError.value.attributeName}" /></em><c:if test="${formFieldError.value.rangeFieldType != null}">:&#32;<em><wp:i18n key="${formFieldError.value.rangeFieldType}" /></em></c:if>&#32;<wp:i18n key="${formFieldError.value.errorKey}" />
			</li>
			</c:forEach>
			</c:if>
		</c:forEach>
	</ul>
</div>
</c:if>
<c:set var="hasUserFilterError" value="${false}" />

<p><button type="button" class="btn btn-info" data-toggle="collapse" data-target="#content-viewer-list-filters"><wp:i18n key="SEARCH_FILTERS_BUTTON" /> <i class="icon-zoom-in icon-white"></i></button></p>

<%-- search form with user filters --%>
<form action="<wp:url />" method="post" class="form-horizontal collapse" id="content-viewer-list-filters">
	<c:forEach var="userFilterOptionVar" items="${userFilterOptionsVar}">
		<c:set var="userFilterOptionVar" value="${userFilterOptionVar}" scope="request" />
		<c:choose>
			<c:when test="${!userFilterOptionVar.attributeFilter && (userFilterOptionVar.key == 'fulltext' || userFilterOptionVar.key == 'category')}">
				<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-${userFilterOptionVar.key}.jsp" />
			</c:when>
			<c:when test="${userFilterOptionVar.attributeFilter}">
				<c:choose>
					<c:when test="${userFilterOptionVar.attribute.type == 'Monotext' || userFilterOptionVar.attribute.type == 'Text' || userFilterOptionVar.attribute.type == 'Longtext' || userFilterOptionVar.attribute.type == 'Hypertext'}">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-Text.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'Enumerator' }">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-Enumerator.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'EnumeratorMap' }">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-EnumeratorMap.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'Number'}">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-Number.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'Date'}">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-Date.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'Boolean' }">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-Boolean.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'CheckBox'}">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-CheckBox.jsp" />
					</c:when>
					<c:when test="${userFilterOptionVar.attribute.type == 'ThreeState'}">
						<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module-entity-ThreeState.jsp" />
					</c:when>
				</c:choose>
			</c:when>
		</c:choose>
	</c:forEach>
	<p class="form-actions">
		<input type="submit" value="<wp:i18n key="SEARCH" />" class="btn btn-primary" />
	</p>
</form>

</div></div>

</c:if>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core"%>

<jacms:contentInfo param="authToEdit" var="canEditThis" />
<jacms:contentInfo param="contentId" var="myContentId" />

<c:if test="${canEditThis}">
	<div class="bar-content-edit">
		<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/edit.action?contentId=<jacms:contentInfo param="contentId" />" class="btn btn-info">
			<wp:i18n key="EDIT_THIS_CONTENT" /> <i class="icon-edit icon-white"></i></a>
	</div>
</c:if>

<jacms:content publishExtraTitle="true" />

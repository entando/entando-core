<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmspui" uri="/jacms-apsadmin-portal-ui" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<form id="forwardEntryContent" action="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/entryContent.action" method="get">
<input type="hidden" name="contentOnSessionMarker" value="<c:out value="${param.contentOnSessionMarker}" />" />
<p style="border-bottom: 0.1em solid #000000; background-color: #eeeeee;">
	<span id="entandoContentPreview" style="font-size: 1em; font-weight: bold; color: #000000"><wp:i18n key="jacms_CONTENT_PREVIEW" /></span>
	<input type="submit" value="<wp:i18n key="jacms_BACK_TO_EDIT_CONTENT" />" />
</p>
</form>
<jacmspui:contentPreview />

<form id="forwardEntryContent" action="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/entryContent.action" method="get">
<input type="hidden" name="contentOnSessionMarker" value="<c:out value="${param.contentOnSessionMarker}" />" />
<p style="border-top: 0.1em solid #000000; background-color: #eeeeee;">
	<span style="font-size: 1em; font-weight: bold; color: #000000"><wp:i18n key="jacms_CONTENT_PREVIEW" /></span>
	<input type="submit" value="<wp:i18n key="jacms_BACK_TO_EDIT_CONTENT" />" />
</p>
</form>
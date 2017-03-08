<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="currentSize" value="screenSize" />
<html>
	<iframe src="<wp:info key="systemParam" paramName="applicationBaseURL" />/preview/<s:property value="lang" />/<s:property value="pageCode" />" height="100%" width="<s:property value="#currentSize.width" />px">
	</iframe> 
</html>



<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="references['jacmsContentManagerUtilizers']">
<h3><s:text name="message.title.referencedContents" /></h3>
<s:set var="referencingContentsId" value="references['jacmsContentManagerUtilizers']" />
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
</s:if>
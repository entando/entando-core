<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<h1><s:text name="jacms.menu.contentAdmin" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<s:form>

<div id="main" role="main">
<h2><s:text name="title.contentEditing" /></h2>

<s:set var="myNameIsJack" value="true" />
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />

<div class="message message_error">
	<h3><s:text name="message.title.ActionErrors" /></h3>
	<p><s:text name="message.note.resolveReferences" />:</p>
</div>

<s:if test="references['jacmsContentManagerUtilizers']">
	<s:set var="referencingContentsId" value="references['jacmsContentManagerUtilizers']" />
	<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
</s:if>

<s:if test="references['PageManagerUtilizers']">
<div class="subsection-light">
<h3><s:text name="title.referencingPages" /></h3>
	<wpsa:subset source="references['PageManagerUtilizers']" count="10" objectName="pageReferences" advanced="true" offset="5" pagerId="pageManagerReferences">
	<s:set name="group" value="#pageReferences" />
	
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	
	<table class="generic" id="pageListTable">
	<caption><span><s:text name="name.pages" /></span></caption>
		<tr>
			<th><s:text name="title.pageActions" /></th>
			<th><s:text name="label.page" /></th>
			<th><s:text name="label.group" /></th>
		</tr>
		<s:iterator var="currentPageVar" >
			<s:set var="canEditCurrentPage" value="%{false}" />
			<c:set var="currentPageGroup"><s:property value="#currentPageVar.group" escape="false"/></c:set>
			<wp:ifauthorized groupName="${currentPageGroup}" permission="managePages"><s:set var="canEditCurrentPage" value="%{true}" /></wp:ifauthorized>
			<tr>
				<td class="icon_double">
					<s:if test="#canEditCurrentPage">
						<a href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#currentPageVar.code" /></s:url>"><img src="<wp:resourceURL />administration/common/img/icons/node-leaf.png" alt="<s:text name="note.goToSomewhere" />: <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />" title="<s:text name="note.goToSomewhere" />: <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />" /></a>
						<a href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#currentPageVar.code" /></s:url>"><img src="<wp:resourceURL />administration/common/img/icons/page-configure.png" alt="<s:text name="title.configPage" />: <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />" title="<s:text name="title.configPage" />: <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />" /></a>
					</s:if>
					<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
				</td>
				<td>
					<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
				</td>
				<td>
					<s:property value="#currentPageVar.group" />
				</td>
			</tr>
		</s:iterator>
	</table>
	
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	
	</wpsa:subset>
</div>
</s:if>

<wpsa:hookPoint key="jacms.contentReferences" objectName="hookPointElements_jacms_contentReferences">
<s:iterator value="#hookPointElements_jacms_contentReferences" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</div>

</s:form>
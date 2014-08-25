<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" namespace="/do/jacms/Content"/>">
			<s:text name="jacms.menu.contentAdmin" />
		</a>
		&#32;/&#32;
		<s:if test="#thirdTitleVar==null">
			<s:text name="label.edit" />
		</s:if>
		<s:else>
			<a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
			<s:text name="label.edit" />
			</a>
			&#32;/&#32;
			<s:property value="#thirdTitleVar" escapeXml="false" escapeHtml="false" />
		</s:else>
	</span>
</h1>
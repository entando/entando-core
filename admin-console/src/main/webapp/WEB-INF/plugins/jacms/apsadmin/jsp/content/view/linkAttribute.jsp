<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#attribute.symbolicLink != null">
	<s:if test="#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4">
		<wpsa:page key="%{#attribute.symbolicLink.pageDest}" var="linkedPage" />
	</s:if>
	<s:if test="#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4">
		<jacmswpsa:content contentId="%{#attribute.symbolicLink.contentDest}" record="true" var="linkedContent" />
	</s:if>
	<s:set var="validLink" value="%{true}" />
	<s:if test="(#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4) && #linkedPage == null">
	<s:text name="LinkAttribute.fieldError.linkToPage.voidPage" />
	<s:set var="validLink" value="%{false}" />
	</s:if>
	<s:if test="(#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4) && (#linkedContent == null || !#linkedContent.onLine)">
	<s:text name="LinkAttribute.fieldError.linkToContent" />
	<s:set var="validLink" value="%{false}" />
	</s:if>
	<s:if test="#validLink"><%-- valid link --%>
		<s:if test="#attribute.symbolicLink.destType == 1">
			<s:set var="statusIconVar">btn btn-default icon fa fa-globe</s:set>
			<s:set var="linkDestination" value="%{getText('note.URLLinkTo') + ': ' + #attribute.symbolicLink.urlDest}" />
		</s:if>
		<s:elseif test="#attribute.symbolicLink.destType == 2">
			<s:set var="statusIconVar">btn btn-default icon fa fa-folder</s:set>
			<s:set var="linkDestination" value="%{getText('note.pageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
		</s:elseif>
		<s:elseif test="#attribute.symbolicLink.destType == 3">
			<s:set var="statusIconVar">btn btn-default icon fa fa-file-text-o</s:set>
			<s:set var="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr}" />
		</s:elseif>
		<s:elseif test="#attribute.symbolicLink.destType == 4">
			<s:set var="statusIconVar">btn btn-default icon fa fa-file-text</s:set>
			<s:set var="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr + ', ' + getText('note.contentOnPageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
		</s:elseif>
		<%-- link icon --%>
			<a href="<s:property value="#attribute.symbolicLink.contentDest" />" class="<s:property value="#statusIconVar" />" title="<s:property value="#linkDestination" />"><span class="sr-only"><s:property value="#linkDestination" /></span></a>
		<%-- text of the link --%>
			<s:if test="%{#attribute.getTextForLang(#lang.code)==null}">
				<span class="text-muted">&#32;<s:property value="#attribute.getText()" /></span>
			</s:if>
			<s:else>
				&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
			</s:else>
	</s:if><%-- valid link --%>
</s:if>
<s:else><s:text name="label.none" /></s:else>
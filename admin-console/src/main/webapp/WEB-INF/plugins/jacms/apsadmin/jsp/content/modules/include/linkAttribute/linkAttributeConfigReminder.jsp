<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="symbolicLink != null">
<div class="alert alert-info mt-20 no-mb">
    <span class="pficon pficon-info"></span>
	<p>
		<s:text name="note.previousLinkSettings" />&#32;
		<s:if test="symbolicLink.destType == 1">
			<s:text name="note.URLLinkTo"></s:text>: <s:property value="symbolicLink.urlDest"/>.
		</s:if>
		<s:if test="symbolicLink.destType == 2 || symbolicLink.destType == 4">
			<s:set var="prevTargetPageVar" value="%{getPage(symbolicLink.pageDest)}" />
			<s:text name="note.pageLinkTo"></s:text>: <s:property value="getTitle(#prevTargetPageVar.code, #prevTargetPageVar.titles)" />
		</s:if>
		<s:if test="symbolicLink.destType == 3 || symbolicLink.destType == 4">
			<s:set var="prevTargetContentVoVar" value="%{getContentVo(symbolicLink.contentDest)}" />
			<s:text name="note.contentLinkTo"></s:text>: <s:property value="symbolicLink.contentDest"/> &ndash; <s:property value="#prevTargetContentVoVar.descr"/>.
		</s:if>
	</p>
</div>
</s:if>

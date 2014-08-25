<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<span class="sr-only"><s:text name="note.linkContent" /></span>
<div class="input-group<s:if test="#attributeTracer.monoListElement||#attributeTracer.compositeElement"> margin-small-top</s:if>">
	<s:if test="#attribute.symbolicLink != null">
		<s:if test="#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4">
			<s:set var="linkedPage" value="%{getPage(#attribute.symbolicLink.pageDest)}" />
		</s:if>
		<s:if test="#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4">
			<s:set var="linkedContent" value="%{getContentVo(#attribute.symbolicLink.contentDest)}" />
		</s:if>
		<s:set var="validLink" value="true" />
		<s:if test="(#attribute.symbolicLink.destType == 2 || #attribute.symbolicLink.destType == 4) && #linkedPage == null">
			<s:text name="LinkAttribute.fieldError.linkToPage.voidPage" />
			<s:set var="validLink" value="false" />
		</s:if>
		<s:if test="(#attribute.symbolicLink.destType == 3 || #attribute.symbolicLink.destType == 4) && (#linkedContent == null || !#linkedContent.onLine)">
			<s:text name="LinkAttribute.fieldError.linkToContent" />
			<s:set var="validLink" value="false" />
		</s:if>
		<s:if test="#validLink">
			<%-- link with a valid value --%>
			<s:if test="#attribute.symbolicLink.destType == 1">
				<s:set var="statusIconVar">btn btn-default disabled icon fa fa-globe</s:set>
				<s:set var="linkDestination" value="%{getText('note.URLLinkTo') + ': ' + #attribute.symbolicLink.urlDest}" />
			</s:if>
			<s:if test="#attribute.symbolicLink.destType == 2">
				<s:set var="statusIconVar">btn btn-default disabled icon fa fa-folder</s:set>
				<s:set var="linkDestination" value="%{getText('note.pageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
			</s:if>
			<s:if test="#attribute.symbolicLink.destType == 3">
				<s:set var="statusIconVar">btn btn-default disabled icon fa fa-file-text-o</s:set>
				<s:set var="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr}" />
			</s:if>
			<s:if test="#attribute.symbolicLink.destType == 4">
				<s:set var="statusIconVar">btn btn-default disabled icon fa fa-file-text</s:set>
				<s:set var="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #attribute.symbolicLink.contentDest + ' - ' + #linkedContent.descr + ', ' + getText('note.contentOnPageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
			</s:if>
				<span class="input-group-btn" title="<s:property value="linkDestination" />">
					<span class="sr-only"><s:property value="linkDestination" /></span>
					<span class="<s:property value="#statusIconVar" />"></span>
				</span>
				<%-- field text --%>
				<wpsf:textfield 
					id="%{#attributeTracer.getFormFieldName(#attribute)}" 
					name="%{#attributeTracer.getFormFieldName(#attribute)}" 
					value="%{#attribute.getTextForLang(#lang.code)}" 
					maxlength="254" 
					placeholder="%{getText('label.alt.text')}" 
					title="%{getText('label.alt.text')}" 
					cssClass="form-control" />
		</s:if>
	</s:if>
	<s:if test="#lang.default">
		<%-- default language --%>
		<wpsa:actionParam action="chooseLink" var="chooseLinkActionName" >
			<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
			<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
			<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
			<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
		</wpsa:actionParam>
		<s:if test="#attribute.symbolicLink != null || ((#attributeTracer.monoListElement) || (#attributeTracer.compositeElement))"><span class="input-group-btn"></s:if>
			<wpsf:submit cssClass="btn btn-default" type="button" action="%{#chooseLinkActionName}" title="%{#attribute.name + ': ' + getText('label.configure')}">
				<span class="icon fa fa-link"></span>&#32;
				<span class="sr-only"><s:property value="#attribute.name" />:</span>
				<s:text name="label.configure" />
			</wpsf:submit>
			<s:if test="#attribute.symbolicLink != null && ((!#attributeTracer.monoListElement) || (#attributeTracer.monoListElement && #attributeTracer.compositeElement))">
				<%-- valorized --%>
				<wpsa:actionParam action="removeLink" var="removeLinkActionName" >
					<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
					<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
					<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
					<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
				</wpsa:actionParam>
					<wpsf:submit cssClass="btn btn-warning" type="button"  action="%{#removeLinkActionName}" title="%{getText('label.remove')}">
						<span class="icon fa fa-times"></span>
						<span class="sr-only"><s:text name="label.remove" /></span>
					</wpsf:submit>
			</s:if>
		<s:if test="#attribute.symbolicLink != null || ((#attributeTracer.monoListElement) || (#attributeTracer.compositeElement))"></span></s:if>
	</s:if>
</div>
<s:if test="!#lang.default">
	<span class="display-block form-control-static text-info"><s:text name="note.editContent.doThisInTheDefaultLanguage.must" />.</span>
</s:if>
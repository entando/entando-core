<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentResource" value="#attribute.resources[#lang.code]" />
<s:set var="defaultResource" value="#attribute.resource" />
<s:if test="#lang.default"><%-- default language --%>
	<s:if test="#currentResource != null">
		<a title="<s:text name="label.download" />: <s:property value="#defaultResource.instance.fileName" />" href="<s:property value="#defaultResource.attachPath" />" class="btn btn-default"><span class="icon fa fa-download"></span><span class="sr-only"><s:text name="label.download" /></span></a>
		&#32;
		<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" /></a>&#32;
			<code><s:property value="%{#defaultResource.instance.fileLength}"/></code>
	</s:if>
	<s:else>
		<s:text name="label.none" />
	</s:else>
</s:if><%-- default language --%>
<s:else><%-- other languages --%>
	<s:if test="#defaultResource == null"><%-- resource null --%>
		<s:text name="label.attribute.resources.null" />
	</s:if>
	<s:else>
		<s:if test="#currentResource != null">
			<a title="<s:text name="label.download" />: <s:property value="#currentResource.instance.fileName" />" href="<s:property value="#currentResource.attachPath" />" class="btn btn-default">
			<span class="icon fa fa-download"></span><span class="sr-only"><s:text name="label.download" /></span></a>
			&#32;
			<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
			&#32;<code><s:property value="%{#currentResource.instance.fileLength}"/></code>
		</s:if>
		<s:else>
			<a title="<s:text name="label.download" />: <s:property value="#defaultResource.instance.fileName" />" href="<s:property value="#defaultResource.attachPath" />" class="btn btn-default disabled">
			<span class="icon fa fa-download text-muted"></span><span class="sr-only"><s:text name="label.download" /></span></a>
			&#32;
			<s:if test="#attribute.getTextForLang(#lang.code)==null"><span class="text-muted"><s:text name="label.attribute.resources.attach.null" /></span></s:if>
			<s:else><s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" /></s:else>
			&#32;<code class="text-muted"><s:property value="%{#defaultResource.instance.fileLength}"/></code>
		</s:else>
	</s:else>
</s:else>
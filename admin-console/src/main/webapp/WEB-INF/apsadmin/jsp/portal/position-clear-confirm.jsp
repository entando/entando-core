<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set name="NScurrent" value="#context['struts.actionMapping'].getNamespace()" />
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
	<s:if test="%{#NScurrent == '/do/Portal/WidgetType'}">
		<a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />"><s:text name="title.widgetManagement" /></a>
	</s:if>
	<s:elseif test="%{#NScurrent == '/do/Page'}">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>
	</s:elseif>
	&#32;/&#32;
	<s:text name="title.widgetManagement.position.clear" />
	</span>
</h1>

<div id="main" role="main">

<s:form action="deleteWidgetFromPage">
	<p class="sr-only">
		<wpsf:hidden name="pageCode"/>
		<wpsf:hidden name="frame" />
		<wpsf:hidden name="widgetTypeCode" />
	</p>

	<div class="alert alert-warning">
		<s:text name="note.clearPosition.areYouSure.position" />
		&#32;<code><s:property value="frame" />&#32;&ndash;&#32;<s:property value="%{getPage(pageCode).model.getFrames()[frame]}"/></code>
		<s:text name="note.clearPosition.areYouSure.page" />&#32;<code><s:property value="%{getPage(pageCode).getTitle(currentLang.getCode())}" /></code>
		<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
		<s:if test="null != #showletType">
			<s:text name="note.clearPosition.areYouSure.widget" />&#32;<code><s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" /></code>
		</s:if>?

		<div class="text-center margin-large-top">
			<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
				<span class="icon fa fa-times-circle"></span>&#32;
				<s:text name="label.clear" />
			</wpsf:submit>
			<s:if test="%{#NScurrent == '/do/Portal/WidgetType'}">
			<a class="btn btn-link" href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType"/>" ><s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" /></a>
			</s:if>
			<s:elseif test="%{#NScurrent == '/do/Page'}">
			<a class="btn btn-link" href="<s:url action="viewTree" namespace="/do/Page"/>" ><s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" /></a>
			</s:elseif>
		</div>
	</div>
</s:form>

</div>
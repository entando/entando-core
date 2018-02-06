<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="NScurrent" value="#context['struts.actionMapping'].getNamespace()" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li>
        <s:if test="%{#NScurrent == '/do/Portal/WidgetType'}">
            <a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />"><s:text name="title.widgetManagement" /></a>
        </s:if>
        <s:elseif test="%{#NScurrent == '/do/Page'}">
            <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>
        </s:elseif>
    </li>
    <li><s:text name="title.widgetManagement.position.clear" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.widgetManagement.position.clear" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">

    <s:form action="deleteWidgetFromPage">
        <p class="sr-only">
            <wpsf:hidden name="pageCode"/>
            <wpsf:hidden name="frame" />
            <wpsf:hidden name="widgetTypeCode" />
        </p>

        <div class="text-center">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline">
                <s:text name="note.clearPosition.areYouSure.position" />
            </p>
            <p class="esclamation-underline-text">
                <s:property value="frame" />&#32;&ndash;&#32;<s:property value="%{getPage(pageCode).model.getFrames()[frame]}"/>
                <s:text name="note.clearPosition.areYouSure.page" />&#32;<strong><s:property value="%{getPage(pageCode).getTitle(currentLang.getCode())}" /></strong>

                <s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
                <s:if test="null != #showletType">
                    <s:text name="note.clearPosition.areYouSure.widget" />&#32;<strong><s:property value="%{getTitle(#showletType.getCode(), #showletType.getTitles())}" /></strong>
                </s:if>?
            </p>
            <div class="text-center margin-large-top">
                <s:if test="%{#NScurrent == '/do/Portal/WidgetType'}">
                    <a class="btn btn-default button-fixed-width" href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType"/>" ><s:text name="label.back" /></a>
                </s:if>
                <s:elseif test="%{#NScurrent == '/do/Page'}">
                    <a class="btn btn-default button-fixed-width" href="<s:url action="viewTree" namespace="/do/Page"/>" ><s:text name="title.pageManagement" /></a>
                </s:elseif>
                <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                    <s:text name="label.delete" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>

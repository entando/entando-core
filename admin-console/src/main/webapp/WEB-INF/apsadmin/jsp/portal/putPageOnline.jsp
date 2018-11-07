<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" />"><s:text name="title.pageTree" /></a></li>
    <li class="page-title-container"><s:text name="title.pageOnline" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.pageOnline" /></h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">

    <s:form action="doSetOnline">
        <p class="sr-only">
            <wpsf:hidden name="selectedNode"/>
            <wpsf:hidden name="pageCode" />
        </p>
        <div class="text-center">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline"><s:text name="note.page.setOnline.areYouSure" /></p>

            <p class="esclamation-underline-text"><s:property value="%{getPage(pageCode).getTitle(currentLang.getCode())}" />&nbsp;?</p>

            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="viewTree" namespace="/do/Page"/>" ><s:text name="label.back" /></a>
                <wpsf:submit type="button" cssClass="btn btn-primary button-fixed-width">
                    <s:text name="label.setOnline" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>


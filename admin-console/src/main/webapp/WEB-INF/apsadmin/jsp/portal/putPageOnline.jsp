<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" />"><s:text name="title.pageTree" /></a></li>
    <li class="page-title-container"><s:text name="title.pageOnline" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.pageOnline" /></h1>

<div id="main" role="main">

    <s:form action="doSetOnline">
        <p class="sr-only">
            <wpsf:hidden name="selectedNode"/>
            <wpsf:hidden name="pageCode" />
        </p>
        <div class="alert alert-warning">
            <p>
                <s:text name="note.page.setOnline.areYouSure" />&#32;
                <code><s:property value="%{getPage(pageCode).getDraftTitle(currentLang.getCode())}" /></code>?
            </p>
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;
                    <s:text name="label.setOnline" />
                </wpsf:submit>
                <a class="btn btn-link" href="<s:url action="viewTree" namespace="/do/Page"/>" ><s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" /></a>
            </div>
        </div>
    </s:form>

</div>
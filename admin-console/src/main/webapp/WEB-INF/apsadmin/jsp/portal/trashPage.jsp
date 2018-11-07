<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" />"><s:text name="title.pageTree" /></a></li>
    <li class="page-title-container"><s:text name="title.pageManagement.pageTrash" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.pageManagement.pageTrash" /></h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">

    <s:form action="delete">
        <p class="sr-only">
            <wpsf:hidden name="selectedNode"/>
            <wpsf:hidden name="nodeToBeDelete" />
        </p>
        <div class="text-center">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline"><s:text name="note.deletePage.areYouSure" />&#32;</p>

            <p class="esclamation-underline-text"><s:property value="%{getPage(nodeToBeDelete).getTitle(currentLang.getCode())}" />&nbsp;?</p>

            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="viewTree" namespace="/do/Page"/>" ><s:text name="label.back" /></a>
                <wpsf:submit type="button" action="delete" cssClass="btn btn-danger button-fixed-width">
                    <s:text name="label.remove" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>


<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a href="<s:url action="list" namespace="/do/FileBrowser" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="menu.filebrowserAdmin" />"><s:text
                name="menu.filebrowserAdmin" /></a></li>
    <li class="page-title-container"><s:text name="label.delete" /></li>
</ol>
<h1 class="page-title-container">
    <s:text name="label.delete" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<s:include
    value="/WEB-INF/apsadmin/jsp/filebrowser/include/breadcrumbs.jsp" />
<div class="text-center">
    <s:form action="delete" namespace="/do/FileBrowser" method="post"
            cssClass="margin-base-top">
        <s:hidden name="deleteFile" />
        <s:hidden name="currentPath" />
        <s:hidden name="filename" />
        <s:hidden name="protectedFolder" />
        <s:hidden name="strutsAction" />

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="label.delete" />
        </p>
        <p class="esclamation-underline-text">
            <s:text name="label.delete.confirm" />
            &#32;<s:property value="%{filename}" />?
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width"
               href="<s:url namespace="/do/FileBrowser" action="list" />">
                <s:text name="note.goToSomewhere" />&#32;<s:text
                    name="label.back" />
            </a>
            <wpsf:submit type="button"
                         cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </wpsf:submit>
        </div>
    </s:form>
</div>

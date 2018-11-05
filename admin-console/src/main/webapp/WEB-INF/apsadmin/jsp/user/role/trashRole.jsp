<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.userSettings" /></li>
    <li><a href="<s:url namespace="/do/Role" action="list" />">
            <s:text name="title.roleManagement" />
        </a></li>

    <li class="page-title-container"><s:text name="title.roleManagement.roleTrash" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.roleManagement.roleTrash" />
</h1>


<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div class="text-center">
    <s:form action="delete">
        <p class="sr-only">
            <wpsf:hidden name="name"/>
        </p>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>

        <p class="esclamation-underline"><s:text name="note.role.delete" /></p>
        <p class="esclamation-underline-text"><s:text name="note.roleConfirm.trash" />&#32;<s:property value="name" /></p>

        <div class="text-center">
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/Role" />">
                <s:text name="label.back" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </wpsf:submit>
        </div>

    </s:form>
</div>

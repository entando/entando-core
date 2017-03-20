<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a></li>

    <li><a href="<s:url namespace="/do/Role" action="list" />">
            <s:text name="title.roleManagement" />
        </a></li>

    <li class="page-title-container"><s:text name="title.roleManagement.roleTrash" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.roleManagement.roleTrash" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="text to be inserted" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
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
        <p class="esclamation-underline-text"><s:text name="note.roleConfirm.trash" /> <s:property value="name" /></p>

        <div class="text-center">

            <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/Role" />">
                <s:text name="title.roleManagement" />
            </a>

            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                
                <s:text name="label.delete" />
            </wpsf:submit>
        </div>

    </s:form>
</div>
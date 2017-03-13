<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.users" /></a></li>
    <li><a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a></li>
    <li><s:text name="title.userManagement.userTrash" /></li>
</ol>


<h1>
    <s:text name="title.userManagement.userTrash" />&nbsp;<s:text name="label.for.user" /> - <s:property value="%{username}" />
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



</h1>
<div class="text-center">
    <s:form action="delete" namespace="/do/User">
        <p class="sr-only">
            <wpsf:hidden name="username"/>
        </p>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>

        <p class="esclamation-underline"><s:text name="note.userConfirm.delete" /></p>
        <p class="esclamation-underline-text"><s:text name="note.userConfirm.trash" />&#32;<s:property value="%{username}" />?</p>
        <br/>

        <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/User" />">
            <s:text name="menu.back" />
        </a>
        <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
            <s:text name="label.delete" />
        </wpsf:submit>



    </s:form>
</div>
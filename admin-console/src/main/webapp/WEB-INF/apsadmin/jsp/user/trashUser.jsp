<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting"/></li>
    <li><a href="<s:url namespace="/do/User" action="list" />"><s:text
                name="title.userManagement"/></a></li>
    <li class="page-title-container"><s:text
            name="title.userManagement.userTrash"/></li>
</ol>


<h1 class="page-title-container">
    <s:text name="title.userManagement.userTrash"/>&#32;
    <s:property value="%{username}"/>
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

        <p class="esclamation-underline">
            <s:text name="note.userConfirm.delete"/>
        </p>
        <p class="esclamation-underline-text">
            <s:text name="note.userConfirm.trash"/>
            &#32;
            <s:property value="%{username}"/>
            ?
        </p>
        <br/>

        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/User" />">
                <s:text name="menu.back"/>
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </wpsf:submit>
        </div>
    </s:form>
</div>

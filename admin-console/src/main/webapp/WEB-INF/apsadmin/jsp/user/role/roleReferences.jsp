<%@ taglib prefix="s" uri="/struts-tags" %>
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
<div id="main" role="main">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"
                aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /></strong>
        <ul>
            <li><s:text name="message.note.resolveReferences" />:</li>
        </ul>
    </div>
    <s:include value="/WEB-INF/apsadmin/jsp/user/role/include/roleInfo-references.jsp" />

</div>

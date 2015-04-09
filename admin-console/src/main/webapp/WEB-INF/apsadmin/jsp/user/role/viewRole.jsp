<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" namespace="/do/Role"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.roleManagement" />">
            <s:text name="title.roleManagement" /></a>
        &#32;/&#32;
        <s:text name="title.roleDetail" />
    </span>
</h1>

<div class="form-horizontal">
    <div class="form-group">
        <label class="control-label col-lg-3 col-md-3"><s:text name="label.group" /></label>
        <div class="col-md-9 col-lg-9 form-control-static">
            <code><s:property value="name" /></code>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-lg-3 col-md-3"><s:text name="label.description" /></label>
        <div class="col-md-9 col-lg-9 form-control-static">
            <s:property value="description" />
        </div>
    </div>
</div>

<div class="form-group">
    <label><s:text name="name.permissions" /></label>
    <s:if test="null != rolePermissions && rolePermissions.size() > 0">
    <ul>
    <s:iterator value="rolePermissions">
        <li>
            <s:property value="description" />&nbsp;(<code><s:property value="name" /></code>)
        </li>
    </s:iterator>
    </ul>
    </s:if>
    <s:else>
        <p class="margin-none"><s:text name="note.role.rolePermissions.empty" /></p>
    </s:else>
</div>

<s:include value="/WEB-INF/apsadmin/jsp/user/role/include/roleInfo-references.jsp" />
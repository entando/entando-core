<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.userSettings"/></li>
    <li><a href="<s:url action="list" namespace="/do/Role"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.roleManagement" />">
        <s:text name="title.roleManagement"/>
    </a></li>
    <li class="page-title-container"><s:text
            name="title.roleManagement.roleDetail"/></li>
</ol>


<h1 class="page-title-container">
    <s:text name="title.roleManagement.roleDetail"/>
    <span class="pull-right"> <a tabindex="0" role="button"
                                 data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                 data-content="<s:text name="page.role.help" />" data-placement="left"
                                 data-original-title=""><i class="fa fa-question-circle-o"
                                                           aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div class="form-horizontal">
    <div class="form-group">
        <label class="control-label col-sm-2"><s:text
                name="label.group"/></label>
        <div class="col-sm-10">
            <s:property value="name"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2"><s:text
                name="label.description"/></label>
        <div class="col-sm-10">
            <s:property value="description"/>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2"><s:text
                name="name.permissions"/></label>
        <div class="col-sm-10">
            <s:if test="null != rolePermissions && rolePermissions.size() > 0">
                <ul style="margin: 5px 0 0 -40px">
                    <s:iterator value="rolePermissions">
                        <li><s:property value="description"/> (<s:property
                                value="name"/>)
                        </li>
                    </s:iterator>
                </ul>
            </s:if>
            <s:else>
                <p>
                    <s:text name="note.role.rolePermissions.empty"/>
                </p>
            </s:else>
        </div>
    </div>
</div>

<s:include
        value="/WEB-INF/apsadmin/jsp/user/role/include/roleInfo-references.jsp"/>

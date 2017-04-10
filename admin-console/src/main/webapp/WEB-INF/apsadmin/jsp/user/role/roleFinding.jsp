<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>


<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a></li>
    <li  class="page-title-container"><s:text name="title.roleManagement" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.roleManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <s:text name="messages.title.ActionErrors" />
        <ul class="margin-base-top">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
                </s:iterator>
        </ul>
    </div>
</s:if>

<!--<a class="btn btn-primary"
   href="<s:url namespace="/do/Role" action="new" />">
    <s:text name="title.roleManagement.roleNew" />
</a>-->

<br>
<a href="<s:url namespace="/do/Role" action="new" />" class="btn btn-primary pull-right" style="margin-bottom: 5px">
        <s:text name="title.roleManagement.roleNew" />
    </a>

<table class="table table-striped table-bordered table-hover">
    <tr>
        <th><s:text name="label.code" /></th>
        <th><s:text name="label.name" /></th>
        <th class="text-designer"><s:text name="label.actions" /></th>
    </tr>

    <s:iterator value="roles" var="role">
        <tr>
            <td><s:property value="#role.name" /></td>
            <td><s:property value="#role.description" /></td>
            <td class=" table-view-pf-actions">

                <div class="dropdown dropdown-kebab-pf">
                    <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-ellipsis-v"></span></button>
                    <ul class="dropdown-menu dropdown-menu-right">								
                        <li>
                            <a href="<s:url action="detail"><s:param name="name" value="#role.name"/></s:url>" 
                               title="<s:text name="note.detailsFor" />: <s:property value="#role.name" />">
                                <s:text name="note.detailsFor" />: <s:property value="#role.name" />
                                <span class="sr-only"><s:text name="note.detailsFor" />: <s:property value="#role.name" /></span>
                            </a>
                        </li>
                        <li>
                            <a title="<s:text name="label.edit" />:&#32;<s:property value="#role.name" />" 
                               href="<s:url action="edit"><s:param name="name" value="#role.name"/></s:url>">
                                <s:text name="label.edit" />:&#32;<s:property value="#role.name" />
                                <span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="#role.name" /></span>
                            </a>
                        </li>
                        <li>
                            <a  title="<s:text name="label.remove" />: <s:property value="#role.name" />" 
                                href="<s:url action="trash"><s:param name="name" value="#role.name"/></s:url>">
                                <s:text name="label.remove" />: <s:property value="#role.name" />
                                <span class="sr-only"><s:text name="label.remove" />: <s:property value="#role.name" /></span>
                            </a>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
    </s:iterator>
</table>


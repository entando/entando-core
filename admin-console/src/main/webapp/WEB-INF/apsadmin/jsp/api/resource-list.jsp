<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.integrations" /></li>
    <li><s:text name="title.apiManagement" /></li>
    <li class="page-title-container"><s:text name="name.api.resource" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.apiResourceManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.api.resources.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <s:if test="hasActionMessages()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="messages.confirm" /></strong>
            <ul>
                <s:iterator value="actionMessages">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
            <ul>
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:set var="resourceFlavoursVar" value="resourceFlavours" />
    <s:if test="#resourceFlavoursVar.size() > 0">
        <%-- icons --%>
        <%-- off --%>
        <s:set var="icon_off"><span class="btn btn-default "><span class="icon fa fa-pause text-warning" title="<s:text name="api.resource.status.off" />"><span class="sr-only"><s:text name="api.resource.status.off.alt" /></span></s:set>
                <%-- free --%>
                <s:set var="icon_free"><span class="btn btn-default "><span class="icon fa fa-check text-success" title="<s:text name="api.resource.status.free" />"><span class="sr-only"><s:text name="api.resource.status.free.alt" /></span></s:set>
                        <%-- logged --%>
                        <s:set var="icon_auth"><span class="btn btn-default "><span class="icon fa fa-user" title="<s:text name="api.resource.status.auth" />"><span class="sr-only"><s:text name="api.resource.status.auth.alt" /></span></s:set>
                                <%-- permission --%>
                                <s:set var="icon_lock"><span class="btn btn-default "><span class="icon fa fa-unlock-alt" title="<s:text name="api.resource.status.lock" />"><span class="sr-only"><s:text name="api.resource.status.lock" /></span></s:set>
                                        <%-- hidden, free --%>
                                        <s:set var="icon_free_hidden"><span class="btn btn-default "><span class="icon fa fa-check text-muted" title="<s:text name="api.resource.status.free" /> (<s:text name="label.hidden" />)"><span class="sr-only"><s:text name="api.resource.status.free" />(<s:text name="label.hidden" />)</span></span></span></s:set>
                                        <%-- hidden, logged --%>
                                        <s:set var="icon_auth_hidden"><span class="btn btn-default "><span class="icon fa fa-user text-muted" title="<s:text name="api.resource.status.auth" /> (<s:text name="label.hidden" />)"><span class="sr-only"><s:text name="api.resource.status.auth.alt" /> (<s:text name="label.hidden" />)</span></s:set>
                                                <%-- hidden, permission --%>
                                                <s:set var="icon_lock_hidden"><span class="btn btn-default "><span class="icon fa fa-unlock-alt text-muted" title="<s:text name="api.resource.status.lock" /> (<s:text name="label.hidden" />)"><span class="sr-only"><s:text name="api.resource.status.lock" /> (<s:text name="label.hidden" />)</span></s:set>
                                                        <%-- not available --%>
                                                        <s:set var="icon_na"><s:text name="api.resource.status.na" /></s:set>
                                                        <%-- icons //end --%>
                                                        <s:iterator var="resourceFlavourVar" value="#resourceFlavoursVar" status="resourceFlavourStatusVar">
                                                            <s:iterator value="#resourceFlavourVar" var="resourceVar" status="statusVar" >
                                                                <s:if test="#statusVar.first">
                                                                    <%-- if we're evaluating the first resource, setup the caption title and table headers --%>
                                                                    <s:if test="#resourceVar.source=='core'"><s:set var="captionVar" value="#resourceVar.source" /></s:if>
                                                                    <s:else><s:set var="captionVar" value="%{getText(#resourceVar.sectionCode+'.name')}" /></s:else>
                                                                    <legend title="<s:text name="label.flavour" />&#32;<s:property value="#captionVar" />"><s:property value="#captionVar" /></legend>
                                                                </s:if>
                                                            </s:iterator>
                                                            <div class="form-group">

                                                                <table class="table table-striped table-bordered table-hover">
                                                                    <s:iterator value="#resourceFlavourVar" var="resourceVar" status="statusVar" >
                                                                        <s:if test="#statusVar.first">
                                                                            <tr>
                                                                                <th class="table-w-15"><s:text name="name.api.resource" /></th>
                                                                                <th class="table-w-20"><s:text name="label.description" /></th>
                                                                                <th class="text-center table-w-5"><s:text name="api.get" /></th>
                                                                                <th class="text-center table-w-5"><s:text name="api.post" /></th>
                                                                                <th class="text-center table-w-5"><s:text name="api.put" /></th>
                                                                                <th class="text-center table-w-5"><s:text name="api.delete" /></th>
                                                                            </tr>
                                                                        </s:if>
                                                                        <tr>
                                                                            <%-- CODE and link to edit --%>
                                                                            <td class="text-nowrap">
                                                                                <s:url var="detailActionURL" action="detail" namespace="/do/Api/Resource"><s:param name="resourceName" value="#resourceVar.resourceName" /><s:param name="namespace" value="#resourceVar.namespace" /></s:url>
                                                                                <a title="<s:text name="label.edit" />: <s:property value="%{#resourceVar.namespace.length()>0?#resourceVar.namespace+'/':''}" /><s:property value="#resourceVar.resourceName" />" href="<s:url action="detail" namespace="/do/Api/Resource"><s:param name="resourceName" value="#resourceVar.resourceName" /><s:param name="namespace" value="#resourceVar.namespace" /></s:url>">
                                                                                    </span>&nbsp;<s:property value="#resourceVar.resourceName" /></a><s:if test="#resourceVar.getMethod != null"><s:if test="#resourceVar.getMethod.canSpawnOthers">&emsp;<a class="btn btn-default " href="<s:url action="newService" namespace="/do/Api/Service"><s:param name="resourceName" value="#resourceVar.resourceName" /><s:param name="namespace" value="#resourceVar.namespace" /></s:url>" title="<s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#resourceVar.resourceName" />"><span class="icon fa fa-code-fork"></span><span class="sr-only"><s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#resourceVar.resourceName" /></span></a></s:if>
                                                                                    </s:if>
                                                                            </td>
                                                                            <%-- DESCRIPTION --%>
                                                                            <td class="col-md-5"><s:property value="#resourceVar.description" /></td>
                                                                            <%-- GET --%>
                                                                            <td class="text-center">
                                                                                <s:if test="#resourceVar.getMethod != null" >
                                                                                    <a href="<s:property value="#detailActionURL" escapeHtml="false" />#GET_tab">
                                                                                        <s:if test="!#resourceVar.getMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
                                                                                        <s:elseif test="#resourceVar.getMethod.requiredPermission != null" ><s:if test="#resourceVar.getMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:elseif test="#resourceVar.getMethod.requiredAuth" ><s:if test="#resourceVar.getMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:else><s:if test="#resourceVar.getMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
                                                                                            </a>
                                                                                </s:if>
                                                                                <s:else>
                                                                                    <s:property value="#icon_na" escapeHtml="false" />
                                                                                </s:else>
                                                                            </td>
                                                                            <%-- POST --%>
                                                                            <td class="text-center">
                                                                                <s:if test="#resourceVar.postMethod != null" >
                                                                                    <a href="<s:property value="#detailActionURL" escapeHtml="false" />#POST_tab">
                                                                                        <s:if test="!#resourceVar.postMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
                                                                                        <s:elseif test="#resourceVar.postMethod.requiredPermission != null" ><s:if test="#resourceVar.postMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:elseif test="#resourceVar.postMethod.requiredAuth" ><s:if test="#resourceVar.postMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:else><s:if test="#resourceVar.postMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
                                                                                            </a>
                                                                                </s:if>
                                                                                <s:else><s:property value="#icon_na" escapeHtml="false" /></s:else>
                                                                                </td>
                                                                            <%-- PUT --%>
                                                                            <td class="text-center">
                                                                                <s:if test="#resourceVar.putMethod != null" >
                                                                                    <a href="<s:property value="#detailActionURL" escapeHtml="false" />#PUT_tab">
                                                                                        <s:if test="!#resourceVar.putMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
                                                                                        <s:elseif test="#resourceVar.putMethod.requiredPermission != null" ><s:if test="#resourceVar.putMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:elseif test="#resourceVar.putMethod.requiredAuth" ><s:if test="#resourceVar.putMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:else><s:if test="#resourceVar.putMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
                                                                                            </a>
                                                                                </s:if>
                                                                                <s:else><s:property value="#icon_na" escapeHtml="false" /></s:else>
                                                                                </td>
                                                                            <%-- DELETE --%>
                                                                            <td class="text-center">
                                                                                <s:if test="#resourceVar.deleteMethod != null" >
                                                                                    <a href="<s:property value="#detailActionURL" escapeHtml="false" />#DELETE_tab">
                                                                                        <s:if test="!#resourceVar.deleteMethod.active" ><s:property value="#icon_off" escapeHtml="false" /></s:if>
                                                                                        <s:elseif test="#resourceVar.deleteMethod.requiredPermission != null" ><s:if test="#resourceVar.deleteMethod.hidden" ><s:property value="#icon_lock_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_lock" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:elseif test="#resourceVar.deleteMethod.requiredAuth" ><s:if test="#resourceVar.deleteMethod.hidden" ><s:property value="#icon_auth_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_auth" escapeHtml="false" /></s:else></s:elseif>
                                                                                        <s:else><s:if test="#resourceVar.deleteMethod.hidden" ><s:property value="#icon_free_hidden" escapeHtml="false" /></s:if><s:else><s:property value="#icon_free" escapeHtml="false" /></s:else></s:else>
                                                                                            </a>
                                                                                </s:if>
                                                                                <s:else><s:property value="#icon_na" escapeHtml="false" /></s:else>
                                                                                </td>
                                                                            </tr>
                                                                    </s:iterator>
                                                                </table>

                                                            </div>
                                                        </s:iterator>
                                                    </s:if>
                                                    <s:else>
                                                        <div class="alert alert-info" style="margin-left: 20px; margin-right: 20px">
                                                            <span class="pficon pficon-info"></span>
                                                            <strong><s:text name="note.api.noResources" /></strong>
                                                        </div>
                                                    </s:else>
                                                    </div>

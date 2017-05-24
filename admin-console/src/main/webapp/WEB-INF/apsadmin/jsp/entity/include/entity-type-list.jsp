<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<%--se arrivo dal plugin webdynamicform visualizzo solo la parte sotto --%>
<s:if test="%{'jpwebdynamicformMessageManager' == entityManagerName}">
    <ol class="breadcrumb page-tabs-header breadcrumb-position">
        <li><s:text name="jpwebdynamicform.menu.integration"/></li>
        <li>
            <s:text name="jpwebdynamicform.menu.uxcomponents"/>
        </li>
        <li class="page-title-container">
            <s:text name="%{'title.' + entityManagerName + '.management'}"/>
        </li>
    </ol>
    <div class="page-tabs-header">
        <div class="row">
            <div class="col-sm-6">
                <h1 class="page-title-container">
                    <s:text name="jpwebdynamicform.name"/>
                    <span class="pull-right">
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                           title="" data-content="TO be inserted" data-placement="left" data-original-title="">
                            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                        </a>
                    </span>
                </h1>
            </div>
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <li>
                        <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />">
                            <s:text name="jpwebdynamicform.menu.messages"/></a>
                    </li>
                    <li>
                        <a href="<s:url namespace="/do/jpwebdynamicform/Message/Config" action="list" />">
                            <s:text name="jpwebdynamicform.menu.config"/></a>
                    </li>
                    <li class="active">
                        <a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>">
                            <s:text  name="%{'title.' + entityManagerName + '.management'}"/>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <br>
</s:if>
<s:else>
    <ol class="breadcrumb page-tabs-header breadcrumb-position">
        <li>
            <s:text name="%{'title.' + entityManagerName + '.menu'}"/>
        </li>
        <li class="page-title-container">
            <s:text name="%{'title.' + entityManagerName + '.management'}"/>
        </li>
    </ol>
    <h1 class="page-title-container">
        <s:text name="%{'title.' + entityManagerName + '.management'}"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.contentTypesManagement.help"/>" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o"  aria-hidden="true">
                </i>
            </a>
        </span>
    </h1>
    <div class="text-right">
        <div class="form-group-separator">
        </div>
    </div>
    <br>
</s:else>

<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.FieldErrors"/></strong>
        <ul>
            <s:iterator value="fieldErrors">
                <s:iterator value="value">
                    <li><s:property escapeHtml="false"/></li>
                    </s:iterator>
                </s:iterator>
        </ul>
    </div>
</s:if>

<a href="<s:url namespace="/do/Entity" action="initAddEntityType" ><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>"
   class="btn btn-primary pull-right mb-5" >
    <s:text name="menu.entityAdmin.entityTypes.new"/>
</a>

<s:set var="entity_list" value="entityPrototypes"/>
<s:if test="%{#entity_list.size > 0}">
    <wpsa:subset source="#entity_list" count="10" objectName="entityGroup" advanced="true" offset="5">
        <s:set var="group" value="#entityGroup"/>
        <div class="mt-20">
            <table class="table table-striped table-bordered table-hover no-mb">
                <thead>
                    <tr>
                        <th><s:text name="label.name"/></th>
                        <th class="text-center table-w-10"><s:text name="label.code"/></th>
                        <th class="text-center table-w-5"><s:text name="label.state"/></th>
                        <th class="text-center table-w-5"><s:text name="label.actions"/></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator var="entityType" status="counter">
                        <s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}"/>
                        <tr>
                            <td>
                                <s:property value="#entityType.typeDescr"/>
                            </td>
                            <td class="text-center">
                                <span>
                                    <s:property value="#entityType.typeCode"/>
                                </span>
                            </td>
                            <td class="text-center">
                                <s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">
                                    <span class="icon fa fa-spinner" title="<s:text name="label.references.status.wip" />">
                                    </span>
                                </s:if>
                                <s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2">
                                    <span class="icon fa fa-exclamation text-warning" title="<s:text name="label.references.status.ko" />">
                                    </span>
                                </s:elseif>
                                <s:elseif  test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0">
                                    <span class="icon fa fa-check text-success"  title="<s:text name="label.references.status.ok" />"> </span>
                                </s:elseif>
                            </td>
                            <td class="text-center text-nowrap">
                                <div class="dropdown dropdown-kebab-pf">
                                    <button class="btn btn-menu-right dropdown-toggle" type="button"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-right"
                                        aria-labelledby="dropdownKebabRight">
                                        <li>
                                            <%-- edit --%> <a class=""
                                               id="<s:property value="#entityAnchor" />"
                                               href="
                                               <s:url namespace="/do/Entity" action="initEditEntityType">
                                                   <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                                   <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                               </s:url>
                                               "
                                               title="<s:text name="label.edit" />: <s:property value="#entityType.typeDescr" />">
                                                <span class="sr-only"><s:text name="label.edit"/>&#32;<s:property
                                                        value="#entityType.typeDescr"/></span> <s:text name="label.edit"/>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="<s:url namespace="/do/Entity" action="reloadEntityTypeReferences" anchor="%{#entityAnchor}"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                               </s:url>" title="<s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2"><s:text name="label.references.status.ko" />
                                               </s:if>
                                               <s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0"><s:text name="label.references.status.ok" /></s:if>" class=""
                                               <s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">disabled="true"</s:if>>
                                                <s:text name="label.reload"/> <span class="sr-only">
                                                    <s:text name="label.references.status.ko"/></span> </span>
                                            </a>
                                        </li>
                                        <%-- remove --%>
                                        <li>
                                            <a href="<s:url namespace="/do/Entity" action="trashEntityType">
                                                   <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                                   <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                               </s:url>" title="<s:text name="label.remove" />: <s:property value="#entityType.typeDescr" />"  class="">
                                                <s:text name="label.delete"/>
                                                <span class="sr-only"><s:text name="label.alt.clear"/></span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
            <div class="content-view-pf-pagination clearfix">
                <div class="form-group">
                    <span><s:include
                            value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/></span>
                    <div class="mt-5">
                        <s:include
                            value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp"/>
                    </div>
                </div>
            </div>
        </div>
    </wpsa:subset>
</s:if>
<s:else>
    <p>
        <s:text name="%{'note.' + entityManagerName + '.empty'}"/>
    </p>
</s:else>
<br>

<s:include value="/WEB-INF/apsadmin/jsp/entity/include/entity-type-references-operations-reload.jsp"/>

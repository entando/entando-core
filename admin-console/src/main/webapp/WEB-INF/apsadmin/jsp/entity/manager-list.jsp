<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li class="page-title-container">
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
            <s:text name="%{'title.' + entityManagerName + '.management'}" />
        </a>
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="%{'title.' + entityManagerName + '.management'}" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div id="main" role="main">
    <p><s:text name="note.entities.intro" /></p>

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.FieldErrors" /></strong>
            <ul>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <p>
        <a href="<s:url namespace="/do/BaseAdmin" action="reloadEntitiesReferences" />"><s:text name="note.entityAdmin.entityManagers.reload" /></a>
    </p>

    <table class="generic" summary="<s:text name="note.entityAdmin.entityManagers.list.summary" />">
        <caption>
            <span><s:text name="title.entityAdmin.entityManagers.list" /></span>
        </caption>
        <tr>
            <th><s:text name="label.description" /></th>
            <th class="icon">
                <span title="<s:text name="label.references.long" />">
                    <s:text name="label.references.short" />
                </span>
            </th>
        </tr>
        <s:iterator value="entityManagers" var="entityManager" status="counter">
            <s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
            <tr>
                <td>
                    <a id="<s:property value="#entityAnchor" />" href="<s:url namespace="/do/Entity" action="initViewEntityTypes" >
                           <s:param name="entityManagerName" value="#entityManager" />
                       </s:url>">
                        <s:text name="%{#entityManager}.name" />
                    </a>
                </td>
                <td class="icon">
                    <s:if test="getEntityManagerStatus(#entityManager) == 1">
                        <a href="
                           <s:url action="viewManagers" namespace="/do/Entity" anchor="%{#entityAnchor}" />
                           " title="<s:text name="label.references.status.wip" />"><img src="<wp:resourceURL />administration/common/img/icons/generic-status-wip.png" alt="<s:text name="label.references.status.wip" />" /></a>
                        </s:if>
                        <s:elseif test="getEntityManagerStatus(#entityManager) == 2">
                        <a href="
                           <s:url action="reloadEntityManagerReferences" namespace="/do/Entity" anchor="%{#entityAnchor}">
                               <s:param name="entityManagerName" value="#entityManager" />
                           </s:url>
                           " title="<s:text name="label.references.status.ko" />"><img src="<wp:resourceURL />administration/common/img/icons/generic-status-ko.png" alt="<s:text name="label.references.status.ko" />" /></a>
                        </s:elseif>
                        <s:elseif test="getEntityManagerStatus(#entityManager) == 0">
                        <a href="
                           <s:url action="reloadEntityManagerReferences" namespace="/do/Entity" anchor="%{#entityAnchor}">
                               <s:param name="entityManagerName" value="#entityManager" />
                           </s:url>
                           " title="<s:text name="label.references.status.ok" />"><img src="<wp:resourceURL />administration/common/img/icons/generic-status-ok.png" alt="<s:text name="label.references.status.ok" />" /></a>
                        </s:elseif>
                </td>
            </tr>
        </s:iterator>
    </table>
</div>

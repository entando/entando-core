<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<s:set var="entityPrototypes" value="entityPrototypes" />
<s:if test="#entityPrototypes != null && #entityPrototypes.size() > 0">
    <%-- discover if one ore more references needs warning --%>
    <s:set var="entityTypesReferencesStatusNotOk" value="%{false}" />
    <s:iterator value="#entityPrototypes" var="entityTypeTest">
        <s:if test="getEntityManagerStatus(entityManagerName, #entityTypeTest.typeCode) > 0">
            <s:set var="entityTypesReferencesStatusNotOk" value="%{true}" />
        </s:if>
    </s:iterator>
    <%-- set css class for panel div --%>
    <s:set var="alertCssClass"> alert-success</s:set>
    <s:if test="#entityTypesReferencesStatusNotOk"><s:set var="alertCssClass">  alert-warning</s:set></s:if>

            <div class="alert <s:property value="#alertCssClass" />"><%-- panel --%>
        <span class="pficon pficon-ok"></span>
        <p id="fagiano_startReloadReferences"><strong>
                <s:text name="title.entityAdmin.entityTypes.references" /></strong></p>
                <s:text name="%{'note.entityAdmin.' + entityManagerName + '.references'}" />
                <s:if test="%{#entityTypesReferencesStatusNotOk}">
                    <s:text name="note.entity.reload.references" />
            <ul>
                <s:iterator value="entityPrototypes" var="entityType" status="counter">
                    <s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">
                        <li>
                            <span title="<s:text name="label.references.status.wip" />">
                                <s:property value="#entityType.typeDescr"/>&#32;
                                <span class="icon fa fa-spinner"></span>
                            </span>
                        </li>
                    </s:if>
                    <s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2">
                        <li>
                            <a href="<s:url namespace="/do/Entity" action="reloadEntityTypeReferences" anchor="%{#entityAnchor}"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param></s:url>" title="<s:text name="label.references.status.ko" />">
                                <s:property value="#entityType.typeDescr"/>&#32;<span class="icon fa fa-exclamation text-warning"></span>
                            </a>
                        </li>
                    </s:elseif>
                </s:iterator>
            </ul>
        </s:if>
        <s:else>
            <span class="label label-success"><s:text name="note.entity.all.references.ok" /></label>
            </s:else>
    </div>
</s:if>
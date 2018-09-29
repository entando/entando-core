<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="%{'title.' + entityManagerName + '.menu'}"/>
    </li>
    <li>
        <a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName"><s:text name="%{entityManagerName}" /></s:param></s:url>">
            <s:text name="%{'title.' + entityManagerName + '.management'}"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.entityTypes.editType.remove" />:
        <s:property  value="entityTypeCode" /> -
        <s:property value="%{getEntityPrototype(entityTypeCode).typeDescr}" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.entityTypes.editType.remove" />
        :
        <s:property value="entityTypeCode" />
        -
        <s:property value="%{getEntityPrototype(entityTypeCode).typeDescr}" />
        <span class="pull-right"> <a tabindex="0" role="button"
                                     data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                     data-content="<s:text name="%{'page.' + entityManagerName + '.help'}"/>" data-placement="left"
                                     data-original-title=""> <i class="fa fa-question-circle-o"
                                       aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">

    <div class="alert alert-warning">
        <s:property value="references.size()" />
        &#32;
        <s:text name="note.entityTypes.deleteType.references" />
    </div>

    <p>
        <s:text name="note.entityTypes.deleteType.references.outro" />
        :
    </p>
    
    <ul class="list-group">
        <s:iterator value="references" var="entityId">
            <li class="list-group-item">
            <s:if test="entityManagerName == 'jacmsContentManager'">
                <a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId"><s:property value="#entityId" /></s:param></s:url>">
                    <s:property value="#entityId" />
                </a>
            </s:if>
            <s:elseif test="entityManagerName == 'UserProfileManager'">
                <a href="<s:url action="edit" namespace="/do/userprofile"><s:param name="username"><s:property value="#entityId" /></s:param></s:url>">
                    <s:property value="#entityId" />
                </a>
            </s:elseif>
            <s:else>
                <s:property value="#entityId" />
            </s:else>
            </li>
        </s:iterator>
    </ul>

    <s:if test="entityManagerName == 'jacmsContentManager'">
        <div class="pull-right margin-large-top">
            <a class="btn btn-default"
               href="<s:url action="search" namespace="/do/jacms/Content"><s:param name="contentType"><s:property value="entityTypeCode" /></s:param><s:param name="viewCode"><s:property value="true" /></s:param><s:param name="viewTypeDescr"><s:property value="true" /></s:param></s:url>"><s:text
                    name="note.goToSomewhere" />: <s:text
                    name="jacms.menu.contentAdmin.list" /></a> <a class="btn btn-default"
                    href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>"><s:text
                    name="note.backToSomewhere" />: <s:text
                    name="title.entityAdmin.manager" />&#32;<s:property
                    value="entityManagerName" /></a>
        </div>
    </s:if>
</div>

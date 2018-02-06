<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core"%>

<div class="with-nav-tabs">
    <ul class="nav nav-tabs" id="ref-tab">
        <li class="active">
            <a data-toggle="tab" class="no-decorations" href="#pages">
                <s:text name="title.group.pages" />
            </a>
        </li>
        <li>
            <a data-toggle="tab" class="no-decorations" href="#users">
                <s:text name="title.group.users" />
            </a>
        </li>
        <li>
            <a data-toggle="tab" class="no-decorations" href="#widget">
                <s:text name="title.group.widgetTypes" />
            </a>
        </li>
        <li>
            <a data-toggle="tab" class="no-decorations" href="#contents">
                <s:text name="title.group.contents" />
            </a>
        </li>
        <li>
            <a data-toggle="tab" class="no-decorations" href="#resources">
                <s:text name="title.group.resources" />
            </a>
        </li>
    </ul>
    <div class="tab-content">
        <div id="pages" class="tab-pane fade in active">
            <s:form action="detail" cssClass="form-horizontal">
                <s:hidden name="name" />
                <s:if test="null != references['PageManagerUtilizers']">
                    <wpsa:subset source="references['PageManagerUtilizers']" count="10"
                                 objectName="pageReferences" advanced="true" offset="5"
                                 pagerId="pageManagerReferences">
                        <s:set var="group" value="#pageReferences" />
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb"
                                   id="pageListTable">
                                <thead>
                                    <tr>
                                        <th><s:text name="label.page" /></th>
                                        <th><s:text name="label.actions" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="currentPageVar">
                                        <s:set var="canEditCurrentPage" value="%{false}" />
                                        <s:set var="currentPageGroup" value="#currentPageVar.group"
                                               scope="page" />
                                        <wp:ifauthorized groupName="${currentPageGroup}"
                                                         permission="managePages">
                                            <s:set var="canEditCurrentPage" value="%{true}" />
                                        </wp:ifauthorized>
                                        <tr>
                                            <td>
                                                <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
                                            </td>
                                            <td class=" text-center table-view-pf-actions">
                                                <s:if
                                                    test="#canEditCurrentPage">
                                                    <div class="dropdown dropdown-kebab-pf">
                                                        <button class="btn btn-menu-right dropdown-toggle"
                                                                type="button" data-toggle="dropdown" aria-haspopup="true"
                                                                aria-expanded="false">
                                                            <span class="fa fa-ellipsis-v"></span>
                                                        </button>
                                                        <ul class="dropdown-menu dropdown-menu-right">
                                                            <li>
                                                                <a title="<s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"
                                                                   href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#currentPageVar.code" /></s:url>">
                                                                       <span>
                                                                       <s:text name="note.goToSomewhere" />: <s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
                                                                   </span>
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <a title="<s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"
                                                                   href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#currentPageVar.code" /></s:url>">
                                                                       <span>
                                                                       <s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
                                                                   </span>
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </s:if>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                        <s:set var="group" value="#pageReferences" />
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span>
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                                </span>
                                <div class="mt-5">
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <br>
                    <p class="margin-none text-center">
                        <s:text name="note.group.referencedPages.empty" />
                    </p>
                </s:else>
            </s:form>
        </div>
        <div id="users" class="tab-pane fade">
            <s:form action="detail" cssClass="form-horizontal">
                <s:hidden name="name" />
                <s:if test="null != references['AuthorizationManagerUtilizers']">
                    <wpsa:subset source="references['AuthorizationManagerUtilizers']"
                                 count="10" objectName="userReferences" advanced="true" offset="5"
                                 pagerId="userManagerReferences">
                        <s:set var="group" value="#userReferences" />
                        <wp:ifauthorized permission="superuser" var="canEditUser" />
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb"
                                   id="userListTable">
                                <thead>
                                    <tr>
                                        <th><s:text name="label.username" /></th>
                                        <th><s:text name="label.date.lastLogin" /></th>
                                        <th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1">
                                            <s:text name="label.state" />
                                        </th>
                                        <th class="<c:out value="${canEditUser ? '' : ' hide'}" />">
                                            <s:text name="label.actions" />
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="usernameVar">
                                        <s:set var="userVar" value="%{getUser(#usernameVar)}" />
                                        <s:if test="null == #userVar || #userVar.disabled">
                                            <s:set var="statusIconClassVar"
                                                   value="%{'icon fa fa-pause text-warning'}" />
                                            <s:set var="statusTextVar"
                                                   value="%{getText('note.userStatus.notActive')}" />
                                        </s:if>
                                        <s:elseif test="!#userVar.entandoUser">
                                            <s:set var="statusIconClassVar" value="%{'icon fa fa-minus'}" />
                                            <s:set var="statusTextVar"
                                                   value="%{getText('note.userStatus.notEntandoUser')}" />
                                        </s:elseif>
                                        <s:elseif test="!#userVar.accountNotExpired">
                                            <s:set var="statusIconClassVar"
                                                   value="%{'icon fa fa-circle-o text-danger'}" />
                                            <s:set var="statusTextVar"
                                                   value="%{getText('note.userStatus.expiredAccount')}" />
                                        </s:elseif>
                                        <s:elseif test="!#userVar.credentialsNotExpired">
                                            <s:set var="statusIconClassVar"
                                                   value="%{'icon fa fa-adjust text-warning'}" />
                                            <s:set var="statusTextVar"
                                                   value="%{getText('note.userStatus.expiredPassword')}" />
                                        </s:elseif>
                                        <s:elseif test="!#userVar.disabled">
                                            <s:set var="statusIconClassVar"
                                                   value="%{'icon fa fa-check text-success'}" />
                                            <s:set var="statusTextVar"
                                                   value="%{getText('note.userStatus.active')}" />
                                        </s:elseif>
                                        <tr>
                                            <!-- username -->
                                            <td>
                                                <code>
                                                    <s:property value="#usernameVar" />
                                                </code>
                                            </td>
                                            <!-- last login -->
                                            <td class="text-center">
                                                <s:if
                                                    test="#userVar.entandoUser && #userVar.lastAccess != null">
                                                    <code title="<s:date name="#userVar.lastAccess" format="EEEE d MMMM yyyy" />">
                                                        <s:date name="#userVar.lastAccess" format="dd/MM/yyyy" />
                                                    </code>
                                                </s:if>
                                                <s:else>
                                                    <span class="icon fa fa-minus text-muted"
                                                          title="<s:text name="label.none" />"></span>
                                                    <span class="sr-only"><s:text name="label.none" /></span>
                                                </s:else>
                                            </td>
                                            <%-- status --%>
                                            <td class="text-center"><span class="sr-only">
                                                    <s:property value="#statusTextVar" /></span>
                                                <span class="<s:property value="#statusIconClassVar" />"
                                                      title="<s:property value="#statusTextVar" />">
                                                </span>
                                            </td>
                                            <%-- actions --%>
                                            <td class="table-view-pf-actions <c:out value="${canEditUser ? '' : ' hide'}" />">
                                                <c:if test="${canEditUser == 'true'}">
                                                    <div class="dropdown dropdown-kebab-pf">
                                                        <button class="btn btn-menu-right dropdown-toggle"
                                                                type="button" data-toggle="dropdown" aria-haspopup="true"
                                                                aria-expanded="false">
                                                            <span class="fa fa-ellipsis-v"></span>
                                                        </button>
                                                        <ul class="dropdown-menu dropdown-menu-right">
                                                            <li>
                                                                <a title="<s:text name="label.edit" />: <s:property value="#usernameVar" />"
                                                                   href="<s:url namespace="/do/User" action="edit"><s:param name="username" value="#usernameVar"/></s:url>">
                                                                    <span><s:text name="label.edit" /></span>
                                                                </a>
                                                            </li>
                                                            <li>
                                                                <a title="<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />"
                                                                   href="<s:url action="edit" namespace="/do/User/Authorization"><s:param name="username" value="#usernameVar"/></s:url>">
                                                                       <span>
                                                                       <s:text name="note.configureAuthorizationsFor" />:&#32;
                                                                       <s:property value="#usernameVar" />
                                                                   </span>
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span>
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                                </span>
                                <div class="mt-5">
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <p class="margin-none text-center">
                        <br>
                        <s:text name="note.group.referencedUsers.empty" />
                    </p>
                </s:else>
            </s:form>
        </div>
        <div id="widget" class="tab-pane fade">
            <s:form action="detail" cssClass="form-horizontal">
                <s:hidden name="name" />
                <s:if test="null != references['WidgetTypeManagerUtilizers']">
                    <wpsa:subset source="references['WidgetTypeManagerUtilizers']"
                                 count="10" objectName="widgetTypeReferencesVar" advanced="true"
                                 offset="5" pagerId="widgetTypeReferences">
                        <s:set var="group" value="#widgetTypeReferencesVar" />
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb"
                                   id="widgetTypeListTable">
                                <thead>
                                    <tr>
                                        <th><s:text name="label.title" /></th>
                                        <th><s:text name="label.code" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="currentWidgetVar">
                                        <tr>
                                            <td>
                                                <s:property value="%{getTitle(#currentWidgetVar.code, #currentWidgetVar.titles)}" />
                                            </td>
                                            <td>
                                                <code>
                                                    <s:property value="#currentWidgetVar.code" />
                                                </code>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span>
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                                </span>
                                <div class="mt-5">
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <p class="margin-none text-center">
                        <br>
                        <s:text name="note.group.referencedWidgetTypes.empty" />
                    </p>
                </s:else>
            </s:form>
        </div>
        <div id="contents" class="tab-pane fade">
            <s:form action="detail" cssClass="form-horizontal">
                <s:hidden name="name" />
                <s:if test="null != references['jacmsContentManagerUtilizers']">
                    <wpsa:subset source="references['jacmsContentManagerUtilizers']"
                                 count="10" objectName="contentReferences" advanced="true"
                                 offset="5" pagerId="contentManagerReferences">
                        <s:set var="group" value="#contentReferences" />
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb"
                                   id="contentListTable">
                                <thead>
                                    <tr>
                                        <th class="col-xs-7 col-sm-7 col-md-7 col-lg-7">
                                            <s:text name="label.description" />
                                        </th>
                                        <th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1">
                                            <s:text name="label.code" />
                                        </th>
                                        <th class="text-center"><s:text name="label.type" /></th>
                                        <th class="text-center"><s:text name="label.lastEdit" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="currentContentIdVar">
                                        <jacmswpsa:content contentId="%{#currentContentIdVar}"
                                                           var="currentContentRecordVar" record="true" />
                                        <jacmswpsa:content contentId="%{#currentContentRecordVar.id}"
                                                           var="currentContentVar" authToEditVar="isAuthToEditVar"
                                                           workVersion="true" />
                                        <tr>
                                            <td>
                                                <s:if test="#isAuthToEditVar">
                                                    <a href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#currentContentVar.id" /></s:url>"
                                                       title="<s:text name="label.edit" />:&#32;<s:property value="#currentContentVar.descr"/>">
                                                        <s:property value="#currentContentVar.descr" />
                                                    </a>
                                                </s:if>
                                                <s:else>
                                                    <s:property value="#currentContentVar.descr" />
                                                </s:else>
                                            </td>
                                            <td class="text-center">
                                                <code>
                                                    <s:property value="#currentContentVar.id" />
                                                </code>
                                            </td>
                                            <td class="text-center">
                                                <s:property value="#currentContentVar.typeDescr" />
                                            </td>
                                            <td class="icon text-center">
                                                <code>
                                                    <s:date name="#currentContentRecordVar.modify"
                                                            format="dd/MM/yyyy" />
                                                </code>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span>
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                                <div class="mt-5">
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <p class="margin-none text-center">
                        <br>
                        <s:text name="note.group.referencedContents.empty" />
                    </p>
                </s:else>
            </s:form>
        </div>
        <div id="resources" class="tab-pane fade">
            <s:form action="detail" cssClass="form-horizontal">
                <s:hidden name="name" />
                <s:if test="null != references['jacmsResourceManagerUtilizers']">
                    <wpsa:subset source="references['jacmsResourceManagerUtilizers']"
                                 count="10" objectName="resourceReferences" advanced="true"
                                 offset="5" pagerId="resourceManagerReferences">
                        <s:set var="group" value="#resourceReferences" />
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb"
                                   id="resourceListTable">
                                <thead>
                                    <tr>
                                        <th><s:text name="label.description" /></th>
                                        <th><s:text name="label.type" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="currentResourceIdVar">
                                        <jacmswpsa:resource resourceId="%{#currentResourceIdVar}"
                                                            var="currentResourceVar" />
                                        <tr>
                                            <s:set var="canEditCurrentResource" value="%{false}" />
                                            <c:set var="currentResourceGroup">
                                                <s:property value="#currentResourceVar.mainGroup"
                                                            escapeHtml="false" />
                                            </c:set>
                                            <td>
                                                <wp:ifauthorized groupName="${currentResourceGroup}"
                                                                 permission="manageResources">
                                                    <s:set var="canEditCurrentResource" value="%{true}" />
                                                </wp:ifauthorized>
                                                <s:if test="#canEditCurrentResource">
                                                    <a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="#currentResourceVar.id" /></s:url>"
                                                       title="<s:text name="label.edit" />:&#32;<s:property value="#currentResourceVar.descr"/>">
                                                        <s:property value="#currentResourceVar.descr" />
                                                    </a>
                                                </s:if>
                                                <s:else>
                                                    <s:property value="#currentResourceVar.descr" />
                                                </s:else>
                                            </td>
                                            <td>
                                                <s:property value="#currentResourceVar.type" />
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span>
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                                </span>
                                <div class="mt-5">
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <p class="margin-none text-center">
                        <br>
                        <s:text name="note.group.referencedResources.empty" />
                    </p>
                </s:else>
            </s:form>
        </div>
    </div>
</div>

<script>
    $('#ref-tab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
</script>

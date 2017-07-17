<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="now" class="java.util.Date"/>
<wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
<fmt:formatDate value="${now}" pattern="EEEE, dd MMMM yyyy" var="currentDate"/>

<!-- Admin console Breadcrumbs -->
<s:url action="results" namespace="/do/jacms/Content" var="contentListURL"/>
<s:text name="note.goToSomewhere" var="contentListURLTitle"/>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
<h1 class="page-title-container">
    <s:text name="title.dashboard" />
    <span class="pull-right">
        <span class="date mr-20"><s:property value="#attr.currentDate"/></span>
    </span>
</h1>

<!-- Default separator -->
<div class="form-group-separator"></div>

<div id="main" role="main" class="dashboard mt-20">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8 col-lg-9 main-column">
                <div class="cards-pf">
                    <div class="container-fluid container-cards-pf no-mt">
                        <div class="row row-cards-pf">

                            <!-- Pages -->
                            <div class="col-xs-12 col-lg-4">
                                <div class="card-pf card-pf-accented card-pf-aggregate-status">
                                    <h2 class="card-pf-title no-mb text-left bold">
                                        <s:text name="dashboard.pageStatus" />
                                    </h2>
                                    <div class="text-left"><span id="lastUpdate-pages"></span></div>
                                    <div class="card-pf-body" id="page-status">
                                        <div class="spinner spinner-xl"></div>
                                        <ul class="mt-10 text-left ml-20 pt-20 hidden">
                                            <li>
                                                <span class="fa fa-circle green mr-10"></span>
                                                <span class="card-pf-aggregate-status-count">
                                                    <span id="online-pages"></span>
                                                    <s:text name="dashboard.pages.online" />
                                                </span>
                                            </li>
                                            <li>
                                                <span class="fa fa-circle yellow mr-10"></span>
                                                <span class="card-pf-aggregate-status-count">
                                                    <span id="onlineWithChanges-pages"></span>
                                                    <s:text name="dashboard.pages.online"/>&#32;&ne;&#32;
                                                    <s:text name="dashboard.pages.draft"/>
                                                </span>
                                            </li>
                                            <li>
                                                <span class="fa fa-circle gray mr-10"></span>
                                                <span class="card-pf-aggregate-status-count">
                                                    <span id="draft-pages"></span> <s:text name="dashboard.pages.draft" />
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                    <wp:ifauthorized permission="managePages">
                                        <s:url namespace="/do/Page" action="viewTree" var="pageListURL" />
                                        <a href="${pageListURL}" class="bottom-link display-block text-right"
                                           title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.pages.pageList" />">
                                            <s:text name="dashboard.pages.pageList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>
                            </div>

                            <!-- Contents -->
                            <div class="col-xs-12 col-lg-8">
                                <div class="card-pf card-pf-utilization card-pf-accented card-pf-aggregate-status">
                                    <h2 class="card-pf-title no-mb text-left bold">
                                        <s:text name="dashboard.contentStatus" />
                                    </h2>
                                    <div class="text-left"><span id="lastUpdate-contents"></span></div>
                                    <div class="card-pf-body" id="content-status">
                                        <div id="contents-donut-chart"
                                             class="example-donut-chart-right-legend">
                                            <div class="spinner spinner-lg"></div>
                                        </div>
                                    </div>
                                    <wp:ifauthorized permission="manageContents">
                                        <s:url action="results" namespace="/do/jacms/Content" var="contentListURL" />
                                        <a href="${contentListURL}" class="bottom-link display-block text-right" title="<s:text name="note.goToSomewhere" />:
                                           <s:text name="dashboard.contents.contentList" />">
                                            <s:text name="dashboard.contents.contentList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Page table -->
                <div class="row">
                    <div class="container-fluid container-cards-pf">
                        <div class="row row-cards-pf">
                            <div class="col-xs-12">
                                <div class="card-pf card-pf-utilization">
                                    <div class="card-pf-heading">
                                        <p class="card-pf-heading-details">
                                            <wp:ifauthorized permission="managePages">
                                                <s:url namespace="/do/Page" action="new" var="addPageURL" />
                                                <a href="${addPageURL}" class="btn btn-primary" title="<s:text name="dashboard.addPage" />">
                                                    <s:text name="label.add" />
                                                </a>
                                            </wp:ifauthorized>
                                        <h2 class="card-pf-title">
                                            <s:text name="dashboard.pageList" />
                                        </h2>
                                    </div>
                                    <div class="card-pf-body" id="page-table">
                                        <div class="spinner spinner-xl"></div>
                                        <div class="table-responsive hidden">
                                            <table id="page-table" class="table table-striped table-bordered no-mb">
                                                <thead>
                                                    <tr>
                                                        <th><s:text name="dashboard.pages.description" /></th>
                                                        <th class="text-center table-w-10"><s:text name="dashboard.pages.status" /></th>
                                                        <th class="text-center w20perc"><s:text name="dashboard.pages.lastModified" /></th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <wp:ifauthorized permission="managePages">
                                        <s:url namespace="/do/Page" action="viewTree"
                                               var="pageListURL" />
                                        <a href="${pageListURL}" class="bottom-link display-block text-right"
                                           title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.pageList" />">
                                            <s:text name="dashboard.pageList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <!-- Content Table -->
                <div class="row">
                    <div class="container-fluid container-cards-pf">
                        <div class="row row-cards-pf">
                            <div class="col-xs-12">
                                <div class="card-pf card-pf-utilization">
                                    <div class="card-pf-heading">
                                        <wp:ifauthorized permission="editContents">
                                            <span class="card-pf-heading-details">
                                                <wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
                                                <span class="btn-group">
                                                    <button type="button" class="btn btn-primary dropdown-toggle"
                                                            data-toggle="dropdown">
                                                        <s:text name="label.add"/>&#32;
                                                        <span class="caret"></span>
                                                    </button>
                                                    <ul class="dropdown-menu" role="menu">
                                                        <s:iterator var="contentTypeVar" value="#contentTypesVar">
                                                            <jacmswpsa:contentType
                                                                typeCode="%{#contentTypeVar.typeCode}"
                                                                property="isAuthToEdit" var="isAuthToEditVar" />
                                                            <s:if test="%{#isAuthToEditVar}">
                                                                <li>
                                                                    <s:url action="createNew" namespace="/do/jacms/Content" var="addContentURL">
                                                                        <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                                                                    </s:url>
                                                                    <a href="${addContentURL}"
                                                                       title="<s:property value="%{#contentTypeVar.typeDescr}" />">
                                                                        <s:property value="%{#contentTypeVar.typeDescr}" />
                                                                    </a>
                                                                </li>
                                                            </s:if>
                                                        </s:iterator>
                                                    </ul>
                                                </span>
                                            </span>
                                        </wp:ifauthorized>
                                        <h2 class="card-pf-title">
                                            <s:text name="dashboard.contentList" />
                                        </h2>
                                    </div>
                                    <div class="card-pf-body" id="content-table">
                                        <div class="table-responsive hidden">
                                            <table class="table table-striped table-bordered no-mb">
                                                <thead>
                                                    <tr>
                                                        <th><s:text name="dashboard.contents.description" /></th>
                                                        <th><s:text name="dashboard.contents.author" /></th>
                                                        <th><s:text name="dashboard.contents.type" /></th>
                                                        <th class="text-center table-w-10"><s:text name="dashboard.contents.status" /></th>
                                                        <th class="text-center w20perc"><s:text name="dashboard.contents.lastModified" /></th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <wp:ifauthorized permission="editContents">
                                        <s:url action="list" namespace="/do/jacms/Content" var="contentListURL" />
                                        <a href="${contentListURL}"
                                           class="bottom-link display-block text-right"
                                           title="<s:text name="note.goToSomewhere" />: <s:text name="dashboard.contentList" />">
                                            <s:text name="dashboard.contentList" />
                                        </a>
                                    </wp:ifauthorized>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column -->
            <div class="col-md-4 col-lg-3 sidebar-pf sidebar-pf-right">
                <div class="card-pf mt-20">
                    <h2 class="card-pf-title bold no-mb mt-10">
                        <s:text name="dashboard.fastSettings"/>
                        <span class="fa fs-20 fa-cog pull-right"></span>
                    </h2>
                    <div class="card-pf-body">
                        <ul class="pt-20 pb-20 pl-10">
                            <wp:ifauthorized permission="viewUsers" var="viewUsersPermVar" />
                            <wp:ifauthorized permission="editUsers" var="editUsersPermVar" />
                            <wp:ifauthorized permission="editUserProfile" var="editUserProfilePermVar" />
                            <li>
                                <c:if test="${viewUsersPermVar || editUsersPermVar || editUserProfilePermVar}">
                                    <a href='<s:url action="list" namespace="/do/User" />'>
                                        <s:text name="dashboard.userList" />
                                    </a>
                                </c:if>
                            </li>
                            <li>
                                <wp:ifauthorized permission="manageCategories">
                                    <a href='<s:url action="viewTree" namespace="/do/Category" />'>
                                        <s:text name="dashboard.categories" />
                                    </a>
                                </wp:ifauthorized>
                            </li>
                            <li>
                                <wp:ifauthorized permission="superuser">
                                    <a href='<s:url action="list" namespace="/do/Lang" />'>
                                        <s:text name="dashboard.labels" /> &amp; <s:text name="dashboard.languages" />
                                    </a>
                                </wp:ifauthorized>
                            </li>
                            <li>
                                <wp:ifauthorized permission="superuser">
                                    <a href='<s:url action="reloadChoose" namespace="/do/BaseAdmin" />'>
                                        <s:text name="dashboard.reloadConfig" />
                                    </a>
                                </wp:ifauthorized>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="bottom-actions">
                    <wp:ifauthorized permission="editContents">
                        <div class="btn-group dropup w100perc mt-10 mb-10">
                            <button type="button" class="btn btn-primary dropdown-toggle btn-block"
                                    data-toggle="dropdown">
                                <s:text name="dashboard.addContent"/>&#32;
                                <span class="caret dashboard-caret-right"></span>
                            </button>
                            <ul class="dropdown-menu w100perc" role="menu">
                                <s:iterator var="contentTypeVar" value="#contentTypesVar">
                                    <jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}"
                                                           property="isAuthToEdit" var="isAuthToEditVar" />
                                    <s:if test="%{#isAuthToEditVar}">
                                        <li>
                                            <s:url action="createNew" namespace="/do/jacms/Content" var="newContentURL">
                                                <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                                            </s:url>
                                            <a href="${newContentURL}"
                                               title="<s:text name="label.add" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" />">
                                                <s:property value="%{#contentTypeVar.typeDescr}" />
                                            </a>
                                        </li>
                                    </s:if>
                                </s:iterator>
                            </ul>
                        </div>
                    </wp:ifauthorized>

                    <wp:ifauthorized permission="manageResources">
                        <div class="btn-group dropup w100perc mt-10 mb-10">
                            <button type="button" class="btn btn-primary dropdown-toggle btn-block"
                                    data-toggle="dropdown">
                                <s:text name="dashboard.addAsset"/>&#32;
                                <span class="caret dashboard-caret-right"></span>
                            </button>
                            <ul class="dropdown-menu w100perc" role="menu">
                                <li>
                                    <s:url namespace="/do/jacms/Resource" action="new" var="addImageURL">
                                        <s:param name="resourceTypeCode" value="'Image'" />
                                    </s:url>
                                    <a href="${addImageURL}"
                                       title="<s:text name="label.new" />&#32;<s:text name="label.image"/>">
                                        <s:text name="label.image"/>
                                    </a>
                                </li>
                                <li>
                                    <s:url namespace="/do/jacms/Resource" action="new" var="addAttachURL">
                                        <s:param name="resourceTypeCode" value="'Attach'" />
                                    </s:url>
                                    <a href="${addAttachURL}"
                                       title="<s:text name="label.new" />&#32;<s:text name="label.attach"/>">
                                        <s:text name="label.attach"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </wp:ifauthorized>
                    <wp:ifauthorized permission="editUsers">
                        <s:url namespace="/do/User" action="new" var="addUserURL" />
                        <a href="${addUserURL}" class="btn btn-primary btn-block mt-10 mb-10"
                           title="<s:text name="dashboard.addUser" />">
                            <s:text name="dashboard.addUser" />
                        </a>
                    </wp:ifauthorized>
                </div>
            </div>
        </div>
    </div>
</div>

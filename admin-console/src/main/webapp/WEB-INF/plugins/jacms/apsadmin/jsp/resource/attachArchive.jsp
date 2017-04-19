<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app"/></li>
    <li><s:text name="breadcrumb.jacms"/></li>
    <s:if test="onEditContent">
        <li>
            <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
                <s:text name="breadcrumb.jacms.content.list"/>
            </a>
        </li>
        <li>
            <a href="<s:url action="backToEntryContent" ><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:if test="getStrutsAction() == 1">
                    <s:text name="breadcrumb.jacms.content.new"/>
                </s:if>
                <s:else>
                    <s:text name="breadcrumb.jacms.content.edit"/>
                </s:else>
            </a></li>
    </s:if>
    <s:else>
        <li><s:text name="breadcrumb.dataAsset"/></li>
    </s:else>
    <li class="page-title-container">
        <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="title.attachManagement"/>
                <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-content="TO be inserted" data-placement="left" data-original-title="">
                    <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li role="presentation" <s:if test="%{resourceTypeCode == 'Image'}">class="active" </s:if>>
                    <s:if test="onEditContent">
                        <s:text name="title.imageManagement"/>
                    </s:if>
                    <s:else>
                        <a href="<s:url action="list" ><s:param name="resourceTypeCode" >Image</s:param></s:url>"
                           role="tab">
                            <s:text name="title.imageManagement"/>
                        </a>
                    </s:else>
                </li>
                <li role="presentation" <s:if test="%{resourceTypeCode == 'Attach'}">class="active" </s:if>>
                    <s:if test="onEditContent">
                        <s:text name="title.attachManagement"/>
                    </s:if>
                    <s:else>
                        <a href="<s:url action="list" ><s:param name="resourceTypeCode" >Attach</s:param></s:url>"
                           role="tab">
                            <s:text name="title.attachManagement"/>
                        </a>
                    </s:else>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div class="tab-content" class="tab-pane active">

    <s:include value="inc/resource_searchForm.jsp"/>

    <wp:ifauthorized permission="manageResources">
        <div class="col-md-offset-1 col-md-10">
            <p><a href="<s:url action="new" >
                    <s:param name="resourceTypeCode" value="resourceTypeCode" />
                    <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                </s:url>"
                  class="btn btn-primary pull-right"
                  title="<s:property value="%{getText('label.' + resourceTypeCode + '.new')}" escapeXml="true" />">
                <s:property value="%{getText('label.' + resourceTypeCode + '.new')}"/>
            </a></p>
        </div>
    </wp:ifauthorized>
    <br/>
    <br/>

    <div class="container-fluid">
        <div class="toolbar-pf">
            <div class="toolbar-pf-action-right">
                <div class="form-group toolbar-pf-view-selector">
                    <button class="btn btn-link" data-toggle="tab" href="#table-view">
                        <i class="fa fa-th-large fa-2x"></i>
                    </button>
                    <button class="btn btn-link" data-toggle="tab" href="#list-view">
                        <i class="fa fa-th-list fa-2x"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-content">
        <div id="table-view" class="tab-pane fade">
            IO SONO UNA GRIGLIA

        </div>
        <div id="list-view" class="tab-pane fade in active">


            <s:form action="search" class="container-fluid">
                <p class="sr-only">
                    <wpsf:hidden name="text"/>
                    <wpsf:hidden name="categoryCode"/>
                    <wpsf:hidden name="resourceTypeCode"/>
                    <wpsf:hidden name="fileName"/>
                    <wpsf:hidden name="ownerGroupName"/>
                    <s:if test="#categoryTreeStyleVar == 'request'">
                        <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                            <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/>
                        </s:iterator>
                    </s:if>
                    <wpsf:hidden name="contentOnSessionMarker"/>
                </p>


                <wpsa:subset source="resources" count="10" objectName="groupResource" advanced="true" offset="5">
                    <div class="list-group list-view-pf list-view-pf-view">
                        <s:set var="group" value="#groupResource"/>
                        <s:if test="onEditContent">
                            <h3><s:text name="title.chooseAttach"/></h3>
                        </s:if>
                        <s:iterator var="resourceid" status="status">
                            <s:set var="resource" value="%{loadResource(#resourceid)}"></s:set>
                            <s:set var="resourceInstance" value="%{#resource.getInstance()}"></s:set>
                            <s:if test="onEditContent">
                                <a href="<s:url action="joinResource" namespace="/do/jacms/Content/Resource">
                                   <s:param name="resourceId" value="%{#resourceid}" />
                                   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>"
                                   title="<s:text name="label.use"/>" class="list-group-item">
                                    <s:if test="!#resource.categories.empty">
                                        <div class="row">
                                            <div class="col-lg-12">
                                                <s:iterator var="category_resource" value="#resource.categories">
                                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                                    <span class="icon fa fa-tag"></span>&#32;
                                                    <s:property value="%{#category_resource.getTitle(currentLang.code)}"/></span>
                                                </s:iterator>
                                            </div>
                                        </div>
                                    </s:if>
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <s:set var="fileDescr" value="#resource.descr"/>
                                            <s:if test='%{#fileDescr.length()>90}'>
                                                <s:set var="fileDescr" value='%{#fileDescr.substring(0,30)+"..."+#fileDescr.substring(#fileDescr.length()-30)}'/>
                                                <abbr title="<s:property value="#resource.descr" />">
                                                    <s:property value="#fileDescr"/>
                                                </abbr>
                                            </s:if>
                                            <s:else>
                                                <s:property value="#resource.descr"/>
                                            </s:else>
                                            <s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}">
                                                <span class="text-muted icon fa fa-lock"></span>
                                            </s:if>
                                            <s:set var="fileNameVar" value="#resource.masterFileName"/>
                                            <s:if test='%{#fileNameVar.length()>25}'>
                                                <s:set var="fileNameVar" value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}'/>
                                                <code class="margin-small-bottom">
                                                    <abbr title="<s:property value="#fileNameVar" />"><s:property value="#fileNameVar"/></abbr>
                                                </code>
                                            </s:if>
                                            <s:else>
                                                <code><s:property value="#fileNameVar"/></code>
                                            </s:else>
                                            <span class="badge">
                                                <s:property value="%{#fileNameVar.replaceAll(' ', '&nbsp;')}" escapeXml="false" escapeHtml="false" escapeJavaScript="false"/>
                                            </span>
                                        </div>
                                    </div>
                                </a>
                            </s:if>
                            <s:if test="!onEditContent">
                                <div class="list-group-item">
                                    <%--<div class="list-view-pf-checkbox">
                                        <input type="checkbox">
                                    </div>--%>

                                    <div class="list-view-pf-actions">
                                        <p class="sr-only"><s:text name="label.actions"/></p>
                                        <div class="dropdown pull-right dropdown-kebab-pf">
                                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight1"
                                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight1">
                                                <%--<li>
                                                    <a href="<s:url action="edit" namespace="/do/LocaleString" />?key=<s:property value="#key" />"
                                                       title="<s:text name="label.edit" />: <s:property value="#key" />">
                                                        <s:text name="label.edit"/>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="<s:url action="trash" namespace="/do/LocaleString"><s:param name="key" value="#key" /></s:url>"
                                                       title="<s:text name="label.remove" />: <s:property value="#key" />">
                                                        <s:text name="label.remove"/>
                                                    </a>
                                                </li>--%>
                                                <li>
                                                    <a href="<s:url action="edit" namespace="/do/jacms/Resource"><s:param name="resourceId" value="%{#resourceid}" /></s:url>"
                                                       title="<s:text name="label.edit" />: <s:property value="#resource.descr" /> ">
                                                        <span><s:text name="label.edit"/></span>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="<s:url action="trash" namespace="/do/jacms/Resource" >
                                                        <s:param name="resourceId" value="%{#resourceid}" />
                                                        <s:param name="resourceTypeCode" value="%{#resource.type}" />
                                                        <s:param name="text" value="%{text}" />
                                                        <s:param name="categoryCode" value="%{categoryCode}" />
                                                        <s:param name="fileName" value="%{fileName}" />
                                                        <s:param name="ownerGroupName" value="%{ownerGroupName}" />
                                                        <s:param name="treeNodesToOpen" value="%{treeNodesToOpen}" />
                                                        </s:url>"
                                                       title="<s:text name="label.remove" />: <s:property value="#resource.descr" /> ">
                                                        <span><s:text name="label.remove"/></span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div class="list-view-pf-main-info">
                                        <div class="list-view-pf-left col-o">
                                            <span class="fa fa-file-text fa-2x"/>
                                            <s:if test="!#resource.categories.empty">
                                                <s:iterator var="category_resource" value="#resource.categories">
                                                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                                        <span class="icon fa fa-tag"></span>&#32;
                                                        <s:property value="%{#category_resource.getTitle(currentLang.code)}"/>
                                                    </span>
                                                </s:iterator>
                                            </s:if>
                                        </div>
                                        <div class="list-view-pf-body">
                                            <div class="list-view-pf">
                                                <div class="list-view-pf-description" style="width: 100%">
                                                    <div class="list-group-item-heading">
                                                        <s:property value="#resource.descr"/>
                                                        <s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}">
                                                            <span class="text-muted icon fa fa-lock"></span>
                                                        </s:if>
                                                    </div>
                                                    <div class="list-group-item-text">
                                                        <a href="<s:property value="%{#resource.documentPath}" />"
                                                           title="<s:text name="label.download" />: <s:property value="#resource.masterFileName" />"
                                                           class="pull-left margin-small-top">
                                                            <s:set var="fileNameVar" value="#resource.masterFileName"/>
                                                            <s:if test='%{#fileNameVar.length()>25}'>
                                                                <s:set var="fileNameVar"
                                                                       value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}'/>
                                                                <span>
                                                                    <abbr title="<s:property value="#resource.masterFileName" />">
                                                                        <s:property value="#fileNameVar"/>
                                                                    </abbr>
                                                                </span>
                                                            </s:if>
                                                            <s:else>
                                                                <span><s:property value="#fileNameVar"/></span>
                                                            </s:else>
                                                        </a>
                                                    </div>
                                                </div>
                                                <div class="list-view-pf-additional-info">
                                                    Size:&nbsp;
                                                    <span class="badge pull-right margin-small-top">
                                                        <s:property value="%{#resourceInstance.fileLength.replaceAll(' ', '&nbsp;')}" escapeXml="false" escapeHtml="false" escapeJavaScript="false"/>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                        </s:iterator>
                    </div>
                    <div class="pager clear margin-more-top">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp"/>
                    </div>

                </wpsa:subset>
            </s:form>
        </div>
    </div>
</div>

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
        <li><s:text name="breadcrumb.digitalAsset"/></li>
        </s:else>
    <li class="page-title-container">
        <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
    </li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="breadcrumb.digitalAsset"/>
                <s:if test="!onEditContent">
                    <span class="pull-right">
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                           data-content="<s:text name="label.digitalAsset.help"/>" data-placement="left" data-original-title="">
                            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                        </a>
                    </span>
                </s:if>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li role="presentation" <s:if test="%{resourceTypeCode == 'Image'}">class="active" </s:if>>
                    <s:if test="!onEditContent">
                        <a href="<s:url action="list" ><s:param name="resourceTypeCode" >Image</s:param></s:url>" role="tab">
                            <s:text name="title.imageManagement"/>
                        </a>
                    </s:if>
                </li>
                <li role="presentation" <s:if test="%{resourceTypeCode == 'Attach'}">class="active" </s:if>>
                    <s:if test="!onEditContent">
                        <a href="<s:url action="list" ><s:param name="resourceTypeCode" >Attach</s:param></s:url>" role="tab">
                            <s:text name="title.attachManagement"/>
                        </a>
                    </s:if>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div class="tab-content" class="tab-pane active">
    <s:include value="inc/resource_searchForm.jsp"/>
    <wp:ifauthorized permission="manageResources">
        <div class="col-sm-12">
            <p><a href="<s:url action="new" >
                      <s:param name="resourceTypeCode" value="resourceTypeCode" />
                      <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                  </s:url>"
                  class="btn btn-primary pull-right mb-5" title="<s:property value="%{getText('label.' + resourceTypeCode + '.new')}" escapeXml="true" />" >
                    <s:property value="%{getText('label.' + resourceTypeCode + '.new')}"/>
                </a></p>
        </div>
    </wp:ifauthorized>
    <br/>

    <div class="container-fluid">
        <div class="toolbar-pf">
            <div class="toolbar-pf-action-right mt-10">
                <div class="form-group toolbar-pf-view-selector">
                    <span class="choose_view"><s:text name="label.visualization"/></span>
                    <button class="btn btn-link" data-toggle="tab" href="#table-view">
                        <i class="fa fa-th-large"></i>
                    </button>
                    <button class="btn btn-link" data-toggle="tab" href="#list-view">
                        <i class="fa fa-th-list"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-content">
        <div id="table-view" class="tab-pane fade">
            <s:form action="search" class="container-fluid container-cards-pf">
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

                    <!--carte-->
                    <s:if test="onEditContent">
                        <h3><s:text name="title.chooseAttach"/></h3>
                    </s:if>

                    <div class="row row-cards-pf">
                        <s:set var="group" value="#groupResource"/>
                        <s:iterator var="resourceid" status="status">
                            <s:set var="resource" value="%{loadResource(#resourceid)}"></s:set>
                            <s:set var="resourceInstance" value="%{#resource.getInstance()}"></s:set>

                                <!--attach card on edit attachment-->
                            <s:if test="onEditContent">
                                <div class="col-xs-6 col-sm-4 col-md-3">
                                    <div class="card-pf card-pf-view card-pf-view-select">
                                        <div class="card-pf-body">
                                            <div class="card-pf-heading-kebab">
                                                <div class="dropdown pull-right dropdown-kebab-pf">
                                                    <button class="btn btn-link dropdown-toggle" type="button"
                                                            id="dropdownKebabRight3"
                                                            data-toggle="dropdown" aria-haspopup="true"
                                                            aria-expanded="true">
                                                        <span class="fa fa-ellipsis-v"></span>
                                                    </button>
                                                    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight3">
                                                        <li>
                                                            <a href="<s:url action="joinResource" namespace="/do/jacms/Content/Resource"><s:param name="resourceId" value="%{#resourceid}" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>"
                                                               title="<s:text name="label.use"/>" class="list-group-item">
                                                                <s:text name="label.use"/>
                                                            </a>
                                                        </li>

                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="card-pf-top-element">
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
                                            <h2 class="card-pf-title text-center">
                                                <s:property value="#resource.descr"/>
                                                <s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}">
                                                    <span class="text-muted icon fa fa-lock"></span>
                                                </s:if>
                                            </h2>
                                            <p class="card-pf-info text-center">
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
                                                    <p class="card-pf-info text-center">
                                                        <s:set var="fileDescr" value="#resource.descr"/>
                                                        <s:if test='%{#fileDescr.length()>90}'>
                                                            <s:set var="fileDescr" value='%{#fileDescr.substring(0,30)+"..."+#fileDescr.substring(#fileDescr.length()-30)}'/>
                                                            <span title="<s:property value="#resource.descr" />">
                                                                <s:property value="#fileDescr"/>
                                                            </span>
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
                                                            <span class="margin-small-bottom">
                                                                <span title="<s:property value="#fileNameVar" />"><s:property value="#fileNameVar"/></span>
                                                            </span>
                                                        </s:if>
                                                        <s:else>
                                                            <span><s:property value="#fileNameVar"/></span>
                                                        </s:else>
                                                    </p>
                                                    <p class="card-pf-info text-center">
                                                        <span class="badge">
                                                            <s:property value="%{#fileNameVar.replaceAll(' ', '&nbsp;')}" escapeXml="false" escapeHtml="false" escapeJavaScript="false"/>
                                                        </span>
                                                    </p>
                                                </div>
                                            </div>
                                            </p>

                                        </div>
                                    </div>
                                </div>

                            </s:if>
                            <!--attach card on edit attachment-->

                            <s:if test="!onEditContent">
                                <div class="col-xs-6 col-sm-4 col-md-3">
                                    <div class="card-pf card-pf-view card-pf-view-select">
                                        <div class="card-pf-body">
                                            <div class="card-pf-heading-kebab">
                                                <div class="dropdown pull-right dropdown-kebab-pf">
                                                    <button class="btn btn-link dropdown-toggle" type="button"
                                                            id="dropdownKebabRight3"
                                                            data-toggle="dropdown" aria-haspopup="true"
                                                            aria-expanded="true">
                                                        <span class="fa fa-ellipsis-v"></span>
                                                    </button>
                                                    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight3">
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
                                                               title="<s:text name="label.delete"/>: <s:property value="#resource.descr" /> ">
                                                                <span><s:text name="label.delete"/></span>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="card-pf-top-element">
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
                                            <h2 class="card-pf-title text-center">
                                                <s:property value="#resource.descr"/>
                                                <s:if test="%{#resource.mainGroup != null && !#resource.mainGroup.equals('free')}">
                                                    <span class="text-muted icon fa fa-lock"></span>
                                                </s:if>
                                            </h2>
                                            <p class="card-pf-info text-center">
                                                <a href="<s:property value="%{#resource.documentPath}" />"
                                                   title="<s:text name="label.download" />: <s:property value="#resource.masterFileName" />"    >
                                                    <s:set var="fileNameVar" value="#resource.masterFileName"/>
                                                    <s:if test='%{#fileNameVar.length()>25}'>
                                                        <s:set var="fileNameVar"
                                                               value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}'/>
                                                        <span>
                                                            <span title="<s:property value="#resource.masterFileName" />">
                                                                <s:property value="#fileNameVar"/>
                                                            </span>
                                                        </span>
                                                    </s:if>
                                                    <s:else>
                                                        <span><s:property value="#fileNameVar"/></span>
                                                    </s:else>
                                                </a>
                                            </p>

                                        </div>
                                    </div>
                                </div>
                            </s:if>
                        </s:iterator>
                    </div>
                    <!--fine lista card attachment-->

                    <div class="pager clear margin-more-top">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                    </div>
                </wpsa:subset>
            </s:form>
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

                    <!--lista attachment-->

                    <s:if test="onEditContent">
                        <h3><s:text name="title.chooseAttach"/></h3>
                    </s:if>
                    <div class="list-group list-view-pf list-view-pf-view">
                        <s:set var="group" value="#groupResource"/>

                        <s:iterator var="resourceid" status="status">
                            <s:set var="resource" value="%{loadResource(#resourceid)}"></s:set>
                            <s:set var="resourceInstance" value="%{#resource.getInstance()}"></s:set>
                            <s:if test="onEditContent">

                                <div class="list-group-item">
                                    <div class="list-view-pf-actions">
                                        <div class="dropdown pull-right dropdown-kebab-pf">
                                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight15" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight15">
                                                <li>
                                                    <a href="<s:url action="joinResource" namespace="/do/jacms/Content/Resource"><s:param name="resourceId" value="%{#resourceid}" /> <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>" title="<s:text name="label.use"/>">
                                                        <s:text name="label.use"/>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div class="list-view-pf-main-info">
                                        <div class="list-view-pf-left">
                                            <span class="fa fa-file-text fa-2x"></span>
                                        </div>
                                        <div class="list-view-pf-body">
                                            <div class="list-view-pf-additional-info">
                                                <p class="list-group-item">
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
                                                            <span title="<s:property value="#resource.descr" />">
                                                                <s:property value="#fileDescr"/>
                                                            </span>
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
                                                            <span class="margin-small-bottom">
                                                                <span title="<s:property value="#fileNameVar" />"><s:property value="#fileNameVar"/></span>
                                                            </span>
                                                        </s:if>
                                                        <s:else>
                                                            <span><s:property value="#fileNameVar"/></span>
                                                        </s:else>
                                                        <span class="badge">
                                                            <s:property value="%{#fileNameVar.replaceAll(' ', '&nbsp;')}" escapeXml="false" escapeHtml="false" escapeJavaScript="false"/>
                                                        </span>
                                                    </div>
                                                </div>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <s:if test="!onEditContent">
                                <div class="list-group-item">
                                    <div class="list-view-pf-actions">
                                        <p class="sr-only"><s:text name="label.actions"/></p>
                                        <div class="dropdown pull-right dropdown-kebab-pf">
                                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight4"
                                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight4">
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
                                                       title="<s:text name="label.delete"/>: <s:property value="#resource.descr" /> ">
                                                        <span><s:text name="label.delete"/></span>
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
                                                                    <span title="<s:property value="#resource.masterFileName" />">
                                                                        <s:property value="#fileNameVar"/>
                                                                    </span>
                                                                </span>
                                                            </s:if>
                                                            <s:else>
                                                                <span><s:property value="#fileNameVar"/></span>
                                                            </s:else>
                                                        </a>
                                                    </div>
                                                </div>
                                                <div class="list-view-pf-additional-info">
                                                    <s:text name="label.size"/>&nbsp;
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
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp"/>
                    </div>
                </wpsa:subset>
            </s:form>
        </div>
    </div>
</div>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app"/>
    </li>
    <li>
        <s:text name="breadcrumb.jacms"/>
    </li>
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
            </a>
        </li>
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
                <s:if test="!onEditContent">
                    <s:text name="breadcrumb.digitalAsset"/>
                </s:if>
                <s:else>
                    <s:text name="title.imageManagement"/>
                </s:else>
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
                        <a href="<s:url action="list" ><s:param name="resourceTypeCode" >Image</s:param></s:url>"
                           role="tab">
                            <s:text name="title.imageManagement"/>
                        </a>
                    </s:if>
                </li>
                <li role="presentation" <s:if test="%{resourceTypeCode == 'Attach'}">class="active" </s:if>>
                    <s:if test="!onEditContent">
                        <a href="<s:url action="list" ><s:param name="resourceTypeCode" >Attach</s:param></s:url>"
                           role="tab">
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
            <p>
                <a href="<s:url action="new" ><s:param name="resourceTypeCode" value="resourceTypeCode" />
                       <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
                   </s:url>" class="btn btn-primary pull-right" title="<s:property value="%{getText('label.' + resourceTypeCode + '.new')}" escapeXml="true" />" style="margin-bottom: 5px">
                    <s:property value="%{getText('label.' + resourceTypeCode + '.new')}"/>
                </a>
            </p>
        </div>
    </wp:ifauthorized>
    <br>

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
                    <div class="row row-cards-pf">
                        <s:set var="group" value="#groupResource"/>
                        <s:set var="imageDimensionsVar" value="imageDimensions"/>
                        <s:iterator var="resourceid" status="status">
                            <s:set var="resource" value="%{loadResource(#resourceid)}"/>
                            <s:set var="resourceInstance" value='%{#resource.getInstance(0,null)}'/>
                            <s:set var="URLoriginal" value="%{#resource.getImagePath(0)}"/>
                            <s:url var="URLedit" action="edit" namespace="/do/jacms/Resource">
                                <s:param name="resourceId" value="%{#resourceid}"/>
                            </s:url>
                            <s:url var="URLuse" action="joinResource" namespace="/do/jacms/Content/Resource">
                                <s:param name="resourceId" value="%{#resourceid}"/>
                                <s:param name="contentOnSessionMarker" value="contentOnSessionMarker"/>
                            </s:url>
                            <s:url var="URLtrash" action="trash" namespace="/do/jacms/Resource">
                                <s:param name="resourceId" value="%{#resourceid}"/>
                                <s:param name="resourceTypeCode" value="%{#resource.type}"/>
                                <s:param name="text" value="%{text}"/>
                                <s:param name="categoryCode" value="%{categoryCode}"/>
                                <s:param name="fileName" value="%{fileName}"/>
                                <s:param name="ownerGroupName" value="%{ownerGroupName}"/>
                                <s:param name="treeNodesToOpen" value="%{treeNodesToOpen}"/>
                            </s:url>
                            <div class="col-xs-6 col-sm-4 col-md-3">
                                <div class="card-pf card-pf-view card-pf-view-select">
                                    <div class="card-pf-body">
                                        <div class="card-pf-heading-kebab">
                                            <div class="dropdown pull-right dropdown-kebab-pf">
                                                <button class="btn btn-link dropdown-toggle" type="button"
                                                        id="dropdownKebabRight1"
                                                        data-toggle="dropdown" aria-haspopup="true"
                                                        aria-expanded="true">
                                                    <span class="fa fa-ellipsis-v"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right"
                                                    aria-labelledby="dropdownKebabRight1">
                                                    <li><s:include value="imageArchive-file-info.jsp"/></li>
                                                    <li role="separator" class="divider"></li>
                                                    <li>
                                                        <s:if test="!onEditContent">
                                                            <a href="<s:property value="URLedit" escapeHtml="false" />"
                                                               title="<s:text name="label.edit" />: <s:property value="#resource.descr" />">
                                                                <s:text name="label.edit"/>
                                                            </a>
                                                        </s:if>
                                                        <s:else>
                                                            <a href="<s:property value="URLuse" escapeHtml="false" />"
                                                               title="<s:text name="note.joinThisToThat" />: <s:property value="content.descr" />">
                                                                <s:text name="label.use"/>
                                                            </a>
                                                        </s:else>
                                                    </li>
                                                    <li>
                                                        <a href="<s:property value="URLtrash" escapeHtml="false" />"><s:text name="label.delete"/></a>
                                                    </li>
                                                </ul>
                                            </div>

                                        </div>

                                        <div class="card-pf-top-element">
                                            <%-- Dimension forced for img thumbnail --%>
                                            <img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" "
                                                 style="height:90px;max-width:130px" class="img-responsive center-block"/>
                                        </div>
                                        <h2 class="card-pf-title text-center">
                                            <s:set var="fileNameVar" value="#resource.masterFileName"/>
                                            <s:if test='%{#fileNameVar.length()>15}'>
                                                <s:set var="fileNameVar"
                                                       value='%{#fileNameVar.substring(0,7)+"..."+#fileNameVar.substring(#fileNameVar.length()-5)}'/>
                                                <s:property value="#fileNameVar"/>
                                            </s:if>
                                            <s:else>
                                                <s:property value="#fileNameVar"/>
                                            </s:else>
                                        </h2>
                                    </div>
                                </div>
                            </div>
                        </s:iterator>
                    </div>
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


                    <div class="list-group list-view-pf list-view-pf-view">
                        <s:set var="group" value="#groupResource"/>
                        <s:set var="imageDimensionsVar" value="imageDimensions"/>
                        <s:iterator var="resourceid" status="status">
                            <s:set var="resource" value="%{loadResource(#resourceid)}"/>
                            <s:set var="resourceInstance" value='%{#resource.getInstance(0,null)}'/>
                            <s:set var="URLoriginal" value="%{#resource.getImagePath(0)}"/>
                            <s:url var="URLedit" action="edit" namespace="/do/jacms/Resource">
                                <s:param name="resourceId" value="%{#resourceid}"/>
                            </s:url>
                            <s:url var="URLuse" action="joinResource" namespace="/do/jacms/Content/Resource">
                                <s:param name="resourceId" value="%{#resourceid}"/>
                                <s:param name="contentOnSessionMarker" value="contentOnSessionMarker"/>
                            </s:url>
                            <s:url var="URLtrash" action="trash" namespace="/do/jacms/Resource">
                                <s:param name="resourceId" value="%{#resourceid}"/>
                                <s:param name="resourceTypeCode" value="%{#resource.type}"/>
                                <s:param name="text" value="%{text}"/>
                                <s:param name="categoryCode" value="%{categoryCode}"/>
                                <s:param name="fileName" value="%{fileName}"/>
                                <s:param name="ownerGroupName" value="%{ownerGroupName}"/>
                                <s:param name="treeNodesToOpen" value="%{treeNodesToOpen}"/>
                            </s:url>

                            <div class="list-group-item">
                                <div class="list-view-pf-actions">
                                    <div class="dropdown pull-right dropdown-kebab-pf">
                                        <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight2"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right"
                                            aria-labelledby="dropdownKebabRight2">
                                            <li>
                                                <s:if test="!onEditContent">
                                                    <a href="<s:property value="URLedit" escapeHtml="false" />"
                                                       title="<s:text name="label.edit" />: <s:property value="#resource.descr" />">
                                                        <s:text name="label.edit"/>
                                                    </a>
                                                </s:if>
                                                <s:else>
                                                    <a href="<s:property value="URLuse" escapeHtml="false" />"
                                                       title="<s:text name="note.joinThisToThat" />: <s:property value="content.descr" />">
                                                        <s:text name="label.use"/>
                                                    </a>
                                                </s:else>
                                            </li>
                                            <li>
                                                <a href="<s:property value="URLtrash" escapeHtml="false" />"><s:text name="label.delete"/></a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="list-view-pf-main-info">
                                    <div class="list-view-pf-left col-o" style="width: 130px">
                                        <img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" " class="img-responsive center-block"/>
                                    </div>
                                    <div class="list-view-pf-body">
                                        <div class="list-view-pf">
                                            <div class="list-group-item-heading" style="font-size: 16px">
                                                <s:set var="fileNameVar" value="#resource.masterFileName"/>
                                                <s:set var="fileDescVar" value="#resource.description"/>
                                                <s:if test='%{#fileNameVar.length()>24}'>
                                                    <s:set var="fileNameVar"
                                                           value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}'/>
                                                    <s:property value="#fileNameVar"/>
                                                </s:if>
                                                <s:else>
                                                    <s:property value="#fileNameVar"/>
                                                </s:else>
                                            </div>
                                            <div class="list-group-item-text">
                                                <s:property value="#fileDescVar"/>
                                            </div>
                                            <br>
                                            <div class="list-view-pf-additional-info" style="width: 100%">


                                                <s:set var="dimensionId" value="0"/>
                                                <s:set var="resourceInstance" value='%{#resource.getInstance(#dimensionId,null)}'/>
                                                <a href="<s:property value="%{#resource.getImagePath(#dimensionId)}" />" class="list-view-pf-additional-info-item">
                                                    <s:text name="label.size.original"/>&nbsp;
                                                    <span class="badge">
                                                        <s:property value='#resourceInstance.fileLength.replaceAll(" ", "&nbsp;")' escapeXml="false" escapeHtml="false"
                                                                    escapeJavaScript="false"/>
                                                    </span>
                                                </a>
                                                <s:set var="dimensionId" value="null"/>
                                                <s:set var="resourceInstance" value="null"/>
                                                <s:iterator value="#imageDimensionsVar" var="dimInfo">
                                                    <s:set var="dimensionId" value="#dimInfo.idDim"/>
                                                    <s:set var="resourceInstance" value='%{#resource.getInstance(#dimensionId,null)}'/>
                                                    <s:if test="#resourceInstance != null">
                                                        &nbsp;|&nbsp;
                                                        <a href="<s:property value="%{#resource.getImagePath(#dimensionId)}"/>" class="list-view-pf-additional-info-item">
                                                            <s:property value="#dimInfo.dimx"/>
                                                            x<s:property value="#dimInfo.dimy"/>&nbsp;px
                                                            <span class="badge">
                                                                <s:property
                                                                    value='#resourceInstance.fileLength.replaceAll(" ", "&nbsp;")'
                                                                    escapeXml="false"
                                                                    escapeHtml="false" escapeJavaScript="false"/>
                                                            </span>
                                                        </a>
                                                    </s:if>
                                                </s:iterator>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </s:iterator>
                    </div>
                    <div class="pager clear margin-more-top">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                    </div>
                </wpsa:subset>
            </s:form>
        </div>
    </div>

    <wp:ifauthorized permission="superuser">
        <s:if test="!onEditContent">
            <s:action name="openAdminProspect" namespace="/do/jacms/Resource/Admin" ignoreContextParams="true"
                      executeResult="true">
                <s:param name="resourceTypeCode" value="resourceTypeCode"></s:param>
            </s:action>
        </s:if>
    </wp:ifauthorized>
</div>

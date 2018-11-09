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
                <s:text name="breadcrumb.jacms.content.edit"/>
            </a>
        </li>
    </s:if>
    <s:else>
        <li>
            <s:text name="breadcrumb.digitalAsset"/>
        </li>
    </s:else>
    <li>
        <s:if test="onEditContent">
            <a href="<s:url action="findResource"><s:param name="resourceTypeCode" value="resourceTypeCode" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
            </a>
        </s:if>
        <s:else>
            <a href="<s:url action="list"><s:param name="resourceTypeCode" value="resourceTypeCode" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}"/>
            </a>
        </s:else>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.%{resourceTypeCode}.new"/>
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.%{resourceTypeCode}.edit"/>
        </s:elseif>
    </li>
</ol>
<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.%{resourceTypeCode}.new"/>
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.%{resourceTypeCode}.edit"/>
    </s:elseif>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-original-title=""
           data-content="<s:property value="%{getText('help.' + resourceTypeCode + '.' + strutsAction + '.info')}" escapeXml="true" />"
           data-placement="left">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<i class="fa fa-asterisk required-icon"></i>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields"/>
    </div>
</div>
<br/>
<s:set var="categoryTreeStyleVar">
    <wp:info key="systemParam" paramName="treeStyle_category"/>
</s:set>
<s:set var="lockGroupSelect" value="%{resourceId != null}"></s:set>
<s:form action="save" method="post" enctype="multipart/form-data" cssClass="form-horizontal image-upload-form">
    <wpsf:hidden name="fieldCount"/>
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/inc_fullErrors.jsp"/>
    <p class="sr-only">
        <wpsf:hidden name="strutsAction"/>
        <wpsf:hidden name="resourceTypeCode"/>
        <wpsf:hidden name="contentOnSessionMarker"/>
        <s:if test="strutsAction != 1">
            <wpsf:hidden name="resourceId"/>
        </s:if>
        <s:if test="%{lockGroupSelect}">
            <wpsf:hidden name="mainGroup"/>
        </s:if>
    </p>

    <%-- mainGroup --%>
    <s:set var="fieldErrorsVar" value="%{fieldErrors['mainGroup']}"/>
    <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()"/>
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}"/>
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <s:set var="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
        <label class="col-sm-2 control-label" for="mainGroup">
            <s:text name="label.group"/>
            <i class="fa fa-asterisk required-icon"></i>
        </label>
        <div class="col-sm-10">
            <wpsf:select name="mainGroup" id="mainGroup" list="allowedGroups" listKey="name" listValue="description"
                         disabled="%{lockGroupSelect}" cssClass="combobox form-control"></wpsf:select>
            <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}">
                    <s:property escapeHtml="false"/>
                    &#32;
                </s:iterator>
            </span>
            </s:if>
        </div>
    </div>

    <%-- upload --%>

    <s:set var="uploadFieldErrorsVar" value="%{fieldErrors['upload']}"/>
    <s:set var="fileNameFieldErrorsVar" value="%{fieldErrors['fileName']}"/>
    <s:set var="hasFieldErrorVar"
           value="(#uploadFieldErrorsVar != null && !#uploadFieldErrorsVar.isEmpty()) || (#fileNameFieldErrorsVar != null && !#fileNameFieldErrorsVar.isEmpty())"/>
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}"/>

    <fieldset class="margin-base-vertical" id="category-content-block">
        <div class="form-group<s:property value="controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label>
                    <s:text name="title.categoriesManagement"/>
                </label>
            </div>
            <div class="col-xs-10">
                <script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>
                <s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/category/categoryTree-extra.jsp"/>

                <s:set var="useAjax" value="true" />
                    <s:set var="selectedTreeNode" value="selectedNode"/>
                    <s:set var="currentRoot" value="categoryRoot"/>
                <s:set var="joinCategoryEndpoint" value="'joinCategory'"/>
                <s:set var="loadTreeActionName" value="''"/>
                        <s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'"/>
                        <s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'"/>
                <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/categoryTreeTable.jsp" />

                <s:if test="extraGroups.size() != 0">
                    <s:iterator value="extraGroups" var="groupName">
                        <wpsa:actionParam action="removeExtraGroup" var="actionName">
                            <wpsa:actionSubParam name="extraGroupName" value="%{#groupName}"/>
                        </wpsa:actionParam>
                        <div class="label label-default label-tag label-sm">
                            <s:property value="%{getSystemGroups()[#groupName].getDescr()}"/>
                            &#32;
                            <wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}"
                                         title="%{getText('label.remove')}" cssClass="btn btn-tag">
                                <span class="icon fa fa-times"></span>
                                <span class="sr-only">x</span>
                            </wpsf:submit>
                        </div>
                    </s:iterator>
                </s:if>

                <span id="categoryList">

                    <p class="sr-only">
                        <s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
                            <input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />"
                                   id="categoryCodes-<s:property value="#rowstatus.index" />"/>
                        </s:iterator>
                    </p>

                    <s:if test="%{categoryCodes != null && !categoryCodes.empty}">
                        <ul class="list-inline mt-20">
                            <s:iterator value="categoryCodes" var="categoryCodeVar">
                                <s:set var="resourceCategory" value="%{getCategory(#categoryCodeVar)}"></s:set>
                                    <li>
                                        <span class="label label-info">
                                            <span class="icon fa fa-tag"></span>
                                            &#32;
                                            <abbr title="<s:property value="#resourceCategory.getFullTitle(currentLang.code)"/>">
                                            <s:property value="#resourceCategory.getShortFullTitle(currentLang.code)"/>
                                        </abbr>
                                        &#32;
                                        <button type="button" class="btn btn-link"
                                                onclick="categoriesAjax.removeCategory('removeCategory', '<s:property value="#resourceCategory.code"/>')"
                                                title="<s:property value="%{getText('label.remove') + ' ' + #resourceCategory.defaultFullTitle}" />">
                                            <span class="pficon pficon-close white"></span>
                                            <span class="sr-only">x</span>
                                        </button>
                                    </span>
                                </li>
                            </s:iterator>
                        </ul>
                    </s:if>
                </span>

            </div>
        </div>
    </fieldset>
    <br>

    <s:if test="%{ (getStrutsAction() == 1 and !isOnEditContent()) or (getStrutsAction() == 1 and isContentListAttribute() and isOnEditContent())}">
        <div class="form-group">
            <div class="col-sm-5 col-sm-offset-2">
                <div id="add-resource-button">
                    <button type="button" id="add-fields"><span class="fa fa-plus-square-o"></span>
                        <s:text name="label.add-fileinput"/>
                    </button>
                </div>
            </div>
        </div>
    </s:if>

    <s:if test="%{!isOnEditContent() || (isOnEditContent() && isContentListAttribute())}">
        <s:set var="startIterationVar" value="0"/>
    </s:if>
    <s:if test="%{isOnEditContent() && !isContentListAttribute()}">
        <s:set var="startIterationVar" value="%{fieldCount}"/>
    </s:if>

    <div id="fields-container">
        <s:iterator begin="#startIterationVar" end="%{fieldCount}" status="ctr">

            <%-- FILE UPLOAD --%>

            <s:set var="fieldErrorsVar" value="%{fieldErrors['descr_' + (#ctr.count - 1)]}"/>
            <s:set var="fieldHasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()"/>
            <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
            <div class="form-group <s:property value="#controlGroupErrorClassVar" />">
                <label class="col-sm-2 control-label" for="descr">
                    <s:text name="label.description"/>
                    <i class="fa fa-asterisk required-icon"></i>
                </label>
                <div class="col-sm-4">
                    <s:if test="%{'' != getFileDescription(#ctr.count - 1)}"><s:set var="descriptionFieldVar"
                                                                                    value="%{getFileDescription(#ctr.count - 1)}"/></s:if>
                    <s:else>
                        <s:set var="paramNameVar" value="%{'descr_' + (#ctr.count - 1)}"/>
                        <s:set var="descriptionFieldVar" value="%{#parameters[#paramNameVar][0]}"/>
                    </s:else>
                    <wpsf:textfield name="descr_%{#ctr.count - 1}" maxlength="250" id="descr_%{#ctr.count - 1}"
                                    cssClass="form-control file-description"
                                    value="%{#descriptionFieldVar}"/>
                    <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="#fieldErrorsVar">
                        <s:property/>
                        &#32;
                    </s:iterator>
                </span>
                    </s:if>
                </div>

                <label class="col-sm-1 control-label" for="upload">
                    <s:text name="label.file"/>
                    <s:if test="%{resourceTypeCode == 'Image'}">
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
                           title=""
                           data-placement="top" data-content="<s:text name="title.resourceManagement.help" />"
                           data-original-title="" style="position: absolute; right: 8px;">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </s:if>
                    <s:elseif test="%{resourceTypeCode == 'Attach'}">
                        <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
                           title=""
                           data-placement="bottom" data-content="<s:text name="title.resourceAttach.help" />"
                           data-original-title="" style="position: absolute; right: 8px;">
                            <span class="fa fa-info-circle"></span>
                        </a>
                    </s:elseif>
                </label>

                <div class="col-sm-4">
                    <s:set var="fieldIdVar" value="%{#ctr.count -1}"/>
                    <s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
                        <s:label id="fileUpload_%{#ctr.count -1}_label" for="fileUpload_%{#ctr.count -1}"
                                 class="btn btn-default" key="label.button-choose-file"/>
                    </s:if>
                    <s:else>
                        <s:label id="fileUpload_%{#ctr.count -1}_label" for="fileUpload_%{#ctr.count -1}"
                                 class="btn btn-default" key="label.button-choose-files"/>
                    </s:else>

                    <s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
                        <s:file name="fileUpload" id="fileUpload_%{#ctr.count -1}" cssClass="input-file-button"/>
                    </s:if>
                    <s:else>
                        <s:file name="fileUpload" id="fileUpload_%{#ctr.count -1}" cssClass="input-file-button"
                                label="label.file"  multiple="true"/>
                    </s:else>

                    <span id="fileUpload_<s:property value="#fieldIdVar" />_selected">
                        <s:text name="label.no-file-selected"/>
                    </span>

                    <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                    <s:iterator value="%{#uploadFieldErrorsVar}">
                        <s:property escapeHtml="false"/>
                        &#32;
                    </s:iterator>
                    <s:iterator value="%{#fileNameFieldErrorsVar}">
                        <s:property escapeHtml="false"/>
                        &#32;
                    </s:iterator>
                    </span>
                    </s:if>
                </div>

                <s:if test="%{resourceTypeCode == 'Attach'}">

                    <button type="button" class="btn-danger delete-fields "
                            title="<s:text name="label.remove-fileinput" />"
                    ><span class="fa fa-times white"></span>
                    </button>
                </s:if>


                <s:if test="%{resourceTypeCode == 'Image'}">
                    <div class="col-sm-1">
                        <div class="list-view-pf-actions">
                            <div class="dropdown pull-right dropdown-kebab-pf">
                                <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="true">
                                    <span class="fa fa-ellipsis-v"></span>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight2">
                                    <li>
                                        <a href="#" class="edit-fields">Edit</a>
                                    </li>
                                    <li>
                                        <a href="#" class="delete-fields">Delete</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </s:if>
            </div>


        </s:iterator>
    </div>


    <div class="modal fade bs-cropping-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog modal-xlg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="<s:text name="cropEditor.button.close"/>">
                    <s:text name="cropEditor.button.close"/>
                    <span class="fa fa-times"></span>
                </button>
                <h4 class="modal-title"><s:text name="cropEditor.label.editImage"/> <span class="image-name"></span>
                </h4>
            </div>
            <div class="container-fluid no-padding">
                <div class="row">
                    <s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
                    <div class="col-md-8 col-md-offset-2">
                        </s:if>
                        <s:else>
                        <div class="col-md-8">
                            </s:else>
                            <!-- Tab panes -->
                            <div class="tab-content">
                                <!-- tab pane blue print -->
                                <div class="tab-pane hidden" id="tab-pane-blueprint">
                                    <div class="container-fluid">
                                        <div class="row">
                                            <div class="col-md-8">
                                                <div class="image-container">
                                                    <img src="" alt="" class="store_item_">
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="docs-preview clearfix">
                                                    <div class="img-preview preview-lg"><img
                                                            src="">
                                                    </div>
                                                    <div class="img-preview preview-md"><img
                                                            src="">
                                                    </div>
                                                    <div class="img-preview preview-sm"><img
                                                            src="">
                                                    </div>
                                                    <div class="img-preview preview-xs"><img
                                                            src="">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="docs-data">
                                                    <div class="field-group row">
                                                        <div class="col-md-6 col">
                                                            <span class="dimensions-label">
                                                              <label class="-text" for="dataX">X</label>
                                                            </span>
                                                            <input type="text" class="form-control dataX"
                                                                   placeholder="x" disabled>
                                                            <span class="text-append">
                                                              <span class="-text">px</span>
                                                            </span>
                                                        </div>
                                                        <div class="col-md-6 col">
                                                            <span class="dimensions-label">
                                                              <label class="-text" for="dataY">Y</label>
                                                            </span>
                                                            <input type="text" class="form-control dataY"
                                                                   placeholder="y" disabled>
                                                            <span class="text-append">
                                                              <span class="-text">px</span>
                                                            </span>
                                                        </div>
                                                    </div>
                                                    <div class="field-group row">
                                                        <div class="col-md-6 col">
                                                            <span class="dimensions-label">
                                                              <label class="-text" for="dataWidth">Width</label>
                                                            </span>
                                                            <input type="text" class="form-control dataWidth"
                                                                   placeholder="width" disabled>
                                                            <span class="text-append">
                                                              <span class="-text">px</span>
                                                            </span>
                                                        </div>
                                                        <div class="col-md-6 col">
                                                            <span class="dimensions-label">
                                                              <label class="-text" for="dataHeight">Height</label>
                                                            </span>
                                                            <input type="text" class="form-control dataHeight"
                                                                   placeholder="height" disabled>
                                                            <span class="text-append">
                                                            <span class="-text">px</span>
                                                            </span>
                                                        </div>
                                                    </div>


                                                    <div class="field-group row">
                                                        <div class="col-md-6 col">
                                                            <span class="dimensions-label">
                                                              <label class="-text"
                                                                     for="dataScaleX">ScaleX</label>
                                                            </span>
                                                            <input type="text" class="form-control dataScaleX"
                                                                   placeholder="scaleX" disabled>
                                                        </div>
                                                        <div class="col-md-6 col">
                                                            <span class="dimensions-label">
                                                              <label class="-text"
                                                                     for="dataScaleY">ScaleY</label>
                                                            </span>
                                                            <input type="text" class="form-control dataScaleY"
                                                                   placeholder="scaleY" disabled>
                                                        </div>
                                                    </div>
                                                    <div class="field-group field-group-full">
                                                        <span class="dimensions-label">
                                                          <label class="-text"
                                                                 for="dataRotate">Rotate</label>
                                                        </span>
                                                        <input type="text" class="form-control dataRotate"
                                                               placeholder="rotate" disabled>
                                                        <span class="text-append">
                                                          <span class="">deg</span>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row flex-container">
                                            <div class="col-md-8">
                                                <div class="toolbar-container flex-container space-between">
                                                    <!-- scale -->
                                                    <div class="btn-group flex-item">
                                                        <span class="btn-group__title"><s:text
                                                                name="cropEditor.label.flip"/></span>
                                                        <button type="button" class="btn btn-primary"
                                                                data-method="scaleX" data-option="-1"
                                                                title="Flip Horizontal">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.scaleX(-1)">
                                                      <span class="fa fa-arrows-h"></span>
                                                    </span>
                                                        </button>
                                                        <button type="button" class="btn btn-primary"
                                                                data-method="scaleY" data-option="-1"
                                                                title="Flip Vertical">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.scaleY(-1)">
                                                      <span class="fa fa-arrows-v"></span>
                                                    </span>
                                                        </button>
                                                    </div>
                                                    <div class="divider flex-item"></div>


                                                    <!-- move -->
                                                    <div class="btn-group flex-item">
                                                        <span class="btn-group__title"><s:text
                                                                name="cropEditor.label.move"/> </span>
                                                        <button type="button" class="btn btn-primary" data-method="move"
                                                                data-option="-10" data-second-option="0"
                                                                title="Move Left">
                                                <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                      data-original-title="cropper.move(-10, 0)">
                                                  <span class="fa fa-arrow-left"></span>
                                                </span>
                                                        </button>
                                                        <button type="button" class="btn btn-primary" data-method="move"
                                                                data-option="10" data-second-option="0"
                                                                title="Move Right">
                                                <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                      data-original-title="cropper.move(10, 0)">
                                                  <span class="fa fa-arrow-right"></span>
                                                </span>
                                                        </button>
                                                        <button type="button" class="btn btn-primary" data-method="move"
                                                                data-option="0" data-second-option="-10"
                                                                title="Move Up">
                                                <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                      data-original-title="cropper.move(0, -10)">
                                                  <span class="fa fa-arrow-up"></span>
                                                </span>
                                                        </button>
                                                        <button type="button" class="btn btn-primary" data-method="move"
                                                                data-option="0" data-second-option="10"
                                                                title="Move Down">
                                                <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                      data-original-title="cropper.move(0, 10)">
                                                  <span class="fa fa-arrow-down"></span>
                                                </span>
                                                        </button>
                                                    </div>
                                                    <div class="divider flex-item"></div>


                                                    <!-- rotate -->
                                                    <div class="btn-group flex-item">
                                                        <span class="btn-group__title"><s:text
                                                                name="cropEditor.label.rotate"/></span>
                                                        <button type="button" class="btn btn-primary"
                                                                data-method="rotate"
                                                                data-option="-45" title="Rotate Left">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.rotate(-45)">
                                                      <span class="fa fa-rotate-left"></span>
                                                    </span>
                                                        </button>
                                                        <button type="button" class="btn btn-primary"
                                                                data-method="rotate"
                                                                data-option="45" title="Rotate Right">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.rotate(45)">
                                                      <span class="fa fa-rotate-right"></span>
                                                    </span>
                                                        </button>
                                                    </div>
                                                    <div class="divider flex-item"></div>


                                                    <!-- zoom -->
                                                    <div class="btn-group flex-item">
                                                        <span class="btn-group__title"><s:text
                                                                name="cropEditor.label.zoom"/></span>
                                                        <button type="button" class="btn btn-primary" data-method="zoom"
                                                                data-option="0.1" title="Zoom In">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.zoom(0.1)">
                                                      <span class="fa fa-search-plus"></span>
                                                    </span>
                                                        </button>
                                                        <button type="button" class="btn btn-primary" data-method="zoom"
                                                                data-option="-0.1" title="Zoom Out">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.zoom(-0.1)">
                                                      <span class="fa fa-search-minus"></span>
                                                    </span>
                                                        </button>
                                                    </div>
                                                    <div class="divider flex-item"></div>


                                                    <!-- save and cancel -->
                                                    <div class="btn-group flex-item">
                                                        <span class="btn-group__title"><s:text
                                                                name="cropEditor.label.crop"/></span>
                                                        <button type="button" class="btn btn-primary" data-method="crop"
                                                                title="<s:text name="cropEditor.label.crop"/>">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.crop()">
                                                      <span class="fa fa-check"></span>
                                                    </span>
                                                        </button>
                                                    </div>
                                                    <div class="divider flex-item"></div>

                                                    <div class="btn-group flex-item">
                                                        <span class="btn-group__title"><s:text
                                                                name="cropEditor.label.cancel"/></span>
                                                        <button type="button" class="btn btn-primary"
                                                                data-method="remove"
                                                                title="Remove">
                                                    <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                          data-original-title="cropper.clear()">
                                                      <span class="fa fa-remove"></span>
                                                    </span>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 aspect-ratio-buttons-container ">
                                                <div class="aspect-ratio-buttons">
                                                    <div class="btn-group d-flex flex-nowrap" data-toggle="buttons">
                                                        <label class="btn btn-primary active"
                                                               data-method="setAspectRatio" data-option="NaN">
                                                            <input type="radio" class="sr-only" id="aspectRatio5"
                                                                   name="aspectRatio" value="NaN">
                                                            <span class="docs-tooltip" data-toggle="tooltip" title=""
                                                                  data-original-title="aspectRatio: NaN"><s:text
                                                                    name="cropEditor.label.free"/></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- /tab pane blue print -->
                            </div>
                        </div>

                        <s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
                        </s:if>
                        <s:else>
                            <div class="col-md-4">
                                <!-- Nav tabs -->
                                <ul class="nav nav-tabs tabs-left image-navigation">
                                    <li><s:text name="cropEditor.label.listOfImages"/></li>

                                </ul>
                                <!-- image navigation item blueprint -->
                                <li class="image-navigation-item__ hidden" id="image-navigation-item-blueprint">
                                    <a href="#first" data-toggle="tab">Blueprint</a></li>
                                <!-- /image navigation item blueprint -->
                            </div>
                        </s:else>


                    </div>
                </div>
            </div>
            <div id="aspect-ratio-values">
                <wp:info key="systemParam" paramName="aspect_ratio"/>
            </div>
        </div>
    </div>

    <script></script>

    <div class="form-horizontal">
        <div class="form-group">
            <div class="col-sm-12 margin-small-vertical">
                <button id="submit" type="submit" value="Submit" class="btn btn-primary pull-right">
                    <s:text name="cropEditor.label.done"/>
                </button>
            </div>
        </div>
    </div>
</s:form>

<s:if test="getStrutsAction() == 2 ">

    <div class="col-xs-12 no-padding">
        <h2><s:text name="title.metadata"/></h2>
    </div>
    <div class="col-xs-12 no-padding">
        <table class="table table-bordered table-hover table-striped">
            <thead>
            <tr>
                <th>
                    <s:text name="label.metadata.name"/>
                </th>
                <th>
                    <s:text name="label.metadata.value"/>
                </th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="metadata" var="metadataVar" status="rowstatus">
            <tr>
                <td><s:property value="#metadataVar.key"/></td>
                <td><s:property value="#metadataVar.value"/></td>
            </tr>
            </s:iterator>
            <tbody>
        </table>
    </div>

    <s:set var="resourceToShowVar" value="%{loadResource(resourceId)}"/>
    <span
            id="imageUrl"
            data-value="<s:property value="%{#resourceToShowVar.getImagePath(0)}"/>"
            class="hidden"
    >
    </span>
</s:if>

<div class="toast-pf alert alert-success alert-dismissable toast-crop-editor-success toast-success-blueprint hidden">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    <span class="toast-message"><s:text name="cropEditor.label.cropCreated"/> </span>
</div>

<%--(getStrutsAction() == 2 this comparision checks if current action is to edit already uploaded image--%>
<s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
    <span class="hidden singleImageUpload"></span>
</s:if>

<s:if test="%{resourceTypeCode == 'Image'}">
    <span class="hidden image_cropper_enabled"></span>
</s:if>

<s:if test="%{resourceTypeCode == 'Attach'}">
    <span class="hidden attachment_upload_enabled"></span>
</s:if>


<template id="hidden-fields-template">

    <div class="form-group">
        <label class="col-sm-2 control-label" for="descr">
            <s:text name="label.description"/>
            <i class="fa fa-asterisk required-icon"></i>
        </label>
        <div class="col-sm-4">
            <wpsf:textfield name="descr" maxlength="250" id="newDescr" cssClass="form-control file-description"/>
        </div>
        <div id="newFileUpload_box">
            <label class="col-sm-1 control-label" for="upload">
                <s:text name="label.file"/>
                <s:if test="%{resourceTypeCode == 'Image'}">
                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-placement="top" data-content="<s:text name="title.resourceManagement.help" />"
                       data-original-title="" style="position: absolute; right: 8px;">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </s:if>
                <s:elseif test="%{resourceTypeCode == 'Attach'}">
                    <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-placement="bottom" data-content="<s:text name="title.resourceAttach.help" />"
                       data-original-title="" style="position: absolute; right: 8px;">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </s:elseif>
            </label>

            <div class="col-sm-4">
                <label id="newFileUpload_label" for="newFileUpload" class="btn btn-default">
                    <s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
                        <s:text name="label.button-choose-file"/>
                    </s:if>
                    <s:else>
                        <s:text name="label.button-choose-files"/>
                    </s:else>
                </label>

                <s:if test="%{(getStrutsAction() == 2) or (isOnEditContent() && !isContentListAttribute())}">
                    <s:file name="fileUpload" id="newFileUpload" cssClass="input-file-button" label="label.file" />
                </s:if>
                <s:else>
                    <s:file name="fileUpload" id="newFileUpload" cssClass="input-file-button" label="label.file"
                            multiple="true"/>
                </s:else>
                <span id="newFileUpload_selected" class="file-upload-selected-name"><s:text name="label.no-file-selected"/></span>
            </div>
        </div>

        <s:if test="%{resourceTypeCode == 'Attach'}">
            <button type="button" class="btn-danger delete-fields "
                    title="<s:text name="label.remove-fileinput" />"
            ><span class="fa fa-times white"></span>
            </button>
        </s:if>

        <s:if test="%{resourceTypeCode == 'Image'}">

            <div class="col-sm-1">
                <div class="list-view-pf-actions">
                    <div class="dropdown pull-right dropdown-kebab-pf">
                        <button class="btn btn-menu-right dropdown-toggle" type="button" id="dropdownKebabRight2"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                            <span class="fa fa-ellipsis-v"></span>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight2">
                            <li>
                                <a href="#" class="edit-fields"><s:text name="cropEditor.label.edit"/></a>
                            </li>
                            <li>
                                <a href="#" class="delete-fields"><s:text name="cropEditor.label.delete"/></a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </s:if>
    </div>

</template>

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/resource/fileUploadFieldLabelI18n.jsp"/>

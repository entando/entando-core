<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.app" />
    </li>
    <li>
        <s:text name="breadcrumb.jacms" />
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
            <s:text name="breadcrumb.digitalAsset" />
        </li>
    </s:else>
    <li>
        <s:if test="onEditContent">
            <a href="<s:url action="findResource"><s:param name="resourceTypeCode" value="resourceTypeCode" /><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}" />
            </a>
        </s:if>
        <s:else>
            <a href="<s:url action="list"><s:param name="resourceTypeCode" value="resourceTypeCode" /></s:url>">
                <s:property value="%{getText('breadcrumb.dataAsset.' + resourceTypeCode + '.list')}" />
            </a>
        </s:else>
    </li>
    <li class="page-title-container">
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.%{resourceTypeCode}.new" />
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.%{resourceTypeCode}.edit" />
        </s:elseif>
    </li>
</ol>
<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.%{resourceTypeCode}.new" />
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.%{resourceTypeCode}.edit" />
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
        <s:text name="label.requiredFields" />
    </div>
</div>
<br />
<s:set var="categoryTreeStyleVar">
    <wp:info key="systemParam" paramName="treeStyle_category" />
</s:set>
<s:set var="lockGroupSelect" value="%{resourceId != null}"></s:set>
<s:form action="save" method="post" enctype="multipart/form-data" cssClass="form-horizontal">
    <wpsf:hidden name="fieldCount" />
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/inc_fullErrors.jsp" />
<p class="sr-only">
    <wpsf:hidden name="strutsAction" />
    <wpsf:hidden name="resourceTypeCode" />
    <wpsf:hidden name="contentOnSessionMarker" />
    <s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
        <input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />"
               id="categoryCodes-<s:property value="#rowstatus.index" />" />
    </s:iterator>
    <s:if test="strutsAction != 1">
        <wpsf:hidden name="resourceId" />
    </s:if>
    <s:if test="#categoryTreeStyleVar == 'request'">
        <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
            <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
        </s:iterator>
    </s:if>
    <s:if test="%{lockGroupSelect}">
        <wpsf:hidden name="mainGroup" />
    </s:if>
</p>

<%-- mainGroup --%>
<s:set var="fieldErrorsVar" value="%{fieldErrors['mainGroup']}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <s:set var="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
        <label class="col-sm-2 control-label" for="mainGroup">
        <s:text name="label.group" />
        <i class="fa fa-asterisk required-icon"></i>
    </label>
    <div class="col-sm-10">
        <wpsf:select name="mainGroup" id="mainGroup" list="allowedGroups" listKey="name" listValue="description"
                     disabled="%{lockGroupSelect}" cssClass="combobox form-control"></wpsf:select>
            <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}">
                    <s:property escapeHtml="false" />
                    &#32;
                </s:iterator>
            </span>
        </s:if>
    </div>
</div>

<%-- upload --%>

<s:set var="uploadFieldErrorsVar" value="%{fieldErrors['upload']}" />
<s:set var="fileNameFieldErrorsVar" value="%{fieldErrors['fileName']}" />
<s:set var="hasFieldErrorVar"
       value="(#uploadFieldErrorsVar != null && !#uploadFieldErrorsVar.isEmpty()) || (#fileNameFieldErrorsVar != null && !#fileNameFieldErrorsVar.isEmpty())" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

<fieldset class="margin-base-vertical" id="category-content-block">
    <div class="form-group<s:property value="controlGroupErrorClassVar" />">
        <div class="col-xs-2 control-label">
            <label>
                <s:text name="title.categoriesManagement" />
            </label>
        </div>
        <div class="col-xs-10">
            <script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>
            <s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/category/categoryTree-extra.jsp" />
            <table id="categoryTree" class="table table-bordered table-hover table-treegrid ${categoryTreeStyleVar}">
                <thead>
                    <tr>
                        <th>
                            <s:text name="label.category.tree" />
                            <s:if test="#categoryTreeStyleVar == 'classic'">
                                <button type="button" class="btn-no-button expand-button" id="expandAll">
                                    <i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>
                                    &#32;
                                    <s:text name="label.category.expandAll" />
                                </button>
                                <button type="button" class="btn-no-button" id="collapseAll">
                                    <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>
                                    &#32;
                                    <s:text name="label.category.collapseAll" />
                                </button>
                            </s:if>
                        </th>
                        <th class="text-center table-w-10">
                            <s:text name="label.category.join" />
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <s:set var="selectedTreeNode" value="selectedNode" />
                    <s:set var="currentRoot" value="categoryRoot" />
                    <s:set var="inputFieldName" value="'categoryCode'" />
                    <s:set var="selectedTreeNode" value="categoryCode" />
                    <s:set var="liClassName" value="'category'" />
                    <s:set var="treeItemIconName" value="'fa-folder'" />
                    <s:if test="%{#categoryTreeStyleVar == 'classic'}">
                        <s:set var="currentRoot" value="%{allowedTreeRootNode}" />
                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />
                    </s:if>
                    <s:elseif test="%{#categoryTreeStyleVar == 'request'}">
                        <s:set var="currentRoot" value="%{showableTree}" />
                        <s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
                        <s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
                    </s:elseif>
                </tbody>
            </table>
            <s:if test="extraGroups.size() != 0">
                <s:iterator value="extraGroups" var="groupName">
                    <wpsa:actionParam action="removeExtraGroup" var="actionName">
                        <wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
                    </wpsa:actionParam>
                    <div class="label label-default label-tag label-sm">
                        <s:property value="%{getSystemGroups()[#groupName].getDescr()}" />
                        &#32;
                        <wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}"
                                     title="%{getText('label.remove')}" cssClass="btn btn-tag">
                            <span class="icon fa fa-times"></span>
                            <span class="sr-only">x</span>
                        </wpsf:submit>
                    </div>
                </s:iterator>
            </s:if>
            <s:if test="%{categoryCodes != null && !categoryCodes.empty}">
                <ul class="list-inline mt-20">
                    <s:iterator value="categoryCodes" var="categoryCodeVar">
                        <s:set var="resourceCategory" value="%{getCategory(#categoryCodeVar)}"></s:set>
                        <li>
                            <span class="label label-info">
                                <span class="icon fa fa-tag"></span>
                                &#32;
                                <abbr title="<s:property value="#resourceCategory.getFullTitle(currentLang.code)"/>">
                                    <s:property value="#resourceCategory.getShortFullTitle(currentLang.code)" />
                                </abbr>
                                &#32;
                                <wpsa:actionParam action="removeCategory" var="actionName">
                                    <wpsa:actionSubParam name="categoryCode" value="%{#resourceCategory.code}" />
                                </wpsa:actionParam>
                                <wpsf:submit type="button" action="%{#actionName}"
                                             title="%{getText('label.remove') + ' ' + #resourceCategory.defaultFullTitle}"
                                             cssClass="btn btn-link">
                                    <span class="pficon pficon-close white"></span>
                                    <span class="sr-only">x</span>
                                </wpsf:submit>
                            </span>
                        </li>
                    </s:iterator>
                </ul>
            </s:if>
        </div>
    </div>
</fieldset>
<br>

<%-- ADD FILE BUTTON --%>     

<s:if test="not isOnEditContent()">
    <s:if test="getStrutsAction() == 1 ">

        <div class="form-group">
            <div class="col-sm-5 col-sm-offset-2">
                <div id="add-resource-button">
                    <button type="button" id="add-fields" >    <span class="fa fa-plus-square-o"></span> 
                        <s:text name="label.add-fileinput" />
                    </button>
                </div>
            </div>
        </div>

    </s:if>

    <s:iterator begin="0" end="%{fieldCount}" status="ctr">

        <%-- FILE UPLOAD --%> 

        <s:set var="fieldErrorsVar" value="%{fieldErrors['descr_' + (#ctr.count - 1)]}" />
        <s:set var="fieldHasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group <s:property value="#controlGroupErrorClassVar" />">
            <label class="col-sm-2 control-label" for="descr">
                <s:text name="label.description" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-4">
                <s:if test="%{'' != getFileDescription(#ctr.count - 1)}" ><s:set var="descriptionFieldVar" value="%{getFileDescription(#ctr.count - 1)}" /></s:if>
                <s:else>
                    <s:set var="paramNameVar" value="%{'descr_' + (#ctr.count - 1)}" />
                    <s:set var="descriptionFieldVar" value="%{#parameters[#paramNameVar][0]}" />
                </s:else>
                <wpsf:textfield name="descr_%{#ctr.count - 1}" maxlength="250" id="descr_%{#ctr.count - 1}" cssClass="form-control file-description" value="%{#descriptionFieldVar}" />
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="#fieldErrorsVar">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>

            <label class="col-sm-1 control-label" for="upload">
                <s:text name="label.file" />
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
                <s:set var="fieldIdVar" value="%{#ctr.count -1}" />
                <s:label id="fileUpload_%{#ctr.count -1}_label" for="fileUpload_%{#ctr.count -1}" class="btn btn-default" key="label.button-choose-file" />
                <s:file name="fileUpload" id="fileUpload_%{#ctr.count -1}" cssClass="input-file-button" label="label.file" />
                <span id="fileUpload_<s:property value="#fieldIdVar" />_selected" >
                    <s:text name="label.no-file-selected" />
                </span>

                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#uploadFieldErrorsVar}">
                            <s:property escapeHtml="false" />
                            &#32;
                        </s:iterator>
                        <s:iterator value="%{#fileNameFieldErrorsVar}">
                            <s:property escapeHtml="false" />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>             
            </div> 

            <s:if test="#ctr.count -1 > 0 ">
                <button type="button" class="btn-danger delete-fields " 
                        title="<s:text name="label.remove-fileinput" />"
                        >    <span class="fa fa-times white"></span> 
                </button>
            </s:if>
        </div>


    </s:iterator>

    <div id="fields-container" >
    </div>

</s:if>


<s:if test="isOnEditContent()">

    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <%-- descr --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['descr']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <label class="col-sm-2 control-label" for="descr">
            <s:text name="label.description" />
            <i class="fa fa-asterisk required-icon"></i>
        </label>
        <div class="col-sm-10">
            <wpsf:textfield name="descr" maxlength="250" id="descr" cssClass="form-control" />
            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>

    <%-- upload --%>
    <s:set var="uploadFieldErrorsVar" value="%{fieldErrors['upload']}" />
    <s:set var="fileNameFieldErrorsVar" value="%{fieldErrors['fileName']}" />
    <s:set var="hasFieldErrorVar"
           value="(#uploadFieldErrorsVar != null && !#uploadFieldErrorsVar.isEmpty()) || (#fileNameFieldErrorsVar != null && !#fileNameFieldErrorsVar.isEmpty())" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
        <label class="col-sm-2 control-label" for="upload">
            <s:text name="label.file" />
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
        <div class="col-sm-10">

            <label id="upload_label" for="upload" class="btn btn-default" >
                <s:text name="label.button-choose-file" /></label>
                <s:file name="upload" id="upload" cssClass="input-file-button" label="label.file" />
            <span id="upload_selected"><s:text name="label.no-file-selected" />
            </span>

            <s:if test="#hasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#uploadFieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                    <s:iterator value="%{#fileNameFieldErrorsVar}">
                        <s:property escapeHtml="false" />
                        &#32;
                    </s:iterator>
                </span>
            </s:if>
        </div>
    </div>

</s:if>

<div class="form-horizontal">
    <div class="form-group">
        <div class="col-sm-12 margin-small-vertical">
            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>
</div>
</s:form>

<s:if test="getStrutsAction() == 2 ">

    <div class="col-xs-12 no-padding">
        <h2> <s:text name="title.metadata" /> </h2>
    </div>
    <div class="col-xs-12 no-padding">
        <table class="table table-bordered table-hover table-striped">
            <thead>
                <tr>
                    <th>
                        <s:text name="label.metadata.name" />
                    </th>
                    <th>
                        <s:text name="label.metadata.value" />
                    </th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="metadata" var="metadataVar" status="rowstatus">
                    <tr>
                        <td><s:property value="#metadataVar.key" /></td> 
                        <td><s:property value="#metadataVar.value" /></td>
                    </tr>
                </s:iterator>      
            <tbody>
        </table>
    </div>
</s:if>

<template id="hidden-fields-template">

    <div class="form-group">
        <label class="col-sm-2 control-label" for="descr">
            <s:text name="label.description" />
            <i class="fa fa-asterisk required-icon"></i>
        </label>
        <div class="col-sm-4">
            <wpsf:textfield name="descr" maxlength="250" id="newDescr" cssClass="form-control file-description" />            
        </div>
        <label class="col-sm-1 control-label" for="upload">
            <s:text name="label.file" />
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
            <label id="newFileUpload_label" for="newFileUpload" class="btn btn-default" >
                <s:text name="label.button-choose-file" /></label>
                <s:file name="fileUpload" id="newFileUpload" cssClass="input-file-button" label="label.file" />
            <span id="newFileUpload_selected"><s:text name="label.no-file-selected" />
            </span>
        </div> 


        <button type="button" class="btn-danger delete-fields " 
                title="<s:text name="label.remove-fileinput" />"
                >    <span class="fa fa-times white"></span> 
        </button>


    </div>

</template>

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/resource/fileUploadAddFields.jsp" />
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/resource/fileUploadFieldLabelI18n.jsp" />


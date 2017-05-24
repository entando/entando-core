<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%-- radios + checkboxes only --%>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core"%>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li class="page-title-container"><s:text name="breadcrumb.jacms.content.list" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}" />
<h1 class="page-title-container">
    <s:text name="title.contentList" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="label.contents.section.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<!-- Default separator -->
<div class="text-right">
    <div class="form-group-separator"></div>
</div>

<div id="main" role="main" class="mt-20">
    <wpsa:entityTypes entityManagerName="jacmsContentManager"
                      var="contentTypesVar" />
    <div class="col-xs-12  ">
        <s:url action="search" var="formAction" namespace="do/jacms/Content" />

        <s:form action="%{'/' + #formAction}" cssClass="form-horizontal"
                role="search">
            <s:set var="categoryTreeStyleVar">
                <wp:info key="systemParam" paramName="treeStyle_category" />
            </s:set>
            <p class="sr-only">
                <wpsf:hidden name="lastGroupBy" />
                <wpsf:hidden name="lastOrder" />
            </p>
            <div class="searchPanel form-group">
                <div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10">
                    <p class="search-label">
                        <s:text name="label.search.label" />
                    </p>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="text" class="sr-only"><s:text
                                name="label.description" /></label>
                        <div class="col-sm-9">
                            <wpsf:textfield name="text" id="text" cssClass="form-control" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="contentIdToken" class="control-label col-sm-2 text-right"><s:text name="label.code" />
                        </label>
                        <div class="col-sm-9">
                            <wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" placeholder="CNG12" />
                        </div>
                    </div>
                    <br>
                    <br>

                    <!-------------Advanced Search----------->
                    <div class="panel-group advanced-search" id="accordion-markup">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <p class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne"><s:text name="label.search.advanced"/></a>
                                </p>
                            </div>
                            <div id="collapseOne" class="panel-collapse collapse">
                                <div class="panel-body">

                                    <div id="search-advanced"
                                         class="collapse-input-group <s:if test="(#categoryTreeStyleVar == 'request' && null != treeNodeActionMarkerCode)">in</s:if>">
                                             <div class="form-group">
                                                 <label for="contentType" class="control-label col-sm-2 text-right"><s:text name="label.type" />
                                             </label>
                                             <div class="col-sm-9 input-group input-20px-leftRight">
                                                 <wpsf:select cssClass="form-control" name="contentType" id="contentType" list="contentTypes" listKey="code"
                                                              listValue="description" headerKey="" headerValue="%{getText('label.all')}" />
                                                 <div class="input-group-btn">
                                                     <wpsf:submit cssClass="btn btn-primary" value="%{getText('label.set')}" />
                                                 </div>
                                             </div>
                                         </div>
                                         <s:set var="searchableAttributes" value="searchableAttributes" />
                                         <s:if test="null != #searchableAttributes && #searchableAttributes.size() > 0">

                                             <s:iterator var="attribute" value="#searchableAttributes">
                                                 <s:set var="currentFieldId"
                                                        value="%{'entityFinding_'+#attribute.name}" />
                                                 <s:if test="#attribute.textAttribute">
                                                     <div class="form-group">
                                                         <s:set var="textInputFieldName">
                                                             <s:property value="#attribute.name" />_textFieldName</s:set>
                                                         <label class="control-label col-sm-2" for="<s:property value="currentFieldId" />" class="control-label col-sm-3 text-right"><s:property
                                                                 value="#attribute.name" />
                                                         </label>
                                                         <div class="col-sm-9">
                                                             <wpsf:textfield id="%{currentFieldId}"  name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}"
                                                                             cssClass="form-control" />
                                                         </div>
                                                     </div>
                                                 </s:if>
                                                 <s:elseif test="#attribute.type == 'Date'">
                                                     <s:set var="dateStartInputFieldName">
                                                         <s:property value="#attribute.name" />_dateStartFieldName</s:set>
                                                     <s:set var="dateEndInputFieldName">
                                                         <s:property value="#attribute.name" />_dateEndFieldName</s:set>

                                                         <div class="form-group">
                                                             <label class="control-label col-sm-2"
                                                                    for="<s:property value="%{currentFieldId}" />_dateStartFieldName_cal"
                                                             class="control-label col-sm-9 text-right"><s:text
                                                                 name="note.range.from.attribute" />&#32;<s:property
                                                                 value="#attribute.name" /></label>
                                                         <div class="col-sm-9">
                                                             <wpsf:textfield
                                                                 id="%{currentFieldId}_dateStartFieldName_cal"
                                                                 name="%{#dateStartInputFieldName}"
                                                                 value="%{getSearchFormFieldValue(#dateStartInputFieldName)}"
                                                                 cssClass="form-control bootstrap-datepicker"
                                                                 placeholder="dd/mm/yyyy" />
                                                         </div>
                                                     </div>
                                                     <div class="form-group">
                                                         <label class="control-label col-sm-2"
                                                                for="<s:property value="%{currentFieldId}" />_dateEndFieldName_cal"
                                                                class="control-label col-sm-9 text-right"><s:text
                                                                 name="note.range.to.attribute" />&#32;<s:property
                                                                 value="#attribute.name" /></label>
                                                         <div class="col-sm-9">
                                                             <wpsf:textfield
                                                                 id="%{currentFieldId}_dateEndFieldName_cal"
                                                                 name="%{#dateEndInputFieldName}"
                                                                 value="%{getSearchFormFieldValue(#dateEndInputFieldName)}"
                                                                 cssClass="form-control bootstrap-datepicker"
                                                                 placeholder="dd/mm/yyyy" />
                                                         </div>
                                                     </div>
                                                 </s:elseif>
                                                 <s:elseif test="#attribute.type == 'Number'">
                                                     <s:set var="numberStartInputFieldName">
                                                         <s:property value="#attribute.name" />_numberStartFieldName</s:set>
                                                     <s:set var="numberEndInputFieldName">
                                                         <s:property value="#attribute.name" />_numberEndFieldName</s:set>
                                                         <div class="form-group">
                                                             <label class="control-label col-sm-2"
                                                                    for="<s:property value="currentFieldId" />_start"><s:text
                                                                 name="note.range.from.attribute" />&#32;<s:property
                                                                 value="#attribute.name" />:</label>
                                                         <div class="col-sm-9">
                                                             <wpsf:textfield id="%{currentFieldId}_start"
                                                                             name="%{#numberStartInputFieldName}"
                                                                             value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"
                                                                             cssClass="form-control" />
                                                         </div>
                                                     </div>
                                                     <div class="form-group">
                                                         <label class="control-label col-sm-2"
                                                                for="<s:property value="currentFieldId" />_end"><s:text
                                                                 name="note.range.to.attribute" />&#32;<s:property
                                                                 value="#attribute.name" />:</label>
                                                         <div class="col-sm-9">
                                                             <wpsf:textfield id="%{currentFieldId}_end"
                                                                             name="%{#numberEndInputFieldName}"
                                                                             value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"
                                                                             cssClass="form-control" />
                                                         </div>
                                                     </div>

                                                 </s:elseif>
                                                 <s:elseif
                                                     test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                                                     <p>
                                                         <span class="important"><s:property
                                                                 value="#attribute.name" /></span><br />
                                                     </p>
                                                     <s:set var="booleanInputFieldName">
                                                         <s:property value="#attribute.name" />_booleanFieldName</s:set>
                                                     <s:set var="booleanInputFieldValue">
                                                         <s:property
                                                             value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
                                                     </s:set>
                                                     <ul class="noBullet radiocheck">
                                                         <li><wpsf:radio id="none_%{#booleanInputFieldName}"
                                                                     name="%{#booleanInputFieldName}" value=""
                                                                     checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" /><label
                                                                 for="none_<s:property value="#booleanInputFieldName" />"
                                                                 class="normal"><s:text name="label.bothYesAndNo" /></label></li>
                                                         <li><wpsf:radio id="true_%{#booleanInputFieldName}"
                                                                     name="%{#booleanInputFieldName}" value="true"
                                                                     checked="%{#booleanInputFieldValue == 'true'}" /><label
                                                                 for="true_<s:property value="#booleanInputFieldName" />"
                                                                 class="normal"><s:text name="label.yes" /></label></li>
                                                         <li><wpsf:radio id="false_%{#booleanInputFieldName}"
                                                                     name="%{#booleanInputFieldName}" value="false"
                                                                     checked="%{#booleanInputFieldValue == 'false'}" /><label
                                                                 for="false_<s:property value="#booleanInputFieldName" />"
                                                                 class="normal"><s:text name="label.no" /></label></li>
                                                     </ul>
                                                 </s:elseif>
                                             </s:iterator>
                                         </s:if>
                                         <div class="form-group">
                                             <label for="contentType"
                                                    class="control-label col-sm-2 text-right"> <s:text
                                                     name="label.category" />
                                             </label>
                                             <div class="col-sm-9">
                                                 <s:action name="showCategoryTreeOnContentFinding"
                                                           namespace="/do/jacms/Content" ignoreContextParams="true"
                                                           executeResult="true"></s:action>
                                                 </div>
                                             </div>
                                         </div>
                                    <s:set var="allowedGroupsVar" value="allowedGroups" />
                                    <s:if
                                        test="null != #allowedGroupsVar && #allowedGroupsVar.size()>1">
                                        <div class="form-group">
                                            <label for="ownerGroupName"
                                                   class="control-label col-sm-2 text-right"><s:text
                                                    name="label.group" /></label>
                                            <div class="col-sm-9">
                                                <wpsf:select name="ownerGroupName" id="ownerGroupName"
                                                             list="#allowedGroupsVar" headerKey=""
                                                             headerValue="%{getText('label.all')}" listKey="name"
                                                             listValue="descr" cssClass="form-control" />
                                            </div>
                                        </div>
                                    </s:if>
                                    <div class="form-group">
                                        <label for="state" class="control-label col-sm-2 text-right"><s:text
                                                name="label.state" /></label>
                                        <div class="col-sm-9">
                                            <wpsf:select name="state" id="state" list="avalaibleStatus" headerKey="" headerValue="%{getText('label.all')}" listKey="key" listValue="%{getText(value)}" cssClass="form-control" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="btn-group col-sm-9 col-sm-offset-2"
                                             data-toggle="buttons">
                                            <label class="btn btn-default <s:if test="('yes' == onLineState)"> active </s:if>">
                                                    <input type="radio" name="onLineState" id="approved"
                                                    <s:if test="('yes' == onLineState)">checked="checked"</s:if>
                                                    value="yes" />&#32; <s:text name="name.isApprovedContent" />
                                            </label> <label class="btn btn-default <s:if test="('no' == onLineState)"> active </s:if>">
                                                    <input type="radio" name="onLineState" id="notApproved"
                                                    <s:if test="('no' == onLineState)">checked="checked"</s:if>
                                                    value="no" />&#32; <s:text name="name.isNotApprovedContent" />
                                            </label> <label class="btn btn-default <s:if test="('yes' != onLineState) && ('no' != onLineState)"> active </s:if>">
                                                    <input type="radio" name="onLineState" id="bothApproved"
                                                    <s:if test="('yes' != onLineState) && ('no' != onLineState)">checked="checked"</s:if>
                                                    value="" />&#32; <s:text name="name.isApprovedOrNotContent" />
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--// search-advanced --%>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                                    <s:text name="label.search" />
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- New Content Button -->
        <div class="col-xs-12">
            <div class="btn-group pull-right mt-20">
                <button type="button" class="btn btn-primary dropdown-toggle"
                        data-toggle="dropdown">

                    <s:text name="label.add" />
                    &#32; <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <s:iterator var="contentTypeVar" value="#contentTypesVar">
                        <jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}"
                                               property="isAuthToEdit" var="isAuthToEditVar" />
                        <s:if test="%{#isAuthToEditVar}">
                            <li><a
                                    href="<s:url action="createNew" namespace="/do/jacms/Content" >
                                        <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                                    </s:url>"><s:text
                                        name="label.new" />&#32;<s:property
                                        value="%{#contentTypeVar.typeDescr}" /></a></li>
                                </s:if>
                            </s:iterator>
                </ul>
            </div>
        </div>

    </s:form>

    <div class="col-xs-12 mt-20">
        <s:form action="search" cssClass="form-horizontal">
            <p class="sr-only">
                <wpsf:hidden name="text" />
                <wpsf:hidden name="contentType" />
                <wpsf:hidden name="state" />
                <wpsf:hidden name="onLineState" />
                <wpsf:hidden name="categoryCode" />
                <wpsf:hidden name="viewTypeDescr" />
                <wpsf:hidden name="viewGroup" />
                <wpsf:hidden name="viewCode" />
                <wpsf:hidden name="viewStatus" />
                <wpsf:hidden name="viewCreationDate" />
                <wpsf:hidden name="lastGroupBy" />
                <wpsf:hidden name="lastOrder" />
                <wpsf:hidden name="contentIdToken" />
                <wpsf:hidden name="ownerGroupName" />
                <s:iterator var="attribute" value="#searchableAttributes">
                    <s:if test="#attribute.textAttribute">
                        <s:set var="textInputFieldName">
                            <s:property value="#attribute.name" />_textFieldName</s:set>
                        <wpsf:hidden name="%{#textInputFieldName}"
                                     value="%{getSearchFormFieldValue(#textInputFieldName)}" />
                    </s:if>
                    <s:elseif test="#attribute.type == 'Date'">
                        <s:set var="dateStartInputFieldName">
                            <s:property value="#attribute.name" />_dateStartFieldName</s:set>
                        <s:set var="dateEndInputFieldName">
                            <s:property value="#attribute.name" />_dateEndFieldName</s:set>
                        <wpsf:hidden name="%{#dateStartInputFieldName}"
                                     value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
                        <wpsf:hidden name="%{#dateEndInputFieldName}"
                                     value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
                    </s:elseif>
                    <s:elseif test="#attribute.type == 'Number'">
                        <s:set var="numberStartInputFieldName">
                            <s:property value="#attribute.name" />_numberStartFieldName</s:set>
                        <s:set var="numberEndInputFieldName">
                            <s:property value="#attribute.name" />_numberEndFieldName</s:set>
                        <wpsf:hidden name="%{#numberStartInputFieldName}"
                                     value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
                        <wpsf:hidden name="%{#numberEndInputFieldName}"
                                     value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
                    </s:elseif>
                    <s:elseif
                        test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                        <s:set var="booleanInputFieldName">
                            <s:property value="#attribute.name" />_booleanFieldName</s:set>
                        <wpsf:hidden name="%{#booleanInputFieldName}"
                                     value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
                    </s:elseif>
                </s:iterator>
            </p>

            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"
                            aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="message.title.ActionErrors" /></strong>
                    <ul class="margin-base-top">
                        <s:iterator value="ActionErrors">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"
                            aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="messages.confirm" /></strong>
                    <ul class="margin-base-top">
                        <s:iterator value="actionMessages">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>

            <s:set var="contentIdsVar" value="contents" />

            <s:if test="%{#contentIdsVar.size() > 0}">

                <!-- Content List -->
                <wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
                    <s:set var="group" value="#groupContent" />


                    <!-- Toolbar -->
                    <div class="col-xs-12 no-padding" id="content-list-toolbar">
                        <div class="row toolbar-pf table-view-pf-toolbar border-bottom">
                            <div class="col-xs-12">

                                <!-- toolbar first row  -->
                                <div class="toolbar-pf-actions">
                                    <!-- items selected -->
                                    <div class="col-xs-6 no-padding">
                                        <div class="selected-items">
                                            <span class="selected-items-counter">0</span> <s:text name="title.itemSelected" />
                                        </div>
                                    </div>

                                    <!-- toolbar -->
                                    <div class="col-xs-6 no-padding">
                                        <label class="col-xs-5 control-label">
                                            <s:text name="label.setAs" />
                                        </label>

                                        <div class="col-xs-7 no-padding">
                                            <div class="btn-toolbar">
                                                <wp:ifauthorized permission="validateContents">
                                                    <div class="btn-group">
                                                        <wpsf:submit action="bulkPutOnline" type="button" title="%{getText('note.button.approve')}"
                                                                     cssClass="btn btn-success"><s:text name="label.approve" />
                                                        </wpsf:submit>
                                                        <wpsf:submit action="bulkPutOffline" type="button" title="%{getText('note.button.suspend')}"
                                                                     cssClass="btn btn-default"><s:text name="label.suspend" />
                                                        </wpsf:submit>
                                                    </div>
                                                </wp:ifauthorized>

                                                <wpsa:hookPoint
                                                    key="jacms.contentFinding.allContents.actions"
                                                    objectName="hookpoint_contentFinding_allContents">
                                                    <div class="btn-group">
                                                        <s:iterator value="#hookpoint_contentFinding_allContents"
                                                                    var="hookPointElement">
                                                            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                                        </s:iterator>
                                                    </div>
                                                </wpsa:hookPoint>

                                                <div class="btn-group pull-right">
                                                    <wpsf:submit action="bulkRemove" type="button"
                                                                 title="%{getText('note.button.delete')}"
                                                                 cssClass="btn btn-danger">
                                                        <s:text name="label.remove" />
                                                    </wpsf:submit>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- toolbar second row -->
                                <div class="row toolbar-pf-results">

                                    <div class="col-xs-6 no-padding">
                                    </div>

                                    <div class="col-xs-6 no-padding">
                                        <label class="col-xs-5 control-label">
                                            <s:text name="label.actions" />
                                        </label>

                                        <div class="col-xs-7 no-padding">
                                            <div class="btn-toolbar">
                                                <div class="dropdown" style="margin:10px 0 0px -4px;">
                                                    <button type="button" class="btn btn-default dropdown-toggle w100perc text-right" id="bulkAction" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                        <span class="pull-left"><s:text name="label.selectAction"/></span><span class="caret"></span>
                                                    </button>
                                                    <ol class="dropdown-menu w100perc" aria-labelledby="bulkAction" >
                                                        <li>
                                                            <wpsa:actionParam action="bulkOnCategories" var="bulkActionName">
                                                                <wpsa:actionSubParam name="strutsAction" value="1" />
                                                            </wpsa:actionParam>
                                                            <wpsf:submit action="%{#bulkActionName}" type="button" title="%{getText('note.button.addCategories')}" cssClass="btn btn-success">
                                                                <span class="icon fa"></span><s:text name="label.addCategories" />
                                                            </wpsf:submit></li>
                                                        <li>
                                                            <wpsa:actionParam action="bulkOnCategories"  var="bulkActionName">
                                                                <wpsa:actionSubParam name="strutsAction" value="4" />
                                                            </wpsa:actionParam>
                                                            <wpsf:submit action="%{#bulkActionName}" type="button" title="%{getText('note.button.removeCategories')}" cssClass="btn btn-success">
                                                                <span class="icon fa"></span>
                                                                <s:text name="label.removeCategories" />
                                                            </wpsf:submit>
                                                        </li>
                                                    </ol>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Content List - Table -->
                    <div class="col-xs-12 no-padding">
                        <div class="alert alert-warning hidden selectall-box no-mb mt-20">
                            <span class="pficon pficon-warning-triangle-o"></span>
                            <label for="allContentsSelected" class="control-label mr-10">
                                <s:text name="label.allContentsSelected"/>
                            </label>
                            <wpsf:checkbox name="allContentsSelected" id="allContentsSelected" cssClass="bootstrap-switch" value="false"/>
                        </div>
                    </div>

                    <div class="col-xs-12 no-padding">
                        <div class="mt-20">
                            <table class="table table-striped table-bordered table-hover content-list" id="contentListTable">
                                <thead>
                                    <tr>
                                        <th class="text-center">
                                            <label class="sr-only" for="selectAll"><s:text name="label.selectAll" /></label>
                                            <input type="checkbox" class="js_selectAll">
                                        </th>
                                        <th>
                                            <a href="<s:url action="changeOrder" includeParams="all" >
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder" ><s:property value="lastOrder" /></s:param>
                                                   <s:param name="groupBy">descr</s:param>
                                                   <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                               </s:url>">
                                                <s:text name="label.description" /></a>
                                        </th>
                                        <th class="table-w-10"><s:text name="label.author"/></th>
                                        <th class="table-w-10">
                                            <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                                   <s:param name="groupBy">code</s:param>
                                                   <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                               </s:url>"><s:text name="label.code" />
                                            </a>
                                        </th>
                                        <th><s:text name="label.type" /></th>
                                        <th class="text-center cell-w100"><s:text name="label.state" /></th>
                                        <th class="text-center cell-w100"><s:text name="label.visible"/></th>
                                        <th><s:text name="label.group" /></th>
                                        <th><a  href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                                    <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                    <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                                    <s:param name="groupBy">created</s:param>
                                                    <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                                </s:url>"><s:text name="label.creationDate" /></a>
                                        </th>
                                        <th><a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                                                   <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                   <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                                   <s:param name="groupBy">lastModified</s:param>
                                                   <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                               </s:url>"><s:text name="label.lastEdit" />
                                            </a>
                                        </th>
                                        <th class="text-center table-w-5"><s:text name="label.actions" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="contentId">
                                        <s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
                                            <tr>
                                                <td class="text-center">
                                                    <input type="checkbox" name="contentIds" id="content_<s:property value="#content.id" />"
                                                       value="<s:property value="#content.id" />" />
                                            </td>
                                            <td>
                                                <s:property value="#content.descr" />&#32;
                                            </td>
                                            <td>
                                                <s:if test="%{#content.lastEditor != null && #content.lastEditor != ''}">
                                                    <s:property value="#content.lastEditor"/>
                                                </s:if>
                                                <s:elseif test="%{#content.firstEditor != null && #content.firstEditor != ''}">
                                                    <s:property value="#content.firstEditor"/>
                                                </s:elseif>
                                            </td>
                                            <td >
                                                <s:property value="#content.id" />
                                            </td>
                                            <td>
                                                <s:property value="%{getSmallContentType(#content.typeCode).descr}" />
                                            </td>
                                            <td class="text-center">
                                                <s:if test="%{#content.onLine && #content.sync}">
                                                    <s:set value="%{getText('name.contentStatus.' + #content.status)}" var="statusLabel" />
                                                    <span class="fa fa-circle green" aria-hidden="true" title="${statusLabel}"></span>
                                                </s:if>
                                                <s:elseif test="%{#content.onLine && !(#content.sync)}">
                                                    <s:set var="statusLabel"><s:property value="%{getText('name.contentStatus.' + 'PUBLIC')}" />&#32;&ne;&#32;<s:property value="%{getText('name.contentStatus.' + 'DRAFT')}" />
                                                    </s:set>
                                                    <span class="fa fa-circle yellow" aria-hidden="true" title="${statusLabel}"></span>
                                                </s:elseif>
                                                <s:else>
                                                    <s:set var="statusLabel" value="%{getText('name.contentStatus.' + 'OFFLINE')}" />
                                                    <span class="fa fa-circle gray" aria-hidden="true" title="${statusLabel}"></span>
                                                </s:else>
                                            </td>
                                            <td class="text-center">
                                                <s:if test="%{#content.mainGroupCode != null && !#content.mainGroupCode.equals('free')}">
                                                    <span class="icon fa fa-lock"></span>
                                                </s:if>
                                                <s:else>
                                                    <span class="icon fa fa-unlock"></span>
                                                </s:else>
                                            </td>
                                            <td ><s:property value="%{getGroup(#content.mainGroupCode).descr}" /></td>
                                            <td class="table-w-10"><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></td>
                                            <td class="table-w-10"><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></td>
                                            <td class="table-view-pf-actions">
                                                <div class="dropdown dropdown-kebab-pf">
                                                    <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown">
                                                        <span class="fa fa-ellipsis-v"></span>
                                                    </button>
                                                    <ul class="dropdown-menu dropdown-menu-right">
                                                        <li>
                                                            <a title="<s:text name="label.copyPaste" />: <s:property value="#content.id" /> - <s:property value="#content.description" />"
                                                               href="<s:url action="copyPaste" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="copyPublicVersion" value="'false'" /></s:url>">
                                                                <s:text name="label.copyPaste" /><span class="sr-only">:
                                                                    <s:property value="#content.id" /> - <s:property  value="#content.descr" />
                                                                </span>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a title="<s:text name="label.inspect" />: [<s:text name="name.work" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />"
                                                               href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'false'" /></s:url>">
                                                                <s:text name="name.version.work" />
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a title="<s:text name="label.inspect" />: [<s:text name="name.onLine" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />"
                                                               href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'true'" /></s:url>">
                                                                <s:text name="name.version.onLine" />
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a  title="<s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" />"
                                                                href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /></s:url>">
                                                                <s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" /><span class="sr-only">
                                                                    <s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" /></span>
                                                            </a>
                                                        </li>
                                                        <wpsa:hookPoint key="jacms.contentFinding.contentRow.actions" objectName="hookpoint_contentFinding_contentRow">
                                                            <s:iterator value="#hookpoint_contentFinding_contentRow" var="hookPointElement">
                                                                <li>
                                                                    <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                                                    </li>
                                                            </s:iterator>
                                                        </wpsa:hookPoint>
                                                    </ul>
                                                </div>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="content-view-pf-pagination table-view-pf-pagination clearfix mt-20 mb-20">
                        <div class="form-group">
                            <span><s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                            <div class="mt-5">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
            </s:if>
            <s:else>
                <div class="alert alert-info">
                    <span class="pficon pficon-info"></span>
                    <strong><s:text name="label.listEmpty" /></strong>&#32;<s:text name="label.noContentFound" />
                </div>
            </s:else>
        </div>

    </s:form>
</div>

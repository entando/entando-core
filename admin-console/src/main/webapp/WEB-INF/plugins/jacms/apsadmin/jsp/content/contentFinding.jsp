<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%-- radios + checkboxes only --%>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jacms.menu.contentAdmin" /></li>
<li class="page-title-container"><s:text name="title.contentList" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.contentList" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"><s:text name="label.requiredFields" /></div>
</div>
<br>

<div id="main" role="main">
    <wpsa:entityTypes entityManagerName="jacmsContentManager" var="contentTypesVar" />
    <div class="col-xs-12  ">
        <s:url action="search" var= "formAction" namespace="do/jacms/Content" />

        <s:form action="%{'/' + #formAction}" cssClass="form-horizontal" role="search">
            <s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
            <p class="sr-only">
            <wpsf:hidden name="lastGroupBy" />
            <wpsf:hidden name="lastOrder" />
            </p>

            <div class="well col-md-offset-3 col-md-6  ">
                <p class="search-label"><s:text name="label.search.label"/></p>

                <div class="form-group">
                    <label class="control-label col-sm-2" for="text" class="sr-only"><s:text name="label.description"/></label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="text" id="text" cssClass="form-control" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.description')}" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-2" for="contentIdToken" class="control-label col-sm-2 text-right"><s:text name="label.code"/></label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" placeholder="CNG12" />
                    </div>
                </div>
                <br>
                <br>
                <!-------------Advanced Search----------->

                <div class="panel-group" id="accordion-markup" style="margin:-24px 0 0 0;" >
                    <div class="panel panel-default">
                        <div class="panel-heading" style="padding:0 0 10px;">
                            <p class="panel-title" style="text-align: end">
                                <a  data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">
                                    Advanced search
                                </a>
                            </p>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">

                                <div id="search-advanced" class="collapse-input-group <s:if test="(#categoryTreeStyleVar == 'request' && null != treeNodeActionMarkerCode)">in</s:if>">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2" for="contentType" class="control-label col-sm-2 text-right">
                                            <s:text name="label.type"/>
                                        </label>
                                        <div class="col-sm-9 input-group" style="padding:0 20px 0 20px">
                                            <wpsf:select cssClass="form-control" name="contentType" id="contentType"
                                                         list="contentTypes" listKey="code" listValue="description"
                                                         headerKey="" headerValue="%{getText('label.all')}" />
                                            <div class="input-group-btn">
                                                <wpsf:submit cssClass="btn btn-default" value="%{getText('label.set')}" />
                                            </div>
                                        </div>
                                    </div>
                                    <s:set var="searchableAttributes" value="searchableAttributes" />
                                    <s:if test="null != #searchableAttributes && #searchableAttributes.size() > 0">

                                        <s:iterator var="attribute" value="#searchableAttributes">
                                            <s:set var="currentFieldId" value="%{'entityFinding_'+#attribute.name}" />
                                            <s:if test="#attribute.textAttribute">
                                                <div class="form-group">
                                                    <s:set var="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
                                                    <label class="control-label col-sm-2" for="<s:property value="currentFieldId" />" class="control-label col-sm-3 text-right"><s:property value="#attribute.name" /></label>
                                                    <div class="col-sm-9">
                                                        <wpsf:textfield id="%{currentFieldId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" cssClass="form-control" />
                                                    </div>
                                                </div>
                                            </s:if>
                                            <s:elseif test="#attribute.type == 'Date'">
                                                <s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
                                                <s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>

                                                <div class="form-group">
                                                    <label class="control-label col-sm-2" for="<s:property value="%{currentFieldId}" />_dateStartFieldName_cal" class="control-label col-sm-9 text-right"><s:text name="note.range.from.attribute" />&#32;<s:property value="#attribute.name" /></label>
                                                    <div class="col-sm-9">
                                                        <wpsf:textfield id="%{currentFieldId}_dateStartFieldName_cal" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" cssClass="form-control bootstrap-datepicker" placeholder="dd/mm/yyyy" />
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-sm-2" for="<s:property value="%{currentFieldId}" />_dateEndFieldName_cal" class="control-label col-sm-9 text-right"><s:text name="note.range.to.attribute" />&#32;<s:property value="#attribute.name" /></label>
                                                    <div class="col-sm-9">
                                                        <wpsf:textfield id="%{currentFieldId}_dateEndFieldName_cal" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" cssClass="form-control bootstrap-datepicker" placeholder="dd/mm/yyyy" />
                                                    </div>
                                                </div>
                                            </s:elseif>
                                            <s:elseif test="#attribute.type == 'Number'">
                                                <s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
                                                <s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
                                                <div class="form-group">
                                                    <label class="control-label col-sm-2" for="<s:property value="currentFieldId" />_start"><s:text name="note.range.from.attribute" />&#32;<s:property value="#attribute.name" />:</label>
                                                    <div class="col-sm-9">
                                                        <wpsf:textfield id="%{currentFieldId}_start" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"  cssClass="form-control" />
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-sm-2" for="<s:property value="currentFieldId" />_end"><s:text name="note.range.to.attribute" />&#32;<s:property value="#attribute.name" />:</label>
                                                    <div class="col-sm-9">
                                                        <wpsf:textfield id="%{currentFieldId}_end" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"  cssClass="form-control"/>
                                                    </div>
                                                </div>

                                            </s:elseif>
                                            <s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                                                <p>
                                                    <span class="important"><s:property value="#attribute.name" /></span><br />
                                                </p>
                                                <s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
                                                <s:set var="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
                                                <ul class="noBullet radiocheck">
                                                    <li><wpsf:radio id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" /><label for="none_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
                                                    <li><wpsf:radio id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" /><label for="true_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.yes"/></label></li>
                                                    <li><wpsf:radio id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" /><label for="false_<s:property value="#booleanInputFieldName" />" class="normal"><s:text name="label.no"/></label></li>
                                                </ul>
                                            </s:elseif>
                                        </s:iterator>
                                    </s:if>
                                    <div class="form-group">
                                        <label for="contentType" class="control-label col-sm-2 text-right">
                                            <s:text name="label.category" />
                                        </label>
                                        <div class="col-sm-9">
                                            <s:action name="showCategoryTreeOnContentFinding" namespace="/do/jacms/Content" ignoreContextParams="true" executeResult="true"></s:action>
                                        </div>
                                    </div>
                                </div>
                                <s:set var="allowedGroupsVar" value="allowedGroups" />
                                <s:if test="null != #allowedGroupsVar && #allowedGroupsVar.size()>1">
                                    <div class="form-group">
                                        <label for="ownerGroupName" class="control-label col-sm-2 text-right"><s:text name="label.group" /></label>
                                        <div class="col-sm-9">
                                            <wpsf:select name="ownerGroupName" id="ownerGroupName" list="#allowedGroupsVar" headerKey="" headerValue="%{getText('label.all')}" listKey="name" listValue="descr" cssClass="form-control" />
                                        </div>
                                    </div>
                                </s:if>
                                <div class="form-group">
                                    <label for="state" class="control-label col-sm-2 text-right"><s:text name="label.state"/></label>
                                    <div class="col-sm-9">
                                        <wpsf:select name="state" id="state" list="avalaibleStatus" headerKey="" headerValue="%{getText('label.all')}" listKey="key" listValue="%{getText(value)}" cssClass="form-control" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="btn-group col-sm-9 col-sm-offset-2" data-toggle="buttons">
                                        <label class="btn btn-default <s:if test="('yes' == onLineState)"> active </s:if>" >
                                            <input type="radio" name="onLineState" id="approved" <s:if test="('yes' == onLineState)">checked="checked"</s:if> value="yes" />&#32;
                                            <s:text name="name.isApprovedContent"/>
                                        </label>
                                        <label class="btn btn-default <s:if test="('no' == onLineState)"> active </s:if>">
                                            <input type="radio" name="onLineState" id="notApproved" <s:if test="('no' == onLineState)">checked="checked"</s:if> value="no" />&#32;
                                            <s:text name="name.isNotApprovedContent"/>
                                        </label>
                                        <label class="btn btn-default <s:if test="('yes' != onLineState) && ('no' != onLineState)"> active </s:if>">
                                            <input type="radio" name="onLineState" id="bothApproved" <s:if test="('yes' != onLineState) && ('no' != onLineState)">checked="checked"</s:if> value="" />&#32;
                                            <s:text name="name.isApprovedOrNotContent" />
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><%--// search-advanced --%>
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
    <hr />

    <!--Aggiunta colonne tabella lista contentui-->

    <div class="col-xs-12">
        <div class="dropdown  pull-right dropdown-menu-right filter-list" id="search-configure-results">
            <button class="btn btn-default dropdown-toggle " type="button" id="dropdownMenu1" data-toggle="dropdown">
                <s:text name="title.searchResultOptions"/>&nbsp;<i class="fa fa-columns" aria-hidden="true"></i>
            </button>
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">
                        <label class="<s:if test="%{viewCreationDate}" > active</s:if>">
                            <wpsf:checkbox name="viewCreationDate" id="viewCreationDate" />&#32;
                            <s:text name="label.creationDate"/>
                        </label></a>
                </li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">
                        <label class="<s:if test="%{viewTypeDescr}" > active</s:if>">
                            <wpsf:checkbox name="viewTypeDescr" id="viewTypeDescr" />&#32;
                            <s:text name="name.contentType" />
                        </label></a>
                </li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">
                        <label class="<s:if test="%{viewStatus}" > active</s:if>">
                            <wpsf:checkbox name="viewStatus" id="viewStatus" />&#32;
                            <s:text name="name.contentStatus" />
                        </label></a>
                </li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">
                        <label class="<s:if test="%{viewGroup}" > active</s:if>">
                            <wpsf:checkbox name="viewGroup" id="viewGroup" />&#32;
                            <s:text name="label.group"/>
                        </label></a>
                </li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="#">
                        <label class="<s:if test="%{viewCode}" > active</s:if>" for="viewCode">
                            <wpsf:checkbox name="viewCode" id="viewCode" />&#32;
                            <s:text name="label.code" />
                        </label></a>
                </li>
                <li role="presentation">
                <wpsf:submit type="button" cssClass="btn-no-button">
                    <s:text name="label.table.update" />
                </wpsf:submit>
                </li>
            </ul>
        </div>
    </div><!-- Fine aggiunta colonne tabella lista contentui-->
</s:form>
<div class="col-xs-12  ">

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
                <s:set var="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
                <wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
            </s:if>
            <s:elseif test="#attribute.type == 'Date'">
                <s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
                <s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
                <wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
                <wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
            </s:elseif>
            <s:elseif test="#attribute.type == 'Number'">
                <s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
                <s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
                <wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
                <wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
            </s:elseif>
            <s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                <s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
                <wpsf:hidden name="%{#booleanInputFieldName}" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
            </s:elseif>
        </s:iterator>
        </p>

        <%-- New Content Button--%>

        <div class="btn-group">
            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">

                <s:text name="label.newContent" />&#32;
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <s:iterator var="contentTypeVar" value="#contentTypesVar">
                    <jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}" property="isAuthToEdit" var="isAuthToEditVar" />
                    <s:if test="%{#isAuthToEditVar}">
                        <li><a href="<s:url action="createNew" namespace="/do/jacms/Content" >
                               <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
                                </s:url>" ><s:text name="label.new" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" /></a></li>
                    </s:if>
                </s:iterator>
            </ul>
        </div>
        <%-- End new Content Button--%>

        <s:if test="hasActionErrors()">
            <!--<div class="alert alert-danger alert-dismissable fade in margin-base-top">-->
            <div class="alert alert-danger alert-dismissable" style="margin-top:3em">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.ActionErrors" />
                <ul class="margin-base-top">
                    <s:iterator value="ActionErrors">
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </ul>
            </div>
            <!--</div>-->
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="messages.confirm" />
                <ul class="margin-base-top">
                    <s:iterator value="actionMessages">
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <s:set var="contentIdsVar" value="contents" />

        <wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
            <s:set var="group" value="#groupContent" />

            <div class="text-center">
                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
            </div>

            <s:if test="%{#contentIdsVar.size() > 0}">
                <table class="table table-striped table-bordered table-hover" id="contentListTable">
                    <caption class="sr-only"><s:text name="title.contentList" /></caption>
                    <tr>

                        <th>
                            <a href="<s:url action="changeOrder" includeParams="all" >
                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                <s:param name="lastOrder" ><s:property value="lastOrder" /></s:param>
                                <s:param name="groupBy">descr</s:param>
                                <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                </s:url>"><s:text name="label.description" /></a>
                        </th>
                    <s:if test="viewCode">
                        <th>
                            <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                <s:param name="groupBy">code</s:param>
                                <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                </s:url>"><s:text name="label.code" /></a>
                        </th>
                    </s:if>
                    <s:if test="viewTypeDescr"><th><s:text name="label.type" /></th></s:if>
                    <s:if test="viewStatus"><th><s:text name="label.state" /></th></s:if>
                    <s:if test="viewGroup"><th><s:text name="label.group" /></th></s:if>
                    <s:if test="viewCreationDate">
                        <th>
                            <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                               <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                                <s:param name="groupBy">created</s:param>
                                <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                                </s:url>"><s:text name="label.creationDate" /></a>
                        </th>
                    </s:if>
                    <th>
                        <a href="<s:url action="changeOrder" anchor="content_list_intro" includeParams="all" >
                           <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                            <s:param name="lastOrder"><s:property value="lastOrder" /></s:param>
                            <s:param name="groupBy">lastModified</s:param>
                            <s:param name="entandoaction:changeOrder">changeOrder</s:param>
                            </s:url>"><s:text name="label.lastEdit" /></a>
                    </th>
                    <th class="text-center"> <s:text name="name.onLine" /></th>

                    <th class="text-center"><s:text name="label.actions" /></th>
                    </tr>
                    <s:iterator var="contentId">
                        <s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
                        <tr>

                            <td>
                                <label>
                                    <input type="checkbox" name="contentIds" id="content_<s:property value="#content.id" />" value="<s:property value="#content.id" />" />&#32;
                                    <s:property value="#content.descr" />&#32;
                                    <s:if test="%{#content.mainGroupCode != null && !#content.mainGroupCode.equals('free')}"><span class="text-muted icon fa fa-lock"></span></s:if>
                                </label>
                            </td>
                        <s:if test="viewCode">
                            <td class="table-txt-wrap">
                            <s:property value="#content.id" />
                            </td>
                        </s:if>
                        <s:if test="viewTypeDescr">
                            <td class="table-txt-wrap">
                            <s:property value="%{getSmallContentType(#content.typeCode).descr}" />
                            </td>
                        </s:if>
                        <s:if test="viewStatus">
                            <td class="table-txt-wrap">
                            <s:property value="%{getText('name.contentStatus.' + #content.status)}" />
                            </td>
                        </s:if>
                        <s:if test="viewGroup">
                            <td class="table-txt-wrap">
                            <s:property value="%{getGroup(#content.mainGroupCode).descr}" />
                            </td>
                        </s:if>
                        <s:if test="viewCreationDate">
                            <td class="text-nowrap" style="width: 118px;">
                            <s:date name="#content.create" format="dd/MM/yyyy HH:mm" />
                            </td>
                        </s:if>
                        <td class=" text-nowrap" style="width: 118px;">
                        <s:date name="#content.modify" format="dd/MM/yyyy HH:mm" />
                        </td>

                        <s:if test="#content.onLine && #content.sync">
                            <s:set var="iconName">check</s:set>
                            <s:set var="textVariant">success</s:set>
                            <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                        </s:if>
                        <s:if test="#content.onLine && !(#content.sync)">
                            <s:set var="iconName">adjust</s:set>
                            <s:set var="textVariant">info</s:set>
                            <s:set var="isOnlineStatus" value="%{getText('label.yes') + ', ' + getText('note.notSynched')}" />
                        </s:if>
                        <s:if test="!(#content.onLine)">
                            <s:set var="iconName">pause</s:set>
                            <s:set var="textVariant">warning</s:set>
                            <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                        </s:if>
                        <td class="text-center" style="width: 40px;">
                            <span class="icon fa fa-<s:property value="iconName" /> text-<s:property value="textVariant" />" title="<s:property value="isOnlineStatus" />"></span>
                            <span class="sr-only"><s:property value="isOnlineStatus" /></span>
                        </td>
                        <td class=" table-view-pf-actions">
                            <div class="dropdown dropdown-kebab-pf">
                                <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-ellipsis-v"></span></button>
                                <ul class="dropdown-menu dropdown-menu-right">								
                                    <li>
                                        <a title="<s:text name="label.copyPaste" />: <s:property value="#content.id" /> - <s:property value="#content.description" />" href="<s:url action="copyPaste" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="copyPublicVersion" value="'false'" /></s:url>">
                                            <s:text name="label.copyPaste" /><span class="sr-only">: <s:property value="#content.id" /> - <s:property value="#content.descr" /></span>
                                        </a>
                                    </li>
                                    <li>
                                        <a title="<s:text name="label.inspect" />: [<s:text name="name.work" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />" href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'false'" /></s:url>">
                                            <s:text name="label.inspect" />&#32;<s:text name="name.work" />
                                        </a>
                                    </li>
                                    <li>
                                        <a title="<s:text name="label.inspect" />: [<s:text name="name.onLine" />] <s:property value="#content.id" /> - <s:property value="#content.descr" />" href="<s:url action="inspect" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /><s:param name="currentPublicVersion" value="'true'" /></s:url>">
                                            <s:text name="label.inspect" />&#32;<s:text name="name.onLine" />
                                        </a>
                                    </li>
                                    <li>
                                        <a title="<s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" />" href="<s:url action="edit" namespace="/do/jacms/Content"><s:param name="contentId" value="#content.id" /></s:url>">
                                            <s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" />
                                            <span class="sr-only"><s:text name="label.edit" />: <s:property value="#content.id" /> - <s:property value="#content.description" /></span>
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
                </table>
            </s:if>

            <fieldset>
                <legend class="sr-only"><s:text name="title.contentActions" /></legend>
                <p class="sr-only"><s:text name="title.contentActionsIntro" /></p>
                <div class="btn-toolbar">
                    <wp:ifauthorized permission="validateContents">
                        <div class="btn-group margin-small-vertical">
                            <wpsf:submit action="approveContentGroup" type="button" title="%{getText('note.button.approve')}" cssClass="btn btn-success">
                                <span class="icon fa fa-check"></span>
                                <s:text name="label.approve" />
                            </wpsf:submit>
                            <wpsf:submit action="suspendContentGroup" type="button" title="%{getText('note.button.suspend')}" cssClass="btn btn-warning">
                                <span class="icon fa fa-pause"></span>
                                <s:text name="label.suspend" />
                            </wpsf:submit>
                        </div>
                    </wp:ifauthorized>
                    <wpsa:hookPoint key="jacms.contentFinding.allContents.actions" objectName="hookpoint_contentFinding_allContents">
                        <s:iterator value="#hookpoint_contentFinding_allContents" var="hookPointElement">
                            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                        </s:iterator>
                    </wpsa:hookPoint>
                    <div class="btn-group margin-small-vertical">
                        <wpsf:submit action="trashContentGroup" type="button" title="%{getText('note.button.delete')}" cssClass="btn btn-danger">
                            <span class="icon fa fa-times-circle"></span>
                            <s:text name="label.remove" />
                        </wpsf:submit>
                    </div>
                </div>
            </fieldset>
            <div class="text-center">
                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
            </div>
        </wpsa:subset>
</div>
</s:form>
</div>

<style>
    .panel {
        margin-bottom: 20px;
        background-color: #fff;
        border: 0px solid transparent;
        border-radius: 1px;
        box-shadow: none;
    }
    .panel-group .panel-heading {
        background-image: none;
    }
    .panel-group .panel-heading+.panel-collapse .panel-body {
        border-top: 1px solid #ecebeb;
    }
    .panel-group .panel-title>a {
        color: #4d5258;
        font-weight: 400;
        font-size: 13px;
    }
    .panel-group .panel-title>a:after {
        margin-left: 10px;
    }
</style>
<script>
    //-- Boostrap-bootstrap-datepicker---
    $('.bootstrap-datepicker').datepicker({
        autoclose: true,
        todayBtn: "linked",
        todayHighlight: true
    });
</script>
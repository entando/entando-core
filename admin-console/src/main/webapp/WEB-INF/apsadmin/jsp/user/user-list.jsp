<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting" /></li>
    <li class="page-title-container"><s:text
            name="title.userManagement" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.userManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.userList.help" />" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<s:form action="search" role="search" cssClass="form-horizontal">
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
            <ul>
                <s:iterator value="actionErrors">
                    <li>
                        <s:property escapeHtml="false" />
                    </li>
                </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"
                    aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.FieldErrors" /></strong>
            <ul>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li>
                            <s:property escapeHtml="false" />
                        </li>
                    </s:iterator>
                </s:iterator>
            </ul>
        </div>
    </s:if>

    <div class="searchPanel form-group">
        <div class="well col-md-offset-3 col-md-6  ">
            <p class="search-label">
                <s:text name="label.search.label" />
            </p>

            <div class="form-group">
                <label class="col-sm-2 control-label"><s:text
                        name="label.username" />
                </label>
                <div class="col-sm-9">
                    <wpsf:textfield name="username" id="search-username" cssClass="form-control " title="%{getText('label.search.by')+' '+getText('label.username')}" placeholder="%{getText('label.username')}" />
                </div>
            </div>
            <s:set var="searchableAttributesVar" value="searchableAttributes" />
            <s:set var="searchableAttributesPageScope"
                   value="%{#searchableAttributesVar}" scope="page" />
            <!--users section-->
            <div class="form-group">
                <label class="control-label col-sm-2"> 
                    <s:text name="label.users" />
                </label>
                <!--button role-->
                <div class="col-sm-10">
                    <div class="btn-group col-sm-10 spacer-form" data-toggle="buttons">
                        <label class="btn btn-default <s:if test="%{withProfile==null}"> active </s:if>">
                            <wpsf:radio id="" name="withProfile" value="" checked="%{withProfile==null}" /> &#32;
                            <s:text name="label.userprofile.search.usersAllProfile" />
                        </label> 
                        <label class="btn btn-default <s:if test="%{withProfile.toString().equalsIgnoreCase('1')}"> active </s:if>">
                            <wpsf:radio id="" name="withProfile" value="1"  checked="%{withProfile.toString().equalsIgnoreCase('1')}" />&#32;
                            <s:text name="label.userprofile.search.usersWithProfile" />
                        </label> 
                        <label class="btn btn-default <s:if test="%{withProfile.toString().equalsIgnoreCase('0')}"> active </s:if>">
                            <wpsf:radio id="" name="withProfile" value="0" checked="%{withProfile.toString().equalsIgnoreCase('0')}" />
                            &#32;<s:text name="label.userprofile.search.usersWithoutProfile" />
                        </label>
                    </div>
                </div>
            </div>

            <div class="panel-group" id="accordion-markup" style="margin: -24px 0 0 0;">

                <div class="panel panel-default">
                    <div class="panel-heading" style="padding: 0 0 10px;">
                        <p class="panel-title" style="text-align: end">
                            <a data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">
                                <s:text name="label.search.advanced" />
                            </a>
                        </p>
                    </div>

                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${empty searchableAttributesPageScope}">
                                <div class="form-group">
                                    <div class="text-center">
                                        <span class="text-info"> <s:text name="note.userprofile.searchAdvanced.chooseType" /></span>
                                    </div>
                                </div>
                            </c:if>
                            <!--users section end-->
                            <div class="form-group">
                                <label class="col-sm-3 control-label"
                                       for="userprofile_src_entityPrototypes"> 
                                    <s:text name="note.userprofile.search.profileType" />
                                </label>
                                <div class="col-sm-8 input-group" style="padding: 0 20px 0 20px">
                                    <wpsf:select id="userprofile_src_entityPrototypes"
                                                 list="entityPrototypes" name="entityTypeCode" headerKey=""
                                                 headerValue="%{getText('label.all')}" listKey="typeCode"
                                                 listValue="typeDescr" cssClass="form-control" />
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" cssClass="btn btn-default"
                                                     action="changeProfileType" value="set">
                                            <s:text name="label.set" />
                                        </wpsf:submit>
                                    </div>
                                </div>
                            </div>

                            <s:if test="null != #searchableAttributesVar && #searchableAttributesVar.size() > 0">
                                <s:iterator value="#searchableAttributesVar" var="attribute">
                                    <%-- Text Attribute --%>
                                    <s:if test="#attribute.textAttribute">
                                        <s:set var="currentAttributeHtmlId"
                                               value="%{'userprofile_src_'+#attribute.name}" />
                                        <s:set var="textInputFieldName"
                                               value="%{#attribute.name+'_textFieldName'}" />
                                        <div class="form-group">
                                            <label class="control-label col-sm-3" style="text-align: end"
                                                   for="<s:property value="#currentAttributeHtmlId" />">
                                                <s:property value="#attribute.name" />
                                            </label>
                                            <div class="col-sm-8">
                                                <wpsf:textfield id="%{#currentAttributeHtmlId}"
                                                                ame="%{#textInputFieldName}"
                                                                value="%{getSearchFormFieldValue(#textInputFieldName)}"
                                                                cssClass="form-control" />
                                            </div>
                                        </div>
                                    </s:if>
                                    <%-- Date Attribute --%>
                                    <s:elseif test="#attribute.type == 'Date'">
                                        <s:set var="currentAttributeHtmlId" value="%{'userprofile_src_'+#attribute.name}" />
                                        <s:set var="dateStartInputFieldName" value="%{#attribute.name+'_dateStartFieldName'}" />
                                        <s:set var="dateEndInputFieldName" value="%{#attribute.name+'_dateEndFieldName'}" />
                                        <div class="form-group">
                                            <label class="control-label col-sm-3" style="text-align: end">
                                                <s:property value="#attribute.name" />
                                            </label>
                                            <div class="col-sm-8">
                                                <label class="sr-only" for="<s:property value="%{#currentAttributeHtmlId+'_dateStartFieldName_cal'}" />">
                                                    <s:property value="#attribute.name" />&#32;<s:text
                                                        name="label.userprofile.from.date" />
                                                </label>
                                                <wpsf:textfield cssClass="form-control"
                                                                placeholder="%{getText('label.userprofile.from.date')}"
                                                                title="%{#attribute.name+' '+getText('label.userprofile.from.date')}"
                                                                id="%{#currentAttributeHtmlId}_dateStartFieldName_cal"
                                                                name="%{#dateStartInputFieldName}"
                                                                value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
                                            </div>
                                            <div class="col-lg-3">
                                                <label class="sr-only"
                                                       for="<s:property value="%{#currentAttributeHtmlId+'_dateEndFieldName_cal'}" />">
                                                    <s:property value="#attribute.name" />&#32;
                                                    <s:text name="label.userprofile.to.date" />
                                                </label>
                                                <wpsf:textfield cssClass="form-control"
                                                                placeholder="%{getText('label.userprofile.to.date')}"
                                                                title="%{#attribute.name+' '+getText('label.userprofile.to.date')}"
                                                                id="%{#currentAttributeHtmlId}_dateEndFieldName_cal"
                                                                name="%{#dateEndInputFieldName}"
                                                                value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
                                            </div>
                                            <div class="col-xs-12">
                                                <span class="help-block">
                                                    <s:text name="label.userprofile.date.pattern" /></span>
                                            </div>
                                        </div>
                                    </s:elseif>
                                    <%-- Number Attribute --%>
                                    <s:elseif test="#attribute.type == 'Number'">
                                        <s:set var="currentAttributeHtmlId" value="%{'userprofile_src_'+#attribute.name}" />
                                        <s:set var="numberStartInputFieldName" value="%{#attribute.name+'_numberStartFieldName'}" />
                                        <s:set var="numberEndInputFieldName" value="%{#attribute.name+'_numberEndFieldName'}" />
                                        <div class="form-group">
                                            <label class="control-label col-sm-3" style="text-align: end">
                                                <s:property value="#attribute.name" />
                                            </label>
                                            <div class="col-lg-2">
                                                <label class="sr-only"
                                                       for="<s:property value="%{#currentAttributeHtmlId+'_start'}" />"><s:property
                                                        value="#attribute.name" />&#32;<s:text
                                                        name="label.userprofile.from.value" /></label>
                                                    <wpsf:textfield
                                                        title="%{#attribute.name+' '+getText('label.userprofile.from.value')}"
                                                        id="%{#currentAttributeHtmlId}_start"
                                                        name="%{#numberStartInputFieldName}"
                                                        value="%{getSearchFormFieldValue(#numberStartInputFieldName)}"
                                                        cssClass="form-control"
                                                        placeholder="%{getText('label.userprofile.from.value')}" />
                                            </div>
                                            <div class="col-lg-2">
                                                <label class="sr-only"
                                                       for="<s:property value="%{#currentAttributeHtmlId+'_end'}" />"><s:property
                                                        value="#attribute.name" />&#32;<s:text
                                                        name="label.userprofile.to.value" /></label>
                                                    <wpsf:textfield
                                                        title="%{#attribute.name+' '+getText('label.userprofile.to.value')}"
                                                        id="%{#currentAttributeHtmlId}_end"
                                                        name="%{#numberEndInputFieldName}"
                                                        value="%{getSearchFormFieldValue(#numberEndInputFieldName)}"
                                                        cssClass="form-control"
                                                        placeholder="%{getText('label.userprofile.to.value')}" />
                                            </div>
                                        </div>
                                    </s:elseif>
                                    <%-- Boolean & ThreeState --%>
                                    <s:elseif
                                        test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                                        <s:set var="booleanInputFieldName"
                                               value="%{#attribute.name+'_booleanFieldName'}" />
                                        <s:set var="booleanInputFieldValue"
                                               value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 "><s:property
                                                    value="#attribute.name" /></label>

                                            <div class="btn-group col-xs-9" data-toggle="buttons">
                                                <label
                                                    class="btn btn-default <s:if test="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}"> active </s:if>">
                                                    <wpsf:radio id="none_%{#booleanInputFieldName}"
                                                                name="%{#booleanInputFieldName}" value=""
                                                                checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" />
                                                    &#32;<s:text name="label.bothYesAndNo" />
                                                </label> <label
                                                    class="btn btn-default <s:if test="%{#booleanInputFieldValue == 'true'}"> active </s:if>">
                                                    <wpsf:radio id="true_%{#booleanInputFieldName}"
                                                                name="%{#booleanInputFieldName}" value="true"
                                                                checked="%{#booleanInputFieldValue == 'true'}" /> &#32;<s:text
                                                                name="label.yes" />
                                                </label> <label
                                                    class="btn btn-default <s:if test="%{#booleanInputFieldValue == 'false'}"> active </s:if>">
                                                    <wpsf:radio id="false_%{#booleanInputFieldName}"
                                                                name="%{#booleanInputFieldName}" value="false"
                                                                checked="%{#booleanInputFieldValue == 'false'}" /> &#32;<s:text
                                                                name="label.no" />
                                                </label>

                                            </div>
                                        </div>
                                    </s:elseif>
                                    <%-- unknown attribute --%>
                                    <s:else>
                                        <s:set var="currentAttributeHtmlId"
                                               value="%{'userprofile_src_'+#attribute.name}" />
                                        <s:set var="textInputFieldName"
                                               value="%{#attribute.name+'_textFieldName'}" />
                                        <div class="form-group">
                                            <label class="control-label col-sm-3" style="text-align: end"
                                                   for="<s:property value="#currentAttributeHtmlId" />">
                                                <s:property value="#attribute.name" />
                                            </label>
                                            <div class="col-sm-8">
                                                <wpsf:textfield id="%{#currentAttributeHtmlId}"
                                                                name="%{#textInputFieldName}"
                                                                value="%{getSearchFormFieldValue(#textInputFieldName)}"
                                                                cssClass="form-control" />
                                            </div>
                                        </div>
                                    </s:else>
                                </s:iterator>
                            </s:if>

                            <%-- search by role --%>
                            <s:set var="attributeRolesVar" value="attributeRoles" />
                            <s:if
                                test="null != #attributeRolesVar && #attributeRolesVar.size() > 0">
                                <s:iterator var="attributeRoleVar" value="#attributeRolesVar">
                                    <s:set var="currentFieldIdVar">userFinding_<s:property value="#attributeRoleVar.name" /></s:set>
                                    <div class="form-group">
                                        <s:if test="%{#attributeRoleVar.formFieldType.toString().equals('TEXT')}">
                                            <div class="col-sm-3" style="text-align: end">
                                                <label for="<s:property value="%{#currentFieldIdVar}" />">
                                                    <s:set var="replacedRoleCodeVar" value="%{#attributeRoleVar.name.replace(':', '.')}" />
                                                    <s:text name="%{'label.userRole.' + #replacedRoleCodeVar}" />
                                                </label>
                                            </div>
                                            <div class="col-sm-8">
                                                <s:set var="textInputFieldName">
                                                    <s:property value="#attributeRoleVar.name" />_textFieldName</s:set>
                                                <wpsf:textfield id="%{#currentFieldIdVar}" name="%{#textInputFieldName}" 
                                                                value="%{getSearchFormFieldValue(#textInputFieldName)}"
                                                                cssClass="form-control" />
                                            </div>
                                        </s:if>
                                    </div>
                                </s:iterator>
                            </s:if>
                            <%-- //search by role --%>
                            <div class="form-group">
                                <%-- hookpoint core.user-list.core-field --%>
                                <wpsa:hookPoint key="core.user-list.form-field"
                                                objectName="hookPointElements_core_user_list_core_field">
                                    <s:iterator value="#hookPointElements_core_user_list_core_field"
                                                var="hookPointElement">
                                        <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                    </s:iterator>
                                </wpsa:hookPoint>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%-- second search button --%>
            <div class="col-sm-12">
                <div class="form-group">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.search" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </div>
    <br>
    <br>
    <a href="<s:url namespace="/do/User" action="new" />"
       class="btn btn-primary pull-right" style="margin-bottom: 5px"> <s:text
            name="title.userManagement.userNew" />
    </a>

    <wpsa:subset source="searchResult" count="10" objectName="groupUserVar"
                 advanced="true" offset="5">
        <s:set var="group" value="#groupUserVar" />
        <wp:ifauthorized permission="editUserProfile"
                         var="hasEditProfilePermission" />
        <s:set var="hasEditProfilePermission"
               value="#attr.hasEditProfilePermission" />
        <div class="col-xs-12 no-padding">
            <table class="table table-striped table-bordered table-hover no-mb">
                <thead>
                    <tr>
                        <th class="table-w-10"><s:text name="label.username" /></th>
                        <th class="table-w-10"><s:text name="label.fullname" /></th>
                        <th class="table-w-15"><s:text name="label.email" /></th>
                            <%-- hookpoint core.user-list.table.th --%>
                            <wpsa:hookPoint key="core.user-list.table.th"
                                            objectName="hookPointElements_core_user_list_table_th">
                                <s:iterator value="#hookPointElements_core_user_list_table_th"
                                            var="hookPointElement">
                                    <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                </s:iterator>
                            </wpsa:hookPoint>
                        <th class="text-center table-w-20"><s:text name="label.state" /></th>
                        <th class="table-w-5 text-center"><s:text name="label.actions" /></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator var="usernameVar">
                        <s:set var="userVar" value="%{getUser(#usernameVar)}" />
                        <s:set var="userProfileVar"
                               value="%{getUserProfile(#usernameVar)}" />
                        <s:url action="edit" var="editUserActionVar">
                            <s:param name="username" value="#usernameVar" />
                        </s:url>
                        <s:url action="edit" namespace="/do/User/Authorization"
                               var="editUserAuthActionVar">
                            <s:param name="username" value="#usernameVar" />
                        </s:url>
                        <s:url action="edit" namespace="/do/userprofile"
                               var="editUserProfileActionVar">
                            <s:param name="username" value="#usernameVar" />
                        </s:url>
                        <s:url action="view" namespace="/do/userprofile"
                               var="viewUserProfileActionVar">
                            <s:param name="username" value="#usernameVar" />
                        </s:url>
                        <s:url action="trash" var="userTrashActionVar">
                            <s:param name="username" value="#usernameVar" />
                        </s:url>
                        <s:if test="null == #userVar || #userVar.disabled">
                            <s:set var="statusIconClassVar"
                                   value="%{'icon fa fa-ban text-danger'}" />
                            <s:set var="statusTextVar"
                                   value="%{getText('note.userStatus.notActive')}" />
                            <s:set var="statusTextClassVar" value="%{'text-danger'}" />
                        </s:if>
                        <s:elseif test="!#userVar.entandoUser">
                            <s:set var="statusIconClassVar"
                                   value="%{'icon fa fa-minus text-danger'}" />
                            <s:set var="statusTextVar"
                                   value="%{getText('note.userStatus.notEntandoUser')}" />
                            <s:set var="statusTextClassVar" value="%{'text-danger'}" />
                        </s:elseif>
                        <s:elseif test="!#userVar.accountNotExpired">
                            <s:set var="statusIconClassVar"
                                   value="%{'icon fa fa-circle-o text-danger'}" />
                            <s:set var="statusTextVar"
                                   value="%{getText('note.userStatus.expiredAccount')}" />
                            <s:set var="statusTextClassVar" value="%{'text-danger'}" />
                        </s:elseif>
                        <s:elseif test="!#userVar.credentialsNotExpired">
                            <s:set var="statusIconClassVar"
                                   value="%{'icon fa fa-adjust text-danger'}" />
                            <s:set var="statusTextVar"
                                   value="%{getText('note.userStatus.expiredPassword')}" />
                            <s:set var="statusTextClassVar" value="%{'text-danger'}" />
                        </s:elseif>
                        <s:elseif test="!#userVar.disabled">
                            <s:set var="statusIconClassVar"
                                   value="%{'icon fa fa-check text-success'}" />
                            <s:set var="statusTextVar"
                                   value="%{getText('note.userStatus.active')}" />
                            <s:set var="statusTextClassVar" value="%{'text-success'}" />
                        </s:elseif>

                        <tr>
                            <%-- username --%>
                            <td>
                                <s:property value="#usernameVar" />
                            </td>
                            <!----------Fullname-------->
                            <td>
                                <s:property value="%{#userProfileVar.getDisplayName()}" />
                            </td>
                            <%-- email --%>
                            <td>
                                <s:if test="null != #userProfileVar">
                                    <s:set var="mailVar" value="#userProfileVar.getValue(#userProfileVar.mailAttributeName)" />
                                    <s:if test="#mailVar.length()>25">
                                        <span title="<s:property value="#mailVar" />"> <s:set
                                                var="chunks" value="%{#mailVar.split('@')}" /> <s:property
                                                value="%{#chunks[0].length() > 8 ? #chunks[0].substring(0,8)+'...' : #chunks[0]}" />@<s:property
                                                value="%{#chunks[1].length() > 8 ? '...'+#chunks[1].substring(#chunks[1].length()-8) : #chunks[1]}" />
                                        </span>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#mailVar" />
                                    </s:else>
                                </s:if> <s:else>
                                    <span title="<s:text name="label.noProfile" />">&ndash;</span>
                                </s:else>
                            </td>

                            <%-- td hookpoint --%>
                            <wpsa:hookPoint key="core.user-list.table.td" objectName="hookPointElements_core_user_list_table_td">
                                <s:iterator value="#hookPointElements_core_user_list_table_td" var="hookPointElement">
                                    <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                </s:iterator>
                            </wpsa:hookPoint>

                            <%-- status --%>
                            <td class="text-center">
                                <span class="<s:property value="#statusIconClassVar" />" title="<s:property value="#statusTextVar" />"> 
                                </span> 
                                <span class="<s:property value="#statusTextClassVar" />"> 
                                    <s:property value="#statusTextVar" />
                                </span>
                            </td>
                            <%-- actions --%>
                            <td class="text-center table-view-pf-actions">
                                <div class="dropdown dropdown-kebab-pf">
                                    <button class="btn btn-menu-right dropdown-toggle"
                                            type="button" data-toggle="dropdown" aria-haspopup="true"
                                            aria-expanded="false">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-right">
                                        <li>
                                            <%-- edit --%> <a
                                                title="<s:text name="label.edit" />:&#32;<s:property value="#usernameVar" />"
                                                href="<s:property value="#editUserActionVar" escapeHtml="false" />">
                                                <span><s:text name="label.edit" /></span>
                                            </a>
                                        </li>
                                        <li>
                                            <%-- edit auth --%> <a
                                                href="<s:property value="#editUserAuthActionVar" escapeHtml="false" />"
                                                title="<s:text name="note.configureAuthorizationsFor" />: <s:property value="#usernameVar" />">

                                                <span><s:text name="note.configureAuthorizationsFor" />:&#32;<s:property
                                                        value="#usernameVar" /></span>
                                            </a>
                                        </li>
                                        <s:if test="#hasEditProfilePermission">
                                            <li>
                                                <%-- edit profile --%> <a
                                                    href="<s:property value="#editUserProfileActionVar" escapeHtml="false" />"
                                                    title="<s:text name="label.editProfile" />:&#32;<s:property value="#usernameVar" />">

                                                    <span><s:text name="label.editProfile" />:&#32;<s:property
                                                            value="#usernameVar" /></span>
                                                </a>
                                            </li>
                                        </s:if>
                                        <s:if test="null != #userProfileVar">
                                            <li>
                                                <%-- view profile --%> <a
                                                    href="<s:property value="#viewUserProfileActionVar" escapeHtml="false" />"
                                                    title="<s:text name="label.viewProfile" />:&#32;<s:property value="#usernameVar" />">
                                                    <span><s:text name="label.viewProfile" />:&#32;<s:property
                                                            value="#usernameVar" /></span>
                                                </a>
                                            </li>
                                        </s:if>
                                        <%-- remove --%>
                                        <li><a
                                                href="<s:property value="#userTrashActionVar" escapeHtml="false" />"
                                                title="<s:text name="label.remove" />:&#32;<s:property value="#usernameVar" />">
                                                <span><s:text name="label.remove" /></span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </div>
        <div class="content-view-pf-pagination clearfix">
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
</s:form>

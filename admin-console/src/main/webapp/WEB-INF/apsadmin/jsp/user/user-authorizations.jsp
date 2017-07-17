<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:set var="targetNS" value="%{'/do/User'}" />


<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.userSetting" /></li>
    <li> <a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a></li>
    <li class="page-title-container"> <s:text name="title.userManagement.userAuthorizations" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.userManagement.userAuthorizations" />&nbsp;<s:text name="label.for" />&nbsp;<s:property value="userAuthsFormBean.username" />
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

<div id="main" role="main">
    <div class="col-sm-12">

        <!--    <p class="margin-more-bottom">
        <s:text name="note.userAuthorizations.intro" />&#32;<code><s:property value="userAuthsFormBean.username" /></code><%--, <s:text name="note.userAuthorizations.youCan" />&#32;<a href="#groups"><s:text name="note.userAuthorizations.configureGroups" /></a>&#32;<s:text name="label.or" />&#32;<a href="#roles"><s:text name="note.userAuthorizations.configureRoles" /></a>. --%>
    </p>-->

        <s:form action="save" cssClass="form-horizontal">

            <s:if test="hasFieldErrors()">
                <div class="alert alert-danger">
                    <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                    <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                </div>


            </s:if>
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button class="close" data-dismiss="alert">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span><s:text name="message.title.ActionErrors" /></span>
                    <ul class="margin-base-top">
                        <s:iterator value="actionErrors">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <p class="sr-only">
                <wpsf:hidden name="username" value="%{userAuthsFormBean.username}"/>
            </p>
            <s:set var="userAuthorizationsVar" value="%{userAuthsFormBean.authorizations}" />
            <s:if test="%{#userAuthorizationsVar.size()>0}">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <tr>
                            <th><s:text name="label.userGroup" /></th>
                            <th><s:text name="label.userRole" /></th>
                            <th style="width: 32px;text-align: center;"><s:text name="label.actions" /></th>
                        </tr>
                        <s:iterator value="#userAuthorizationsVar" var="userAuthorizationVar" status="elementStatus">
                            <tr>

                                <td>
                                    <s:property value="#userAuthorizationVar.group.description" />&#32;<code><s:property value="#userAuthorizationVar.group.name" /></code>
                                </td>
                                <td>
                                    <s:set var="roleVar" value="#userAuthorizationVar.role" />
                                    <s:if test="null != #roleVar">
                                        <s:property value="#roleVar.description" />&#32;<code><s:property value="#roleVar.name" /></code>
                                    </s:if>
                                    <s:else><code>&ndash;</code></s:else>
                                    </td>
                                    <td style="text-align: center;">
                                    <s:set var="elementIndexVar" value="#elementStatus.index" />
                                    <wpsa:actionParam action="removeAuthorization" var="actionName" >
                                        <wpsa:actionSubParam name="index" value="%{#elementIndexVar}" />
                                    </wpsa:actionParam>
                                    <wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-menu-right">
                                        <i class="fa fa-trash-o fa-lg" aria-hidden="true" style="color: #23a8e0"></i>
                                    </wpsf:submit>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </div>
            </s:if>
            <s:else>
                <p><s:text name="note.userAuthorizations.empty" /></p>
            </s:else>

            <hr />


            <h1><s:text name="title.newAuthorization" /></h1>

            <div class="form-group">
                <div class="col-xs-12">
                    <label class="col-sm-2 control-label" for="userGroup"><s:text name="label.userGroup" /></label>
                    <div class="col-sm-8">
                        <wpsf:select id="userGroup" name="groupName" list="groups"
                                     headerKey="" headerValue="%{getText('note.choose')}"
                                     listKey="name" listValue="description" cssClass="form-control" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-12">
                    <label class="col-sm-2 control-label" for="userRole"><s:text name="label.userRole" /></label>
                    <div class="col-sm-8">
                        <wpsf:select id="userRole" name="roleName" list="roles"
                                     headerKey="" headerValue="%{getText('note.choose')}"
                                     listKey="name" listValue="description" cssClass="form-control" />
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-xs-12">
                    <label class="col-sm-2"></label>
                    <div class="col-sm-8 button-position-on-save">
                        <wpsa:actionParam action="addAuthorization" var="actionName" />
                        <wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-primary">

                            <s:text name="label.addAuthorization"></s:text>
                        </wpsf:submit>
                        </span>
                        <%-- </div> --%>
                    </div>

                </div>
            </div>
            <hr>
            <div class="form-group ">
                <div class="col-xs-12">
                    <label class="col-sm-2"></label>
                    <div class="col-sm-8 button-position-on-save">
                        <wpsf:submit type="button" cssClass="btn btn-primary">
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </s:form>
    </div>
</div>

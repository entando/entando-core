<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<style>
    .nav-tabs-pattern > li > a {
        background: #f5f5f5;
        border: 1px solid #ccc !important;
        margin-bottom: 0 !important;
    }

    .nav-tabs-pattern > li > a {
        line-height: 59px;
        padding-bottom: 0;
        padding-top: 0;
    }
</style>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li>
        <s:text name="title.languageAndLabels"/>
    </li>
    <li class="page-title-container"><s:text name="title.languageAdmin"/></li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.languageAndLabels"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="page.lang.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active"><a href="<s:url namespace="/do/Lang" action="list" />">
                        <s:text name="title.languageAdmin"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/LocaleString" action="list" />">
                        <s:text name="title.languageAdmin.labels"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br/>

<div class="tab-content">
    <div id="language" class="tab-pane fade in active">
        <s:form action="add" cssClass="form-horizontal">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <strong><s:text name="message.title.ActionErrors"/></strong>
                    <ul class="margin-base-top">
                        <s:iterator value="actionErrors">
                            <li><s:property escapeHtml="false"/></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <div class="form-group">
                <div class="col-xs-12">
                    <label for="langCode"><s:text name="name.chooseALanguage"/></label>
                    <div class="input-group">
                        <select name="langCode" id="langCode" class="form-control">
                            <option value=""><s:text name="note.choose"/></option>
                            <s:iterator var="lang" value="assignableLangs">
                                <option value="<s:property value="#lang.code"/>"><s:property value="#lang.code"/>
                                    &ndash;
                                    <s:property value="#lang.descr"/></option>
                                </s:iterator>
                        </select>
                        <div class="input-group-btn">
                            <wpsf:submit type="button" cssClass="btn btn-primary">
                                <s:text name="label.add"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </div>

            <table class="table table-striped table-bordered table-hover">
                <thead>
                    <tr>
                        <th class="col-sm-5"><s:text name="label.code"/></th>
                        <th class="col-sm-6"><s:text name="label.description"/></th>
                        <th class="text-center col-sm-1"><s:text name="label.remove"/></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator var="lang" value="langs">
                        <tr>
                            <td>
                                <s:set var="labelModifier" value="''"/>
                                <s:set var="labelTitle" value="''"/>
                                <s:if test="#lang.default">
                                    <s:set var="labelModifier" value="'*'"/>
                                    <s:set var="labelTitle">
                                        title="<s:text name="label.default"/>"
                                    </s:set>
                                </s:if>
                                <span class="text-capitalize" <s:property value="labelTitle"/>>
                                    <s:property value="#lang.code"/><s:property value="labelModifier"/>
                                </span>
                            </td>
                            <td><s:property value="#lang.descr"/></td>
                            <td class="text-center">
                                <a href="<s:url action="remove"><s:param name="langCode" value="#lang.code"/></s:url>"
                                   title="<s:text name="label.remove" />: <s:property value="#lang.descr" />">
                                    <span class="fa fa-trash-o fa-lg"></span>
                                </a>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </s:form>
    </div>
</div>

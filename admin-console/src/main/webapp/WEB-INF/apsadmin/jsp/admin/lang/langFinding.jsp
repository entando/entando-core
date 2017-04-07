<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure"/></a></li>
    <li>
        <a href="<s:url namespace="/do/LocaleString" action="list" />">
            <s:text name="title.languageAndLabels"/>
        </a>
    </li>
    <li class="page-title-container"><s:text name="title.languageAdmin"/></li>
</ol>
<h1 class="page-title-container row">
    <div class="col-sm-7">
        <s:text name="title.languageAdmin"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
    <div class="col-sm-5">
        <ul class="nav nav-tabs nav-justified">
            <li class="active"><a href="<s:url namespace="/do/Lang" action="list" />"><s:text
                    name="title.languageAdmin"/></a></li>
            <li>
                <a href="<s:url namespace="/do/LocaleString" action="list" />"><s:text
                        name="title.languageAdmin.labels"/></a>
            </li>
        </ul>
    </div>
</h1>
<br>


<div class="tab-content">
    <div id="language" class="tab-pane fade in active">
        <s:form action="add" cssClass="form-horizontal">
            <%-- <p><s:text name="title.languageAdmin.languages" /></p> --%>
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable fade in">
                    <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                    <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors"/></h2>
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
                            <s:iterator var="lang" value="assignableLangs">
                                <option value="<s:property value="#lang.code"/>"><s:property value="#lang.code"/>
                                    &ndash;
                                    <s:property value="#lang.descr"/></option>
                            </s:iterator>
                        </select>
                        <div class="input-group-btn">
                            <wpsf:submit type="button" cssClass="btn btn-primary">
                                <span class="icon fa fa-plus-square"></span>&#32;
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

                                <a
                                        href="<s:url action="remove"><s:param name="langCode" value="#lang.code"/></s:url>"
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



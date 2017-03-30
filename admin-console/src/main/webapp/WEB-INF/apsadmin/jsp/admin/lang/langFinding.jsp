<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li class="page-title-container"><s:text name="title.languageAdmin"/></li>
</ol>
<h1 class="page-title-container"><s:text name="title.languageAdmin"/></h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


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
                        <option value="<s:property value="#lang.code"/>"><s:property value="#lang.code"/> &ndash;
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
        <tr>
            <th><s:text name="label.code"/></th>
            <th><s:text name="label.description"/></th>
            <th class="text-center"><s:text name="label.remove"/></th>
        </tr>
        <s:iterator var="lang" value="langs">
            <tr>
                <td>
                    <s:set var="labelModifier" value="'default'"/>
                    <s:set var="labelTitle" value="''"/>
                    <s:if test="#lang.default">
                        <s:set var="labelModifier" value="'success'"/>
                        <s:set var="labelTitle">
                            title="<s:text name="label.default"/>"
                        </s:set>
                    </s:if>
                    <code class="label label-<s:property value="labelModifier" />" <s:property
                            value="labelTitle"/>><s:property value="#lang.code"/></code>
                </td>
                <td><s:property value="#lang.descr"/></td>
                <td class="text-center">

                    <a
                            class="btn btn-xs btn-warning"
                            href="<s:url action="remove"><s:param name="langCode" value="#lang.code"/></s:url>"
                            title="<s:text name="label.remove" />: <s:property value="#lang.descr" />">
                        <span class="fa fa-trash-o fa-lg"></span>
                    </a>
                </td>

            </tr>
        </s:iterator>

    </table>
</s:form>
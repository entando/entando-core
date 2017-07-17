<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="strutsAction == 1">
    <s:set var="labelTitle" value="%{getText('title.bulk.content.category.join')}"/>
</s:if>
<s:elseif test="strutsAction == 4" >
    <s:set var="labelTitle" value="%{getText('title.bulk.content.category.rem')}"/>
</s:elseif>

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/Content"/>">
            <s:text name="breadcrumb.jacms.content.list" />
        </a>
    </li>
    <li class="page-title-container"><s:property value="%{#labelTitle}" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:property value="%{#labelTitle}" />&#32;&ndash;&#32;<s:text name="title.bulk.confirm" />
    <span class="pull-right">

    </span>
</h1>

<!-- Default separator -->
<div class="form-group-separator"></div>

<div id="main" role="main">
    <s:if test="hasErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.ActionErrors" />
            <ul>
                <s:if test="hasActionErrors()">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:if>
                    <s:if test="hasFieldErrors()">
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                    </s:if>
            </ul>
        </div>
    </s:if>
    <s:form action="apply" namespace="/do/jacms/Content/Category" >
        <p class="sr-only">
            <wpsf:hidden name="strutsAction"/>
            <s:iterator var="contentId" value="selectedIds" >
                <wpsf:hidden name="selectedIds" value="%{#contentId}" />
            </s:iterator>
            <s:iterator var="categoryCode" value="categoryCodes" >
                <wpsf:hidden name="categoryCodes" value="%{#categoryCode}" />
            </s:iterator>
        </p>

        <s:if test="strutsAction == 1">
            <s:text name="note.bulk.content.category.join.doYouConfirm" var="message"><s:param name="items" value="%{selectedIds.size()}" /></s:text>
            <s:set var="labelAction" value="%{getText('label.join')}"/>
            <s:set var="iconClass" value="%{'pficon-add-circle-o'}"/>
        </s:if>
        <s:elseif test="strutsAction == 4">
            <s:text name="note.bulk.content.category.rem.doYouConfirm" var="message"><s:param name="items" value="%{selectedIds.size()}" /></s:text>
            <s:set var="labelAction" value="%{getText('label.remove')}"/>
            <s:set var="iconClass" value="%{'fa fa-exclamation-circle'}"/>
        </s:elseif>

        <div class="blank-slate-pf mt-20">
            <div class="blank-slate-pf-icon">
                <span class="${iconClass}"></span>
            </div>
            <h2>
                <s:property value="#labelTitle"/>
            </h2>
            <p>
                <s:property value="#message" />
            </p>
            <ul class="list-inline mt-20">
                <s:iterator value="categoryCodes" var="categoryCode">
                    <s:set var="category" value="%{getCategory(#categoryCode)}" />
                    <li>
                        <span class="label label-info">
                            <span class="icon fa fa-tag"></span>&#32;
                            <span title="<s:property value="#category.getFullTitle(currentLang.code)"/>"><s:property value="#category.getShortFullTitle(currentLang.code)" /></span>&#32;
                            <wpsa:actionParam action="disjoin" var="actionName" >
                                <wpsa:actionSubParam name="categoryCode" value="%{#category.code}" />
                            </wpsa:actionParam>
                        </span>
                    </li>
                </s:iterator>
            </ul>
            <div class="blank-slate-pf-main-action">
                <s:if test="strutsAction == 1">
                    <wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-primary btn-lg">
                        <s:property value="%{#labelAction}" />
                    </wpsf:submit>
                </s:if>
                <s:else>
                    <wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-danger btn-lg">
                        <s:property value="%{#labelAction}" />
                    </wpsf:submit>
                </s:else>
            </div>
        </div>
        <fieldset class="col-xs-12 no-padding">
            <legend><s:text name="label.bulk.report.summary" /></legend>
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/bulk/inc/inc_contentSummary.jsp" />
        </fieldset>
    </s:form>
</div>

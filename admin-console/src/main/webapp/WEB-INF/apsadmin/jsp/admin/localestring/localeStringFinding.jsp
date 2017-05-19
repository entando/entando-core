<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li>
        <s:text name="title.languageAndLabels"/>
    </li>
    <li class="page-title-container">
        <s:text name="title.languageAdmin.labels"/>
    </li>
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
                <li>
                    <a href="<s:url namespace="/do/Lang" action="list" />"><s:text name="title.languageAdmin"/></a>
                </li>
                <li class="active">
                    <a href="<s:url namespace="/do/LocaleString" action="list" />"><s:text
                            name="title.languageAdmin.labels"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main" role="main">
    <div class="col-xs-12  ">
        <div class="well col-md-offset-3 col-md-6  ">
            <p class="search-label"><s:text name="label.message.seach"/></p>
            <s:form action="search" class="search-pf has-button " cssClass="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="text" class="sr-only">
                        <s:text name="label.search.by"/>&#32;<s:text name="label.text"/>
                    </label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="text" id="text" cssClass="form-control input-lg"
                                        title="%{getText('label.search.by') +' '+ getText('label.text')}"
                                        placeholder="%{getText('label.search.by') +' '+ getText('label.text')}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2"></label>
                    <div class="col-sm-9">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="label.search"/>
                        </wpsf:submit>
                    </div>
                </div>
            </s:form>
        </div>
    </div>

    <br>
    <br>

    <div class="row">
        <div class="col-sm-12">
            <a href="<s:url namespace="/do/LocaleString" action="new" />" class="btn btn-primary pull-right"
               style="margin-bottom: 5px">
                <s:text name="locale.addNewLabel"/>
            </a>
        </div>
    </div>


    <ul class="nav nav-tabs">
        <s:iterator value="systemLangs" var="lang">
            <s:set var="labelModifier" value="''"/>
            <s:set var="thModifier" value="''"/>
            <s:set var="labelTitle" value="''"/>
            <s:set var="activeTab" value="''"/>
            <s:if test="#lang.default">
                <s:set var="labelModifier" value="'*'"/>
                <s:set var="thModifier" value="' (Default Language)'"/>
                <s:set var="activeTab">
                    class=active
                </s:set>
            </s:if>
            <li <s:property value="activeTab"/>>
                <a class="text-capitalize" data-toggle="tab" href="#<s:property value="#lang.code"/>"
                   title="<s:property value="#lang.descr" />">
                    <s:property value="#lang.code"/><s:property value="labelModifier"/>
                </a>
            </li>
        </s:iterator>
    </ul>


    <div class="tab-content">
        <s:iterator value="systemLangs" var="lang">
            <s:set var="classModifier" value="''"/>
            <s:if test="#lang.default">
                <s:set var="classModifier" value="'in active'"/>
            </s:if>
            <div id="<s:property value="#lang.code"/>" class="tab-pane fade <s:property value="classModifier"/>">
                <s:form action="search" cssClass="form-horizontal margin-large-top">
                    <p class="sr-only">
                    <wpsf:hidden name="text"/>
                    <wpsf:hidden name="searchOption"/>
                    </p>
                    <s:set var="currentLocaleStrings" value="localeStrings"/>
                    <wpsa:subset source="currentLocaleStrings" count="20" objectName="groupContent"
                                 advanced="true" offset="5">
                        <s:set var="group" value="#groupContent"/>
                        <p class="sr-only">
                        <wpsf:hidden name="lastGroupBy"/>
                        <wpsf:hidden name="lastOrder"/>
                        </p>
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb" id="labelTable">
                                <thead>
                                    <tr>
                                        <th><s:text name="label.code"/></th>
                                        <th><s:property value="#lang.descr"/><s:property value="thModifier"/></th>
                                        <th class="text-center table-w-5"><s:text name="label.actions"/></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:if test="%{#currentLocaleStrings.size > 0}">
                                    <p class="sr-only"><s:text name="title.generalSettings.locale.installedLabels"/></p>
                                    <s:iterator var="key">
                                        <tr class="dl-horizontal dl-striped panel padding-base-top padding-base-bottom">
                                            <s:set var="currentLabel" value="labels[#key]"/>
                                            <td class="col-md-5"><s:property value="#key"/></td>
                                            <td class="col-md-6 td-grid-ellipsis">
                                                <s:if test="%{#currentLabel[#lang.code]==null || #currentLabel[#lang.code].length()==0}">
                                                    <abbr title="empty">&ndash;</abbr>
                                                </s:if>
                                                <s:else>
                                                    <s:property value="#currentLabel[#lang.code]"/>
                                                </s:else>
                                            </td>
                                            <td class="table-view-pf-actions text-center">
                                                <div class="dropdown dropdown-kebab-pf">
                                                    <p class="sr-only"><s:text name="label.actions"/></p>
                                                    <span class="btn btn-menu-right dropdown-toggle" type="button"
                                                          data-toggle="dropdown" aria-haspopup="true"
                                                          aria-expanded="false">
                                                        <span class="fa fa-ellipsis-v"></span>
                                                    </span>
                                                    <ul class="dropdown-menu dropdown-menu-right">
                                                        <li>
                                                            <a href="<s:url action="edit" namespace="/do/LocaleString" />?key=<s:property value="#key" />"
                                                               title="<s:text name="label.edit" />: <s:property value="#key" />">
                                                                <s:text name="label.edit"/>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a href="<s:url action="trash" namespace="/do/LocaleString"><s:param name="key" value="#key" /></s:url>"
                                                               title="<s:text name="label.remove" />: <s:property value="#key" />">
                                                                <s:text name="label.remove"/>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </s:if>
                                </tbody>
                            </table>
                        </div>
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span>
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                                </span>
                                <div class="mt-5">
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp"/>
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:form>
            </div>
        </s:iterator>
    </div>
</div>

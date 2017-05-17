<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a
            href="<s:url action="entry" namespace="/do/Admin/Database" />"
            title="<s:text name="note.goToSomewhere" />: <s:text name="title.databaseManagement" />"><s:text
                name="title.databaseManagement" /></a></li>
    <li class="page-title-container"><s:text
            name="title.databaseBackup.details" /></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.databaseBackup.details" />
        <span class="pull-right"><a tabindex="0" role="button"
                                    data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                    data-content="<s:text name="page.databaseBackup.help" />" data-placement="left"
                                    data-original-title=""> <i class="fa fa-question-circle-o"
                                       aria-hidden="true"></i>
            </a> </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable fade in">
        <button class="close" data-dismiss="alert">
            <span class="icon fa fa-times"></span>
        </button>
        <h2 class="h4 margin-none">
            <s:text name="message.title.ActionErrors" />
        </h2>
        <ul class="margin-base-top">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
                </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable fade in">
        <button class="close" data-dismiss="alert">
            <span class="icon fa fa-times"></span>
        </button>
        <h2 class="h4 margin-none">
            <s:text name="message.title.FieldErrors" />
        </h2>
        <ul class="margin-base-top">
            <s:iterator value="fieldErrors">
                <s:iterator value="value">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasActionMessages()">
    <div class="alert alert-info alert-dismissable fade in">
        <button class="close" data-dismiss="alert">
            <span class="icon fa fa-times"></span>
        </button>
        <h2 class="h4 margin-none">
            <s:text name="messages.confirm" />
        </h2>
        <ul class="margin-base-top">
            <s:iterator value="actionMessages">
                <li><s:property escapeHtml="false" /></li>
                </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="managerStatus != 0">
    <div class="alert alert-info">
        <s:text name="database.management.note.dump.in.progress" />
        ( <a href="<s:url namespace="/do/Admin/Database" action="entry" />"
             class="alert-link"><s:text name="database.management.refresh" /></a>
        )
    </div>
</s:if>
<s:else>
    <s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
    <s:if test="null == #dumpReportVar">
        <div class="alert alert-warning">
            <s:text name="database.management.note.dump.not.available" />
        </div>
    </s:if>
    <s:else>
        <div class="well panel">
            <dl class="margin-none dl-horizontal">
                <dt>
                    <s:text name="database.management.label.date" />
                </dt>
                <dd>
                    <code>
                        <s:date name="#dumpReportVar.date"
                                format="EEEE dd/MMM/yyyy, HH:mm:ss" />
                    </code>
                </dd>
                <dt>
                    <s:text name="database.management.label.time.required" />
                </dt>
                <dd>
                    <code>
                        <s:property value="#dumpReportVar.requiredTime" />
                        &#32;
                        <s:text name="database.management.label.milliseconds" />
                    </code>
                </dd>
            </dl>
        </div>

        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th>Component Name</th>
                    <th class="text-center">Dump Date</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator var="componentHistoryVar"
                            value="#dumpReportVar.componentsHistory">
                    <tr>
                        <td><s:set var="labelComponentDescrVar"
                               value="%{#componentHistoryVar.componentCode + '.name'}" /> <s:text
                               name="%{#labelComponentDescrVar}" var="componentDescrVar" /> <s:if
                               test="%{#componentDescrVar.equals(#labelComponentDescrVar)}">
                                <s:property value="#componentHistoryVar.componentCode" />
                            </s:if> <s:else>
                                <s:property value="#componentDescrVar" />
                            </s:else></td>
                        <td class="text-center"><code>
                                <s:date name="#componentHistoryVar.date"
                                        format="dd/MM/yyyy HH:mm:ss" />
                            </code></td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>

        <p class="help-block text-center">
            <button type="button" data-toggle="collapse"
                    data-target="#datasource-details" class="btn btn-link">
                <s:text name="DataSource Details" />
                <span class="icon fa fa-chevron-down"></span>
            </button>
        </p>
        <div id="datasource-details" class="collapse">
            <s:iterator var="dataSourceNameVar"
                        value="#dumpReportVar.dataSourceNames" begin="0"
                        end="%{(#dumpReportVar.dataSourceNames.size()/2)-1}" status="status">
                <table class="table table-striped table-bordered">
                    <caption>
                        <span> <span lang="en">Datasource</span>&#32;<em><s:property
                                    value="#dataSourceNameVar" /></em>
                        </span>
                    </caption>
                    <s:set var="tableReportsVar"
                           value="#dumpReportVar.dataSourcesReports[#dataSourceNameVar]" />
                    <thead>
                        <tr>
                            <th><s:text name="database.management.label.table" /></th>
                            <th class="text-center"><s:text
                                    name="database.management.label.rows" /></th>
                            <th class="text-center"><s:text
                                    name="database.management.label.time.required" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator var="tableReportVar" value="#tableReportsVar">
                            <tr>
                                <td><s:if test="#tableReportVar.rows == 0">
                                        <s:property value="#tableReportVar.tableName" />
                                    </s:if> <s:else>
                                        <a
                                            title="<s:text name="database.management.label.download" />: <s:property value="#dataSourceNameVar" />/<s:property value="#tableReportVar.tableName" />"
                                            href="<s:url namespace="/do/Admin/Database" action="extractTableDump" >
                                                <s:param name="tableName" value="#tableReportVar.tableName" />
                                                <s:param name="dataSourceName" value="#dataSourceNameVar" />
                                                <s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
                                            </s:url>"><span
                                                class="icon fa fa-arrow-circle-o-down"></span>&#32;<s:property
                                                value="#tableReportVar.tableName" /></a>
                                    </s:else></td>
                                <td class="text-center text-nowrap"><s:property
                                        value="#tableReportVar.rows" /></td>
                                <td class="text-center text-nowrap"><s:property
                                        value="#tableReportVar.requiredTime" /> &#32;<abbr
                                        title="<s:text name="database.management.label.milliseconds" />"><s:text
                                            name="database.management.label.milliseconds.short" /></abbr></td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </s:iterator>
            <s:iterator var="dataSourceNameVar"
                        value="#dumpReportVar.dataSourceNames"
                        begin="%{(#dumpReportVar.dataSourceNames.size()/2)}" status="status">
                <table class="table table-striped table-bordered">
                    <caption>
                        <span> <span lang="en">Datasource</span>&#32;<em><s:property
                                    value="#dataSourceNameVar" /></em>
                        </span>
                    </caption>
                    <s:set var="tableReportsVar"
                           value="#dumpReportVar.dataSourcesReports[#dataSourceNameVar]" />
                    <thead>
                        <tr>
                            <th><s:text name="database.management.label.table" /></th>
                            <th class="text-center"><s:text
                                    name="database.management.label.rows" /></th>
                            <th class="text-center"><s:text
                                    name="database.management.label.time.required" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator var="tableReportVar" value="#tableReportsVar">
                            <tr>
                                <td><s:if test="#tableReportVar.rows == 0">
                                        <s:property value="#tableReportVar.tableName" />
                                    </s:if> <s:else>
                                        <a
                                            title="<s:text name="database.management.label.download" />: <s:property value="#dataSourceNameVar" />/<s:property value="#tableReportVar.tableName" />"
                                            href="<s:url namespace="/do/Admin/Database" action="extractTableDump" >
                                                <s:param name="tableName" value="#tableReportVar.tableName" />
                                                <s:param name="dataSourceName" value="#dataSourceNameVar" />
                                                <s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
                                            </s:url>"><span
                                                class="icon fa fa-arrow-circle-o-down"></span>&#32;<s:property
                                                value="#tableReportVar.tableName" /></a>
                                    </s:else></td>
                                <td class="text-center text-nowrap"><s:property
                                        value="#tableReportVar.rows" /></td>
                                <td class="text-center text-nowrap"><s:property
                                        value="#tableReportVar.requiredTime" />&#32;<abbr
                                        title="<s:text name="database.management.label.milliseconds" />"><s:text
                                            name="database.management.label.milliseconds.short" /></abbr></td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </s:iterator>
        </div>
        <s:if test="restoreEnabled">
            <hr />
            <s:form namespace="/do/Admin/Database" method="get"
                    cssClass="margin-more-top">
                <p class="text-center">
                <wpsf:hidden name="subFolderName"
                             value="%{#dumpReportVar.subFolderName}" />
                <wpsf:submit type="button" action="restoreIntro"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="database.management.label.restore" />
                </wpsf:submit>
            </p>
        </s:form>
    </s:if>
</s:else>
</s:else>
<br>
<br>

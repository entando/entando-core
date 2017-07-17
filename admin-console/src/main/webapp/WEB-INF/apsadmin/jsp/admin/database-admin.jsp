<%@ page contentType="charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li class="page-title-container"><s:text
            name="title.databaseManagement" /></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.databaseManagement" />
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
<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" />
        </strong>
        <ul class="margin-base-top">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
                </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasFieldErrors()">
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong>
            <s:text name="message.title.FieldErrors" />
        </strong>
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
    <div class="alert alert-success">
        <span class="pficon pficon-ok"></span>
        <strong><s:text name="messages.confirm" /></strong>
        <s:iterator value="actionMessages">
            <li><s:property escapeHtml="false" /></li>
            </s:iterator>
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
    <div class="row form-group">
        <div class="col-sm-12">
            <a class="btn btn-primary pull-right"
               href="<s:url namespace="/do/Admin/Database" action="backupIntro" />" style="margin-bottom: -10px">
                <s:text name="database.management.label.backup.create" />
            </a>
        </div>
    </div>
    <s:set var="dumpReportsVar" value="dumpReports" />
    <s:if test="null == #dumpReportsVar || #dumpReportsVar.isEmpty()">
        <p class="alert alert-info">
            <s:text name="database.management.note.no.backups" />
        </p>
    </s:if>
    <s:else>
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1"><s:text
                            name="database.management.label.number" /></th>
                    <th class="text-center"><s:text
                            name="database.management.label.date" /></th>
                    <th class="text-center"><s:text
                            name="database.management.label.time.required" /></th>
                    <th class="text-center col-xs-1 col-sm-1 col-md-1 col-lg-1"><s:text
                            name="label.edit" /></th>
                <tr>
            </thead>
            <tbody>
                <s:iterator var="dumpReportVar" value="#dumpReportsVar"
                            status="status">
                    <tr>
                        <td class="text-nowrap text-center"><s:property
                                value="#status.count" /></td>
                        <td class="text-nowrap text-center"><a
                                title="<s:text name="database.management.label.details" />:&#32;<s:property value="#status.count" />&#32;&ndash;&#32;<s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />"
                                href="<s:url namespace="/do/Admin/Database" action="entryBackupDetails" >
                                    <s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
                                </s:url>"><code>
                                    <s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />
                                </code></a></td>
                        <td class="text-nowrap text-center"><s:property
                                value="#dumpReportVar.requiredTime" />&#32;<s:text
                                name="database.management.label.milliseconds" /></td>
                        <td class="text-nowrap text-center"><a
                                title="<s:text name="database.management.label.remove" />:&#32;<s:property value="#status.count" />&#32;&ndash;&#32;<s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />"
                                href="<s:url namespace="/do/Admin/Database" action="trashBackup" >
                                    <s:param name="subFolderName" value="#dumpReportVar.subFolderName" />
                                </s:url>"><i
                                    class="glyphicon glyphicon-trash"></i></a></td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
    </s:else>
</s:else>

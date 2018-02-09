<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a
            href="<s:url action="entry" namespace="/do/Admin/Database" />"
            title="<s:text name="note.goToSomewhere" />: <s:text name="title.databaseManagement" />"><s:text
                name="title.databaseManagement" /></a></li>
    <li><s:text name="title.databaseBackup.details" /></li>
    <li class="page-title-container"><s:text
            name="title.databaseBackup.restore" /></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.databaseBackup.restore" />
    <span class="pull-right"> <a tabindex="0" role="button"
                                 data-toggle="popover" data-trigger="focus" data-html="true"
                                 title="title.databaseManagement" data-content="<s:text name="page.databaseBackup.help" />"
                                 data-placement="left" data-original-title=""><i
                class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<s:if test="managerStatus != 0">
    <div class="alert alert-info">
        <s:text name="database.management.note.dump.in.progress" />
        ( <a href="<s:url namespace="/do/Admin/Database" action="entry" />"
             class="alert-link"><s:text name="database.management.refresh" /></a>
        )
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
        </dl>
    </div>
    <s:set var="dumpReportVar" value="getDumpReport(subFolderName)" />
    <s:set var="installedComponentsVar" value="currentComponents" />
    <div class="row">
        <div class="col-md-6 col-lg-6">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h2 class="h4 margin-none margin-small-bottom">
                        <s:text name="database.management.label.components.current" />
                    </h2>
                    <s:text name="database.management.note.components.current" />
                    <br />
                    <ul class="margin-base-top list-unstyled">
                        <li class="margin-small-bottom"><code>entandoCore</code></li>
                            <s:iterator var="currentComponentVar"
                                        value="#installedComponentsVar">
                            <li class="margin-small-bottom"><s:set
                                    var="labelComponentDescrVar"
                                    value="%{#currentComponentVar.code + '.name'}" /> <s:text
                                    name="%{#labelComponentDescrVar}" var="componentDescrVar" /> <code
                                    title="<s:property value="#currentComponentVar.description" />">
                                    <s:property
                                        value="%{#componentDescrVar.equals(#labelComponentDescrVar) ? #currentComponentVar.code : #componentDescrVar }" />
                                </code></li>
                            </s:iterator>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-6">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h2 class="h4 margin-none margin-small-bottom">
                        <s:text name="database.management.label.components.backup" />
                    </h2>
                    <s:text name="database.management.note.components.backup" />
                    <br />
                    <ul class="margin-base-top list-unstyled">
                        <s:iterator var="currentComponentVar"
                                    value="#dumpReportVar.componentsHistory">
                            <li class="margin-small-bottom"><s:set
                                    var="installedCheckVar" value="%{false}" /> <s:iterator
                                    value="#installedComponentsVar" var="c">
                                    <s:if
                                        test="#c.code.equals(#currentComponentVar.componentCode) || #currentComponentVar.componentCode.equals('entandoCore')">
                                        <s:set var="installedCheckVar" value="%{true}" />
                                    </s:if>
                                </s:iterator> <s:if test="%{#installedCheckVar}">
                                    <span class="icon fa fa-check text-muted"></span>
                                </s:if> <s:else>
                                    &emsp;
                                </s:else> <s:set var="labelComponentDescrVar"
                                                 value="%{#currentComponentVar.componentCode + '.name'}" /> <s:text
                                                 name="%{#labelComponentDescrVar}" var="componentDescrVar" /> <code>
                                                 <s:property
                                                     value="%{#componentDescrVar.equals(#labelComponentDescrVar) ? #currentComponentVar.componentCode : #componentDescrVar }" />
                                </code></li>
                            </s:iterator>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 col-lg-12">
            <s:set var="restoreCheckVar"
                   value="%{checkRestore(#installedComponentsVar, #dumpReportVar)}" />
            <s:if test="#restoreCheckVar">
                <p class="text-center text-success">
                    <span class="icon fa fa-check">&#32;<s:text
                            name="database.management.note.restore.do.fits" /></span>
                </p>
            </s:if>
            <s:else>
                <p class="text-center text-warning">
                    <s:text name="database.management.note.restore.do.not.fits" />
                </p>
            </s:else>
            <s:form action="restoreBackup" method="get"
                    namespace="/do/Admin/Database">
                <p class="pull-right">
                <wpsf:hidden name="subFolderName" value="%{subFolderName}" />
                <wpsf:submit type="button"
                             cssClass="btn btn-%{#restoreCheckVar ? 'success' : 'warning'}">
                    <s:text name="database.management.label.restore" />
                </wpsf:submit>
                </p>
            </s:form>
        </div>
    </div>
</s:else>

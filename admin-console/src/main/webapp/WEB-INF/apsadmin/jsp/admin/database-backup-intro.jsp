<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a
            href="<s:url action="entry" namespace="/do/Admin/Database" />"
            title="<s:text name="note.goToSomewhere" />: <s:text name="title.databaseManagement" />"><s:text
                name="title.databaseManagement" /></a></li>
    <li><s:text name="title.databaseBackup.new" /></li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.databaseBackup.new" />
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
<s:if test="managerStatus != 0">
    <div class="alert alert-info">
        <s:text name="database.management.note.dump.in.progress" />
        ( <a href="<s:url namespace="/do/Admin/Database" action="entry" />"
             class="alert-link"><s:text name="database.management.refresh" /></a>
        )
    </div>
</s:if>
<s:else>
    <s:set var="currentComponentsVar" value="currentComponents" />
    <p>
        <s:text name="database.management.note.backup" />
    </p>
    <dl class="dl-horizontal">
        <dt>
            <abbr
                title="<s:text name="database.management.label.component" /> Core (core)">Core</abbr>
        </dt>
        <dd>
            <s:set var="tableMappingVar" value="entandoTableMapping" />
            <s:include
                value="/WEB-INF/apsadmin/jsp/admin/inc/datasource-table-names.jsp" />
            <hr class="visible-xs hidden-sm hidden-md hidden-lg" />
        </dd>
        <s:set var="currentComponentsVar" value="currentComponents" />
        <s:iterator var="currentComponentVar" value="#currentComponentsVar">
            <dt class="margin-medium-top">
                <abbr
                    title="<s:text name="database.management.label.component" />&#32;<s:property value="#currentComponentVar.description" /> (<s:property value="#currentComponentVar.code" />)"><s:property
                        value="#currentComponentVar.description" /></abbr>
            </dt>
            <dd>
                <s:set var="tableMappingVar"
                       value="#currentComponentVar.tableMapping" />
                <s:include
                    value="/WEB-INF/apsadmin/jsp/admin/inc/datasource-table-names.jsp" />
                <hr class="visible-xs hidden-sm hidden-md hidden-lg" />
            </dd>
        </s:iterator>
    </dl>
    <div class="pull-right">
        <div class="row form-group">
            <div class="col-sm-12">
                <s:form action="executeBackup" namespace="/do/Admin/Database"
                        method="get">
                    <a class="btn btn-default"
                       href="<s:url namespace="/do/Admin/Database" action="entry" />">
                        <s:text name="database.management.label.go.to.list" />
                    </a>
                    <wpsf:submit type="button" action="executeBackup"
                                 value="%{getText('database.management.label.backup.execute')}"
                                 cssClass="btn btn-primary">
                        <s:text name="database.management.label.backup.execute" />
                    </wpsf:submit>
                </s:form>
            </div>
        </div>
    </div>
</s:else>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a
            href="<s:url action="entry" namespace="/do/Admin/Database" />"
            title="<s:text name="note.goToSomewhere" />: <s:text name="title.databaseManagement" />"><s:text
                name="title.databaseManagement" /></a></li>
    <li class="page-title-container"><s:text
            name="title.databaseBackup.remove" /></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.databaseBackup.remove" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<s:set var="subFolderNameVar" value="subFolderName" />
<s:set var="dumpReportVar" value="getDumpReport(#subFolderNameVar)" />
<div class="text-center">
    <s:form action="deleteBackup">
        <p class="sr-only">
        <wpsf:hidden name="subFolderName" />
    </p>

    <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
    <p class="esclamation-underline">
        <s:text name="title.databaseBackup.remove" />
        <s:text name="database.management.label.remove.areyousure" />
    </p>
    <p class="esclamation-underline">     
            <s:date name="#dumpReportVar.date" format="dd/MM/yyyy HH:mm:ss" />?
    </p>
    <br>
    <div class="text-center margin-large-top">
        <a class="btn btn-default button-fixed-width"
           href="<s:url namespace="/do/Admin/Database" action="entry" />">
            <s:text name="label.back" />
        </a>
        <wpsf:submit type="button"
                     cssClass="btn btn-danger button-fixed-width">
            <s:text name="label.confirm" />
        </wpsf:submit>
    </div>
</s:form>
</div>

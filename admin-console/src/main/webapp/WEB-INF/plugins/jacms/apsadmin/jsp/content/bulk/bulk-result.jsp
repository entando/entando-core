<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="command" value="command" />

<s:if test="%{#command == null}" >
    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <s:text name="note.bulk.report.notFound" />
    </div>
</s:if>
<s:else>

    <s:set var="labelTitle" value="%{getText('title.bulk.' + #command.name)}"/>

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
        <s:property value="%{#labelTitle}" />&#32;&ndash;&#32;<s:text name="title.bulk.result" />
    </h1>

    <!-- Default separator -->
    <div class="form-group-separator"></div>

    <div id="main" role="main">

        <div class="alert alert-success mt-20">
            <span class="pficon pficon-ok"></span>

            <s:set var="report" value="#command.report" />
            <p>
                <strong><s:text name="label.bulk.status" /></strong>: <s:text name="name.bulk.status.%{#command.status}" />
            </p>
            <s:if test="%{#report.endingTime != null}" >
                <p>
                    <strong><s:text name="label.bulk.report.endingTime" /></strong>: <s:date name="#report.endingTime" format="dd/MM/yyyy HH:mm:ss"/>
                </p>
            </s:if>
            <p>
                <strong><s:text name="label.bulk.report.total" /></strong>:
                <s:text name="label.bulk.report.total.count" >
                    <s:param name="applyTotal" value="%{#report.applyTotal}" />
                    <s:param name="total" value="%{#report.total}" />
                </s:text>
            </p>
            <p>
                <strong><s:text name="label.bulk.report.successCount" /></strong>: <s:property value="%{#report.applySuccesses}" />
            </p>
            <p>
                <strong><s:text name="label.bulk.report.errorCount" /></strong>: <s:property value="%{#report.applyErrors}" />
            </p>

            <s:if test="%{#report.endingTime == null}" >
                <s:form action="viewResult" namespace="/do/jacms/Content/Bulk" method="get" >
                    <wpsf:hidden name="commandId"/>
                    <s:set var="labelAction" value="%{getText('label.bulk.report.refresh')}"/>
                    <wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-success">
                        <span class="icon fa fa-times-circle"></span>
                        <s:property value="%{#labelAction}" />
                    </wpsf:submit>
                </s:form>
            </s:if>
        </div>

        <s:if test="%{#report.applyErrors > 0}" >
            <div class="alert alert-danger">
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.ActionErrors" />
                <ul>
                    <s:iterator var="error" value="%{#report.errors}" >
                        <jacmswpsa:content contentId="${error.key}" var="content" workVersion="true" />
                        <s:text name="error.bulk.%{#command.name}.%{#error.value}" var="errorDescr" />
                        <li>
                            <s:text name="label.bulk.report.error" >
                                <s:param name="content" value="%{#content.description}" />
                                <s:param name="error" value="%{#errorDescr}" />
                            </s:text>
                        </li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

    </div>
</s:else>

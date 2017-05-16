<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="list" namespace="/do/PageModel"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
            <s:text name="title.pageModelManagement" /></a></li>
    <li class="page-title-container"><s:text name="title.pageModelDetail" /></li>

</ol>

<h1 class="page-title-container">
    <s:text name="title.pageModelDetail" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main">

    <table class="table table-bordered">
        <tr>
            <th class="td-pagetree-width"><s:text name="label.description" /></th>
            <td><s:property value="description" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.code" /></th>
            <td>/<s:property value="code" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.pluginCode" /></th>
            <td><s:property value="pluginCode" /></td>
        </tr>
        <%--
        <tr>
                <th class="td-pagetree-width"><s:text name="label.configuration" /></th>
                <td><s:property value="xmlConfiguration" /></td>
        </tr>
        --%>
        <tr>
            <th class="td-pagetree-width"><s:text name="label.template" /></th>
            <td>
                <s:set var="template"><s:property value="template" /></s:set>
                <pre><s:property
                        value="#template.replaceAll('\t', '&emsp;').replaceAll('\r', '').replaceAll('\n', '<br />')"
                        escapeCsv="false"
                        escapeHtml="false"
                        escapeJavaScript="false"
                        escapeXml="false"
                        />
                </pre>
            </td>
        </tr>
    </table>

    <p class="text-right">
        <a
            class="btn btn-primary"
            href="<s:url namespace="/do/PageModel" action="edit">
                <s:param name="code"><s:property value="code" /></s:param>
            </s:url>">
            <s:text name="label.edit" />
        </a>
    </p>
    <hr>
    <s:include value="/WEB-INF/apsadmin/jsp/portal/model/include/pageModel-references.jsp" />

    <wpsa:hookPoint key="core.pageModelDetails" objectName="hookPointElements_core_pageModelDetails">
        <s:iterator value="#hookPointElements_core_pageModelDetails" var="hookPointElementVar">
            <wpsa:include value="%{#hookPointElementvar.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>



</div>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<jsp:include page="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/portal/configPage-extra.jsp" />

<c:set var="carriageReturnChar" value="\r"/>
<c:set var="tabChar" value="\t"/>

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
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.pageModelManagement.pagemodels" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="mb-20">
    <div class="col-xs-12">
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
                <td>
                    <s:property value="pluginCode" />
                </td>
            </tr>
            <tr>
                <th class="td-pagetree-width"><s:text name="label.configuration" /></th>
                <td>
                    <c:set var="xmlConfigurationVar"><s:property value="xmlConfiguration" /></c:set>
                    <c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(xmlConfigurationVar, tabChar, '&emsp;'),carriageReturnChar, '')}" />
                    <pre><code><c:out value="${ESCAPED_STRING}" escapeXml="true" /></code></pre>
                </td>
            </tr>
            <tr>
                <th class="td-pagetree-width"><s:text name="label.template" /></th>
                <td>
                    <c:set var="templateVar"><s:property value="template" /></c:set>
                    <c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(templateVar, tabChar, '&emsp;'),carriageReturnChar, '')}" />
                    <pre><code><c:out value="${ESCAPED_STRING}" escapeXml="true" /></code></pre>
                </td>
            </tr>
            <tr>
                <th class="td-pagetree-width"><s:text name="label.template.preview" /></th>
                <td>
                    <!-- the grid (or alert) will be appended here -->
                    <div class="grid-container"></div>
                </td>
            </tr>
        </table>

        <p class="text-right">
            <a class="btn btn-primary"
               href="<s:url namespace="/do/PageModel" action="edit">
                   <s:param name="code"><s:property value="code" /></s:param>
               </s:url>">
                <s:text name="label.edit" />
            </a>
        </p>
    </div>
    <hr>
</div>
<div class="mb-20">
    <s:include value="/WEB-INF/apsadmin/jsp/portal/model/include/pageModel-references.jsp" />
    <wpsa:hookPoint key="core.pageModelDetails" objectName="hookPointElements_core_pageModelDetails">
        <s:iterator value="#hookPointElements_core_pageModelDetails" var="hookPointElementVar">
            <wpsa:include value="%{#hookPointElementvar.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>
</div>

<script>
    jQuery(function () {
        $('.autotextarea').on('focus', function (ev) {
            var t = $(this);
            var h = screen.availHeight;
            if (h) {
                t.css('height', (h / 2) + 'px');
            }
        });
    });
</script>

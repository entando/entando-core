<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>


<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="list" namespace="/do/Portal/GuiFragment"></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.guiFragmentManagement" />">
            <s:text name="title.guiFragmentManagement" /></a>
    </li>
    <li class="page-title-container"><s:text name="title.guiFragmentDetail" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.guiFragmentDetail" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.guiFragments.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main">
    <table class="table table-bordered ">
        <tr>
            <th class="td-pagetree-width"><s:text name="label.guiFragment" /></th>
            <td><s:property value="code" /></td>
        </tr>
        <tr>
            <th><s:text name="label.widgetType" /></th>
            <td>
                <s:set value="%{getWidgetType(widgetTypeCode)}" var="widgetTypeVar" />
                <s:property value="getTitle(#widgetTypeVar.code, #widgetTypeVar.titles)" />
            </td>
        </tr>
        <tr>
            <th><s:text name="label.pluginCode" /></th>
            <td><s:property value="pluginCode"/></td>
        </tr>
    </table>
    <a href="<s:url namespace="/do/Portal/GuiFragment" action="edit"> <s:param name="code"><s:property value="code" /></s:param>
       </s:url>" class="btn btn-primary pull-right">
        <s:text name="label.edit" />
    </a>
    <br>
    <hr>
    <!--
    <wpsa:hookPoint key="core.groupDetails" objectName="hookPointElements_core_groupDetails">
        <s:iterator value="#hookPointElements_core_groupDetails" var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
        </s:iterator>
    </wpsa:hookPoint>
    -->
    <s:include value="/WEB-INF/apsadmin/jsp/portal/guifragment/include/guiFragmentInfo-references.jsp" />
</div>

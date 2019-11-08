<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/entando-widget-icons.css"/>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li class="page-title-container"><s:text name="title.widgetManagement" /></li>
</ol>

<h1 class="page-title-container">
    <span class="page-title-big"><s:text name="title.widgetManagement" /></span>
    <span class="pull-right"><a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="widget.title.help" />" data-placement="left" ><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></span>
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br />
<div id="main" role="main">
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></h2></strong>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <div class="row">
        <div class="form-group col-md-12">
            <a class="btn btn-primary pull-right" href="<s:url namespace="/do/Portal/WidgetType" action="newWidget" />">
                <s:text name="label.add" />
            </a>
        </div>
    </div>
    <s:set var="pluginTitleCheck" value="'false'" />
    <s:set var="showletFlavours" value="showletFlavours" />
    <s:set var="showletTypeApiMappingsVar" value="showletTypeApiMappings" />
    <s:iterator var="showletFlavour" value="#showletFlavours">
        <s:set var="firstType" value="%{#showletFlavour.get(0)}"></s:set>
            <!-- ----------Section legend--------- -->
            <legend>
            <s:if test="%{#firstType.optgroup == 'stockShowletCode'}">
                <s:text name="title.widgetManagement.widgets.stock" />
            </s:if>
            <s:elseif test="%{#firstType.optgroup == 'customShowletCode'}">
                <s:text name="title.widgetManagement.widgets.custom" />
            </s:elseif>
            <s:elseif test="%{#firstType.optgroup == 'userShowletCode'}">
                <s:text name="title.widgetManagement.widgets.user" />
            </s:elseif>
            <s:else>
                <s:if test="#pluginTitleCheck.equals('false')">
                    <span class="sr-only"><s:text name="title.widgetManagement.widgets.plugin" /></span>&#32;
                </s:if>
                <s:set var="pluginTitleCheck" value="'true'" ></s:set>

                    <wpsa:set var="pluginPropertyName" value="%{getText(#firstType.optgroup + '.name')}" />
                    <wpsa:set var="pluginPropertyCode" value="%{getText(#firstType.optgroup + '.code')}" />

                <s:property value="%{#pluginPropertyName}" />
            </s:else>
        </legend>

        <!-- HEADER table Start -->
        <div class="col-xs-12">
            <table class="table table-striped table-bordered table-hover" id="labelTable">
                <thead>
                    <tr>
                        <th><s:text name="widget.name" /></th>
                        <th><s:text name="widget.code" /></th>
                        <th class="table-w-5 text-center "><span title="<s:text name="title.widgetManagement.howmanypages.long" />"><s:text name="widget.time.used" /></span></th>
                        <th class="table-w-5 text-center"><s:text name="title.pageActions" /></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator var="showletType" value="#showletFlavour" >
                        <s:set var="showletUtilizers" value="%{getWidgetUtilizerCodes(#showletType.key)}" ></s:set>
                        <s:set var="concreteShowletTypeVar" value="%{getShowletType(#showletType.key)}"></s:set>
                            <tr>
                                <td>
                                    <div class="list-view-pf-left">
                                        <span class="fa fa-default list-view-pf-icon-sm widget-icon fa-<s:property value="#showletType.key" />"></span>
                                    <a class="ml-10" href="<s:url namespace="/do/Portal/WidgetType" action="edit">
                                           <s:param name="widgetTypeCode" value="#showletType.key" /></s:url>" title="<s:text name="label.configWidget" />: <s:property value="#showletType.value" />" >
                                       <s:property value="#showletType.value" />
                                    </a>
                                    <s:if test="%{#concreteShowletTypeVar.mainGroup != null && !#concreteShowletTypeVar.mainGroup.equals('free')}"><span class="text-muted icon fa fa-lock"></span></s:if>
                                    </div>
                                </td>
                                <td class="td-line-height-custom">
                                <s:property value="#showletType.key" />
                            </td>
                            <td class="text-center td-line-height-custom">
                                <span title="<s:text name="title.widgetManagement.howmanypages.long" />: <s:property value="#showletType.value" />">
                                    <s:property value="#showletUtilizers.size()" />
                                </span>
                            </td>

                            <td class="text-center table-view-pf-actions">
                                <wp:ifauthorized permission="superuser">
                                    <s:if test="#concreteShowletTypeVar.isLogic()">
                                        <s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#concreteShowletTypeVar.parentType.code]" />
                                    </s:if>
                                    <s:elseif test="null != #concreteShowletTypeVar.typeParameters && #concreteShowletTypeVar.typeParameters.size() > 0">
                                        <s:set var="relatedApiMethodVar" value="#showletTypeApiMappingsVar[#concreteShowletTypeVar.code]" />
                                    </s:elseif>
                                    <s:if test="%{(null != #relatedApiMethodVar) || (null != #concreteShowletTypeVar.typeParameters && #concreteShowletTypeVar.typeParameters.size() > 0) ||(#firstType.optgroup == 'userShowletCode' && !#concreteShowletTypeVar.isLocked() || (#showletUtilizers != null && #showletUtilizers.size() > 0))}">
                                        <div class="dropdown dropdown-kebab-pf">
                                            <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <s:if test="#showletUtilizers != null && #showletUtilizers.size() > 0">
                                                    <li>
                                                        <a href="<s:url namespace="/do/Portal/WidgetType" action="viewWidgetUtilizers">
                                                               <s:param name="widgetTypeCode" value="#showletType.key" /></s:url>" title="<s:text name="title.widgetManagement.howmanypages.goToSee" />:
                                                           <s:property value="#showletType.value" />" ><s:text name="info" />
                                                        </a>
                                                    </li>
                                                </s:if>
                                                <s:if test="null != #relatedApiMethodVar">
                                                    <s:if test="#concreteShowletTypeVar.isLogic()">
                                                        <s:url action="newService" namespace="/do/Api/Service" var="newServiceUrlVar">
                                                            <s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
                                                            <s:param name="namespace" value="#relatedApiMethodVar.namespace" />
                                                            <s:param name="widgetTypeCode" value="#concreteShowletTypeVar.code" />
                                                        </s:url>
                                                    </s:if>
                                                    <s:else>
                                                        <s:url action="newService" namespace="/do/Api/Service" var="newServiceUrlVar">
                                                            <s:param name="resourceName" value="#relatedApiMethodVar.resourceName" />
                                                            <s:param name="namespace" value="#relatedApiMethodVar.namespace" />
                                                        </s:url>
                                                    </s:else>
                                                    <li>
                                                        <a href="<s:property value="#newServiceUrlVar" escapeHtml="false" />" title="<s:text name="note.api.apiMethodList.createServiceFromMethod" />: <s:property value="#relatedApiMethodVar.methodName" />" >
                                                            <s:text name="widget.add.Service" />
                                                        </a>
                                                    </li>
                                                    <s:set var="newServiceUrlVar" value="null" />
                                                </s:if>
                                                <s:set var="relatedApiMethodVar" value="null" />
                                                <s:if test="null != #concreteShowletTypeVar.typeParameters && #concreteShowletTypeVar.typeParameters.size() > 0">
                                                    <li>
                                                        <a href="<s:url namespace="/do/Portal/WidgetType" action="newUserWidget">
                                                               <s:param name="parentShowletTypeCode" value="#showletType.key" /></s:url>" title="<s:text name="label.userWidget.new.from" />: <s:property value="#showletType.value" />">
                                                           <s:text name="widget.add.Widget" />
                                                        </a>
                                                    </li>
                                                </s:if>
                                                <s:if test="#firstType.optgroup == 'userShowletCode' && !#concreteShowletTypeVar.isLocked() && (#showletUtilizers == null || #showletUtilizers.size() == 0)">
                                                    <li>
                                                        <a href="<s:url namespace="/do/Portal/WidgetType" action="trash"><s:param name="widgetTypeCode" value="#showletType.key" /></s:url>" title="<s:text name="label.remove" />:
                                                           <s:property value="#showletType.value" />" >
                                                            <s:text name="label.remove" />
                                                        </a>
                                                    </li>
                                                </s:if>
                                            </ul>
                                        </div>
                                    </s:if>
                                </wp:ifauthorized>
                                <wpsa:hookPoint key="core.showletType.list.table.td" objectName="hookPointElements_core_showletType_list_table_td">
                                    <s:iterator value="#hookPointElements_core_showletType_list_table_td" var="hookPointElement">
                                        <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
                                    </s:iterator>
                                </wpsa:hookPoint>
                            </td>
                        </tr>
                    </s:iterator>                          
                </tbody>
            </table>
        </div>
        <s:set var="showletUtilizers"></s:set>
    </s:iterator>
</div>

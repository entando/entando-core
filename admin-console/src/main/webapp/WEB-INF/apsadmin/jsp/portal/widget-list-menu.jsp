<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/entando-widget-icons.css"/>

<div class="right-menu-title">
    <s:text name="title.widgetlist" /><span class="pull-right drawer-pf-icons-right-menu"><i id="widgetGrid" class="fa fa-th-large pointer" aria-hidden="true"></i>&nbsp;&nbsp;<i id="widgetList" class="fa fa-th-list pointer"  aria-hidden="true"></i></span>
    <input id="search-widget" class="input-pf-right-menu" type="text" placeholder="<s:text name="widget.search" />" />
</div>

<div id="main" role="main">
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
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
    <s:set var="pluginTitleCheck" value="'false'" />
    <s:set var="showletFlavours" value="showletFlavours" />
    <s:set var="showletTypeApiMappingsVar" value="showletTypeApiMappings" />
    <div class="list-group list-view-pf widget-list">
        <s:property value="widgets" />
        <s:iterator var="showletFlavour" value="#showletFlavours">
            <s:set var="firstType" value="%{#showletFlavour.get(0)}"></s:set>
                <div class="widget-spacer">
                    <h2 class="panel-title widget-title">
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
                        <s:text name="%{#pluginPropertyName}" />
                    </s:else>
                </h2>
            </div>
            <s:iterator var="showletType" value="#showletFlavour" >
                <s:set var="showletUtilizers" value="getShowletUtilizers(#showletType.key)" ></s:set>
                <s:set var="concreteShowletTypeVar" value="%{getShowletType(#showletType.key)}"></s:set>
                    <!-- list item start-->
                    <div class="list-group-item widget-square list-group-item-custom pointer" data-widget-id="<s:property value="#showletType.key" />">
                    <div class=" hidden list-view-pf-left icon-pos">
                        <span class="  fa fa-default list-view-pf-icon-sm  fa-<s:property value="#showletType.key" />"></span>
                    </div>
                    <div class="list-view-pf-main-info">
                        <div class="list-view-pf-left">
                            <span class="fa fa-default list-view-pf-icon-sm widget-icon fa-<s:property value="#showletType.key" />"></span>
                        </div>
                        <div class="list-view-pf-body">
                            <div class="list-view-pf-description">
                                <div id="description-widget" class="list-group-item-heading widget-name widget-name-list ">
                                    <s:property value="#showletType.value" />
                                    <s:if test="%{#concreteShowletTypeVar.mainGroup != null && !#concreteShowletTypeVar.mainGroup.equals('free')}"><span class="text-muted icon fa fa-lock"></span></s:if>
                                    </div>
                                </div>
                                <div class="list-view-pf-additional-info">
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- list item end-->
            </s:iterator>
            <s:set var="showletUtilizers"></s:set>
        </s:iterator>
    </div>

</div>

<script>

    $("#search-widget").on("keyup input", function () {
        var txt = $('#search-widget').val();
        $('.widget-name-list').each(function () {
            if ($(this).text().toUpperCase().indexOf(txt.toUpperCase()) != -1) {
                $(this).parentsUntil('.list-group-item').show();
            } else {
                $(this).parentsUntil('.list-group-item').hide();
            }
        });
    });
    $("#widgetGrid").click(function () {
        $(".widget-square").addClass("widget-grid");
        $(".icon-pos").removeClass("hidden");
        $(".list-group-item-heading").addClass("descrition-widget-overlay");

    });
    $("#widgetList").click(function () {
        $(".widget-square").removeClass("widget-grid");
        $(".icon-pos").addClass("hidden");
        $(".list-group-item-heading").removeClass("descrition-widget-overlay");
    })

</script>

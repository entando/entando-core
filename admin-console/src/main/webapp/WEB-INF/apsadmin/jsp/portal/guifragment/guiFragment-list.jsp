<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li class="page-title-container"><s:text name="title.guiFragmentManagement" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="title.guiFragmentManagement" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.guiFragments.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern" id="frag-tab">
                <li class="active"><a href="<s:url action="list" namespace="/do/Portal/GuiFragment"/>" role="tab"><s:text name="label.list" /></a></li>
                <li><a href="<s:url action="systemParams" namespace="/do/Portal/GuiFragment"/>" role="tab"><s:text name="label.settings" /></a></li>
            </ul>
        </div>
    </div>
</div>
<br/>

<!-- Tab panes -->
<div class="tab-content margin-large-bottom">
    <div class="tab-pane active" id="frag-list">

        <s:form action="list" cssClass="form-horizontal">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <s:text name="message.title.ActionErrors" />
                    <ul class="margin-base-top">
                        <s:iterator value="actionErrors">
                            <li><s:property escapeHtml="false" /></li>
							</s:iterator>
                    </ul>
                </div>
            </s:if>

            <div class="form-group ">
                <div class="well col-md-offset-3 col-md-6  ">

                    <p class="search-label"><s:text name="label.search.label"/></p>

                    <div class="form-group">
                        <label for="search-code" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.code"/></label>
                        <label class="col-sm-2 control-label"><s:text name="label.code"/></label>
                        <div class="col-sm-9">
                            <wpsf:textfield id="guiFragment_code" name="code" cssClass="form-control" title="%{getText('label.search.by')+' '+getText('label.code')}" placeholder="%{getText('label.code')}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="guiFragment_widgetTypeCode">
                            <s:text name="label.widgetType"/>
                        </label>
                        <div class="col-sm-9">
                            <select name="widgetTypeCode" id="guiFragment_widgetTypeCode" class="form-control" />
                            <option value=""><s:text name="label.all" /></option>
                            <s:iterator var="widgetFlavourVar" value="widgetFlavours">
                                <wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
									<s:iterator var="widgetTypeVar" value="#widgetFlavourVar" >
										<s:if test="#widgetTypeVar.optgroup != #tmpShowletType">
											<s:if test="#widgetTypeVar.optgroup == 'stockShowletCode'">
                                            <wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.stock" /></wpsa:set>
											</s:if>
											<s:elseif test="#widgetTypeVar.optgroup == 'customShowletCode'">
                                            <wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.custom" /></wpsa:set>
											</s:elseif>
											<s:elseif test="#widgetTypeVar.optgroup == 'userShowletCode'">
                                            <wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.user" /></wpsa:set>
											</s:elseif>
											<s:else>
                                            <wpsa:set var="pluginPropertyName" value="%{getText(#widgetTypeVar.optgroup + '.name')}" />
                                            <wpsa:set var="pluginPropertyCode" value="%{getText(#widgetTypeVar.optgroup + '.code')}" />
                                            <wpsa:set var="optgroupLabel"><s:text name="%{#pluginPropertyName}" /></wpsa:set>
											</s:else>
                                        <optgroup label="<s:property value="#optgroupLabel" />">
                                        </s:if>
                                        <option <s:if test="%{#widgetTypeVar.key.equals(widgetTypeCode)}"> selected="selected" </s:if> value="<s:property value="#widgetTypeVar.key" />"><s:property value="#widgetTypeVar.value" /></option>
									<wpsa:set var="tmpShowletType"><s:property value="#widgetTypeVar.optgroup" /></wpsa:set>
                                    </s:iterator>
                                </optgroup>
                            </s:iterator>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="guiFragment_pluginCode">
                            <s:text name="label.plugin"/>
                        </label>
                        <div class="col-sm-9">
                            <wpsf:select name="pluginCode" id="guiFragment_pluginCode" headerKey="" headerValue="%{getText('label.all')}" list="guiFragmentPlugins" listKey="key" listValue="value" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2"></label>
                        <div class="col-sm-9">
                            <s:submit type="button" cssClass="btn btn-primary pull-right">
                                <s:text name="label.search" />
                            </s:submit>
                        </div>
                    </div>
                </div>
            </div>
        </s:form>
        <a href="<s:url action="new" />" class="btn btn-primary pull-right" title="<s:text name="guiFragment.label.new" />" style="margin-bottom: 5px">
            <s:text name="guiFragment.label.new" />
        </a>
        <s:form action="search">
            <p class="sr-only">
			<wpsf:hidden name="code" />
			<wpsf:hidden name="widgetTypeCode" />
			<wpsf:hidden name="pluginCode" />
            </p>
            <s:set var="guiFragmentsCodes_list" value="guiFragmentsCodes" />
            <s:if test="#guiFragmentsCodes_list.size > 0">
                <wpsa:subset source="#guiFragmentsCodes_list" count="10" objectName="groupGuiFragments" advanced="true" offset="5">
                    <s:set var="group" value="#groupGuiFragments" />
                    <div class="col-xs-12 no-padding">
                        <table class="table table-striped table-bordered table-hover no-mb">
                            <tr>
                                <th><s:text name="label.code" /></th>
                                <th><s:text name="label.widgetType" /></th>
                                <th><s:text name="label.plugin" /></th>
                                <th class="text-center table-w-5"><s:text name="label.actions" /></th>
                            </tr>
                            <s:iterator var="codeVar">
                                <s:set var="guiFragmentVar" value="%{getGuiFragment(#codeVar)}" />
                                <s:url action="edit" var="editGuiFragmentActionVar"><s:param name="code" value="#codeVar"/></s:url>
                                    <tr>
										<td>
                                        <s:property value="#codeVar"/>
                                    </td>
                                    <td>
                                        <s:set value="%{getWidgetType(#guiFragmentVar.widgetTypeCode)}" var="widgetTypeVar" />
                                        <s:property value="%{getTitle(#widgetTypeVar.code, #widgetTypeVar.titles)}" />
                                    </td>
                                    <td>
                                        <s:if test="%{null != #guiFragmentVar.pluginCode}">
                                            <s:text name="%{#guiFragmentVar.pluginCode+'.name'}" />&nbsp;<s:property value="#guiFragmentVar.pluginCode"/>
                                        </s:if>
                                        <s:else>
                                            &ndash;
                                        </s:else>
                                    </td>
                                    <td class=" text-center table-view-pf-actions">
                                        <div class="dropdown dropdown-kebab-pf">
                                            <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-ellipsis-v"></span></button>
                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <li>
                                                    <%-- edit --%>
                                                    <a  title="<s:text name="label.edit" />&#32;<s:property value="#codeVar" />" href="<s:property value="#editGuiFragmentActionVar" escapeHtml="false" />">
                                                        <span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#codeVar" /></span>
                                                        <s:text name="label.edit" />&#32;<s:property value="#codeVar" />
                                                    </a>
                                                </li>
                                                <li>
                                                    <%-- detail --%>
                                                    <a href="<s:url action="detail"><s:param name="code" value="#codeVar"/></s:url>"
													   title="<s:text name="note.detailsFor" />: <s:property value="#codeVar" />">
                                                        <s:text name="note.detailsFor" />: <s:property value="#codeVar" />
                                                        <span class="sr-only"><s:text name="note.detailsFor" />: <s:property value="#codeVar" /></span>
                                                    </a>
                                                </li>
                                                <li>
                                                    <s:if test="%{!#guiFragmentVar.locked}" >
                                                        <%-- remove --%>
                                                        <s:url action="trash" var="trashGuiFragmentActionVar"><s:param name="code" value="#codeVar"/></s:url>
                                                        <a href="<s:property value="#trashGuiFragmentActionVar" escapeHtml="false" />"
                                                           title="<s:text name="label.remove" />: <s:property value="#codeVar" />">
                                                            <span class="sr-only"><s:text name="label.alt.clear" /></span>
                                                            <s:text name="label.delete" />
                                                        </a>
                                                    </s:if>
                                                </li>
                                            </ul>
                                        </div>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </div>
                    <div class="content-view-pf-pagination clearfix">
                        <div class="form-group">
                            <span><s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                            <div class="mt-5">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
            </s:if>
            <s:else>
                <div class="alert alert-info">
                    <s:text name="guiFragment.message.list.empty" />
                </div>
            </s:else>
        </s:form>
    </div>
    <div class="tab-pane" id="frag-settings">
    </div>
</div>
</div>

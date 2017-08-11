<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="list" />"><s:text name="title.guiFragmentManagement" /></a></li>

    <s:if test="getStrutsAction() == 1">
        <li><s:text name="guiFragment.label.new" /></li>
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
        <li><s:text name="guiFragment.label.edit" /></li>
        </s:elseif>
</ol>


<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="guiFragment.label.new" />
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="guiFragment.label.edit" />
    </s:elseif>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.guiFragments.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main">
    <s:form action="save" cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <p><s:text name="message.title.FieldErrors" /></p>
            </div>
        </s:if>
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
        <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <s:if test="getStrutsAction() == 2">
            <wpsf:hidden name="code" />
            <wpsf:hidden name="widgetTypeCode" />
            <wpsf:hidden name="pluginCode" />
            <wpsf:hidden name="defaultGui" />
        </s:if>
    </p>

    <%-- code --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['code']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">

        <label class="control-label col-sm-2" for="guiFragment_code"><s:text name="label.code" />&nbsp;
            <i class="fa fa-asterisk required-icon"></i>
        </label>

        <div class="col-sm-10">
            <wpsf:textfield disabled="%{getStrutsAction() == 2}" name="code" id="guiFragment_code" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <p class="text-danger"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
            </s:if>
        </div>
    </div>

    <%-- widgetTypeCode --%>
    <s:if test="%{widgetTypeCode!=null}">
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="control-label col-sm-2" for="guiFragment_widgetTypeCode"><s:text name="label.widgetType" /></label>
            <div class="col-sm-10">
                <s:set value="%{getWidgetType(widgetTypeCode)}" var="widgetTypeVar" />
                <s:property value="getTitle(#widgetTypeVar.code, #widgetTypeVar.titles)" />

            </div>
        </div>
    </s:if>

    <%-- pluginCode --%>
    <s:if test="%{pluginCode != null}">
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <label class="control-label col-sm-2" for="guiFragment_pluginCode"><s:text name="label.plugin" /></label>
            <div class="col-sm-10">
                <s:text name="%{pluginCode+'.name'}" />
            </div>
        </div>
    </s:if>
    <hr>
    <!-- Nav tabs -->
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <label class="control-label col-sm-2"></label>
        <div class=" col-sm-10">
            <ul id="gui-tab" class="nav nav-tabs">
                <li class="active"><a href="#gui-edit" data-toggle="tab">Gui Code</a></li>
                <li><a href="#gui-default" data-toggle="tab">Default Gui Code</a></li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content margin-large-bottom">
                <div class="tab-pane active" id="gui-edit">
                    <%-- gui --%>
                    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['gui']}" />
                    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
                    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
                    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
                        <div class="col-xs-12">
                            <label for="guiFragment_gui" class="sr-only"><s:text name="label.gui" /></label>
                            <wpsf:textarea name="gui" id="guiFragment_gui" cssClass="form-control" rows="8" cols="50" />
                            <s:if test="#fieldHasFieldErrorVar">
                                <p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
                            </s:if>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="gui-default">
                    <%-- defaultGui --%>
                    <s:if test="null != defaultGui">
                        <%-- setup replace --%>
                        <% pageContext.setAttribute("tabChar", "\t"); %>
                        <% pageContext.setAttribute("carriageReturnChar", "\r"); %>
                        <% pageContext.setAttribute("newLineChar", "\n");%>
                        <c:set var="brChar"><br /></c:set>
                            <%-- set the string --%>
                        <c:set var="defguiVar"><s:property value="defaultGui" escapeXml="true" /></c:set>
                        <c:set var="ESCAPED_STRING" value="${
                               fn:replace(
                                   fn:replace(
                                   fn:replace(
                                   defguiVar, tabChar, '&emsp;'),
                                   carriageReturnChar, ''
                                   ),
                                   newLineChar, brChar
                                   )
                               }" />
                        <%-- output --%>
                        <pre><code><c:out value="${ESCAPED_STRING}" escapeXml="false" /></pre></code>
                    </s:if>
                    <s:else>
                        <div class="margin-none alert alert-info">
                            Not available.
                        </div>
                    </s:else>
                </div>
            </div>
        </div>
    </div>
    <%-- save button --%>
    <div class="form-group">
        <div class="col-xs-12">
            <s:submit type="button" action="save" cssClass="btn btn-primary pull-right">

                <s:text name="label.save" />
            </s:submit>
        </div>
    </div>
</s:form>
</div>

<script>
    $('#gui-tab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
</script>

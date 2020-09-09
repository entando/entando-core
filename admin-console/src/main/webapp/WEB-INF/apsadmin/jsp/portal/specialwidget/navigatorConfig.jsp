<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li>
        <s:url action="configure" namespace="/do/Page" var="configureURL">
            <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
        </s:url>
        <s:set var="configureTitle">
            <s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />
        </s:set>
        <a href="${configureURL}" title="${configureTitle}"><s:text name="title.configPage" /></a>
    </li>
    <li class="page-title-container"><s:text name="name.widget" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:text name="name.widget" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" data-content="<s:text name="name.widget.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<hr>

<div id="main" role="main">

    <!-- Info Details  -->
    <div class="button-bar mt-20">
        <s:action namespace="/do/Page" name="printPageDetails"
                  executeResult="true" ignoreContextParams="true">
            <s:param name="selectedNode" value="currentPage.code"></s:param>
        </s:action>
    </div>

    <!-- Form -->
    <s:url action="saveNavigatorConfig" namespace="do/Page/SpecialWidget/Navigator" anchor="expressions" var="formAction"/>
    <s:form action="%{'/' + #formAction}" cssClass="form-horizontal">
        <div class="panel panel-default mt-20">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>

            <div class="panel-body">

                <h2 class="h5 margin-small-vertical">
                    <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                </h2>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
                    <wpsf:hidden name="navSpec" />
                </p>

                <s:if test="hasActionErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <h3 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h3>
                        <ul class="margin-base-vertical">
                            <s:iterator value="actionErrors">
                                <li><s:property escapeHtml="false" /></li>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
                        <ul class="margin-base-vertical">
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <!-- Expression List -->
                <fieldset class="col-xs-12 no-padding">
                    <legend id="expressions"><s:text name="widget.configNavigator.expressionList" /></legend>
                    <s:if test="expressions.size != 0">
                        <div class="col-lg-6 col-md-10 col-sm-offset-2 no-padding">
                            <ul class="list-group">
                                <s:iterator value="expressions" var="expression" status="rowstatus">
                                    <li class="list-group-item">
                                        <div class="row">
                                            <div class="col-md-8 col-sm-8 col-xs-7">
                                                <span class="label label-info"><s:property value="#rowstatus.index + 1"/></span>&#32;
                                                <label class="label label-default"><s:text name="widget.configNavigator.navSpec" /></label>&#32;
                                                <s:if test="#expression.specId == 1"><s:text name="widget.configNavigator.currentPage" /></s:if>
                                                <s:elseif test="#expression.specId == 2"><s:text name="widget.configNavigator.parentCurrent" /></s:elseif>
                                                <s:elseif test="#expression.specId == 3"><s:text name="widget.configNavigator.parentFromCurrent" />: <s:property value="specSuperLevel" /></s:elseif>
                                                <s:elseif test="#expression.specId == 4"><s:text name="widget.configNavigator.parentFromRoot" />: <s:property value="specAbsLevel" /></s:elseif>
                                                <s:elseif test="#expression.specId == 5"><s:text name="widget.configNavigator.specifiedPage" />:
                                                    <s:set var="specPageVar" value="%{getPage(specCode)}" ></s:set>
                                                    <s:property value="%{getFullTitle(#specPageVar, currentLang.code)}" /><s:if test="!#specPageVar.showable"> [i]</s:if>
                                                    <s:if test="null == #specPageVar" ><s:text name="note.noPageSet" /></s:if>
                                                </s:elseif>
                                                <s:else>ERROR</s:else>
                                                    &#32;
                                                <s:if test="#expression.operatorId == -1"></s:if>
                                                <s:elseif test="#expression.operatorId == 1">
                                                    <label class="label label-default" title="<s:text name="widget.configNavigator.operator" />"><span class="icon fa fa-angle-right"></span></label>&#32;
                                                        <s:text name="widget.configNavigator.allChildren" />
                                                    </s:elseif>
                                                    <s:elseif test="#expression.operatorId == 2">
                                                    <label class="label label-default" title="<s:text name="widget.configNavigator.operator" />"><span class="icon fa fa-angle-right"></span></label>&#32;
                                                        <s:text name="widget.configNavigator.allNodes" />
                                                    </s:elseif>
                                                    <s:elseif test="#expression.operatorId == 3">
                                                    <label class="label label-default" title="<s:text name="widget.configNavigator.operator" />"><span class="icon fa fa-angle-right"></span></label>&#32;
                                                    <abbr title="<s:text name="widget.configNavigator.levelOfNodesTothisLevel" />"><s:text name="widget.configNavigator.nodesTothisLevel" /></abbr>: <s:property value="operatorSubtreeLevel" />
                                                </s:elseif>
                                                <s:else>ERROR</s:else>
                                                </div>
                                                <div class="col-md-4 col-sm-4 col-xs-5">
                                                    <div class="btn-toolbar pull-right">
                                                        <div class="btn-group btn-group-xs">
                                                        <wpsa:actionParam action="moveExpression" var="actionName" >
                                                            <wpsa:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="UP" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit action="%{#actionName}" type="button" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-asc"></span>
                                                        </wpsf:submit>

                                                        <wpsa:actionParam action="moveExpression" var="actionName" >
                                                            <wpsa:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="DOWN" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit action="%{#actionName}" type="button" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-desc"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                    <div class="btn-group btn-group-xs">
                                                        <wpsa:actionParam action="removeExpression" var="actionName" >
                                                            <wpsa:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit action="%{#actionName}" type="button" title="%{getText('label.remove')}" cssClass="btn-remove">
                                                            <span class="fa fa-trash-o fa-lg"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </s:iterator>
                            </ul>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="alert alert-info">
                            <span class="pficon pficon-info"></span>
                            <s:text name="note.noRuleSet" />.
                        </div>
                    </s:else>
                </fieldset>

                <!-- Button toolbar -->
                <fieldset class="col-xs-12 no-padding">
                    <legend><s:text name="widget.configNavigator.navSpec" /></legend>
                    <div class="form-group">
                        <div class="btn-group btn-with-form-control-container col-sm-offset-2 col-sm-10" data-toggle="buttons">

                            <label class="btn btn-default">
                                <input type="radio" name="specId" id="specId1" value="1" />
                                <s:text name="widget.configNavigator.currentPage" />
                            </label>

                            <label class="btn btn-default">
                                <input type="radio" name="specId" id="specId2" value="2" />
                                <s:text name="widget.configNavigator.parentCurrent" />
                            </label>

                            <div class="btn btn-default btn-with-form-control">
                                <input type="radio" name="specId" id="specId3" value="3" />
                                <label class="sr-only" for="specId3">
                                    <s:text name="widget.configNavigator.parentFromCurrent" />
                                </label>
                                <label class="label-btn-with-form-control" for="specSuperLevel">
                                    <s:text name="widget.configNavigator.parentFromCurrent" />
                                </label>&#32;<wpsf:select name="specSuperLevel" id="specSuperLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" cssClass="form-control" />
                            </div>

                            <div class="btn btn-default btn-with-form-control">
                                <input type="radio" name="specId" id="specId4" value="4" />
                                <label class="sr-only" for="specId4">
                                    <s:text name="widget.configNavigator.parentFromRoot" />
                                </label>
                                <label class="label-btn-with-form-control">
                                    <s:text name="widget.configNavigator.parentFromRoot" />
                                </label>&#32;<wpsf:select name="specAbsLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" cssClass="form-control" />
                            </div>

                            <div class="btn btn-default btn-with-form-control">
                                <input type="radio" name="specId" id="specId5" value="5" />
                                <label class="sr-only" for="specId5">
                                    <s:text name="widget.configNavigator.specifiedPage" />
                                </label>
                                <label class="label-btn-with-form-control">
                                    <s:text name="widget.configNavigator.specifiedPage" /></label>&#32;
                                <select name="specCode" class="form-control">
                                    <s:iterator var="page" value="pages">
                                        <option value="<s:property value="#page.code"/>"><s:property value="%{getShortFullTitle(#page, currentLang.code)}"/><s:if test="!#page.showable"> [i]</s:if></option>
                                    </s:iterator>
                                </select>
                            </div>
                        </div>
                    </div>
                </fieldset>
                
                <script>
                    $('.btn-with-form-control select').click(function (e) {
                        e.stopPropagation();
                    });
                </script>
                
                <!-- Operator -->
                <fieldset class="col-xs-12 no-padding">
                    <legend><s:text name="widget.configNavigator.operator" /></legend>
                    <div class="form-group">
                        <label for="operatorId" class="col-sm-2 control-label"><s:text name="widget.configNavigator.operatorType" /></label>
                        <div class="col-sm-10">
                            <select name="operatorId" id="operatorId" class="form-control">
                                <option value="0"><s:text name="widget.configNavigator.none" /></option>
                                <option value="1"><s:text name="widget.configNavigator.allChildren" /></option>
                                <option value="2"><s:text name="widget.configNavigator.allNodes" /></option>
                                <option value="3"><s:text name="widget.configNavigator.nodesTothisLevel" /></option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="operatorSubtreeLevel" class="col-sm-2 control-label"><s:text name="widget.configNavigator.levelOfNodesTothisLevel" />:</label>
                        <div class="col-sm-10">
                            <wpsf:select name="operatorSubtreeLevel" id="operatorSubtreeLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <wpsf:submit action="addExpression" type="button" cssClass="btn btn-primary">
                                <s:text name="widget.configNavigator.addExpression" />
                            </wpsf:submit>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <wpsf:submit action="saveNavigatorConfig" type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>

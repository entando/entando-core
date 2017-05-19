<%@ taglib prefix="s" uri="/struts-tags" %>
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
        <a tabindex="0" role="button"
           data-toggle="popover" data-trigger="focus" data-html="true"
           data-content="${dataContent}" data-placement="left"
           data-original-title="${dataOriginalTitle}">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<hr>

<div id="main" role="main">
    <%--
    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
    --%>

    <!-- Info Details  -->
    <div class="button-bar mt-20">
        <s:action namespace="/do/Page" name="printPageDetails"
                  executeResult="true" ignoreContextParams="true">
            <s:param name="selectedNode" value="currentPage.code"></s:param>
        </s:action>
    </div>

    <s:form namespace="/do/jacms/Page/SpecialWidget/ListViewer" cssClass="form-horizontal mt-20">
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>
            <div class="panel-body">
                <s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
                    <h2 class="h5 margin-small-vertical">
                        <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(#showletType.code, #showletType.titles)}" />
                </h2>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" />
                </p>

                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <p class="sr-only">
                    <wpsf:hidden name="contentType" />
                    <wpsf:hidden name="categories" value="%{#parameters['categories']}" />
                    <wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}" />
                    <wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}" />
                    <wpsf:hidden name="filters" />
                    <wpsf:hidden name="modelId" />
                    <wpsf:hidden name="maxElemForItem" />
                    <wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
                    <s:iterator var="lang" value="langs">
                        <wpsf:hidden name="%{'linkDescr_' + #lang.code}" value="%{#parameters['linkDescr_' + #lang.code]}" />
                        <wpsf:hidden name="%{'title_' + #lang.code}" value="%{#parameters['title_' + #lang.code]}" />
                    </s:iterator>
                </p>

                <s:if test="filterTypeId < 0">

                    <fieldset class="margin-base-top"><legend><s:text name="label.info" /></legend>
                        <p>
                            <label for="filterKey" class="control-label"><s:text name="label.type"/>:</label>
                            <wpsf:select name="filterKey" id="filterKey" list="filterTypes" listKey="key" listValue="value" cssClass="form-control" />
                            <wpsf:submit action="setFilterType" value="%{getText('label.continue')}" cssClass="button" />
                        </p>
                    </fieldset>

                </s:if>
                <s:else>
                    <p class="sr-only">
                        <wpsf:hidden name="filterKey" />
                        <wpsf:hidden name="filterTypeId" />
                        <wpsf:hidden name="attributeFilter" value="%{filterTypeId>0 && filterTypeId<5}"/>
                    </p>

                    <s:set var="filterDescription" value="%{filterKey}" />
                    <s:if test="%{filterKey == 'created'}">
                        <s:set var="filterDescription" value="%{getText('label.creationDate')}" />
                    </s:if>
                    <s:elseif test="%{filterKey == 'modified'}">
                        <s:set var="filterDescription" value="%{getText('label.lastModifyDate')}" />
                    </s:elseif>

                    <p>
                        <span class="icon fa fa-filter" title="<s:text name="note.filterTypes.intro" />"></span>&#32;
                        <s:property value="filterDescription" />
                        <span class="label label-info">
                            <s:if test="filterTypeId == 0">
                                <s:text name="note.filterTypes.metadata" /></span>
                        </p>
                    </s:if>

                    <s:elseif test="filterTypeId==1">
                        <%-- String --%>
                        <s:text name="note.filterTypes.TextAttribute" /></span>
                        </p>
                        <fieldset class="margin-base-top"><legend><s:text name="label.settings"/></legend>
                            <div class="form-group">
                                <div class="col-xs-12">
                                    <label for="filterOptionId"><s:text name="label.option"/></label>
                                    <s:if test="filterOptionId<=-1">
                                        <div class="input-group">
                                        </s:if>
                                        <wpsf:select id="filterOptionId" name="filterOptionId" list="#{3:getText('label.presenceOptionFilter'),4:getText('label.absenceOptionFilter'),1:getText('label.valueLikeOptionFilter'),2:getText('label.rangeOptionFilter')}" disabled="filterOptionId>-1" cssClass="form-control" />
                                        <s:if test="filterOptionId>-1"><wpsf:hidden name="filterOptionId" /></s:if>
                                        <s:else>
                                            <div class="input-group-btn">
                                                <wpsf:submit action="setFilterOption" value="%{getText('label.continue')}" cssClass="btn btn-info" />
                                            </div>
                                        </div>
                                    </s:else>
                                </div>
                            </div>

                            <s:if test="filterOptionId==1">
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="stringValue"><s:text name="label.filterValue" /></label>
                                        <wpsf:textfield name="stringValue" id="stringValue" cssClass="form-control" />
                                    </div>
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <wpsf:checkbox name="like" />&#32;<s:text name="label.filterValue.isLike" />
                                    </label>
                                </div>
                            </s:if>

                            <s:if test="filterOptionId==2">
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="stringStart"><s:text name="label.filterFrom" /></label>
                                        <wpsf:textfield name="stringStart" id="stringStart" cssClass="form-control" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="stringEnd"><s:text name="label.filterTo" /></label>
                                        <wpsf:textfield name="stringEnd" id="stringEnd" cssClass="form-control" />
                                    </div>
                                </div>
                            </s:if>
                        </fieldset>
                        <%-- // String --%>
                    </s:elseif>

                    <s:elseif test="filterTypeId==2">
                        <%-- Number --%>
                        <s:text name="note.filterTypes.NumberAttribute" /><span>
                            </p>
                            <fieldset class="margin-base-top"><legend><s:text name="label.settings"/></legend>
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="filterOptionId"><s:text name="label.option"/></label>
                                        <s:if test="filterOptionId<=-1">
                                            <div class="input-group">
                                            </s:if>
                                            <wpsf:select name="filterOptionId" id="filterOptionId" list="#{3:getText('label.presenceOptionFilter'),4:getText('label.absenceOptionFilter'),1:getText('label.valueOptionFilter'),2:getText('label.rangeOptionFilter')}" disabled="filterOptionId>-1" cssClass="form-control" />
                                            <s:if test="filterOptionId>-1"><wpsf:hidden name="filterOptionId" /></s:if>
                                            <s:else>
                                                <div class="input-group-btn">
                                                    <wpsf:submit action="setFilterOption" value="%{getText('label.continue')}" cssClass="btn btn-info" />
                                                </div>
                                            </div>
                                        </s:else>
                                    </div>
                                </div>

                                <s:if test="filterOptionId==1">
                                    <div class="form-group">
                                        <div class="col-xs-12">
                                            <label for="numberValue"><s:text name="label.filterValue.exact" /></label>
                                            <wpsf:textfield name="numberValue" id="numberValue" cssClass="form-control" />
                                        </div>
                                    </div>
                                </s:if>

                                <s:if test="filterOptionId==2">
                                    <div class="form-group">
                                        <div class="col-xs-12">
                                            <label for="numberStart"><s:text name="label.filterFrom" /></label>
                                            <wpsf:textfield name="numberStart" id="numberStart" cssClass="form-control" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12">
                                            <label for="numberEnd"><s:text name="label.filterTo" /></label>
                                            <wpsf:textfield name="numberEnd" id="numberEnd" cssClass="form-control" />
                                        </div>
                                    </div>
                                </s:if>
                            </fieldset>

                            <%-- // Number --%>
                        </s:elseif>

                        <s:elseif test="filterTypeId==3">
                            <%-- Boolean --%>
                            <s:text name="note.filterTypes.BooleanAttribute" /><span>
                                </p>
                                <fieldset class="margin-base-top"><legend><s:text name="label.settings"/></legend>

                                    <div class="form-group">
                                        <div class="btn-group col-sm-5" data-toggle="buttons">

                                            <label class="btn btn-default active">
                                                <input type="radio" name="booleanValue" id="booleanValue_true" value="true" />&#32;
                                                <s:text name="label.yes" />
                                            </label>
                                            <label class="btn btn-default">
                                                <input type="radio" name="booleanValue" id="booleanValue_false" value="false" />&#32;
                                                <s:text name="label.no" />
                                            </label>
                                            <label class="btn btn-default">
                                                <input type="radio" name="booleanValue" id="booleanValue_none" checked="checked" value="" />
                                                <s:text name="label.all" />
                                            </label>
                                        </div>
                                    </div>
                                </fieldset>
                                <%-- // Boolean --%>
                            </s:elseif>

                            <s:elseif test="filterTypeId==4">
                                <%-- Date --%>
                                <s:text name="note.filterTypes.DateAttribute" /></span>
                            </p>
                            <fieldset class="margin-base-top"><legend><s:text name="label.settings"/></legend>

                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="filterOptionId"><s:text name="label.option"/></label>
                                        <s:if test="filterOptionId<=-1">
                                            <div class="input-group">
                                            </s:if>
                                            <wpsf:select name="filterOptionId" id="filterOptionId" list="#{3:getText('label.presenceOptionFilter'),4:getText('label.absenceOptionFilter'),1:getText('label.valueOptionFilter'),2:getText('label.rangeOptionFilter')}" disabled="filterOptionId>-1" cssClass="form-control" />
                                            <s:if test="filterOptionId>-1"><wpsf:hidden name="filterOptionId" /></s:if>
                                            <s:else>
                                                <span class="input-group-btn">
                                                    <wpsf:submit action="setFilterOption" value="%{getText('label.continue')}" cssClass="btn btn-info" />
                                                </span>
                                            </div>
                                        </s:else>
                                    </div>
                                </div>

                                <s:if test="filterOptionId==1">

                                    <div class="form-group">
                                        <div class="btn-group btn-with-form-control-container col-xs-12" data-toggle="buttons">
                                            <label class="btn btn-default">
                                                <input type="radio" name="dateValueType" id="dateValueType_today"
                                                       value="2" <s:if test="(2 == dateValueType)">checked="checked"</s:if> />&#32;
                                                <s:text name="label.today" />
                                            </label>
                                            <div class="inline-block">
                                                <div class="input-group">
                                                    <div class="btn btn-default btn-with-form-control input-group-addon">
                                                        <label for="dateStartType_today">
                                                            <input type="radio" name="dateValueType" id="dateValueType_today" value="2"
                                                                   <s:if test="(2 == dateValueType)">checked="checked"</s:if> />&#32;
                                                            <s:text name="label.today" />
                                                        </label>
                                                    </div>
                                                    <label for="valueDateDelay" class="sr-only"><s:text name="label.delay" /></label>
                                                    <wpsf:textfield name="valueDateDelay" id="valueDateDelay" cssClass="form-control"
                                                                    placeholder="%{getText('label.delay')}" />
                                                </div>
                                            </div>
                                            <div class="inline-block">
                                                <div class="input-group">
                                                    <div class="btn btn-default btn-with-form-control input-group-addon">
                                                        <label for="dateStartType_today">
                                                            <input type="radio" name="dateValueType" id="dateValueType_chosen" value="3"
                                                                   <s:if test="(3 == dateValueType)">checked="checked"</s:if> />&#32;
                                                            <s:text name="label.chosenDate" />
                                                        </label>
                                                    </div>
                                                    <label for="dateValue_cal" class="sr-only"><s:text name="label.filterValue.exact" /></label>
                                                    <wpsf:textfield name="dateValue" id="dateValue_cal" cssClass="form-control datepicker"
                                                                    placeholder="dd/MM/yyyy" data-provide="datepicker"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </s:if>
                            </fieldset>

                            <s:if test="filterOptionId==2">

                                <!-- Start Date -->
                                <fieldset class="margin-base-top"><legend><s:text name="label.filterFrom" /></legend>
                                    <div class="form-group">
                                        <div class="btn-group btn-with-form-control-container col-xs-12" data-toggle="buttons">
                                            <label class="btn btn-default">
                                                <input type="radio" name="dateStartType"
                                                       id="dateStartType_none" value="1"
                                                       <s:if test="(1 == dateStartType)">checked="checked"</s:if> />&#32;
                                                <s:text name="label.none" />
                                            </label>
                                            <div class="inline-block">
                                                <div class="input-group">
                                                    <div class="btn btn-default btn-with-form-control input-group-addon">
                                                        <label for="dateStartType_today">
                                                            <input type="radio" name="dateStartType"
                                                                   id="dateStartType_today" value="2"
                                                                   <s:if test="(2 == dateStartType)">checked="checked"</s:if> />&#32;
                                                            <s:text name="label.today" />
                                                        </label>
                                                    </div>
                                                    <label for="startDateDelay" class="sr-only"><s:text name="label.delay" /></label>
                                                    <wpsf:textfield name="startDateDelay" id="startDateDelay"
                                                                    cssClass="form-control" placeholder="%{getText('label.delay')}" />
                                                </div>
                                            </div>
                                            <div class="inline-block">
                                                <div class="input-group">
                                                    <div class="btn btn-default btn-with-form-control input-group-addon">
                                                        <label for="dateStartType_today">
                                                            <input type="radio" name="dateStartType" id="dateStartType_chosen"
                                                                   value="3" <s:if test="(3 == dateStartType)">checked="checked"</s:if> />&#32;
                                                            <s:text name="label.chosenDate" />
                                                        </label>
                                                    </div>
                                                    <label for="dateStart_cal" class="sr-only"><s:text name="label.filterValue.exact" /></label>
                                                    <wpsf:textfield name="dateStart" id="dateStart_cal"
                                                                    cssClass="form-control datepicker" placeholder="dd/MM/yyyy" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <!-- End Date -->
                                <fieldset class="margin-base-top"><legend><s:text name="label.filterTo" /></legend>
                                    <div class="form-group">
                                        <div class="btn-group btn-with-form-control-container col-xs-12" data-toggle="buttons">
                                            <label class="btn btn-default">
                                                <input type="radio" name="dateEndType" id="dateEndType_none"
                                                       value="1" <s:if test="(1 == dateEndType)">checked="checked"</s:if> />&#32;
                                                <s:text name="label.none" />
                                            </label>
                                            <div class="inline-block">
                                                <div class="input-group">
                                                    <div class="btn btn-default btn-with-form-control input-group-addon">
                                                        <label for="dateEndType_today">
                                                            <input type="radio" name="dateEndType" id="dateEndType_today"
                                                                   value="2" <s:if test="(2 == dateEndType)">checked="checked"</s:if> />&#32;
                                                            <s:text name="label.today" />
                                                        </label>
                                                    </div>
                                                    <label for="endDateDelay" class="sr-only"><s:text name="label.delay" /></label>
                                                    <wpsf:textfield name="endDateDelay" id="endDateDelay" cssClass="form-control"
                                                                    placeholder="%{getText('label.delay')}" />
                                                </div>
                                            </div>
                                            <div class="inline-block">
                                                <div class="input-group">
                                                    <div class="btn btn-default btn-with-form-control input-group-addon">
                                                        <label for="dateEndType_chosen">
                                                            <input type="radio" name="dateEndType" id="dateEndType_chosen" value="3"
                                                                   <s:if test="(3 == dateEndType)">checked="checked"</s:if> />
                                                            <s:text name="label.chosenDate" />
                                                        </label>
                                                    </div>
                                                    <label for="dateEnd_cal" class="sr-only"><s:text name="label.filterValue.exact" />:</label>
                                                    <wpsf:textfield name="dateEnd" id="dateEnd_cal"
                                                                    cssClass="form-control datepicker" placeholder="dd/MM/yyyy" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>
                            </s:if>

                            <%-- // Date --%>
                        </s:elseif>

                        <fieldset class="margin-base-top"><legend><s:text name="label.order" /></legend>
                            <div class="form-group">
                                <div class="btn-group col-sm-5" data-toggle="buttons">

                                    <label class="btn btn-default active">
                                        <input type="radio" name="order" checked="checked" value="" />&#32;
                                        <s:text name="label.none" />
                                    </label>
                                    <label class="btn btn-default">
                                        <input type="radio" name="order" value="ASC" <s:if test="('ASC' == order)">checked="checked"</s:if> />&#32;
                                        <s:text name="label.order.ascendant" />
                                    </label>
                                    <label class="btn btn-default">
                                        <input type="radio" name="order" value="DESC" <s:if test="('DESC' == order)">checked="checked"</s:if> />&#32;<s:text name="label.order.descendant" />
                                        </label>
                                    </div>
                                </div>
                            </fieldset>

                        <s:set var="saveFilterActionName"><s:if test="filterTypeId == 0">saveFilter</s:if><s:elseif test="filterTypeId == 1">saveTextFilter</s:elseif><s:elseif test="filterTypeId == 2">saveNumberFilter</s:elseif><s:elseif test="filterTypeId == 3">saveBooleanFilter</s:elseif><s:elseif test="filterTypeId == 4">saveDateFilter</s:elseif></s:set>


                    </s:else>

            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <wpsf:submit type="button" action="%{#saveFilterActionName}"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>

    </s:form>

</div>

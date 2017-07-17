<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
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

    <s:form action="searchContents" cssClass="form-horizontal">
        <div class="panel panel-default mt-20">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>
            <div class="panel-body">
                <s:set var="widgetType" value="%{getWidgetType(widgetTypeCode)}"></s:set>
                    <h2 class="h5 margin-small-top margin-large-bottom">
                        <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(#widgetType.code, #widgetType.titles)}" />
                </h2>

                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" />
                    <wpsf:hidden name="modelId" />
                </p>

                <!-- Form errors -->
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>
                        <ul class="margin-base-vertical">
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <!-- Search Form -->
                <div class="well col-md-offset-3 col-md-6">
                    <p class="search-label"><s:text name="label.search.label"/></p>
                    <div class="form-group">
                        <label for="text" class="control-label col-sm-2"><s:text name="label.description"/></label>
                        <div class="col-sm-9">
                            <wpsf:textfield name="text" id="text"
                                            cssClass="form-control"
                                            placeholder="%{getText('label.search.topic')}"
                                            title="%{getText('label.search.by')} %{getText('label.description')}" />
                        </div>
                    </div>

                    <!-- Advanced Search -->
                    <fieldset class="fields-section-pf" id="search-advanced">
                        <legend class="fields-section-header-pf">
                            <span class="fa fa-angle-right fa-angle-down field-section-toggle-pf"></span>
                            <a href="#search-advanced" class="field-section-toggle-pf"><s:text name="label.search.advanced" /></a>
                        </legend>

                        <div class="form-group">
                            <label for="contentType" class="control-label col-sm-2 text-right">
                                <s:text name="label.type" />
                            </label>
                            <div class="col-sm-9">
                                <wpsf:select name="contentType" id="contentType"
                                             list="contentTypes" listKey="code" listValue="description"
                                             headerKey="" headerValue="%{getText('label.all')}"
                                             cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="contentIdToken" class="control-label col-sm-2 text-right">
                                <s:text name="label.code" />
                            </label>
                            <div class="col-sm-9">
                                <wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="state" class="control-label col-sm-2 text-right"><s:text name="label.state" /></label>
                            <div class="col-sm-9">
                                <wpsf:select name="state" id="state" list="avalaibleStatus"
                                             headerKey="" headerValue="%{getText('label.all')}"
                                             cssClass="form-control" listKey="key" listValue="%{getText(value)}" />
                            </div>
                        </div>
                    </fieldset>

                    <div class="form-group">
                        <div class="col-sm-9 col-sm-offset-2">
                            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                                <s:text name="label.search" />
                            </wpsf:submit>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12">
                        <wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
                            <s:set var="group" value="#groupContent" />
                            <p class="sr-only">
                                <wpsf:hidden name="lastGroupBy" />
                                <wpsf:hidden name="lastOrder" />
                            </p>
                            <div class="table-responsive"><%-- table-responsive --%>
                                <table class="table table-striped table-bordered table-hover no-mb">
                                    <thead>
                                        <tr>
                                            <th class="text-center">
                                                <input type="radio" disabled />
                                            </th>
                                            <th><a href="
                                                   <s:url action="changeContentListOrder">
                                                       <s:param name="text">
                                                           <s:property value="#request.text"/>
                                                       </s:param>
                                                       <s:param name="contentIdToken">
                                                           <s:property value="#request.contentIdToken"/>
                                                       </s:param>
                                                       <s:param name="contentType">
                                                           <s:property value="#request.contentType"/>
                                                       </s:param>
                                                       <s:param name="state">
                                                           <s:property value="#request.state"/>
                                                       </s:param>
                                                       <s:param name="pagerItem">
                                                           <s:property value="#groupContent.currItem"/>
                                                       </s:param>
                                                       <s:param name="pageCode">
                                                           <s:property value="#request.pageCode"/>
                                                       </s:param>
                                                       <s:param name="frame">
                                                           <s:property value="#request.frame"/>
                                                       </s:param>
                                                       <s:param name="modelId">
                                                           <s:property value="#request.modelId"/>
                                                       </s:param>
                                                       <s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
                                                       <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                       <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                       <s:param name="groupBy">descr</s:param>
                                                   </s:url>
                                                   "><s:text name="label.description" /></a></th>
                                            <th><a href="
                                                   <s:url action="changeContentListOrder">
                                                       <s:param name="text">
                                                           <s:property value="#request.text"/>
                                                       </s:param>
                                                       <s:param name="contentIdToken">
                                                           <s:property value="#request.contentIdToken"/>
                                                       </s:param>
                                                       <s:param name="contentType">
                                                           <s:property value="#request.contentType"/>
                                                       </s:param>
                                                       <s:param name="state">
                                                           <s:property value="#request.state"/>
                                                       </s:param>
                                                       <s:param name="pagerItem">
                                                           <s:property value="#groupContent.currItem"/>
                                                       </s:param>
                                                       <s:param name="pageCode">
                                                           <s:property value="#request.pageCode"/>
                                                       </s:param>
                                                       <s:param name="frame">
                                                           <s:property value="#request.frame"/>
                                                       </s:param>
                                                       <s:param name="modelId">
                                                           <s:property value="#request.modelId"/>
                                                       </s:param>
                                                       <s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
                                                       <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                       <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                       <s:param name="groupBy">code</s:param>
                                                   </s:url>
                                                   "><s:text name="label.code" /></a></th>
                                            <th><s:text name="label.group" /></th>
                                            <th class="text-center"><a href="
                                                                       <s:url action="changeContentListOrder">
                                                                           <s:param name="text">
                                                                               <s:property value="#request.text"/>
                                                                           </s:param>
                                                                           <s:param name="contentIdToken">
                                                                               <s:property value="#request.contentIdToken"/>
                                                                           </s:param>
                                                                           <s:param name="contentType">
                                                                               <s:property value="#request.contentType"/>
                                                                           </s:param>
                                                                           <s:param name="state">
                                                                               <s:property value="#request.state"/>
                                                                           </s:param>
                                                                           <s:param name="pagerItem">
                                                                               <s:property value="#groupContent.currItem"/>
                                                                           </s:param>
                                                                           <s:param name="pageCode">
                                                                               <s:property value="#request.pageCode"/>
                                                                           </s:param>
                                                                           <s:param name="frame">
                                                                               <s:property value="#request.frame"/>
                                                                           </s:param>
                                                                           <s:param name="modelId">
                                                                               <s:property value="#request.modelId"/>
                                                                           </s:param>
                                                                           <s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
                                                                           <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                                           <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                                           <s:param name="groupBy">created</s:param>
                                                                       </s:url>
                                                                       "><s:text name="label.creationDate" /></a></th>
                                            <th class="text-center"><a href="
                                                                       <s:url action="changeContentListOrder">
                                                                           <s:param name="text">
                                                                               <s:property value="#request.text"/>
                                                                           </s:param>
                                                                           <s:param name="contentIdToken">
                                                                               <s:property value="#request.contentIdToken"/>
                                                                           </s:param>
                                                                           <s:param name="contentType">
                                                                               <s:property value="#request.contentType"/>
                                                                           </s:param>
                                                                           <s:param name="state">
                                                                               <s:property value="#request.state"/>
                                                                           </s:param>
                                                                           <s:param name="pagerItem">
                                                                               <s:property value="#groupContent.currItem"/>
                                                                           </s:param>
                                                                           <s:param name="pageCode">
                                                                               <s:property value="#request.pageCode"/>
                                                                           </s:param>
                                                                           <s:param name="frame">
                                                                               <s:property value="#request.frame"/>
                                                                           </s:param>
                                                                           <s:param name="modelId">
                                                                               <s:property value="#request.modelId"/>
                                                                           </s:param>
                                                                           <s:param name="widgetTypeCode"><s:property value="widgetTypeCode" /></s:param>
                                                                           <s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
                                                                           <s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
                                                                           <s:param name="groupBy">lastModified</s:param>
                                                                       </s:url>
                                                                       "><s:text name="label.lastEdit" /></a></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <s:iterator var="contentId">
                                            <s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
                                                <tr>
                                                    <td class="text-center"><input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" /></td>
                                                <td><s:property value="#content.descr" /></td>
                                                <td><s:property value="#content.id" /></td>
                                                <td><s:property value="%{getGroup(#content.mainGroupCode).descr}" /></td>
                                                <td class="text-center text-nowrap">
                                                    <s:date name="#content.create" format="dd/MM/yyyy HH:mm" />
                                                </td>
                                                <td class="text-center text-nowrap">
                                                    <s:date name="#content.modify" format="dd/MM/yyyy HH:mm" />
                                                </td>
                                            </tr>
                                        </s:iterator>
                                    </tbody>
                                </table>

                                <div class="content-view-pf-pagination table-view-pf-pagination clearfix">
                                    <div class="form-group">
                                        <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                                        <div style="margin: 5px">
                                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-12">
                <wpsf:submit type="button" action="joinContent" cssClass="btn btn-primary pull-right">
                    <s:text name="label.confirm" />
                </wpsf:submit>
            </div>
        </div>

    </s:form>
</div>

<!-- Patternfly collapse form field -->
<script type="text/javascript">
    // Initialize form
    $(document).ready(function () {
        // set default state to collapsed for expandable field section
        $('.fields-section-header-pf').attr('aria-expanded', 'false');
        $('.fields-section-pf .form-group').addClass('hidden');
        $('.fa.field-section-toggle-pf').removeClass('fa-angle-down');
        // click the field section heading to expand the section
        $("#search-advanced .field-section-toggle-pf").click(function (event) {
            $(this).parents(".fields-section-pf").find(".fa").toggleClass("fa-angle-down");
            $(this).parents(".fields-section-pf").find(".form-group").toggleClass("hidden");
            if ($(this).parent().attr('aria-expanded') == 'false') {
                $(this).parent().attr('aria-expanded', 'true');
            } else {
                $(this).parent().attr('aria-expanded', 'false');
            }
        })
    });
</script>

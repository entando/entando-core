<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>Integrations</li>
    <li><s:text name="title.apiManagement" /></li>
    <li class="page-title-container"><s:text name="title.apiConsumerManagement" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.apiConsumerManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<s:form action="search"  role="search" cssClass="form-horizontal">
    <s:if test="hasActionMessages()">
        <div class="alert alert-info alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
            <ul class="margin-base-top">
                <s:iterator value="actionMessages">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-top">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <div class="searchPanel form-group">
        <div class="well col-md-offset-3 col-md-6  ">
            <p class="search-label"><s:text name="label.search.label"/></p>

            <div class="form-group">
                <label class="col-sm-2 control-label"><s:text name="label.search.by"/>&#32;<s:text name="label.consumer.description"/></label>
                <div class="col-sm-9">
                    <wpsf:textfield name="text" id="search_consumer_description" cssClass="form-control input-lg" placeholder="%{getText('label.consumer.description')}" title="%{getText('label.search.by')} %{getText('label.consumer.description')}" />
                </div>
            </div>

            <div class="panel-group" id="accordion-markup" style="margin:-24px 0 0 0;" >
                <div class="panel panel-default">
                    <div class="panel-heading" style="padding:0 0 10px;">
                        <p class="panel-title" style="text-align: end">
                            <a  data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">
                                <s:text name="label.search.advanced" />
                            </a>
                        </p>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">   
                            <div class="form-group">
                                <label class="control-label col-sm-3" style="text-align:end" for="search_consumer_key">Key</label>
                                <div class="col-sm-8">
                                    <wpsf:textfield name="insertedKey" id="search_consumer_key" cssClass="form-control" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-12">
                <div class="form-group">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.search" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </div>

    <hr />
    <a href="<s:url action="new" />" class="btn btn-primary pull-right" style="margin-bottom: 5px">
        <s:text name="label.add" />
    </a>

    <p class="sr-only">
        <wpsf:hidden name="insertedKey" />
        <wpsf:hidden name="insertedDescription" />
    </p>
    <wpsa:subset source="searchResult" count="10" objectName="groupSearchResult" advanced="true" offset="5">
        <s:set var="group" value="#groupSearchResult" />
        <s:set var="tokenOccurrencesVar" value="tokenOccurrencesByConsumer" />

        <div class="col-xs-12 no-padding">
            <table class="table table-striped table-bordered table-hover no-mb">
                <thead>
                    <tr>
                        <th><s:text name="label.key" /></th>
                        <th><s:text name="label.description" /></th>
                        <th class="text-center col-sm-1"><abbr title="<s:text name="label.tokens.full" />"><s:text name="label.tokens.short" /></abbr></th>
                        <th><s:text name="label.actions" /></th>
                            <%--
                            <th><s:text name="label.expirationDate" /></th>
                            <th><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
                            --%>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator var="consumerKeyVar" status="status">
                        <s:set var="consumerVar" value="%{getConsumer(#consumerKeyVar)}" />
                        <tr>
                            <td><code><s:property value="#consumerKeyVar" /></code></td>
                            <td>
                                <s:if test="%{#consumerVar.description.length()>140}">
                                    <abbr title="<s:property value="#consumerVar.description" />"><s:property value="%{#consumerVar.description.substring(0,140)}" />&hellip;</abbr>
                                </s:if>
                                <s:else>
                                    <s:property value="#consumerVar.description" />
                                </s:else>
                            </td>
                            <td class="text-center">
                                <s:if test="null == #tokenOccurrencesVar[#consumerKeyVar]" >0</s:if>
                                <s:else><s:property value="#tokenOccurrencesVar[#consumerKeyVar]" /></s:else>
                                <span class="sr-only"><s:text name="label.tokens.full" /></span>
                            </td>
                            <td class=" table-view-pf-actions">
                                <div class="dropdown dropdown-kebab-pf">
                                    <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-ellipsis-v"></span></button>
                                    <ul class="dropdown-menu dropdown-menu-right">								
                                        <li>
                                            <a href="<s:url action="edit"><s:param name="consumerKey" value="#consumerKeyVar" /></s:url>" title="<s:text name="label.edit" />: <s:property value="#consumerKeyVar" />">
                                                <span><s:text name="label.edit" /></span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="<s:url action="trash"><s:param name="consumerKey" value="#consumerKeyVar"/></s:url>" title="<s:text name="label.remove" />: <s:property value="#consumerKeyVar" />">
                                                <span><s:text name="label.delete" /></span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </div>          
        <div class="content-view-pf-pagination clearfix">
            <div class="form-group">
                <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                <div class="mt-5">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                </div>
            </div>
        </div>
    </wpsa:subset>
</s:form>
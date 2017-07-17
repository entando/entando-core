<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.integrations" /></li>
    <li><s:text name="title.apiManagement" /></li>
    <li class="page-title-container"><s:text name="title.apiConsumerManagement" /></li>
</ol>

<h1 class="page-title-container"><s:text name="title.apiConsumerManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.api.resources.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<s:form action="search"  role="search" cssClass="form-horizontal">
    <s:if test="hasActionMessages()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
            <ul class="">
                <s:iterator value="actionMessages">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
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
                <label class="col-sm-2 control-label"><s:text name="label.search.by"/></label>
                <div class="col-sm-9">
                    <wpsf:textfield name="insertedDescription" id="search_consumer_description" cssClass="form-control" placeholder="%{getText('label.consumer.description')}" title="%{getText('label.search.by')} %{getText('label.consumer.description')}" />
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="search_consumer_key" ><s:text name="api.consumer.key"/></label>
                <div class="col-sm-9">
                    <wpsf:textfield name="insertedKey" id="search_consumer_key" placeholder="%{getText('api.consumer.key')}" cssClass="form-control"  />
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

    <wpsa:subset source="searchResult" count="10" objectName="groupSearchResult" advanced="true" offset="5">
        <s:set var="group" value="#groupSearchResult" />
        <s:set var="tokenOccurrencesVar" value="tokenOccurrencesByConsumer" />

        <div class="col-xs-12 no-padding">
            <table class="table table-striped table-bordered table-hover no-mb">
                <thead>
                    <tr>
                        <th class="table-w-20"><s:text name="label.key" /></th>
                        <th><s:text name="label.description" /></th>
                        <th class="table-w-10"><s:text name="label.tokens.full" /></th>
                        <th class="table-w-5 text-center"><s:text name="label.actions" /></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator var="consumerKeyVar" status="status">
                        <s:set var="consumerVar" value="%{getConsumer(#consumerKeyVar)}" />
                        <tr>
                            <td><s:property value="#consumerKeyVar" /></td>
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
                            <td class="text-center table-view-pf-actions">
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

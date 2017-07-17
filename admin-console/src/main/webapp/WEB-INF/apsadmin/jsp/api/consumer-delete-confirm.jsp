<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.integrations" /></li>
    <li><s:text name="title.apiManagement" /></li>
    <li><a href="<s:url action="list" />"><s:text name="title.apiConsumerManagement" /></a></li>
    <li class="page-title-container"><s:text name="title.apiConsumerManagement.trash" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.apiConsumerManagement.trash" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div id="main" role="main">
    <div class="text-center">
        <s:form action="delete">
            <p class="sr-only">
                <wpsf:hidden name="consumerKey" />
            </p>
            <s:set var="consumerVar" value="%{getConsumer(consumerKey)}" />
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline"><s:text name="note.api.consumer.trash" /></p>
            <p class="esclamation-underline-text"><s:property value="#consumerVar.description" /> <s:property value="consumerKey" />?</p>
            <br>
            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/Api/Consumer" />"><s:text name="label.back" /></a>
                <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                    <s:text name="label.remove" />
                </wpsf:submit>
            </div>
        </s:form>
    </div>
</div>

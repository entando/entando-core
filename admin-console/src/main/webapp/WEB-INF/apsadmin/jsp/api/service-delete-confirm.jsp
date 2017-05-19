<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.integrations" /></li>
    <li><s:text name="title.apiManagement" /></li>


    <li>
        <a href='<s:url action="list" namespace="/do/Api/Service" />'>
            <span class="list-group-item-value"><s:text name="name.api.services" /></span>
        </a>
    </li>

    <li class="page-title-container"><s:text name="title.api.apiService.trash" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.api.apiService.trash" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:form action="delete">

        <p class="sr-only">
            <wpsf:hidden name="serviceKey" />
        </p>
        <div class="text-center">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline"><s:text name="note.api.apiService.trash" /></p>
            <p class="esclamation-underline-text"><s:property value="serviceKey" />?</p>
            <br>

            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="list" namespace="/do/Api/Service"/>" ><s:text name="label.back" /></a>
                <wpsf:submit type="button" action="delete" cssClass="btn btn-danger button-fixed-width">
                    <s:text name="label.remove" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>

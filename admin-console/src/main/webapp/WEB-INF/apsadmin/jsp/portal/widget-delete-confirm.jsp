<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.widgetManagement" />">
            <s:text name="title.widgetManagement" /></a>
    </li>
    <li><s:text name="title.widgetManagement.type.delete" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.widgetManagement.type.delete" /> - <s:property value="widgetTypeCode" />
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:form namespace="/do/Portal/WidgetType" action="delete">
        <p class="sr-only">
            <wpsf:hidden name="widgetTypeCode" />
        </p>
        <div class="text-center">


            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>

            <p class="esclamation-underline">
                <s:text name="note.deleteType.areYouSure" />
            </p>
            <p class="esclamation-underline-text">
                <s:property value="widgetTypeCode" />?
            </p>
            <br/>

            <div class="text-center margin-large-top">
                <a class="btn btn-default button-fixed-width" href="<s:url action="viewWidgets" namespace="/do/Portal/WidgetType"/>" ><s:text name="label.back" /></a>
                <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                    <s:text name="label.remove" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>



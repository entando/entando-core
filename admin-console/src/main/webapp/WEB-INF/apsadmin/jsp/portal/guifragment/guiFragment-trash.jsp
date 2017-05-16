<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="list" />"><s:text name="title.guiFragmentManagement" /></a></li>
        <s:if test="getStrutsAction() == 4">
        <li class="page-title-container"><s:text name="guiFragment.label.delete" /></li>
        </s:if>
</ol>



<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 4"></li>
        <s:text name="guiFragment.label.delete" />
    </s:if>
<span class="pull-right">
    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
</span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form action="delete" cssClass="form-horizontal">
        <p class="sr-only">
            <wpsf:hidden name="strutsAction" />
            <wpsf:hidden name="code" />
        </p>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="note.guiFragment.delete" /></p>
        <p>
            <s:text name="note.deleteGuiFragment.areYouSure" />&#32;
            <s:property value="code"/>
        </p>
        <%-- save button --%>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width" href="<s:url action="list"/>" ><s:text name="title.guiFragmentManagement" /></a>
            <s:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </s:submit>
        </div>
    </s:form>
</div>


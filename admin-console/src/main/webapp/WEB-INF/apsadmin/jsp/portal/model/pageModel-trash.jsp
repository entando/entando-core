<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="list" namespace="/do/PageModel"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
            <s:text name="title.pageModelManagement" />
        </a>
    </li>
    <li class="page-title-container"><s:text name="title.pageModelManagement.pageModelTrash" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.pageModelManagement.pageModelTrash" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div class="text-center">
    <s:form action="delete">
        <p class="sr-only"><wpsf:hidden name="code"/></p>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="note.pageModel.delete" /></p>
        <p>
            <s:text name="note.pageModelConfirm.trash" />&#32;
            <s:property value="code" />

        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" />">
                <s:text name="page.models" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </wpsf:submit>
        </div>

    </s:form>
</div>

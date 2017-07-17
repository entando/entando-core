<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.app" /></li>
    <li><s:text name="breadcrumb.jacms" /></li>
    <li>
        <a href="<s:url action="list" namespace="/do/jacms/ContentModel" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.contentModels" />">
            <s:text  name="title.contentModels" />
        </a>
    </li>
    <li class="page-title-container"><s:text
            name="title.contentModels.remove" /></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.contentModels.remove" />

</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div class="text-center">
    <s:form action="delete">
        <p class="sr-only">
            <wpsf:hidden name="modelId" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="note.deleteContentModel.areYouSure" />
        </p>
        <p class="esclamation-underline-text">
            <s:property value="modelId" />?
        </p>

        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width"
               href="<s:url action="list" namespace="/do/jacms/ContentModel"/>">
                <s:text name="note.back" />
            </a>
            <wpsf:submit type="button"
                         cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete" />
            </wpsf:submit>
        </div>
    </s:form>
</div>

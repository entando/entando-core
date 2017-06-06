<%@ taglib prefix="s" uri="/struts-tags" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="list" namespace="/do/PageModel"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageModelManagement" />">
            <s:text name="title.pageModelManagement" />
        </a></li>
    <li><s:text name="label.delete" /></li>
</ol>


<h1 class="page-title-container">
    <s:text name="label.delete" />
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div id="main" role="main">

    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="message.title.ActionErrors" /></strong>
        <p><s:text name="message.note.resolveReferences" /></p>
    </div>

    <s:include value="/WEB-INF/apsadmin/jsp/portal/model/include/pageModel-references.jsp" />

</div>

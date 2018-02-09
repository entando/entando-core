<%@ taglib prefix="s" uri="/struts-tags" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li><a href="<s:url action="list" namespace="/do/Group"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.guiFragmentManagement" />">
            <s:text name="title.guiFragmentManagement" />
        </a></li>
    <li class="page-title-container"><s:text name="guiFragment.label.delete" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="guiFragment.label.delete" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.guiFragments.help" />" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main" role="main">
    <div class="message message_error">
        <h2><s:text name="message.title.ActionErrors" /></h2>
        <p><s:text name="message.note.resolveReferences" />:</p>
    </div>
    <s:include value="/WEB-INF/apsadmin/jsp/portal/guifragment/include/guiFragmentInfo-references.jsp" />
</div>

<%@ taglib prefix="s" uri="/struts-tags" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="list" namespace="/do/Group"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />">
            <s:text name="title.groupManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.groupManagement.groupTrash" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.groupManagement.groupTrash" />
    </div>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>

<div id="main" role="main">

    <div class="message message_error">
        <h2><s:text name="message.title.ActionErrors" /></h2>
        <p><s:text name="message.note.resolveReferences" />:</p>

    </div>

    <s:include value="/WEB-INF/apsadmin/jsp/user/group/include/groupInfo-references.jsp" />

</div>

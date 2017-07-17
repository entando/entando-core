<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure" /></li>
    <li><a href="<s:url action="list" namespace="/do/Group" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.groupManagement" />"><s:text
                name="title.groupManagement" /></a></li>
    <li class="page-title-container"><s:text name="title.groupDetail" /></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.groupDetail" />
    <span class="pull-right"> <a tabindex="0" role="button"
                                 data-toggle="popover" data-trigger="focus" data-html="true" title=""
                                 data-content="<s:text name="page.groupManagement.help" />" data-placement="left"
                                 data-original-title=""><i class="fa fa-question-circle-o"
                                  aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="form-horizontal">
    <div class="form-group">
        <label class="control-label col-lg-3 col-md-3"><s:text
                name="label.group" /></label>
        <div class="col-md-9 col-lg-9 form-control-static">
            <code>
                <s:property value="name" />
            </code>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-lg-3 col-md-3"><s:text
                name="label.description" /></label>
        <div class="col-md-9 col-lg-9 form-control-static">
            <s:property value="description" />
        </div>
    </div>
    <wpsa:hookPoint key="core.groupDetails"
                    objectName="hookPointElements_core_groupDetails">
        <s:iterator value="#hookPointElements_core_groupDetails"
                    var="hookPointElement">
            <wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
            </s:iterator>
    </wpsa:hookPoint>
</div>
<s:include
    value="/WEB-INF/apsadmin/jsp/user/group/include/groupInfo-references.jsp" />

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li class="page-title-container">
        <s:text name="title.reload.config" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.reload.config" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="page.reloadConfig.help" />" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<br>

<div class="text-center">
    <wp:ifauthorized permission="superuser">
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="menu.reload.config"/></p>
        <p>
            <s:text name="label.reload.confirm"/>
        </p>
        <div class="btn btn-danger button-fixed-width">
            <a href="<s:url namespace="/do/BaseAdmin" action="reloadConfig" />" class="btn-danger button-fixed-width">
                <s:text name="label.reload" />
            </a>
        </div>
    </wp:ifauthorized>
</div>

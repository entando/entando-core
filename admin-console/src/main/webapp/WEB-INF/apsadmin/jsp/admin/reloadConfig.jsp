<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li>
        <a href="<s:url action="reloadChoose" namespace="/do/BaseAdmin" />">
            <s:text name="title.reload.config" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="menu.reload.config" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="menu.reload.config" />
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


<s:if test="1 == reloadingResult">
    <div class="alert alert-success">
        <span class="pficon pficon-ok"></span>
        <strong><s:text name="messages.confirm" /></strong>!&#32;
        <s:text name="message.reloadConfig.ok" />.
    </div>
</s:if>
<s:else>
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        <strong><s:text name="messages.error" /></strong>!&#32;
        <s:text name="message.reloadConfig.ko" />.
    </div>
</s:else>

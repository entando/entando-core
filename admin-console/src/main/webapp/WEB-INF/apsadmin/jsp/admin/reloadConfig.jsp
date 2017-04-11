<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure"/></li>
	<li>
		<a href="<s:url action="reloadChoose" namespace="/do/BaseAdmin" />">
			<s:text name="menu.reload" />
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
			   data-content="TO be inserted" data-placement="left" data-original-title="">
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
	<h2 class="text-center">
		<span class="fa fa-check fa-2x"></span>&#32;
		<strong><s:text name="messages.confirm" /></strong>!&#32;
		<s:text name="message.reloadConfig.ok" />.
	</h2>
</s:if>
<s:else>
	<h2 class="text-center">
		<span class="fa fa-close fa-2x"></span>&#32;
		<strong><s:text name="messages.error" /></strong>!&#32;
		<s:text name="message.reloadConfig.ko" />.
	</h2>
</s:else>

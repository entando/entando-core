<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure"/></a></li>
	<li class="page-title-container">
		<s:text name="menu.reload" />
	</li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="menu.reload" />
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


<div>
	<wp:ifauthorized permission="editContents">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" class="btn btn-default btn-lg btn-block">
				<span class="icon fa fa-refresh"></span>&#32;
				<s:text name="menu.reload.contents" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.reload.contents.keywords" />
			</small>
		</div>
	</wp:ifauthorized>

	<wp:ifauthorized permission="superuser">
		<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 margin-large-bottom">
			<a href="<s:url namespace="/do/BaseAdmin" action="reloadConfig" />" class="btn btn-default btn-lg btn-block">
				<span class="icon fa fa-refresh"></span>&#32;
				<s:text name="menu.reload.config" />
			</a>
			<small class="text-muted display-block margin-base-bottom">
				<s:text name="menu.reload.config.keywords" />
			</small>
		</div>
	</wp:ifauthorized>
</div>
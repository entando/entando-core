<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure"/></li>
	<li>
		<a href="<s:url action="reloadChoose" namespace="/do/BaseAdmin" />">
			<s:text name="menu.reload" />
		</a>
	</li>
	<li class="page-title-container">
		<s:text name="title.reload.contentReferences" />
	</li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="title.reload.contentReferences" />
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

<div class="cards-pf">
	<div class="container-fluid container-cards-pf">
		<div class="row row-cards-pf">
			<div class="col-xs-6 col-sm-6 col-md-6">
				<div class="card-pf">
					<div class="card-pf-heading">
						<h2 class="card-pf-title">
							<s:text name="title.reload.contentReferences" />
						</h2>
					</div>
					<div class="card-pf-body">
						<s:if test="contentManagerStatus == 1">
							<p class="text-info">
								<s:text name="note.reload.contentReferences.status.working" />&#32;
								(&#32;
								<a href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" title="<s:text name="note.reload.contentReferences.refresh" />">
									<s:text name="label.refresh" />
								</a>&#32;
								)
							</p>
						</s:if>
						<s:else>
							<p>
								<s:if test="contentManagerStatus == 2">
									<span class="text-info"><s:text name="note.reload.contentReferences.status.needToReload" /></span>
								</s:if>
								<s:elseif test="contentManagerStatus == 0">
									<s:text name="note.reload.contentReferences.status.ready" />
								</s:elseif>
								(&#32;
								<a href="<s:url action="reloadContentsReference" namespace="/do/jacms/Content/Admin" />"><s:text name="note.reload.contentReferences.start" /></a>&#32;
								)
							</p>
						</s:else>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-sm-6 col-md-6">
				<div class="card-pf">
					<div class="card-pf-heading">
						<h2 class="card-pf-title">
							<s:text name="title.reload.contentIndexes" />
						</h2>
					</div>
					<div class="card-pf-body">
						<s:if test="lastReloadInfo != null">
							<p class="text-info">
								<s:text name="note.reload.contentIndexes.lastOn.intro" />&#32;<span class="important"><s:date name="lastReloadInfo.date" format="dd/MM/yyyy HH:mm" /></span>,
								<s:if test="lastReloadInfo.result == 0">
									<span class="text-error"><s:text name="note.reload.contentIndexes.lastOn.ko" /></span>.
								</s:if>
								<s:else>
									<s:text name="note.reload.contentIndexes.lastOn.ok" />.
								</s:else>
							</p>
						</s:if>
						<s:if test="searcherManagerStatus == 1">
							<p class="text-info">
								<s:text name="note.reload.contentIndexes.status.working" />
								(
								<a href="<s:url action="openIndexProspect" namespace="/do/jacms/Content/Admin" />" title="<s:text name="note.reload.contentIndexes.refresh" />"><s:text name="label.refresh" /></a>
								)
							</p>
						</s:if>
						<s:else>
							<p>
								<s:if test="searcherManagerStatus == 2">
									<span class="text-warning"><s:text name="note.reload.contentIndexes.status.needToReload" /></span>
								</s:if>
								<s:elseif test="searcherManagerStatus == 0">
									<span><s:text name="note.reload.contentIndexes.status.ready" /></span>
								</s:elseif>
								(
								<a href="<s:url action="reloadContentsIndex" namespace="/do/jacms/Content/Admin" />">
									<s:text name="note.reload.contentIndexes.start" />
								</a>
								)
							</p>
						</s:else>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

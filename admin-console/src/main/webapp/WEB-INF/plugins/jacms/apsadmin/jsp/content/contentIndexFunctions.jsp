<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		/
		<s:text name="title.reload.contentReferences" />
	</span>
</h1>
<div class="panel panel-default">
	<div class="panel-body">
		<h2 class="margin-none"><s:text name="title.reload.contentReferences" /></h2>
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


<div class="panel panel-default">
	<div class="panel-body">
		<h2 class="margin-none"><s:text name="title.reload.contentIndexes" /></h2>
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
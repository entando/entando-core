<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
<div id="page-link" class="tab-pane">
	<form id="form_pageLink">
		<div class="col-xs-12">
			<p class="sr-only"><s:text name="note.choosePageToLink" />.</p>
			<p class="sr-only">
				<input type="hidden" name="contentOnSessionMarker" value="<s:property value="contentOnSessionMarker" />" />
			</p>
			<div class="well">
				<ul id="pageTree" class="fa-ul list-unstyled">
					<s:set var="inputFieldName" value="%{'selectedNode'}" />
					<s:set var="selectedTreeNode" value="%{selectedNode}" />
					<s:set name="liClassName" value="'page'" />
					<s:set var="treeItemIconName" value="'fa-folder'" />

					<s:if test="#pageTreeStyleVar == 'classic'">
						<s:set name="currentRoot" value="allowedTreeRootNode" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
					</s:if>
					<s:elseif test="#pageTreeStyleVar == 'request'">
						<s:set var="treeNodeExtraParamsMap" value="#{'contentOnSessionMarker': contentOnSessionMarker}" />
						<s:set name="currentRoot" value="showableTree" />
						<s:set var="treeNodeExtraParamName" value="'activeTab'" />
						<s:set var="treeNodeExtraParamValue" value="1" />
						<s:set name="closeTreeActionName" value="'entandoInternalLink'" />
						<s:set name="openTreeActionName" value="'entandoInternalLink'" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
					</s:elseif>
				</ul>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<button type="submit" id="button_pageLink" name="button_pageLink" class="btn btn-primary btn-block">
					<s:text name="label.confirm" />
				</button>
			</div>
		</div>
	</form>
</div>

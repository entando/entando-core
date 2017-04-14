<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li class="page-title-container"><s:text
			name="title.categoryManagement" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.categoryManagement" />
	<span class="pull-right"> <a tabindex="0" role="button"
		data-toggle="popover" data-trigger="focus" data-html="true" title=""
		data-content="TO be inserted" data-placement="left"
		data-original-title=""> <i class="fa fa-question-circle-o"
			aria-hidden="true"></i>
	</a>
	</span>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
<!-- 	<p> -->
<%-- 		<s:text name="note.categoryTree.intro" /> --%>
<!-- 	</p> -->
	<div role="search">
		<a href="<s:url namespace="/do/Category" action="new" />"
			class="btn btn-primary pull-right"
			title="<s:text name="label.new" />" style="margin-bottom: 5px"> <s:text
				name="label.add" />
		</a>

		<s:form cssClass="action-form">
			<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable fade in">
					<button class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.ActionErrors" />
					</h2>
					<ul class="margin-base-top">
						<s:iterator value="actionErrors">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasActionMessages()">
				<div class="alert alert-info alert-dismissable fade in">
					<button class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="messages.confirm" />
						</h3>
						<ul class="margin-base-top">
							<s:iterator value="actionMessages">
								<li><s:property escapeHtml="false" /></li>
							</s:iterator>
						</ul>
					</h2>
				</div>
			</s:if>
			<%-- <fieldset class="margin-more-top"><legend><s:text name="title.categoryTree" /></legend> --%>

			<s:if test="serviceStatus != 0">
				<div class="alert alert-info">
					<button class="close hidden" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<span
						data-entando-progress-url="<s:url action="displayUpdatingReferencesStatus" />"
						data-entando-progress-template-wip="<strong>Updating references {{percentage}}%&hellip;</strong>&#32;<small class='text-muted'>({{done}} / {{total}})</small>"
						data-entando-progress-template-done="<strong>References updated successfully!</strong>">
						<strong>Updating references</strong>
					</span>
				</div>
			</s:if>


			<s:set var="categoryTreeStyleVar">
				<wp:info key="systemParam" paramName="treeStyle_category" />
			</s:set>

			<script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>

			<div class="table-responsive overflow-visible">
				<table id="categoryTree"
					class="table table-bordered table-hover table-treegrid">
					<thead>
						<tr>
<!-- 						class="col-sm-10" -->
							<th> <s:text name="label.category.tree"/> 
								<button type="button" class="btn-no-button expand-button"
									id="expandAll">
									<i class="fa fa-plus-square-o treeInteractionButtons"
										aria-hidden="true"></i>&#32;<s:text name="label.category.expandAll"/>
								</button>
								<button type="button" class="btn-no-button" id="collapseAll">
									<i class="fa fa-minus-square-o treeInteractionButtons"
										aria-hidden="true"></i>&#32;<s:text name="label.category.collapseAll"/>
								</button>
							</th>
							<th class="col-sm-1 text-center"><s:text name="label.category.actions"/></th>
						</tr>
					</thead>

					<tbody>
						<s:set var="inputFieldName" value="%{'selectedNode'}" />
						<s:set var="selectedTreeNode" value="%{selectedNode}" />
						<s:set var="selectedPage" value="%{getCategory(selectedNode)}" />
						<s:set var="isPosition" value="true" />

						<s:if test="#categoryTreeStyleVar == 'classic'">
							<s:set var="currentRoot" value="treeRootNode" />
							<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
						</s:if>
						<s:elseif test="#categoryTreeStyleVar == 'request'">
							<s:set var="openTreeActionName" value="'openCloseCategoryTree'" />
							<s:set var="closeTreeActionName" value="'openCloseCategoryTree'" />
							<s:set var="currentRoot" value="showableTree" />
							<s:include
								value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
						</s:elseif>
					</tbody>
				</table>
			</div>

			<script>
				$('.table-treegrid').treegrid();
			</script>
		</s:form>
	</div>
</div>
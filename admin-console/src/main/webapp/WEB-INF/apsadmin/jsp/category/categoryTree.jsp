<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><a
		href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text
				name="menu.configure" /></a></li>
	<li class="page-title-container"><s:text
			name="title.categoryManagement" /></li>
</ol>
<h1 class="page-title-container">
	<s:text name="title.categoryManagement" />
	<span class="pull-right"> <a tabindex="0" role="button"
		data-toggle="popover" data-trigger="focus" data-html="true" title=""
		data-content="TO be inserted" data-placement="left"
		data-original-title=""><i class="fa fa-question-circle-o"
			aria-hidden="true"></i></a>
	</span>
</h1>
<br>


<div id="main" role="main">

	<p>
		<s:text name="note.categoryTree.intro" />
	</p>
	<div role="search">

		<s:include
			value="/WEB-INF/apsadmin/jsp/category/include/categorySearchForm.jsp" />

		<hr />

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
			
<%-- 			<script>
				//one domready to rule 'em all
				$(function() {
					<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/js_trees_context_menu.jsp" />
					
					$('#modal-move-branch').on('show.bs.modal', function(){
						$('input[type="text"]', this).val("");
					});
					
					new EntandoTypeaheadTree({
						url: '<s:url action="searchParentsForMove" />',
						treetypeahead: $('#treetypeahead'),
						labelNoResult: '<s:property value="%{getText('label.noResults')" escapeCsv="false" escapeHtml="false" escapeJavaScript="true" escapeXml="false" />',
						labelError: '<s:property value="%{getText('label.searchError')" escapeCsv="false" escapeHtml="false" escapeJavaScript="true" escapeXml="false" />',
						dataBuilder: function(query) {
								return jQuery.extend({
								'selectedNode': $('[name="selectedNode"]:checked', '#categoryTree').first().val()
							}, {
								'pageCodeToken': query
							})
						}
					})
					
					Entando.UpdateBar($('[data-entando-progress-url]'));
					
					$('[data-entando-progress-url]').on('entando.progress', function(ev, perc) {
						if(perc==100){
							$(this).parents('.alert.alert-info').removeClass('alert-info').addClass('alert-success');
						}
					});
				});
 			</script> --%>
			

			<table id="categoryTree" class="table table-bordered table-hover table-treegrid">
				<thead>
					<tr>
						<th>Coso</th>
						<th>Action</th>
					</tr>
				</thead>

				<s:set var="inputFieldName" value="%{'selectedNode'}" />
				<s:set var="selectedTreeNode" value="%{selectedNode}" />

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

			<script>
				$('.table-treegrid').treegrid();
			</script> 

			

			<p class="sr-only">
				<s:text name="title.categoryActionsIntro" />
			</p>
			<fieldset data-toggle="tree-toolbar">
				<legend>
					<s:text name="title.categoryActions" />
				</legend>
				<div class="btn-toolbar" data-toggle="tree-toolbar-actions">
					<div
						class="btn-group btn-group-sm margin-small-top margin-small-bottom">
						<wpsf:submit type="button" action="detail"
							title="%{getText('category.options.detail')}"
							data-toggle="tooltip" cssClass="btn btn-info">
							<span class="sr-only"><s:text
									name="category.options.detail" /></span>
							<span
								class="icon<script src="<wp:resourceURL />administration/js/entando-updater.js"></script>
							 fa fa-info"></span>
						</wpsf:submit>
					</div>
					<div
						class="btn-group btn-group-sm margin-small-top margin-small-bottom">
						<wpsf:submit type="button" action="new"
							title="%{getText('category.options.new')}" data-toggle="tooltip"
							cssClass="btn btn-info">
							<span class="sr-only"><s:text name="category.options.new" /></span>
							<span class="icon fa fa-plus-circle"></span>
						</wpsf:submit>
						<wpsf:submit type="button" action="edit"
							title="%{getText('category.options.modify')}"
							data-toggle="tooltip" cssClass="btn btn-info">
							<span class="sr-only"><s:text
									name="category.options.modify" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</wpsf:submit>
					</div>
					<wp:ifauthorized permission="superuser">
						<div
							class="btn-group btn-group-sm margin-small-top margin-small-bottom">
							<button class="btn btn-info" data-toggle="modal"
								data-target="#modal-move-tree"
								title="Move this categoty or branch within another one">
								<span class="icon fa fa-sort"></span> </span>
							</button>
						</div>
					</wp:ifauthorized>
					<div
						class="btn-group btn-group-sm margin-small-top margin-small-bottom">
						<wpsf:submit type="button" action="trash"
							title="%{getText('category.options.delete')}"
							data-toggle="tooltip" cssClass="btn btn-warning">
							<span class="sr-only"><s:text
									name="category.options.delete" /></span>
							<span class="icon fa fa-times-circle"></span>
						</wpsf:submit>
					</div>
				</div>
			</fieldset>

			<wp:ifauthorized permission="manageCategories">
				<!-- Modal modal-move-tree -->
				<div class="modal fade" id="modal-move-tree" tabindex="-1"
					role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">
									<span class="icon fa fa-times"></span>
								</button>
								<h4 class="modal-title">Inject branch to</h4>
							</div>
							<div class="modal-body">
								<div class="">
									<input type="text" class="form-control" id="treetypeahead"
										name="parentCategoryCode" autocomplete="off" />
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Cancel</button>
								<wpsf:submit action="moveTree" type="button"
									cssClass="btn btn-primary">
									<s:text name="Apply" />
								</wpsf:submit>
							</div>
						</div>
					</div>
				</div>
			</wp:ifauthorized>

		</s:form>
	</div>
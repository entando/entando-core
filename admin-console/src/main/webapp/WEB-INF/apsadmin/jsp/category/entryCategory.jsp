<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="menu.configure" /></li>
	<li><a href="<s:url namespace="/do/Category" action="viewTree" />"><s:text
				name="title.categoryManagement" /></a></li>
	<li class="page-title-container"><s:if test="strutsAction == 1">
			<s:text name="title.addCategory" />
		</s:if> <s:elseif test="strutsAction == 2">
			<s:text name="title.editCategory" />
		</s:elseif></li>
</ol>
<h1 class="page-title-container">
	<s:if test="strutsAction == 1">
		<s:text name="title.addCategory" />
	</s:if>
	<s:elseif test="strutsAction == 2">
		<s:text name="title.editCategory" />
	</s:elseif>
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
	<s:form action="save" cssClass="form-horizontal">
		<s:set var="currentFieldFieldErrorsVar"
			value="%{fieldErrors['parentCategoryCode']}" />
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">
					<span class="pficon pficon-close"></span>
				</button>
				<span class="pficon pficon-error-circle-o"></span>
				<s:if test="currentFieldFieldErrorsVar">
					<div>
						<strong><s:text name="error.category.noParentSelected" /></strong>
					</div>
				</s:if>
				<s:else>
					<div>
						<s:text name="message.title.FieldErrors" />
					</div>
				</s:else>
			</div>
		</s:if>


		<p class="sr-only">
			<wpsf:hidden name="strutsAction" />
			<s:if test="getStrutsAction() == 2">
				<wpsf:hidden name="categoryCode" />
			</s:if>
			<wpsf:hidden name="parentCategoryCode" />
		</p>

		<%-- languages --%>
		<s:iterator value="langs">
			<s:set var="currentFieldFieldErrorsVar"
				value="%{fieldErrors['lang'+code]}" />
			<s:set var="currentFieldHasFieldErrorVar"
				value="#currentFieldFieldErrorsVar != null && !#currentFieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar"
				value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div
				class="form-group<s:property value="controlGroupErrorClassVar" />">
				<div class="col-xs-2 control-label">
					<label for="lang<s:property value="code" />"> <abbr
						class="label label-info" title="<s:property value="descr" />"><s:property
								value="code" /></abbr>&#32; <s:text name="name.categoryTitle" />
					</label>
				</div>
				<div class="col-xs-10">
					<wpsf:textfield name="%{'lang'+code}" id="%{'lang'+code}"
						value="%{titles.get(code)}" cssClass="form-control" />
					<s:if test="#currentFieldHasFieldErrorVar">
						<p class="help help-block">
							<s:iterator value="#currentFieldFieldErrorsVar">
								<s:property />
								&#32;
							</s:iterator>
						</p>
					</s:if>
				</div>
			</div>
		</s:iterator>

		<%-- category code --%>
		<s:set var="currentFieldFieldErrorsVar"
			value="%{fieldErrors['categoryCode']}" />
		<s:set var="currentFieldHasFieldErrorVar"
			value="#currentFieldFieldErrorsVar != null && !#currentFieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar"
			value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div
			class="form-group<s:property value="controlGroupErrorClassVar" />">
			<div class="col-xs-2 control-label">
				<label for="categoryCode"><s:text name="name.categoryCode" /></label>
			</div>
			<div class="col-xs-10">
				<wpsf:textfield name="categoryCode" id="categoryCode"
					disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
				<s:if test="getStrutsAction() != 2">
					<wpsf:hidden name="selectedNode" value="%{parentCategoryCode}" />
				</s:if>
				<s:elseif test="getStrutsAction() == 2">
					<wpsf:hidden name="selectedNode" value="%{categoryCode}" />
				</s:elseif>
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="help help-block">
						<s:iterator value="#currentFieldFieldErrorsVar">
							<s:property />
							&#32;
						</s:iterator>
					</p>
				</s:if>
			</div>
		</div>

		<!-- tree position -->
		<div
			class="form-group<s:property value="controlGroupErrorClassVar" />">
			<div class="col-xs-2 control-label">
				<label><s:text name="name.tree.position" /></label>
			</div>
			<div class="col-xs-10">
				<script
					src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>
				<s:include
					value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/category/categoryTree-extra.jsp" />

				<table id="categoryTree"
					class="table table-bordered table-hover table-treegrid">
					<thead>
						<tr>
							<!-- 						class="col-sm-10" -->
							<th><s:text name="label.category.tree" />
								<button type="button" class="btn-no-button expand-button"
									id="expandAll">
									<i class="fa fa-plus-square-o treeInteractionButtons"
										aria-hidden="true"></i>&#32;
									<s:text name="label.category.expandAll" />
								</button>
								<button type="button" class="btn-no-button" id="collapseAll">
									<i class="fa fa-minus-square-o treeInteractionButtons"
										aria-hidden="true"></i>&#32;
									<s:text name="label.category.collapseAll" />
								</button></th>
						</tr>
					</thead>
					<tbody>

						<s:set var="inputFieldName" value="%{'parentCategoryCode'}" />
						<s:set var="selectedTreeNode" value="%{parentCategoryCode}" />
						<s:set var="selectedPage"
							value="%{getCategory(parentCategoryCode)}" />
						<s:set var="currentRoot" value="treeRootNode" />
						<s:set var="isPosition" value="false" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />

					</tbody>
				</table>
				<script>
					$('.table-treegrid').treegrid();
				</script>
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12">
				<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>

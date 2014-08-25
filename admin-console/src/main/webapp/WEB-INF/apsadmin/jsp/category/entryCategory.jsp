<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Category" />"
		title="<s:text name="note.goToSomewhere" />: <s:text name="title.categoryManagement" />">
		<s:text name="title.categoryManagement" /></a>
		&#32;/&#32;
		<s:if test="strutsAction == 1">
			<s:text name="title.newCategory" />
		</s:if>
		<s:elseif test="strutsAction == 2">
			<s:text name="title.editCategory" />
		</s:elseif>
	</span>
</h1>
<div id="main" role="main">
	<s:if test="strutsAction == 2"><s:set var="breadcrumbs_pivotCategoryCode" value="categoryCode" /></s:if>
	<s:else><s:set var="breadcrumbs_pivotCategoryCode" value="parentCategoryCode" /></s:else>
	<s:include value="/WEB-INF/apsadmin/jsp/category/include/categoryInfo_breadcrumbs.jsp" />

	<s:form action="save" cssClass="form-horizontal">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
					<%-- <ul class="margin-base-top">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
					--%>
			</div>
		</s:if>

		<p class="sr-only">
			<wpsf:hidden name="strutsAction" />
			<s:if test="getStrutsAction() == 2"><wpsf:hidden name="categoryCode" /></s:if>
			<wpsf:hidden name="parentCategoryCode" />
		</p>
		<%-- category code --%>
			<s:set var="currentFieldFieldErrorsVar" value="%{fieldErrors['categoryCode']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldFieldErrorsVar != null && !#currentFieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="controlGroupErrorClassVar" />">
				<div class="col-xs-12">
					<label for="categoryCode"><s:text name="name.categoryCode" /></label>
						<wpsf:textfield name="categoryCode" id="categoryCode" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
						<s:if test="getStrutsAction() != 2">
							<wpsf:hidden name="selectedNode" value="%{parentCategoryCode}" />
						</s:if>
						<s:elseif test="getStrutsAction() == 2">
							<wpsf:hidden name="selectedNode" value="%{categoryCode}" />
						</s:elseif>
						<s:if test="#currentFieldHasFieldErrorVar">
							<p class="help help-block"><s:iterator value="#currentFieldFieldErrorsVar"><s:property />&#32;</s:iterator></p>
						</s:if>
				</div>
			</div>
			<s:iterator value="langs">
				<s:set var="currentFieldFieldErrorsVar" value="%{fieldErrors['lang'+code]}" />
				<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldFieldErrorsVar != null && !#currentFieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="lang<s:property value="code" />">
							<abbr class="label label-info" title="<s:property value="descr" />"><s:property value="code" /></abbr>&#32;
							<s:text name="name.categoryTitle" />
						</label>
						<wpsf:textfield name="%{'lang'+code}" id="%{'lang'+code}" value="%{titles.get(code)}" cssClass="form-control" />
						<s:if test="#currentFieldHasFieldErrorVar">
							<p class="help help-block"><s:iterator value="#currentFieldFieldErrorsVar"><s:property />&#32;</s:iterator></p>
						</s:if>
					</div>
				</div>
			</s:iterator>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
      		<span class="icon fa fa-floppy-o">&#32;</span>
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
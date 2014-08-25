<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
		<s:text name="%{'title.' + entityManagerName + '.management'}" /></a>
		&#32;/&#32;
		<s:if test="operationId == 1">
			<s:text name="title.entityTypes.editType.new" />
		</s:if>
		<s:else>
			<s:text name="title.entityTypes.editType.edit" />
		</s:else>
	</span>
</h1>

<div id="main" role="main">

<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4"><s:text name="message.title.FieldErrors" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
			            <li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
	</div>
</s:if>

<div class="panel panel-default">
	<div class="panel-body">
		<s:text name="note.entityTypes.editType.intro.2" />
	</div>
</div>

<s:form action="saveEntityType">

<s:set var="entityType" value="entityType" />
<s:if test="operationId != 1">
	<p class="sr-only">	
		<wpsf:hidden name="entityTypeCode" value="%{#entityType.typeCode}" />
	</p>
</s:if>

<fieldset class="col-xs-12"><legend><s:text name="label.info" /></legend>
	<div class="form-group">
		<s:if test="operationId == 1">
			<label for="entityTypeCode"><s:text name="label.code" /></label>
			<wpsf:textfield name="entityTypeCode" id="entityTypeCode" value="%{#entityType.typeCode}" cssClass="form-control" />
		</s:if>
		<s:else>
			<label for="entityTypeCode"><s:text name="label.code" /></label>
			<wpsf:textfield name="entityTypeCode" id="entityTypeCode" value="%{#entityType.typeCode}" cssClass="form-control" disabled="true" />	
		</s:else>
	</div>
	
	<div class="form-group">
		<label for="entityTypeDescription"><s:text name="label.description" /></label>
		<wpsf:textfield name="entityTypeDescription" id="entityTypeDescription" value="%{#entityType.typeDescr}" cssClass="form-control" />
	</div>
</fieldset>

<%-- 
	hookpoint for meta-info and the like
	allowed Plugins: jacms (but so far we have not a check on this)
	
	Based on the Plugin Pattern, we can calculate a proper path for this inclusion
	/WEB-INF/plugins/<plugin_code>/apsadmin/jsp/entity/include/entity-type-entry.jsp 
--%>
<s:if test="null != #hookpoint_plugin_code">
	<s:include value="%{'/WEB-INF/plugins/' + #hookpoint_plugin_code + '/apsadmin/jsp/entity/include/entity-type-entry.jsp'}" />
</s:if>

<fieldset class="col-xs-12"><legend><s:text name="label.attributes" /></legend>
	<s:include value="/WEB-INF/apsadmin/jsp/entity/include/attribute-operations-add.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/entity/include/attribute-list.jsp" />
</fieldset>

<div class="form-horizontal">
	<div class="form-group">
	  <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
	    <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="saveEntityType" >
	      <span class="icon fa fa-floppy-o"></span>&#32;
	      <s:text name="label.save" />
	    </wpsf:submit>
	  </div>
	</div>
</div>

</s:form>

</div>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
		<s:text name="%{'title.' + entityManagerName + '.management'}" /></a>
		&#32;/&#32;
		<a href="<s:url action="initEditEntityType" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="entityType.typeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityTypes.editType.edit" />"><s:text name="title.entityTypes.editType.edit" />: <code><s:property value="entityType.typeCode" /> - <s:property value="entityType.typeDescr" /></code></a>
	</span>
</h1>
<div id="main" role="main">

<s:form action="saveCompositeAttribute" cssClass="form-horizontal">

<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none">
			<s:text name="message.title.FieldErrors" />
   		</h2>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</div>
</s:if>

<s:set var="listAttribute" value="listAttribute" />
<s:set var="compositeAttribute" value="compositeAttributeOnEdit" />

<div class="alert alert-info">
	<s:text name="note.workingOnAttribute" />:&#32;
	<s:if test="null != #listAttribute">
		<code><s:property value="#compositeAttribute.type" /></code>,&#32;
		<s:text name="note.workingOnAttributeIn" />&#32;
		<code><s:property value="#listAttribute.name" /></code>&#32;
		(<code><s:property value="#listAttribute.type" /></code>)
	</s:if>
	<s:else>
		<code><s:property value="#compositeAttribute.name" /></code>
	</s:else>
</div>

<fieldset class="margin-more-top"><legend><s:text name="label.info" /></legend>
	<div class="form-group">
		<div class="col-xs-12">
			<label for="compositeAttribute.type"><s:text name="label.type" />:</label>
			<wpsf:textfield cssClass="form-control" id="compositeAttribute.type" name="compositeAttribute.type" value="%{#compositeAttribute.type}" disabled="true" />
		</div>
	</div>
</fieldset>

<fieldset><legend><s:text name="label.attributes" /></legend>
	<div class="form-group">
	<div class="col-xs-12">
		<label for="attributeTypeCode"><s:text name="label.type" />:</label>
	
		<div class="input-group">
			<wpsf:select list="allowedAttributeElementTypes" id="attributeTypeCode" name="attributeTypeCode" listKey="type" listValue="type" cssClass="form-control"/>
			<span class="input-group-btn">
				<wpsf:submit type="button" value="%{getText('label.add')}" action="addAttributeElement" cssClass="btn btn-default" />
			</span>
		</div>
	</div>
	</div>

<s:if test="#compositeAttribute.attributes.size > 0">
	<div class="table-responsive margin-base-vertical">
	<table class="table table-bordered" id="fagiano_compositeTypesList">
	<tr>
		<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
		<th><s:text name="label.code" /></th>
		<th><s:text name="label.type" /></th>
		<th class="text-center text-nowrap" class="text-center text-nowrap"><abbr title="<s:text name="Entity.attribute.flag.mandatory.full" />"><s:text name="Entity.attribute.flag.mandatory.short" /></abbr></th>
	</tr>

	<s:iterator value="#compositeAttribute.attributes" var="attribute" status="elementStatus">
		<tr>
			<td>
				<s:set var="elementIndex" value="#elementStatus.index" />
				<s:include value="/WEB-INF/apsadmin/jsp/entity/include/attribute-operations-misc-composite.jsp" />
			</td>
			<td><code><s:property value="#attribute.name" /></code></td>
	 		<td><s:property value="#attribute.type" /></td>
			<td class="text-center">
				<s:if test="#attribute.required">
					<span class="icon fa fa-check-square-o" title="<s:text name="label.yes" />"></span>
				</s:if> 
				<s:else>
					<span class="icon fa fa-square-o" title="<s:text name="label.no" />"></span>
				</s:else>
			</td>
		</tr>
	</s:iterator>

	</table>
</s:if>
</fieldset>

<div class="form-group">
  <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
    <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="saveCompositeAttribute" >
      <span class="icon fa fa-floppy-o"></span>&#32;
      <s:text name="label.save" />
    </wpsf:submit>
  </div>
</div>

</s:form>
</div>
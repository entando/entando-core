<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
		<s:text name="%{'title.' + entityManagerName + '.management'}" /></a>
		&#32;/&#32;
		<a href="<s:url action="initEditEntityType" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="entityType.typeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityTypes.editType.edit" />"><s:text name="title.entityTypes.editType.edit" />: <code><s:property value="entityType.typeCode" /> - <s:property value="entityType.typeDescr" /></code></a>
	</span>
</h1>

<div id="main" role="main">

<s:set name="listAttribute" value="listAttribute" />
<s:set name="compositeAttribute" value="compositeAttributeOnEdit" />

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

<s:set name="attribute" value="getAttributePrototype(attributeTypeCode)" />

<s:form action="saveAttributeElement">


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

<p class="sr-only">
	<wpsf:hidden name="attributeTypeCode" />
	<wpsf:hidden name="strutsAction" />
</p>
<fieldset class="col-xs-12"><legend><s:text name="label.info" /></legend>
	<div class="form-group">
		<label for="attributeType"><s:text name="label.type" /></label>
		<wpsf:textfield  cssClass="form-control" id="attributeType" name="attributeType" value="%{attributeTypeCode}" disabled="true" />
	</div>
	<div class="form-group">
		<label for="attributeName"><s:text name="label.code" /></label>
		<wpsf:textfield name="attributeName" id="attributeName" cssClass="form-control"/> 
	</div>
	<div class="form-group">
		<label for="attributeDescription"><s:text name="label.description" /></label>
		<wpsf:textfield name="attributeDescription" id="attributeDescription" cssClass="form-control"/> 
	</div>
	<ul>
	<li class="checkbox">
		<label for="required"><s:text name="Entity.attribute.flag.mandatory.full" /><wpsf:checkbox name="required" id="required"/></label>
	</li>
	<s:if test="isEntityManagerSearchEngineUser() && isIndexableOptionSupported(attributeTypeCode)">
	<li class="checkbox">
		<label for="indexable"><s:text name="Entity.attribute.flag.indexed.full" /><wpsf:checkbox name="indexable" id="indexable"/></label>
	</li>
	</s:if>
	</ul>
</fieldset>

<s:if test="#attribute.textAttribute">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-text.jsp" />
</s:if>

<s:elseif test="#attribute.type == 'Number'">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-number.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-date.jsp" />
</s:elseif>

<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-ognl.jsp" />

<div class="form-horizontal">
	<div class="form-group">
	  <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
	    <wpsf:submit type="button" cssClass="btn btn-primary btn-block" >
	      <span class="icon fa fa-floppy-o"></span>&#32;
	      <s:text name="label.save" />
	    </wpsf:submit>
	  </div>
	</div>
</div>
</s:form>
</div>
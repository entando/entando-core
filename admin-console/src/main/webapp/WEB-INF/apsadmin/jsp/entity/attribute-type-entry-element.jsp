<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
		<s:text name="%{'title.' + entityManagerName + '.management'}" /></a>
		&#32;/&#32;
		<s:if test="strutsAction == 2">
		<a href="<s:url action="initEditEntityType" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="entityType.typeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityTypes.editType.edit" />">
		</s:if>

		<s:text name="title.entityTypes.editType.edit" />: 
		<code><s:property value="entityType.typeCode" /> - <s:property value="entityType.typeDescr" /></code>
		
		<s:if test="strutsAction == 2">
		</a>
		</s:if>
	</span>
</h1>

<div id="main" role="main">

<s:set name="listAttribute" value="listAttribute" />
<s:set name="attributeElement" value="attributeElement" />

<div class="alert alert-info">
	<s:text name="note.workingOnAttribute" />:&#32;
	<s:property value="#attributeElement.type" />,&#32;
	<s:text name="note.workingOnAttributeIn" />&#32;
	<code><s:property value="#listAttribute.name" /></code>&#32;
		(<s:property value="#listAttribute.type" />)
</div>

<s:form action="saveListElement">

<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
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
	<wpsf:hidden name="attributeName"/>
	<wpsf:hidden name="attributeTypeCode"/>
	<wpsf:hidden name="strutsAction"/>
</p>

<s:if test="isEntityManagerSearchEngineUser() && isIndexableOptionSupported(attributeTypeCode)">
<fieldset class="col-xs-12"><legend><s:text name="label.info" /></legend>

	<div class="form-group">
		<label for="attributeTypeCode"><s:text name="label.type" />:</label>
		<wpsf:textfield id="attributeTypeCode" name="attributeTypeCode" value="%{attributeTypeCode}" disabled="true" cssClass="form-control" />
	</div>
	<div class="form-group">
		<wpsf:checkbox name="indexable" id="indexable" />
		<label for="indexable"><s:text name="Entity.attribute.flag.indexed.full" /></label>
	</div>

</fieldset>
</s:if>

<s:if test="#attributeElement.textAttribute">
<s:set var="attribute" value="#attributeElement"></s:set>
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-text.jsp"/>
</s:if>

<s:elseif test="#attributeElement.type == 'Number'">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-number.jsp"/>
</s:elseif>

<s:elseif test="#attributeElement.type == 'Date'">
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-date.jsp"/>
</s:elseif>

<s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-ognl.jsp"/>

<div class="form-group">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
			<s:text name="label.continue" />
		</wpsf:submit>
	</div>
</div>
</s:form>

</div>
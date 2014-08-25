<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12 margin-base-top">
	<legend><s:text name="label.settings" /></legend>
<s:if test="#attribute.type == 'Enumerator'">
	<div class="form-group">
		<label for="enumeratorStaticItems"><s:text name="Entity.attribute.setting.enumerator.items" /></label>
		<wpsf:textfield name="enumeratorStaticItems" id="enumeratorStaticItems" cssClass="form-control" />
	</div>
	
	<div class="form-group">
		<label for="enumeratorStaticItemsSeparator"><s:text name="Entity.attribute.setting.enumerator.separator" /></label>
		<wpsf:textfield name="enumeratorStaticItemsSeparator" id="enumeratorStaticItemsSeparator" cssClass="form-control" />
	</div>
	
	<s:set var="enumeratorExtractorBeans" value="enumeratorExtractorBeans"></s:set>
	<s:if test="null != #enumeratorExtractorBeans && #enumeratorExtractorBeans.size() > 0">
	<div class="form-group">
		<label for="enumeratorExtractorBean"><s:text name="Entity.attribute.setting.enumerator.extractorBean" /></label>
		<wpsf:select list="#enumeratorExtractorBeans" name="enumeratorExtractorBean" id="enumeratorExtractorBean" headerKey="" headerValue="" cssClass="form-control"/>
	</div>
	</s:if>
</s:if>
<s:elseif test="#attribute.textAttribute">
	<div class="form-group">
		<label for="minLength"><s:text name="Entity.attribute.flag.minLength.full" /></label>
		<wpsf:textfield name="minLength" id="minLength" cssClass="form-control"/>
	</div>

	<div class="form-group">
		<label for="maxLength"><s:text name="Entity.attribute.flag.maxLength.full" /></label>
		<wpsf:textfield name="maxLength" id="maxLength" cssClass="form-control" />
	</div>
	
	<div class="form-group">
		<label for="regexp"><s:text name="Entity.attribute.setting.regexp.full" /></label>
		<wpsf:textfield name="regexp" id="regexp" cssClass="form-control" />
	</div>
</s:elseif>
	
	<%--
	<s:set var="sameAttributesList" value="sameAttributes" />
	
	<p>
		<label for="rangeStartString">** rangeStartString ** :</label><br />
		<wpsf:textfield name="rangeStartString" id="rangeStartString" cssClass="text"/>
	</p>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<p>
		<label for="rangeStartStringAttribute">** OR rangeStartStringAttribute ** :</label><br />
		<wpsf:select name="rangeStartStringAttribute" id="rangeStartStringAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="" listKey="name" listValue="name"></wpsf:select>
	</p>
	</s:if>
	
	<p>
		<label for="rangeEndString"> ** rangeEndString ** :</label><br />	
		<wpsf:textfield name="rangeEndString" id="rangeEndString" cssClass="text" />
	</p>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<p>
		<label for="rangeEndStringAttribute"> ** OR rangeEndStringAttribute ** :</label><br />	
		<wpsf:select name="rangeEndStringAttribute" id="rangeEndStringAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="" listKey="name" listValue="name"></wpsf:select>
	</p>
	</s:if>
	
	<p>
		<label for="equalString"> ** equalString ** :</label><br />	
		<wpsf:textfield name="equalString" id="equalString" cssClass="text" />
	</p>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<p>
		<label for="equalStringAttribute"> ** OR equalStringAttribute ** :</label><br />	
		<wpsf:select name="equalStringAttribute" id="equalStringAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="" listKey="name" listValue="name"></wpsf:select>
	</p>
	</s:if>
	--%>
	
</fieldset>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="label.settings" /></legend>
	
	<s:set var="sameAttributesList" value="sameAttributes" />
	
	<div class="form-group">
		<label for="rangeStartNumber"><s:text name="note.range.from" /></label>
		<wpsf:textfield name="rangeStartNumber" id="rangeStartNumber" cssClass="form-control"/>
	</div>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<div class="form-group">
		<label for="rangeStartNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.from.attribute" />:</label>
		<wpsf:select name="rangeStartNumberAttribute" id="rangeStartNumberAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass="form-control"/>
	</div>			
	</s:if>
		
	<div class="form-group">
		<label for="rangeEndNumber"><s:text name="note.range.to" /></label>
		<wpsf:textfield name="rangeEndNumber" id="rangeEndNumber" cssClass="form-control" />
	</div>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<div class="form-group">
		<label for="rangeEndNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.to.attribute" />:</label>	
		<wpsf:select name="rangeEndNumberAttribute" id="rangeEndNumberAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass="form-control"/>
	</div>
	</s:if>
	
	<div class="form-group">
		<label for="equalNumber"><s:text name="note.equals.to" /></label>	
		<wpsf:textfield name="equalNumber" id="equalNumber" cssClass="form-control" />
	</div>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<div class="form-group">
		<label for="equalNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.equals.to.attribute" />:</label>	
		<wpsf:select name="equalNumberAttribute" id="equalNumberAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass="form-control"/>
	</div>
	</s:if>
	
</fieldset>
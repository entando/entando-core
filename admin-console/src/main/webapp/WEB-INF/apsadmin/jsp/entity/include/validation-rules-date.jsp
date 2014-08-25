<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="label.settings" /></legend>
	
	<s:set var="sameAttributesList" value="sameAttributes" />
	
	<div class="form-group">
		<label for="rangeStartDate_cal"><s:text name="note.range.from" /></label>
		<s:date name="rangeStartDate" format="dd/MM/yyyy" var="rangeStartDateValue" />
		<wpsf:textfield name="rangeStartDate" id="rangeStartDate_cal" value="%{#rangeStartDateValue}" cssClass="form-control"/>
		<span class="help-block"><s:text name="label.date.format" /></span>
	</div>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<div class="form-group">
		<label for="rangeStartDateAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.from.attribute" />:</label>
		<wpsf:select name="rangeStartDateAttribute" id="rangeStartDateAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass="form-control"/>
	</div>
	</s:if>
	
	<div class="form-group">
		<label for="rangeEndDate_cal"><s:text name="note.range.to" /></label>
		<s:date name="rangeEndDate" format="dd/MM/yyyy" var="rangeEndDateValue" />
		<wpsf:textfield name="rangeEndDate" id="rangeEndDate_cal" value="%{#rangeEndDateValue}" cssClass="form-control" />
		<span class="help-block"><s:text name="label.date.format" /></span>
	</div>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<div class="form-group">
		<label for="rangeEndDateAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.to.attribute" />:</label>
		<wpsf:select name="rangeEndDateAttribute" id="rangeEndDateAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass="form-control" />
	</div>
	</s:if>
	
	<div class="form-group">
		<label for="equalDate_cal"><s:text name="note.equals.to" /></label>
		<s:date name="equalDate" format="dd/MM/yyyy" var="equalDateValue" />
		<wpsf:textfield name="equalDate" id="equalDate_cal" value="%{#equalDateValue}" cssClass="form-control" />
		<span class="help-block"><s:text name="label.date.format" /></span>
	</div>
	<s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
	<div class="form-group">
		<label for="equalDateAttribute"><s:text name="note.or" />&#32;<s:text name="note.equals.to.attribute" />:</label>	
		<wpsf:select name="equalDateAttribute" id="equalDateAttribute" 
			list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass="form-control" />
	</div>
	</s:if>
	
</fieldset>
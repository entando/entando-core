<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12 margin-base-top"><legend><s:text name="name.ognlCodes" /></legend>

<p><s:text name="note.ognlCodes.intro.1" /></p>

<div class="form-group">
	<label for="ognlValidationRule.expression"><s:text name="name.expression" /></label>
	<wpsf:textfield id="ognlValidationRule.expression" name="ognlValidationRule.expression" cssClass="form-control" />
</div>

<div class="form-group">
	<wpsf:checkbox name="ognlValidationRule.evalExpressionOnValuedAttribute" id="attributeEnhanced" cssClass="radiocheck" />
	<label for="attributeEnhanced"><s:text name="name.attributeEnhanced" /></label>
</div>

<p><s:text name="note.ognlCodes.intro.2" /></p>

<div class="form-group">
	<label for="ognlValidationRule.helpMessage"><s:text name="name.helpMessage.text" /></label>
	<wpsf:textfield id="ognlValidationRule.helpMessage" name="ognlValidationRule.helpMessage" cssClass="form-control" />
</div>

<div class="form-group">
	<label for="ognlValidationRule.helpMessageKey"><s:text name="name.helpMessage.key" /></label>
	<wpsf:textfield id="ognlValidationRule.helpMessageKey" name="ognlValidationRule.helpMessageKey" cssClass="form-control" />
</div>

<div class="form-group">
	<label for="ognlValidationRule.errorMessage"><s:text name="name.errorMessage.text" /></label>
	<wpsf:textfield id="ognlValidationRule.errorMessage" name="ognlValidationRule.errorMessage" cssClass="form-control" />
</div>

<div class="form-group">
	<label for="ognlValidationRule.errorMessageKey"><s:text name="name.errorMessage.key" /></label>
	<wpsf:textfield id="ognlValidationRule.errorMessageKey" name="ognlValidationRule.errorMessageKey" cssClass="form-control" />
</div>
</fieldset>
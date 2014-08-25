<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div class="panel panel-default">
	<div class="panel-body">
		<div class="form-group">
			<label for="attributeTypeCode" class="control-label"><s:text name="label.type" /></label>
			<div class="input-group">
			<wpsf:select id="attributeTypeCode" list="attributeTypes" name="attributeTypeCode" 
				listKey="type" listValue="type" cssClass="form-control" />
				<span class="input-group-btn">
					<wpsf:submit type="button" action="addAttribute" cssClass="btn btn-default" value="%{getText('label.add')}"/>
				</span>
			</div>
		</div>
	</div>
</div>

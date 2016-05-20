<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h4><s:text name="note.URLLinkTo" /></h4>

<form id="form_externalUrl">
	<div class="col-xs-12">
		<div class="form-group">
			<%--
			<label class="display-block" for="txtName">
				<s:text name="label.url" />
			</label>
			--%>
			<input type="hidden" name="contentOnSessionMarker" value="<s:property value="contentOnSessionMarker" />" />
			<wpsf:textfield id="txtName" name="txtName" maxlength="255" cssClass="form-control" />
			<span class="help help-block">
				<s:text name="note.typeValidURL" />
			</span>
		</div>
	</div>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<button type="submit" id="button_externalURL" name="button_externalURL" class="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.confirm" />
			</button>
		</div>
	</div>
</form>

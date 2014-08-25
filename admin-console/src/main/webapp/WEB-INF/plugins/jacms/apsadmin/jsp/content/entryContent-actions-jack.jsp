<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div class="row margin-none margin-large-top">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit
			id="edit-saveAndContinue"
			data-loading-text="%{getText('label.autosaving.button.text')}"
			data-button-type="autosave"
			action="saveAndContinue"
			type="button"
			cssClass="btn btn-default btn-block"
			title="%{getText('note.button.saveAndContinue')}">
				<span class="icon fa fa-play-circle-o"></span>&#32;
				<s:text name="label.saveAndContinue" />
		</wpsf:submit>
	</div>

	<div class="input-group col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<label for="status" class="sr-only"><s:text name="label.state" /></label>
		<wpsf:select name="status" id="status" list="avalaibleStatus" value="%{content.status}" listKey="key" listValue="%{getText(value)}" cssClass="form-control" />
		<span class="input-group-btn">
			<wpsf:submit action="save" type="button" cssClass="btn btn-default" title="%{getText('note.button.saveContent')}">
				<span class="icon fa fa-floppy-o"></span>
			</wpsf:submit>
		</span>
	</div>

	<wp:ifauthorized permission="validateContents">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit action="saveAndApprove" type="button" cssClass="btn btn-success btn-block" title="%{getText('note.button.saveAndApprove')}" >
				<span class="icon fa fa-check"></span>&#32;
				<s:text name="label.saveAndApprove" />
			</wpsf:submit>
		</div>
		<s:if test="content.onLine">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit action="suspend" type="button" cssClass="btn btn-warning btn-block" title="%{getText('note.button.suspend')}">
					<span class="icon fa fa-pause"></span>&#32;
					<s:text name="label.suspend" />
				</wpsf:submit>
			</div>
		</s:if>
	</wp:ifauthorized>
</div>

<div class="row margin-none margin-large-bottom">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-none">
		<small class="help help-block text-muted text-center">
			<span class="hide">
				<span class="icon fa fa-check-square-o"></span>&#32;<s:text name="note.autosaved.at" />: <span data-autosave="last-save-time"></span>
			</span>&nbsp;<%-- little hack prevents to scroll down the page the first time the message appears --%>
		</small>
	</div>
</div>
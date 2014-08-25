<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.pageModelManagement" />
	</span>
</h1>
<div id="main">
	<s:form action="save" cssClass="form-horizontal">
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<p class="sr-only">
			<wpsf:hidden name="strutsAction" />
			<s:if test="getStrutsAction() == 2">
				<wpsf:hidden name="code" />
			</s:if>
		</p>


		<div class="form-group">
			<div class="col-xs-12">
				<label for="key">Key</label>
				<wpsf:textfield name="code" id="code" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="description">Description</label>
				<wpsf:textfield name="description" id="description" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="pluginCode">Plugin Code</label>
				<wpsf:textfield name="pluginCode" id="pluginCode" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="xmlConfiguration">Xml Configuration</label>
				<wpsf:textarea name="xmlConfiguration" id="xmlConfiguration" cssClass="autotextarea form-control" rows="8" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<label for="template">Template</label>
				<wpsf:textarea name="template" id="template" cssClass="autotextarea  form-control" rows="8" />
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>

	</s:form>
</div>

<script>
jQuery(function(){
	$('.autotextarea').on('focus', function(ev){
		var t = $(this);
		var h = screen.availHeight;
		if(h) {
			t.css('height', (h/2)+'px');
		}
	});
})
</script>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.apiServiceManagement" /></span>
</h1>
<div id="main" role="main">
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
	<s:if test="hasActionMessages()">
		<div class="alert alert-info alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:set var="resourceFlavoursVar" value="resourceFlavours" />
	<s:set var="serviceFlavoursVar" value="serviceFlavours" />

	<s:form action="newService" cssClass="form-horizontal">
		<div class="form-group">
			<label class="control-label col-lg-3 col-md-3" for="service-list">
				<s:text name="label.api.service.createNew" />
			</label>
			<div class="input-group col-md-9 col-lg-9">
				<select id="service-list" name="resourceCode" class="form-control">
					<s:iterator value="#resourceFlavoursVar" var="resourceFlavourGroup">
						<optgroup label="<s:property value="%{getText(#resourceFlavourGroup.get(0).getSectionCode()+'.name')}" escapeHtml="false" />">
							<s:iterator value="#resourceFlavourGroup" var="resource">
								<option value="<s:property value="#resource.code" />">
									<s:property value="#resource.code" /> - <s:property value="#resource.description" />
								</option>
							</s:iterator>
						</optgroup>
					</s:iterator>
				</select>
				<span class="input-group-btn">
					<wpsf:submit type="button" cssClass="btn btn-primary" action="newService">
					<span class="icon fa fa-plus-circle"></span>&#32;<s:text name="api.button.create" /></wpsf:submit>
				</span>
			</div>
		</div>
	</s:form>
	<s:if test="#serviceFlavoursVar.size()>0">
		<s:iterator var="resourceFlavour" value="#resourceFlavoursVar" status="varStatus">
			<s:set var="serviceGroupVar" value="#resourceFlavour.get(0).getSectionCode()" />
			<s:set var="servicesByGroupVar" value="#serviceFlavoursVar[#serviceGroupVar]" />
				<s:if test="null != #servicesByGroupVar && #servicesByGroupVar.size() > 0">
					<p class="sr-only">
						<wpsf:hidden name="serviceGroup" value="%{#serviceGroupVar}" />
					</p>
					<div class="panel panel-default" id="<s:property value="#serviceGroupVar" />">
						<div class="panel-heading"><s:text name="%{#serviceGroupVar}.name" /></div>
						<table class="table table-responsive">
							<tr>
								<th class="text-center"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
								<th><s:text name="name.api.service" /></th>
								<th><s:text name="label.description" /></th>
								<th class="text-center"><abbr title="<s:text name="label.active" />">A</abbr></th>
								<th class="text-center"><abbr title="<s:text name="label.public" />">P</abbr></th>
							</tr>
							<s:iterator value="#servicesByGroupVar" var="service">
								<tr>
									<td class="text-center">
										<a href="<s:url action="trash"><s:param name="serviceKey"><s:property value="#service.key" /></s:param></s:url>" title="<s:text name="label.remove" />: <s:property value="#service.key" />" class="btn btn-xs btn-warning">
											<span class="icon fa fa-times-circle-o"></span>
											<span class="sr-only"><s:text name="label.remove" />: <s:property value="#service.key" /></span>
										</a>
									</td>
									<td>
										<wpsf:hidden name="%{#service.key + '_checkField'}" value="true" />
										<a title="<s:text name="label.edit" />: <s:property value="#service.key" />" href="<s:url action="edit"><s:param name="serviceKey"><s:property value="#service.key" /></s:param></s:url>">
										<span class="icon fa fa-cog"></span>
										<s:property value="#service.key" /></a>
								 	</td>
									<td>
								 		<s:property value="#service.value" />
								 	</td>
									<td class="text-center">
										<s:if test="#service.activeItem">
											<span title="<s:text name="label.active" />" class="icon fa fa-check"></span>
											<span class="sr-only"><s:text name="label.active" /></span>
										</s:if>
										<s:else>
											<span title="<s:text name="label.api.notactive" />" class="icon fa fa-minus text-muted"></span>
											<span class="sr-only"><s:text name="label.api.notactive" /></span>
										</s:else>
									</td>
									<td class="text-center">
										<s:if test="#service.publicItem">
											<span title="<s:text name="label.public" />" class="icon fa fa-check"></span>
											<span class="sr-only"><s:text name="label.public" /></span>
										</s:if>
										<s:else>
											<span title="<s:text name="label.api.notpublic" />" class="icon fa fa-minus text-muted"></span>
											<span class="sr-only"><s:text name="label.api.notpublic" /></span>
										</s:else>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
				</s:if>
		</s:iterator>
	</s:if>
	<s:else>
		<p><s:text name="note.api.noServices" /></p>
	</s:else>
</div>
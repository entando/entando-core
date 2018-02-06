<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<section>

<p>
	<a href="<wp:action path="/ExtStr2/do/Front/Api/Resource/list.action" />" class="btn btn-primary"><i class="icon-arrow-left icon-white"></i>&#32;<wp:i18n key="ENTANDO_API_GOTO_LIST" /></a>
</p>

<h2><wp:i18n key="ENTANDO_API_GOTO_SERVICE_LIST" /></h2>
<s:if test="hasActionErrors()">
	<div class="alert alert-block alert-error">
		<h3 class="alert-heading"><s:text name="message.title.ActionErrors" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasFieldErrors()">
	<div class="alert alert-block alert-error">
		<h3 class="alert-heading"><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:if test="hasActionMessages()">
	<div class="alert alert-block alert-info">
		<h3 class="alert-heading"><s:text name="messages.confirm" /></h3>
		<ul>
			<s:iterator value="actionMessages">
				<li><s:property escapeHtml="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>
<s:set var="resourceFlavoursVar" value="resourceFlavours" />
<s:set var="serviceFlavoursVar" value="serviceFlavours" />


<s:if test="#serviceFlavoursVar != null && #serviceFlavoursVar.size() > 0">
<div class="tabbable tabs-left">
	<ul class="nav nav-tabs">
		<s:iterator var="resourceFlavour" value="#resourceFlavoursVar" status="statusVar">
			<s:set var="serviceGroupVar" value="#resourceFlavour.get(0).getSectionCode()" />
			<s:set var="servicesByGroupVar" value="#serviceFlavoursVar[#serviceGroupVar]" />
			<s:if test="null != #servicesByGroupVar && #servicesByGroupVar.size() > 0">
				<s:if test="#serviceGroupVar == 'core'"><s:set var="captionVar" value="%{#serviceGroupVar}" /></s:if>
				<s:else><s:set var="captionVar" value="%{getText(#serviceGroupVar + '.name')}" /></s:else>
				<li<s:if test="#statusVar.first"> class="active"</s:if>>
					<a href="#api-flavour-<s:property value='%{#captionVar.toLowerCase().replaceAll("[^a-z0-9-]", "")}' />" data-toggle="tab"><s:property value='%{#captionVar}' /></a>
				</li>
			</s:if>
		</s:iterator>
	</ul>

  <div class="tab-content">
	<s:set var="servicesEmptyVar" value="true" />
	<s:iterator var="resourceFlavour" value="#resourceFlavoursVar" status="moreStatusVar">
		<s:set var="serviceGroupVar" value="#resourceFlavour.get(0).getSectionCode()" />
		<s:set var="servicesByGroupVar" value="#serviceFlavoursVar[#serviceGroupVar]" />
		<s:if test="null != #servicesByGroupVar && #servicesByGroupVar.size() > 0">
			<s:set var="servicesEmptyVar" value="false" />
			<s:if test="#serviceGroupVar == 'core'"><s:set var="captionVar" value="%{#serviceGroupVar}" /></s:if>
			<s:else><s:set var="captionVar" value="%{getText(#serviceGroupVar + '.name')}" /></s:else>
			<div class="tab-pane<s:if test="#moreStatusVar.first"> active</s:if>" id="api-flavour-<s:property value='%{#captionVar.toLowerCase().replaceAll("[^a-z0-9]", "")}' />">
			<table class="table table-striped table-bordered table-condensed">
				<caption>
					<s:property value="#captionVar" />
				</caption>
				<tr>
					<th><wp:i18n key="ENTANDO_API_SERVICE" /></th>
					<th><wp:i18n key="ENTANDO_API_DESCRIPTION" /></th>
				</tr>
				<s:iterator var="serviceVar" value="#servicesByGroupVar" >
					<tr>
						<td class="monospace">
							<wp:action path="/ExtStr2/do/Front/Api/Service/detail.action" var="detailActionURL">
								<wp:parameter name="serviceKey"><s:property value="#serviceVar.key" /></wp:parameter>
							</wp:action>
							<a href="<c:out value="${detailActionURL}" escapeXml="false" />"><s:property value="#serviceVar.key" /></a>
						</td>
						<td><s:property value="#serviceVar.value" /></td>
					</tr>
				</s:iterator>
			</table>
			</div>
		</s:if>
	</s:iterator>
	</div>
</div>
</s:if>
<s:else>
<div class="alert alert-block alert-info">
	<p><wp:i18n key="ENTANDO_API_NO_SERVICES" escapeXml="false" /></p>
</div>
</s:else>

<p>
	<a href="<wp:action path="/ExtStr2/do/Front/Api/Resource/list.action" />" class="btn btn-primary"><i class="icon-arrow-left icon-white"></i>&#32;<wp:i18n key="ENTANDO_API_GOTO_LIST" /></a>
</p>

</section>
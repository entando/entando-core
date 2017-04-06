<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="strutsAction == 1">
	<s:set var="labelTitle" value="%{getText('title.bulk.content.group.join')}"/>
</s:if>
<s:elseif test="strutsAction == 4" >
	<s:set var="labelTitle" value="%{getText('title.bulk.content.group.rem')}"/>
</s:elseif>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="list" namespace="/do/jacms/Content"/>"><s:text name="jacms.menu.contentAdmin" /></a></li>
    <li>
		<s:property value="%{#labelTitle}" />
    </li>
</ol>

<h1 class="page-title-container"><s:property value="%{#labelTitle}" />&#32;-&#32;<s:text name="title.bulk.groupChoice" /></h1>

<div id="main" role="main">
	<s:if test="hasErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">
				<span class="pficon pficon-close"></span>
			</button>
			<span class="pficon pficon-error-circle-o"></span>
			<s:text name="message.title.ActionErrors" />
			<ul>
			<s:if test="hasActionErrors()">
				<s:iterator value="actionErrors">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:if>
			<s:if test="hasFieldErrors()">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
			</s:if>
			</ul>
		</div>
	</s:if>
	<s:form action="checkApply" namespace="/do/jacms/Content/Group" >
		<p class="sr-only">
			<wpsf:hidden name="strutsAction"/>
		<s:iterator var="contentId" value="selectedIds" >
			<wpsf:hidden name="selectedIds" value="%{#contentId}" />
		</s:iterator>
		</p>
		<div>
			<p>
			<s:if test="strutsAction == 1">
				<s:text name="note.bulk.content.group.join.choose" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</s:if>
			<s:elseif test="strutsAction == 4">
				<s:text name="note.bulk.content.group.rem.choose" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</s:elseif>
			</p>
		</div>
		<div>
			<ul>
			<s:iterator var="group" value="allowedGroups" >
				<li>
					<wpsf:checkbox name="extraGroupNames" value="%{extraGroupNames.contains(#group.authority)}" fieldValue="%{#group.authority}" />&#32;
					<s:property value="#group.description" />
				</li>
			</s:iterator>
			</ul>
		</div>

		<s:if test="strutsAction == 1">
			<s:set var="labelAction" value="%{getText('label.bulk.content.group.join')}"/>
		</s:if>
		<s:elseif test="strutsAction == 4" >
			<s:set var="labelAction" value="%{getText('label.bulk.content.group.rem')}"/>
		</s:elseif>
		<div class="col-xs-12">
			<wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-success">
				<span class="icon fa fa-times-circle"></span>
				<s:property value="%{#labelAction}" />
			</wpsf:submit>
		</div>
	</s:form>
</div>
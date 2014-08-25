<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<h1><s:text name="jacms.menu.contentAdmin" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main" role="main">
<h2 class="margin-more-bottom"><s:text name="title.contentEditing" /></h2>
<h3 class="sr-only"><s:text name="title.contentInfo" /></h3>

	<s:if test="hasFieldErrors()">
<div class="message message_error">
<h4><s:text name="message.title.FieldErrors" /></h4>	
	<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
		<li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
	</ul>
</div>
	</s:if>

<s:form action="createNewVoid">
<fieldset><legend><s:text name="label.info" /></legend>
<p><s:text name="note.editContentIntro" /></p>
	<p>
	<label for="contentTypeCode" class="basic-mint-label"><s:text name="name.contentType" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentTypeCode" id="contentTypeCode" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
	</p>
	<p>
	<label for="contentDescription" class="basic-mint-label"><s:text name="label.description" />:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" name="contentDescription" id="contentDescription" cssClass="text" />
	</p>
	<p>
	<label for="contentMainGroup" class="basic-mint-label"><s:text name="label.ownerGroup" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentMainGroup" id="contentMainGroup" list="allowedGroups" listKey="name" listValue="descr" cssClass="text" />
	</p>
	<p>
	<label for="contentStatus" class="basic-mint-label"><s:text name="label.state" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentStatus" id="contentStatus" list="avalaibleStatus" cssClass="text" listKey="key" listValue="%{getText(value)}" />
	</p>
</fieldset>	
	<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.continue')}" title="%{getText('note.button.nextToContentEditing')}" cssClass="button" /></p>
</s:form>
</div>
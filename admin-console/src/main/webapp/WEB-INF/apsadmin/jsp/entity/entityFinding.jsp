<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%-- //TODO: refactor all this section -->
<%--
http://localhost:8080/PortalExample/do/Entity/search.action?entityManagerName=jacmsContentManager
 --%>
<h1>**ENTITIES FROM MANAGER** <s:property value="entityManagerName" /> </h1>

	<s:if test="hasFieldErrors()">
		<div class="message message_error">
		<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
		</div>
	</s:if>

<s:form action="search"  role="search">
	
	<fieldset>
		<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>  
		
		<div class="accordion_element">
			
			<s:if test="null != entityTypeCode && entityTypeCode != ''">
				<s:set var="entityPrototype" value="entityPrototype" />
				*<s:property value="#entityPrototype.typeDescr" />* <wpsf:submit useTabindexAutoIncrement="true" value="CAMBIA" cssClass="button" action="changeEntityType" />
				<wpsf:hidden name="entityTypeCode" />
			</s:if>
			<s:else>
				<wpsf:select useTabindexAutoIncrement="true" list="entityPrototypes" name="entityTypeCode" headerKey="" headerValue="%{getText('note.choose')}" 
					listKey="typeCode" listValue="typeDescr"></wpsf:select>
			</s:else>
			
			<s:set var="attributeRolesVar" value="attributeRoles" ></s:set>
			
			<s:if test="null != #attributeRolesVar && #attributeRolesVar.size() > 0">
				
				<s:iterator var="attributeRoleVar" value="#attributeRolesVar">
					<s:set var="currentFieldIdVar">entityFinding_<s:property value="#attributeRoleVar.name" /></s:set> 
					
					<s:if test="%{#attributeRoleVar.formFieldType.toString().equals('TEXT')}">
						<p>
							<label for="<s:property value="%{#currentFieldIdVar}" />"><s:property value="#attributeRoleVar.name" /></label><br />
							<s:set var="textInputFieldName"><s:property value="#attributeRoleVar.name" />_textFieldName</s:set>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldIdVar}" cssClass="text" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" /><br />
						</p>
					</s:if>
					
					<s:elseif test="%{#attributeRoleVar.formFieldType.toString().equals('DATE')}">
						<s:set var="dateStartInputFieldName" ><s:property value="#attributeRoleVar.name" />_dateStartFieldName</s:set>
						<s:set var="dateEndInputFieldName" ><s:property value="#attributeRoleVar.name" />_dateEndFieldName</s:set>
						<p>
							<label for="<s:property value="%{#currentFieldIdVar}" />_dateStartFieldName"><s:property value="#attributeRoleVar.name" /> ** from date **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldIdVar}_dateStartFieldName" cssClass="text" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</p>
						<p>
							<label for="<s:property value="%{#currentFieldIdVar}" />_dateEndFieldName"><s:property value="#attributeRoleVar.name" />** to date **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldIdVar}_dateEndFieldName" cssClass="text" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</p>
					</s:elseif>
					
					<s:elseif test="%{#attributeRoleVar.formFieldType.toString().equals('NUMBER')}">
						<s:set var="numberStartInputFieldName" ><s:property value="#attributeRoleVar.name" />_numberStartFieldName</s:set>
						<s:set var="numberEndInputFieldName" ><s:property value="#attributeRoleVar.name" />_numberEndFieldName</s:set>
						<p>
							<label for="<s:property value="%{#currentFieldIdVar}" />_start"><s:property value="#attributeRoleVar.name" /> ** from value **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldIdVar}_start" cssClass="text" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" /><br />
						</p>
						<p>
							<label for="<s:property value="%{#currentFieldIdVar}" />_end"><s:property value="#attributeRoleVar.name" /> ** to value **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldIdVar}_end" cssClass="text" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" /><br />
						</p>
					</s:elseif>
					
					<s:elseif test="%{#attributeRoleVar.formFieldType.toString().equals('NUMBER')}">
						<p>
							<span class="important"><s:property value="#attributeRoleVar.name" /></span><br />
						</p>
						<s:set var="booleanInputFieldName" ><s:property value="#attributeRoleVar.name" />_booleanFieldName</s:set>
						<s:set var="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
						<ul class="noBullet">
							<li><wpsf:radio useTabindexAutoIncrement="true" id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" cssClass="radio" /><label for="none_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
							<li><wpsf:radio useTabindexAutoIncrement="true" id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" cssClass="radio" /><label for="true_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.yes"/></label></li>
							<li><wpsf:radio useTabindexAutoIncrement="true" id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" cssClass="radio" /><label for="false_<s:property value="#booleanInputFieldName" />" class="normal"><s:text name="label.no"/></label></li>
						</ul>
					</s:elseif>
					
				</s:iterator>
			
			</s:if>
			
			<s:set var="searchableAttributesVar" value="searchableAttributes" ></s:set>
			
			<s:if test="null != #searchableAttributesVar && #searchableAttributesVar.size() > 0">
				
				<s:iterator var="attribute" value="#searchableAttributesVar">
					<s:set var="currentFieldId">entityFinding_<s:property value="#attribute.name" /></s:set> 
					
					<s:if test="#attribute.textAttribute"> 
						<p>
							<label for="<s:property value="%{#currentFieldId}" />"><s:property value="#attribute.name" /></label><br />
							<s:set var="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldId}" cssClass="text" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" /><br />
						</p>
					</s:if>
					
					<s:elseif test="#attribute.type == 'Date'">
						<s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
						<s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
						<p>
							<label for="<s:property value="%{currentFieldId}" />_dateStartFieldName"><s:property value="#attribute.name" /> ** from date **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_dateStartFieldName" cssClass="text" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</p>
						<p>
							<label for="<s:property value="%{currentFieldId}" />_dateEndFieldName"><s:property value="#attribute.name" />** to date **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_dateEndFieldName" cssClass="text" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Number'">
						<s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
						<s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
						<p>
							<label for="<s:property value="currentFieldId" />_start"><s:property value="#attribute.name" /> ** from value **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_start" cssClass="text" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" /><br />
						</p>
						<p>
							<label for="<s:property value="currentFieldId" />_end"><s:property value="#attribute.name" /> ** to value **</label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_end" cssClass="text" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" /><br />
						</p>
					</s:elseif>
					
					<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'"> 
						<p>
							<span class="important"><s:property value="#attribute.name" /></span><br />
						</p>
						<s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
						<s:set var="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
						<ul class="noBullet">
							<li><wpsf:radio useTabindexAutoIncrement="true" id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" cssClass="radio" /><label for="none_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
							<li><wpsf:radio useTabindexAutoIncrement="true" id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" cssClass="radio" /><label for="true_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.yes"/></label></li>
							<li><wpsf:radio useTabindexAutoIncrement="true" id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" cssClass="radio" /><label for="false_<s:property value="#booleanInputFieldName" />" class="normal"><s:text name="label.no"/></label></li>
						</ul>
					</s:elseif>
					
				</s:iterator>
				
			</s:if>
			
			<p><wpsf:submit useTabindexAutoIncrement="true" value="Cerca" cssClass="button" action="search" /></p>
		</div>
	</fieldset>
</s:form>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="entityTypeCode" />
		<s:iterator var="attribute" value="#searchableAttributesVar">
			<s:if test="#attribute.textAttribute">
				<s:set var="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
				<wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
			</s:if>
			<s:elseif test="#attribute.type == 'Date'">
				<s:set var="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
				<s:set var="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
				<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
				<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Number'">
				<s:set var="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
				<s:set var="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
				<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
				<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'"> 
				<s:set var="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
				<wpsf:hidden name="%{#booleanInputFieldName}" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
			</s:elseif>
		</s:iterator>
	</p>
	
	<s:set var="entityIds" value="searchResult" />
	<%-- <s:if test="#entityIds.isEmpty()">no risultati </s:if> --%>
	<wpsa:subset source="#entityIds" count="15" objectName="entityGroup" advanced="true" offset="5">
		<s:set var="group" value="#entityGroup" />
		
		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>   
		
		<br/><br/>
		
		<s:iterator var="entityId">
			<s:property value="#entityId" />
			<br />
		</s:iterator>
		
		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
		
	</wpsa:subset>
	
</s:form>
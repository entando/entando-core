<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a>
		&#32;/&#32;
		<s:text name="title.userProfileDetails" />
	</span>
</h1>
<div id="main" role="main">

	<s:set name="lang" value="defaultLang" />
	<s:set var="userProfileVar" value="%{getUserProfile(username)}" />
	<s:if test="%{null != #userProfileVar}">
	<table class="table table-bordered">
		<tr>
			<th class="text-right"><s:text name="label.username" /></th>
			<td><code><s:property value="username" /></code></td>
		</tr>

				<s:set name="lang" value="defaultLang" />
				<s:iterator value="#userProfileVar.attributeList" var="attribute">
				<tr>
				<%-- INIZIALIZZAZIONE TRACCIATORE --%>
				<s:set name="attributeTracer" value="initAttributeTracer(#attribute, #lang)" />
					<s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
					<s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>
					
					<%-- VISUALIZZAZIONE CONTENUTO ATTRIBUTI  --%>
					<th class="text-right"><s:property value="#attributeLabelVar" /></th>
					<td>
						<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
						<s:if test="#attribute.type == 'Monotext'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monotextAttribute.jsp" />
						</s:if>
					
						<%-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# --%>
						<s:elseif test="#attribute.type == 'Text'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/textAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO TESTOLUNGO ############# --%>
						<s:elseif test="#attribute.type == 'Longtext'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/longtextAttribute.jsp" />	
						</s:elseif>
					
						<%-- ############# ATTRIBUTO HYPERTEXT ############# --%>
						<s:elseif test="#attribute.type == 'Hypertext'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/hypertextAttribute.jsp" />	
						</s:elseif>
					
						<%-- ############# ATTRIBUTO Boolean ############# --%>
						<s:elseif test="#attribute.type == 'Boolean'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/booleanAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO ThreeState ############# --%>
						<s:elseif test="#attribute.type == 'ThreeState'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/threeStateAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO Number ############# --%>
						<s:elseif test="#attribute.type == 'Number'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/numberAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO Date ############# --%>
						<s:elseif test="#attribute.type == 'Date'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/dateAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
						<s:elseif test="#attribute.type == 'Enumerator'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/enumeratorAttribute.jsp" />
						</s:elseif>
				
						<%-- ############# ATTRIBUTO Monolist ############# --%>
						<s:elseif test="#attribute.type == 'Monolist'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monolistAttribute.jsp" />
						</s:elseif>
					
						<%-- ############# ATTRIBUTO List ############# --%>
						<s:elseif test="#attribute.type == 'List'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
						</s:elseif>
				
						<%-- ############# ATTRIBUTO Composite ############# --%>
						<s:elseif test="#attribute.type == 'Composite'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
						</s:elseif>
						
						<%-- ############# ATTRIBUTO CheckBox ############# --%>
						<s:elseif test="#attribute.type == 'CheckBox'">
							<s:include value="/WEB-INF/apsadmin/jsp/entity/view/checkBoxAttribute.jsp" />
						</s:elseif>
					</td>
				</s:iterator>
			</table>
		</div>
	</s:if>
	
	<p>
		<a href="<s:url namespace="/do/User" action="list" />" ><s:text name="note.userprofile.returnTo.search" /></a> 
	</p>
</div>
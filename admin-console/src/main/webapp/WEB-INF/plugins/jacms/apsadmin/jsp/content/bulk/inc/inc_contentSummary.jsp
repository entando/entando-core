<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="summary" value="summary" />
<div class="alert alert-info">
	<s:text name="note.summary.info" />
	<ul class="margin-base-top">
		<li>
			<s:text name="note.summary.aligned" />:&#32;<s:property value="#summary.aligned.size()"/>
		<s:if test="%{contentIds.size() < 10}" >
			<ul>
			<s:iterator var="contentId" value="#summary.aligned" >
				<jacmswpsa:content contentId="${contentId}" var="content" workVersion="true" />
				<li><s:property value="%{#content.description}" /></li>
			</s:iterator>
			</ul>
		</s:if>
		</li>
		<li>
			<s:text name="note.summary.workAhead" />:&#32;<s:property value="#summary.workAhead.size()"/>
		<s:if test="%{contentIds.size() < 10}" >
			<ul>
			<s:iterator var="contentId" value="#summary.workAhead" >
				<jacmswpsa:content contentId="${contentId}" var="content" workVersion="true" />
				<li><s:property value="%{#content.description}" /></li>
			</s:iterator>
			</ul>
		</s:if>
		</li>
		<li>
			<s:text name="note.summary.notOnline" />:&#32;<s:property value="#summary.notOnline.size()"/>
		<s:if test="%{contentIds.size() < 10}" >
			<ul>
			<s:iterator var="contentId" value="#summary.notOnline" >
				<jacmswpsa:content contentId="${contentId}" var="content" workVersion="true" />
				<li><s:property value="%{#content.description}" /></li>
			</s:iterator>
			</ul>
		</s:if>
		</li>
	</ul>
</div>
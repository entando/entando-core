<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:form>
<p class="sr-only">
	<wpsf:hidden name="selectedNode" />
</p>

<s:set var="publishedContents" value="getPublishedContents(selectedNode)" />

<s:if test="!#publishedContents.empty">

<wpsa:subset source="#publishedContents" count="10" objectName="publishedContentsGroup" advanced="true" offset="5" pagerId="publishedContents">
<s:set var="group" value="#publishedContentsGroup" />
<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<div class="table-responsive">
                <table class="table table-striped table-bordered" id="contentListTable" summary="<s:text name="note.content.publishedContent.summary" />">
                    <p><s:text name="title.publishedContent" /></p>
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/contentReferencesTable_header.jsp" />
		<s:iterator var="currentContentVar" value="#publishedContents">
		<jacmswpsa:content contentId="%{#currentContentVar.id}" var="currentContentRecordVar" record="true" />
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/contentReferencesTable_row.jsp" />
		</s:iterator>
	</table>
</div>

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>
</wpsa:subset>

</s:if>
<s:else>
        <div class="alert alert-info">
            <span class="pficon pficon-info"></span>
            <strong><s:text name="note.publishedContent.empty" /></strong>
        </div>
</s:else>

<hr />

<s:set var="referencingContentsId" value="getReferencingContentsId(selectedNode)" />
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/portal/include/referencingContents.jsp" />
</s:form>

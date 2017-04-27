<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:if test="!#referencingContentsId.empty">
	<wpsa:subset source="#referencingContentsId" count="10" objectName="contentReferencesGroup" advanced="true" offset="5" pagerId="referencingContentsId">
		<s:set var="group" value="#contentReferencesGroup" />

		<div class="table-responsive">
			<table class="table table-striped table-bordered table-hover no-mb" id="contentListTable" summary="<s:text name="note.resources.referencingContent.summary" />">
			<caption class="text-strong margin-base-vertical"><s:text name="title.referencedContents" /></caption>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/contentReferencesTable_header.jsp" />

				<s:iterator var="currentContentIdVar" value="#referencingContentsId">
					<jacmswpsa:content contentId="%{currentContentIdVar}" record="true" var="currentContentRecordVar" />
					<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/contentReferencesTable_row.jsp" />
				</s:iterator>
			</table>
		</div>

		<div class="content-view-pf-pagination table-view-pf-pagination clearfix">
			<div class="form-group">
				<span>
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
				</span>
			</div>
		</div>
		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
	</wpsa:subset>
</s:if>
<s:else>
    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <strong><s:text name="note.referencedContent.empty" /></strong>
    </div>
</s:else>

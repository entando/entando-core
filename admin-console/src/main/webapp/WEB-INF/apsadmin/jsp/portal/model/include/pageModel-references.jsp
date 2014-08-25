<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:form cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="code" />
	</p>
	<%-- referenced pages --%>
	<div class="panel panel-default">
		<div class="panel-heading"><h3 class="margin-none"><s:text name="title.pageModel.referencedPages" /></h3></div>
		<div class="panel-body">
			<s:if test="null != references['PageManagerUtilizers']">
				<wpsa:subset source="references['PageManagerUtilizers']" count="10" objectName="pageReferences" advanced="true" offset="5" pagerId="pageManagerReferences">
					<s:set name="group" value="#pageReferences" />
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
						<table class="table table-bordered" id="pageListTable">
							<tr>
								<!-- <th class="text-center text-nowrap col-xs-6 col-sm-3 col-md-3 col-lg-3"> -->
								<th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2">
									<abbr title="<s:text name="label.actions" />">&ndash;</abbr>
								</th>
								<th><s:text name="label.page" /></th>
							</tr>
							<s:iterator var="currentPageVar">
								<s:set var="canEditCurrentPage" value="%{false}" />
								<s:set var="currentPageGroup" value="#currentPageVar.group" scope="page" />
								<wp:ifauthorized groupName="${currentPageGroup}" permission="managePages"><s:set var="canEditCurrentPage" value="%{true}" /></wp:ifauthorized>
								<tr>
									<td class="text-center text-nowrap"><s:if test="#canEditCurrentPage"><div class="btn-group btn-group-xs"><a
													class="btn btn-default" 
													href="<s:url namespace="/do/Page" action="viewTree"><s:param name="selectedNode" value="#currentPageVar.code" /></s:url>"
													title="<s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"><span class="icon fa fa-folder"></span><span class="sr-only"><s:text name="note.goToSomewhere" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span></a><a
													class="btn btn-default" 
													href="<s:url namespace="/do/Page" action="configure"><s:param name="pageCode" value="#currentPageVar.code" /></s:url>"
													title="<s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />"><span class="icon fa fa-cog"></span><span class="sr-only"><s:text name="title.configPage" />:&#32;<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" /></span></a></div></s:if></td>
									<td>
										<s:property value="%{#currentPageVar.getFullTitle(currentLang.code)}" />
									</td>
								</tr>
							</s:iterator>
						</table>
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				</wpsa:subset>
			</s:if>
			<s:else>
				<p class="margin-none"><s:text name="note.pageModel.referencedPages.empty" /></p>
			</s:else>
		</div>
	</div>
	<%-- hoookpoint core.groupReferences --%>
	<wpsa:hookPoint key="core.pageModelReferences" objectName="hookPointElements_core_pageModelReferences">
		<s:iterator value="#hookPointElements_core_pageModelReferences" var="hookPointElement">
			<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
		</s:iterator>
	</wpsa:hookPoint>
</s:form>

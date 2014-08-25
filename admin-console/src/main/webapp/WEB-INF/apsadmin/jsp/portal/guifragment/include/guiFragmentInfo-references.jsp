<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:form cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="name" />
	</p>
	<%-- referenced Fragments --%>
	<div class="panel panel-default">
		<div class="panel-heading"><h3 class="margin-none"><s:text name="title.guiFragment.referencedFragments" /></h3></div>
		<div class="panel-body">
			<s:if test="null != references['GuiFragmentManagerUtilizers']">
				<wpsa:subset source="references['GuiFragmentManagerUtilizers']" count="10" objectName="guiFragmentReferencesVar" advanced="true" offset="5" pagerId="pageManagerReferences">
					<s:set name="group" value="#guiFragmentReferencesVar" />
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
						<table class="table table-bordered" id="guiFragListTable">
							<tr>
								<!-- <th class="text-center text-nowrap col-xs-6 col-sm-3 col-md-3 col-lg-3"> -->
								<th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2">
									<abbr title="<s:text name="label.actions" />">&ndash;</abbr>
								</th>
								<th><s:text name="label.guiFragment" /></th>
							</tr>
							<s:iterator var="currentGuiFragmentVar">
								<tr>
									<td class="text-center text-nowrap">
										<a class="btn btn-default" 
											href="<s:url namespace="/do/Portal/GuiFragment" action="edit"><s:param name="code" value="#currentGuiFragmentVar.code" /></s:url>"
											title="<s:text name="label.edit" />:&#32;<s:property value="%{#currentGuiFragmentVar.code}" />"><span class="icon fa fa-pencil-square-o"></span><span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="%{#currentGuiFragmentVar.code}" /></span>
										</a>
									</td>
									<td><s:property value="#currentGuiFragmentVar.code" /></td>
								</tr>
							</s:iterator>
						</table>
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				</wpsa:subset>
			</s:if>
			<s:else>
				<p class="margin-none"><s:text name="note.guiFragment.referencedFragments.empty" /></p>
			</s:else>
		</div>
	</div>
	<%-- referenced PageModels --%>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="margin-none"><s:text name="title.guiFragment.referencedPageModels" /></h3>
		</div>
			<div class="panel-body">
				<s:if test="null != references['PageModelManagerUtilizers']">
					<wpsa:subset source="references['PageModelManagerUtilizers']" count="10" objectName="pageModelReferencesVar" advanced="true" offset="5" pagerId="userManagerReferences">
						<s:set name="group" value="#pageModelReferencesVar" />
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
						<div class="table-responsive">
							<table class="table table-bordered" id="userListTable">
								<tr>
									<th class="text-center col-xs-5 col-sm-3 col-md-2 col-lg-2">
										<abbr title="<s:text name="label.actions" />">&ndash;</abbr>
									</th>
									<th><s:text name="label.pageModel.key" /></th>
									<th><s:text name="label.pageModel.descr" /></th>
								</tr>
								<s:iterator var="pageModelVar">
									<tr class="text-center text-nowrap">
										<%-- actions --%>
										<td class="text-center text-nowrap">
											<a class="btn btn-default" 
												href="<s:url namespace="/do/PageModel" action="edit"><s:param name="code" value="#pageModelVar.code" /></s:url>"
												title="<s:text name="label.edit" />:&#32;<s:property value="%{#pageModelVar.code}" />"><span class="icon fa fa-pencil-square-o"></span><span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="%{#pageModelVar.code}" /></span>
											</a>
										</td>
										<td><s:property value="#pageModelVar.code" /></td>
										<td><s:property value="#pageModelVar.description" /></td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="text-center">
							<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
						</div>
					</wpsa:subset>
				</s:if>
				<s:else>
					<p class="margin-none"><s:text name="note.guiFragment.referencedPageModels.empty" /></p>
				</s:else>
			</div>
	</div>
	<%-- referenced widgets --%>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="margin-none"><s:text name="title.guiFragment.referencedWidgetTypes" /></h3>
		</div>
		<div class="panel-body">
			<s:if test="null != references['WidgetTypeManagerUtilizers']">
				<wpsa:subset source="references['WidgetTypeManagerUtilizers']" count="10" objectName="widgetTypeReferencesVar" advanced="true" offset="5" pagerId="widgetTypeReferences">
					<s:set name="group" value="#widgetTypeReferencesVar" />
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					<table class="table table-bordered" id="widgetTypeListTable">
						<tr>
							<th><s:text name="label.title" /></th>
							<th><s:text name="label.code" /></th>
						</tr>
						<s:iterator var="currentWidgetVar">
							<tr>
								<td><s:property value="%{getTitle(#currentWidgetVar.code, #currentWidgetVar.titles)}" /></td>
								<td><code><s:property value="#currentWidgetVar.code" /></code></td>
							</tr>
						</s:iterator>
					</table>
					<div class="text-center">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				</wpsa:subset>
			</s:if>
			<s:else>
				<p class="margin-none"><s:text name="note.guiFragment.referencedWidgetTypes.empty" /></p>
			</s:else>
		</div>
	</div>
</s:form>

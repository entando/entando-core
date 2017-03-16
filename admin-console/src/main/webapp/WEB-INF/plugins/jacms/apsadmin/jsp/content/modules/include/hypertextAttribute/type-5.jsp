<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h4><s:text name="note.resourceLinkTo" /></h4>

	<s:form action="entandoResourceSearch" cssClass="form-horizontal" role="search">
		<p class="sr-only"><s:text name="note.chooseResourceToLink" />.</p>
		<p class="sr-only">
			<wpsf:hidden name="activeTab" value="3" />
			<wpsf:hidden name="internalResourceActionName" value="entandoResourceSearch" />
			<wpsf:hidden name="contentOnSessionMarker" />
			<wpsf:hidden name="linkTypeVar" value="5" />
		</p>
		<div class="form-group">
			<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<span class="input-group-addon" title="<s:text name="label.search.by" />&#32;<s:text name="label.description" />">
					<span class="icon fa fa-file-text-o fa-lg"></span>
				</span>
				<label class="sr-only" for="text"><s:text name="label.search.by" />&#32;<s:text name="label.description" /></label>
				<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')+' '+getText('label.description')}" />
				<span class="input-group-btn">
					<wpsf:submit type="button" title="%{getText('label.search')}" cssClass="btn btn-primary btn-lg">
						<span class="icon fa fa-search"></span>
						<span class="sr-only"><s:text name="label.search" /></span>
					</wpsf:submit>
					<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-resource-advanced" title="<s:text name="title.searchFilters" />">
						<span class="sr-only"><s:text name="title.searchFilters" /></span>
						<span class="caret"></span>
					</button>
				</span>
			</div>
			<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div id="search-resource-advanced" class="collapse well collapse-input-group">
					<%-- type --%>
					<div class="form-group">
						<label class="control-label col-sm-2 text-right" for="resourceTypeCode"><s:text name="label.type"/></label>
						<div class="col-sm-5 input-group">
							<wpsf:select name="resourceTypeCode" id="resourceTypeCode"
								list="resourceTypeCodes" headerKey="" 
								headerValue="%{getText('label.all')}" cssClass="form-control" />
						</div>
					</div>
					<%-- search --%>
					<div class="form-group">
						<div class="col-sm-5 col-sm-offset-2">
							<wpsf:submit type="button" cssClass="btn btn-primary">
								<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
							</wpsf:submit>
						</div>
					</div>
				</div>
			</div>
		</div>
	</s:form>
	<s:form action="entandoResourceSearch" >
		<div class="col-xs-12">
			<p class="sr-only">
				<wpsf:hidden name="text" />
				<wpsf:hidden name="resourceTypeCode" />
				<wpsf:hidden name="activeTab" value="3" />
				<wpsf:hidden name="internalResourceActionName" value="entandoResourceSearch" />
				<wpsf:hidden name="contentOnSessionMarker" />
				<wpsf:hidden name="linkTypeVar" value="5" />
			</p>
			<wpsa:subset source="resources" count="10" objectName="groupContent" advanced="true" offset="5">
				<s:set var="group" value="#groupContent" />
				<div class="text-center">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
				<s:if test="%{getResources().size() > 0}">
					<table class="table table-bordered" id="resourceListTable">
						<caption class="sr-only"><s:text name="title.resourceList" /></caption>
						<tr>
							<%-- description --%>
							<th><s:text name="label.description" /></th>
							<%-- key --%>
							<th class="text-left"><s:text name="label.code" /></th>
							<%-- type code --%>
							<th class="text-center"><s:text name="label.typeCode" /></th>
						</tr>
						<s:iterator var="resourceIdVar">
							<s:set var="resourceVar" value="%{loadResource(#resourceIdVar)}" />
							<tr>
								<%-- description --%>
								<td>
									<label for="resourceId_<s:property value="#resourceIdVar"/>">
										<input type="radio" name="resourceId" id="resourceId_<s:property value="#resourceIdVar"/>" value="<s:property value="#resourceIdVar"/>" />&nbsp;
										<s:property value="#resourceVar.descr" />
									</label>
								</td>
								<%-- key --%>
								<td class="text-left">
									<code><s:property value="#resourceIdVar"/></code>
								</td>
								<%-- type code --%>
								<td class="text-center">
									<code><s:property value="#resourceVar.type" /></code>
								</td>
							</tr>
						</s:iterator>
					</table>
				</s:if>
				<div class="text-center">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
			</wpsa:subset>
		</div>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<button type="submit" id="button_resourceLink" name="button_resourceLink" class="btn btn-primary btn-block">
					<s:text name="label.confirm" />
				</button>
			</div>
		</div>
	</s:form>
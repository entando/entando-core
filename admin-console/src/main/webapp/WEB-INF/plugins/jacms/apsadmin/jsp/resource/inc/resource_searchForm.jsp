<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<s:form action="search" cssClass="form-horizontal" role="search">
	<p class="sr-only">
		<wpsf:hidden name="resourceTypeCode" />
		<s:if test="#categoryTreeStyleVar == 'request'">
			<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
		</s:if>
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>
	<div class="searchPanel form-group">

		<div class="well col-md-offset-3 col-md-6">
			<p class="search-label col-sm-12"><s:text name="label.search.label"/></p>
			<div class="form-group">
				<s:form action="search" cssClass="search-pf has-button">
					<div class="col-sm-12 has-clear">
						<wpsf:textfield name="text" id="text" cssClass="form-control input-lg"
										title="%{getText('label.search.by')+' '+getText('label.description')}" placeholder="%{getText('label.search.label')}"/>
					</div>
				</s:form>
			</div>
			<div class="panel-group" id="accordion-markup" >
				<div class="panel panel-default">
					<div class="panel-heading" style="padding:0 0 10px;">
						<p class="panel-title active" style="text-align: end">
							<a data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">
								<s:text name="label.search.advanced" />
							</a>
						</p>
					</div>
					<div id="collapseOne" class="panel-collapse collapse">
						<div class="panel-body">
								<%-- groups --%>
							<s:set var="allowedGroupsVar" value="allowedGroups"></s:set>
							<s:if test="null != #allowedGroupsVar && #allowedGroupsVar.size()>1">
								<div class="form-group">
									<label for="ownerGroupName" class="control-label col-sm-2" ><s:text name="label.group" /></label>
									<div class="col-sm-9" >
										<wpsf:select name="ownerGroupName" id="ownerGroupName" list="#allowedGroupsVar" headerKey="" headerValue="%{getText('label.all')}" listKey="name" listValue="descr" cssClass="form-control" />
									</div>
								</div>
							</s:if>

								<%-- filename --%>
							<div class="form-group">
								<label for="fileName" class="control-label col-sm-2"><s:text name="label.filename" /></label>
								<div class="col-sm-9">
									<wpsf:textfield name="fileName" id="fileName" cssClass="form-control"/>
								</div>
							</div>

								<%-- category tree --%>
							<div class="form-group">
								<label class="control-label col-sm-2">
									<s:text name="label.category" />
								</label>
								<div class="col-sm-9">
									<div class="well">
										<ul id="categoryTree" class="fa-ul list-unstyled">
											<s:set var="inputFieldName" value="'categoryCode'" />
											<s:set var="selectedTreeNode" value="categoryCode" />
											<s:set var="liClassName" value="'category'" />
											<s:set var="treeItemIconName" value="'fa-folder'" />

											<s:if test="#categoryTreeStyleVar == 'request'">
												<s:set var="currentRoot" value="showableTree" />
												<s:set var="openTreeActionName" value="'openCloseCategoryTreeNodeOnResourceFinding'" />
												<s:set var="closeTreeActionName" value="'openCloseCategoryTreeNodeOnResourceFinding'" />
												<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
											</s:if>
											<s:else>
												<s:set var="currentRoot" value="categoryRoot" />
												<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
											</s:else>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
            <div class="col-sm-12">
                <div class="form-group">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.search" />
                    </wpsf:submit>
                </div>
            </div>
		</div>
	</div>
</s:form>
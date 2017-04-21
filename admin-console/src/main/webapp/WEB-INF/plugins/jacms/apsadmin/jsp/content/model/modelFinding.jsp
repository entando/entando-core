<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li class="page-title-container"><s:text
			name="title.contentModels" /></li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="title.contentModels" />
		<span class="pull-right"> <a tabindex="0" role="button"
			data-toggle="popover" data-trigger="focus" data-html="true" title=""
			data-content="TO be inserted" data-placement="left"
			data-original-title=""> <i class="fa fa-question-circle-o"
				aria-hidden="true"></i>
		</a>
		</span>
	</div>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<div id="main" role="main">
	<div class="col-xs-12">
		<div class="well col-md-offset-3 col-md-6">
			<p class="search-label">
				<s:text name="label.search.by" />
				&#32;
				<s:text name="label.type" />
			</p>
			<s:form action="search" cssClass="form-horizontal" role="search">
				<div class="form-group">
					<div class="col-sm-12">
						<wpsf:select name="contentType" id="contentType"
							cssClass="form-control input-lg" list="smallContentTypes"
							listKey="code" listValue="descr" headerKey=""
							headerValue="%{getText('label.all')}" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="pull-right">
							<wpsf:submit type="button" cssClass="btn btn-primary">
								<s:text name="label.search" />
							</wpsf:submit>
						</div>
					</div>
				</div>
			</s:form>
		</div>
	</div>

	<a href="<s:url action="new" namespace="/do/jacms/ContentModel"/>"
		class="btn btn-primary pull-right" style="margin-bottom: 5px">
		<s:text name="contentModels.label.add" />
	</a>

	<s:form action="search">

		<p class="sr-only">
			<wpsf:hidden name="contentType" />
		</p>

		<s:if test="%{contentModels.size > 0}">

			<wpsa:subset source="contentModels" count="10"
				objectName="groupContentModel" advanced="true" offset="5">
				<s:set var="group" value="#groupContentModel" />

				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center"><s:text
									name="contentModel.id" /></th>
							<th class="text-center"><s:text name="label.description" /></th>
							<th class="text-center"><s:text name="contentModel.type" /></th>
							<th class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center"><s:text
									name="label.actions" /></th>
						</tr>
					</thead>
					<tbody>
						<s:iterator var="model">
							<tr>
								<td class="text-center text-nowrap"><code>
										<s:property value="#model.id" />
									</code></td>
								<td><s:property value="#model.description" /></td>
								<td class="text-center"><s:property
										value="%{getSmallContentType(#model.contentType).descr}" /></td>
								<td class="table-view-pf-actions">
									<div class="dropdown dropdown-kebab-pf">
										<button class="btn btn-menu-right dropdown-toggle"
											type="button" data-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false">
											<span class="fa fa-ellipsis-v"></span>
										</button>
										<ul class="dropdown-menu dropdown-menu-right">
											<li><a
												href="<s:url action="edit" namespace="/do/jacms/ContentModel" />?modelId=<s:property value="#model.id" />"
												title="<s:text name="label.edit" />: <s:property value="#model.description" />">
													<span><s:text name="label.edit" /></span>
											</a></li>
											<li><a
												href="<s:url action="trash" namespace="/do/jacms/ContentModel" />?modelId=<s:property value="#model.id" />"
												title="<s:text name="label.delete" />: <s:property value="#model.description" />"><span><s:text
															name="label.delete" /></span></a></li>
										</ul>
									</div>
								</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
				<div class="text-center">
					<s:include
						value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
			</wpsa:subset>
		</s:if>
		<s:else>
			<p>
				<s:text name="contentModel.noModels" />
			</p>
		</s:else>
	</s:form>

</div>
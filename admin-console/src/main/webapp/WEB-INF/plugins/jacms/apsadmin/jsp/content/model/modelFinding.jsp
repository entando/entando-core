<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="title.contentModels" /></span></h1>

<div id="main" role="main">

<s:form action="search" cssClass="form-horizontal" role="search">
	<div class="form-group">
		<label for="contentType" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.type"/></label>
		<div class="input-group col-sm-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg"></span>
			</span>
			<wpsf:select name="contentType" id="contentType" cssClass="form-control input-lg" list="smallContentTypes" listKey="code" listValue="descr" headerKey="" headerValue="%{getText('label.all')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-lg">
					<span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
				</wpsf:submit>
			</div>
		</div>
	</div>
</s:form>

<a href="<s:url action="new" namespace="/do/jacms/ContentModel"/>" class="btn btn-default">
	<span class="icon fa fa-plus-circle"></span>&#32;
	<s:text name="title.contentModels.new" />
</a>

<s:form action="search">

<p class="sr-only"><wpsf:hidden name="contentType" /></p>

	<s:if test="%{contentModels.size > 0}" >
	
		<wpsa:subset source="contentModels" count="10" objectName="groupContentModel" advanced="true" offset="5">
		<s:set name="group" value="#groupContentModel" />
		
		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
			
		<div class="table-responsive">
		<table class="table table-bordered">
			
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th class="text-right"><s:text name="contentModel.id" /></th>
				<th><s:text name="label.description" /></th>
				<th><s:text name="contentModel.type" /></th>
			</tr>
			
			<s:iterator id="model">
				<tr>
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<%-- edit --%>
							<a class="btn btn-default" href="<s:url action="edit" namespace="/do/jacms/ContentModel" />?modelId=<s:property value="#model.id" />" title="<s:text name="label.edit" />: <s:property value="#model.description" />">
								<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#model.description" /></span>
								<span class="icon fa fa-pencil-square-o"></span>
							</a>
						</div>
						<%-- remove --%>
						<div class="btn-group btn-group-xs">
							<a href="<s:url action="trash" namespace="/do/jacms/ContentModel" />?modelId=<s:property value="#model.id" />" title="<s:text name="label.remove" />: <s:property value="#model.description" />" class="btn btn-warning">
							<span class="icon fa fa-times-circle-o"></span>&#32;
							<span class="sr-only"><s:text name="label.alt.clear" /></span>
							</a>
						</div>
					</td>
					<td class="text-right text-nowrap">
						<code><s:property value="#model.id" /></code>
					</td>
					<td>
						<s:property value="#model.description" />
					</td>  
					<td>
						<s:property value="%{getSmallContentType(#model.contentType).descr}" />
					</td>

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
		<p><s:text name="contentModel.noModels" /></p> 
	</s:else>
</s:form>

</div>
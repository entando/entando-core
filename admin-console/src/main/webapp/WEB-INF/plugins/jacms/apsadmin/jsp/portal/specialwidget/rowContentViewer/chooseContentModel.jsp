<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="<s:url action="configure" namespace="/do/Page">
					<s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
				</s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget" />
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

<s:form action="executeJoinContent" namespace="/do/jacms/Page/SpecialWidget/RowListViewer" cssClass="form-horizontal">

<div class="panel panel-default">
	<div class="panel-heading">
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
	</div>
	<div class="panel-body">
		<h2 class="h5 margin-small-vertical">
			<label class="sr-only"><s:text name="name.widget" /></label>
			<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
			<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
		</h2>
		<p class="sr-only">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" />
			
			
			
			<wpsf:hidden name="contents" value="%{#parameters['contents']}"  />
			<wpsf:hidden name="maxElemForItem" value="%{#parameters['maxElemForItem']}"  />
			<wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
			<wp:info key="langs" var="langsVar" />
			<c:forEach var="iteratorLang" items="${langsVar}" varStatus="status">
			<s:set var="langCodeVar" ><c:out value="${iteratorLang.code}" /></s:set>
			<wpsf:hidden name="%{'linkDescr_' + #langCodeVar}" value="%{#parameters['linkDescr_' + #langCodeVar]}" />
			<wpsf:hidden name="%{'title_' + #langCodeVar}" value="%{#parameters['title_' + #langCodeVar]}" />
			</c:forEach>
			<wpsf:hidden name="contentId" />
			
			
			
		</p>
		<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
			<ul class="margin-base-vertical">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
		</s:if>
		
		<s:set var="contentVoVar" value="%{getContentVo(contentId)}"></s:set>
		
		<fieldset class="margin-large-top"><legend><s:text name="label.info" /></legend>
			<div class="form-group">
				<div class="col-xs-12">
					<label class="control-label"><s:text name="label.content" /></label>
					<p class="form-control-static">
						<code><s:property value="#contentVoVar.id" /></code>&#32;&mdash;&#32;
						<s:property value="#contentVoVar.descr" />
					</p>
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit action="searchContents" value="%{getText('label.change')}" cssClass="btn btn-info btn-block" />
				</div>
			</div>
		</fieldset>

		<fieldset class="margin-large-top">
			<div class="form-group">
				<div class="col-xs-12">
					<label for="modelId" class="control-label"><s:text name="label.contentModel" /></label>
					<wpsf:select id="modelId" name="modelId" list="%{getModelsForContent(contentId)}" 
								 headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="form-control" />
				</div>
			</div>
		</fieldset>
		
	</div>
</div>
<div class="form-group">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit type="button" action="executeJoinContent" cssClass="btn btn-primary btn-block">
			<span class="icon fa fa-floppy-o"></span>&#32;
			<s:text name="label.save" />
		</wpsf:submit>
	</div>
</div>
</s:form>
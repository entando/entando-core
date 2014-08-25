<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div id="divContentLink" class="tab">
<s:form action="configInternalLink" cssClass="searchForm" role="search">
<p><s:text name="note.chooseContentToLink" />.</p>

<p class="sr-only">
	<wpsf:hidden name="lastGroupBy" />
	<wpsf:hidden name="lastOrder" />
	<wpsf:hidden name="activeTab" value="2" />
	<wpsf:hidden name="internalActionName" value="search" />
	<wpsf:hidden name="contentOnSessionMarker" />
</p>

	<div class="form-group">
		<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<span class="input-group-addon" title="<s:text name="label.search.by" />&#32;<s:text name="label.description" />">
				<span class="icon fa fa-file-text-o fa-lg"></span>
			</span>
			<label class="sr-only" for="text"><s:text name="label.search.by" />&#32;<s:text name="label.description" /></label>
			<input type="text" class="form-control input-lg" placeholder="" title="" />
			<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')+' '+getText('label.description')}" />
			<span class="input-group-btn">
				<wpsf:submit type="button" title="%{getText('label.search')}" cssClass="btn btn-primary btn-lg">
					<span class="icon fa fa-search"></span>
					<span class="sr-only"><s:text name="label.search" /></span>
				</wpsf:submit>
				<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
				</button>
			</span>
		</div>
		<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div id="search-advanced" class="collapse well collapse-input-group">
				<div class="form-group">
					<label class="control-label col-sm-2 text-right"><s:text name="label.code"/></label>
					<div class="col-sm-5 input-group">
						<wpsf:textfield name="contentIdToken" cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right"><s:text name="label.type"/></label>
					<div class="col-sm-5 input-group">
					 	<wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
					</div>
				</div>
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

<s:form action="configInternalLink" >

<p class="sr-only">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="contentType" />
	<wpsf:hidden name="state" />
	<wpsf:hidden name="categoryCode" />
	<wpsf:hidden name="viewTypeDescr" />
	<wpsf:hidden name="viewGroup" />
	<wpsf:hidden name="viewCode" />
	<wpsf:hidden name="viewStatus" />
	<wpsf:hidden name="viewCreationDate" />
	<wpsf:hidden name="lastGroupBy" />
	<wpsf:hidden name="lastOrder" />
	<wpsf:hidden name="contentIdToken" />
	<wpsf:hidden name="activeTab" value="2" />
	<wpsf:hidden name="internalActionName" value="search" />
	<wpsf:hidden name="contentOnSessionMarker" />
</p>

<wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
<s:set name="group" value="#groupContent" />

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<s:if test="%{getContents().size() > 0}">
	<table class="generic" id="contentListTable" summary="<s:text name="note.content.fck_entandolink.summary" />">
	<caption><span><s:text name="title.contentList" /></span></caption>
	<tr>
		<th><a href="<s:url action="configInternalLink" includeParams="all" >
			<s:param name="activeTab">2</s:param>
			<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
			<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
			<s:param name="groupBy">descr</s:param>
			<s:param name="internalActionName">changeOrder</s:param>
		</s:url>#divContentLink"><s:text name="label.description" /></a></th>
		
		<th><a href="<s:url action="configInternalLink" includeParams="all" >
			<s:param name="activeTab">2</s:param>
			<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
			<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
			<s:param name="groupBy">code</s:param>
			<s:param name="internalActionName">changeOrder</s:param>
		</s:url>#divContentLink"><s:text name="label.code" /></a></th>
		
		<th><s:text name="label.creationDate" /></th>
		<th><s:text name="label.lastEdit" /></th>
	</tr>
	<s:iterator id="contentId">
	<s:set name="content" value="%{getContentVo(#contentId)}"></s:set>
	<tr>
	<td><input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" />
	<label for="contentId_<s:property value="#content.id"/>"><s:property value="#content.descr" /></label></td>
	<td><s:property value="#content.id"/></td><%-- AGGIUNTO DA MATTEO--%>
	<td class="centerText"><span class="monospace"><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></span></td>
	<td class="icon"><span class="monospace"><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></span></td>
	</tr>
	</s:iterator>
	</table>
</s:if>		

<div class="text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>
<p><input id="button_contentLink" name="button_contentLink" type="submit" value="<s:text name="label.confirm" />" class="button" /></p>

</s:form>
</div>
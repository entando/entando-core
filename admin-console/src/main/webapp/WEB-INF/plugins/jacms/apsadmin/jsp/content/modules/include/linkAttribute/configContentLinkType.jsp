<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="thirdTitleVar">
	<s:text name="title.configureLinkAttribute" />
</s:set>
<s:include value="linkAttributeConfigIntro.jsp" />
<s:include value="linkAttributeConfigReminder.jsp"/>
<p class="sr-only"><s:text name="note.chooseContentToLink" /></p>
<s:form action="search" cssClass="form-horizontal">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h4>
			<%--
			<ul class="margin-none margin-base-top">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
									<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
			--%>
		</div>
	</s:if>
	<div class="form-group" role="search">
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
				<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
				</button>
			</span>
		</div>
		<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div id="search-advanced" class="collapse well collapse-input-group">
				<%-- code --%>
					<div class="form-group">
						<label class="control-label col-sm-2 text-right" for="contentIdToken"><s:text name="label.code"/></label>
						<div class="col-sm-5 input-group">
							<wpsf:textfield name="contentIdToken" id="contentIdToken" cssClass="form-control" />
						</div>
					</div>
				<%-- type --%>
					<div class="form-group">
						<label class="control-label col-sm-2 text-right" for="contentType"><s:text name="label.type"/></label>
						<div class="col-sm-5 input-group">
							<wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
						</div>
					</div>
				<%-- status --%>
					<div class="form-group">
						<label class="control-label col-sm-2 text-right" for="state"><s:text name="label.state"/></label>
						<div class="col-sm-5 input-group">
							<wpsf:select name="state" id="state" list="avalaibleStatus" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" listKey="key" listValue="%{getText(value)}" />
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
	<hr />
	<wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
		<p class="sr-only">
			<wpsf:hidden name="lastGroupBy" />
			<wpsf:hidden name="lastOrder" />
			<wpsf:hidden name="contentOnSessionMarker" />
		</p>
		<s:set var="group" value="#groupContent" />
		<div class="text-center">
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
			<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
		</div>
		<s:if test="%{getContents().size() > 0}">
			<div class="table-responsive">
				<table class="table table-bordered">
					<caption class="sr-only"><s:text name="title.contentList" /></caption>
					<tr>
						<th><a href="
						<s:url action="changeOrder">
							<s:param name="text">
								<s:property value="#request.text"/>
							</s:param>
							<s:param name="contentIdToken">
								<s:property value="#request.contentIdToken"/>
							</s:param>
							<s:param name="contentType">
								<s:property value="#request.contentType"/>
							</s:param>
							<s:param name="state">
								<s:property value="#request.state"/>
							</s:param>
							<s:param name="pagerItem">
								<s:property value="#groupContent.currItem"/>
							</s:param>
							<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
							<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
							<s:param name="groupBy">descr</s:param>
							<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
						</s:url>
					"><s:text name="label.description" /></a></th>
						<th class="text-center text-nowrap"><a href="
						<s:url action="changeOrder">
							<s:param name="text">
								<s:property value="#request.text"/>
							</s:param>
							<s:param name="contentIdToken">
								<s:property value="#request.contentIdToken"/>
							</s:param>
							<s:param name="contentType">
								<s:property value="#request.contentType"/>
							</s:param>
							<s:param name="state">
								<s:property value="#request.state"/>
							</s:param>
							<s:param name="pagerItem">
								<s:property value="#groupContent.currItem"/>
							</s:param>
							<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
							<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
							<s:param name="groupBy">code</s:param>
							<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
						</s:url>
					"><s:text name="label.code" /></a></th>
						<th><s:text name="label.group" /></th>
						<th class="text-center text-nowrap"><a href="
						<s:url action="changeOrder">
							<s:param name="text">
								<s:property value="#request.text"/>
							</s:param>
							<s:param name="contentIdToken">
								<s:property value="#request.contentIdToken"/>
							</s:param>
							<s:param name="contentType">
								<s:property value="#request.contentType"/>
							</s:param>
							<s:param name="state">
								<s:property value="#request.state"/>
							</s:param>
							<s:param name="pagerItem">
								<s:property value="#groupContent.currItem"/>
							</s:param>
							<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
							<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
							<s:param name="groupBy">created</s:param>
							<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
						</s:url>
					"><s:text name="label.creationDate" /></a></th>
						<th class="text-center text-nowrap"><a href="
						<s:url action="changeOrder">
							<s:param name="text">
								<s:property value="#request.text"/>
							</s:param>
							<s:param name="contentIdToken">
								<s:property value="#request.contentIdToken"/>
							</s:param>
							<s:param name="contentType">
								<s:property value="#request.contentType"/>
							</s:param>
							<s:param name="state">
								<s:property value="#request.state"/>
							</s:param>
							<s:param name="pagerItem">
								<s:property value="#groupContent.currItem"/>
							</s:param>
							<s:param name="lastGroupBy"><s:property value="lastGroupBy"/></s:param>
							<s:param name="lastOrder"><s:property value="lastOrder"/></s:param>
							<s:param name="groupBy">lastModified</s:param>
							<s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
						</s:url>
					"><s:text name="label.lastEdit" /></a></th>
					</tr>
					<s:iterator var="contentId">
						<s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
						<tr>
						<td><input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" />
						<label for="contentId_<s:property value="#content.id"/>"><s:property value="#content.descr" /></label></td>
						<td class="text-center text-nowrap"><code><s:property value="#content.id" /></code></td>
						<td>
							<s:property value="%{getGroup(#content.mainGroupCode).descr}" />
						</td>
						<td class="text-center text-nowrap"><code><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></code></td>
						<td class="text-center text-nowrap"><code><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></code></td>
						</tr>
					</s:iterator>
				</table>
			</div>
			<div class="text-center">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</s:if>
		<hr />
	</wpsa:subset>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" action="joinContentLink" cssClass="btn btn-primary btn-block">
				<s:text name="label.confirm" />
			</wpsf:submit>
		</div>
		<div class="form-group">
			<div class="margin-base-vertical">
				<label class="chebox-inline" for="contentOnPageType">
					<wpsf:checkbox useTabindexAutoIncrement="true" name="contentOnPageType" id="contentOnPageType"/>
					&#32;<s:text name="note.makeContentOnPageLink" />
				</label>
			</div>
	</div>
</s:form>
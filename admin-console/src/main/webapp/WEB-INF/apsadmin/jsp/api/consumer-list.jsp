<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.apiConsumerManagement" /></span>
</h1>
<div id="main" role="main">
	<s:if test="hasActionMessages()">
		<div class="alert alert-info alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

	<s:form action="search" cssClass="form-horizontal" role="search">
		<div class="form-group">
			<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<span class="input-group-addon">
					<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.consumer.description"/>"></span>
				</span>
				<label for="search_consumer_description" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.consumer.description"/></label>
				<wpsf:textfield name="text" id="search_consumer_description" cssClass="form-control input-lg" placeholder="%{getText('label.description')}" title="%{getText('label.search.by')} %{getText('label.consumer.description')}" />
				<span class="input-group-btn">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
						<span class="sr-only"><s:text name="label.search" /></span>
						<span class="icon fa fa-search"></span>
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
							<label for="search_consumer_key" class="control-label col-sm-2 text-right">Key</label>
							<div class="col-sm-5">
								<wpsf:textfield name="insertedKey" id="search_consumer_key" cssClass="form-control" />
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-5 col-sm-offset-2">
								<wpsf:submit type="button" cssClass="btn btn-primary">
									<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
								</wpsf:submit>
							</div>
						</div>
					</div><%--// search-advanced --%>
				</div>
			</div>
	</s:form>
	<hr />
	<a href="<s:url action="new" />" class="btn btn-default">
		<span class="icon fa fa-plus-circle"></span>&#32;<s:text name="label.new" />
	</a>
	<s:form action="search">
		<p class="sr-only">
			<wpsf:hidden name="insertedKey" />
			<wpsf:hidden name="insertedDescription" />
		</p>
		<wpsa:subset source="searchResult" count="10" objectName="groupSearchResult" advanced="true" offset="5">
			<s:set name="group" value="#groupSearchResult" />
			<s:set name="tokenOccurrencesVar" value="tokenOccurrencesByConsumer" />
			<div class="text-center">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			<div class="table-responsive">
				<table class="table table-bordered">
					<caption class="sr-only"><span><s:text name="title.apiManagement.consumerList" /></span></caption>
					<tr>
						<th class="text-center padding-large-left padding-large-right"><abbr title="<s:text name="label.actions" />">&ndash;</abbr>
						<th><s:text name="label.key" /></th>
						<th><s:text name="label.description" /></th>
						<th><abbr title="<s:text name="label.tokens.full" />"><s:text name="label.tokens.short" /></abbr></th>
						<%--
						<th><s:text name="label.expirationDate" /></th>
						<th><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
						--%>
					</tr>
					<s:iterator var="consumerKeyVar" status="status">
						<s:set var="consumerVar" value="%{getConsumer(#consumerKeyVar)}" />
						<tr>
							<td class="text-center text-nowrap">
								<div class="btn-group btn-group-xs">
									<a class="btn btn-default" href="<s:url action="edit"><s:param name="consumerKey" value="#consumerKeyVar" /></s:url>" title="<s:text name="label.edit" />: <s:property value="#consumerKeyVar" />">
										<span class="icon fa fa-pencil-square-o"></span>
										<span class="sr-only"><s:text name="label.edit" /></span>
									</a>
								</div>
								<div class="btn-group btn-group-xs">
									<a class="btn btn-warning" href="<s:url action="trash"><s:param name="consumerKey" value="#consumerKeyVar"/></s:url>" title="<s:text name="label.remove" />: <s:property value="#consumerKeyVar" />">
										<span class="icon fa fa-times-circle-o"></span>
										<span class="sr-only"><s:text name="label.alt.clear" /></span>
									</a>
								</div>
							</td>
							<td><code><s:property value="#consumerKeyVar" /></code></td>
							<td>
								<s:if test="%{#consumerVar.description.length()>140}">
									<abbr title="<s:property value="#consumerVar.description" />"><s:property value="%{#consumerVar.description.substring(0,140)}" />&hellip;</abbr>
								</s:if>
								<s:else>
									<s:property value="#consumerVar.description" />
								</s:else>
							</td>
							<td class="text-right">
								<s:if test="null == #tokenOccurrencesVar[#consumerKeyVar]" >0</s:if>
								<s:else><s:property value="#tokenOccurrencesVar[#consumerKeyVar]" /></s:else>
								<span class="sr-only"><s:text name="label.tokens.full" /></span>
							</td>

						<%--
							<td class="monospace"><a href="<s:url action="edit"><s:param name="consumerKey" value="#consumerKeyVar" /></s:url>" title="<s:text name="label.edit" />: <s:property value="#consumerKeyVar" />" ><s:property value="#consumerKeyVar" /></a></td>
							<td>
								<s:if test="%{#consumerVar.description.length()>200}">
									<s:property value="%{#consumerVar.description.substring(0,200)}" />&#133;
								</s:if>
								<s:else>
									<s:property value="#consumerVar.description" />
								</s:else>
							</td>
							<td class="centerText monospace">
								<s:if test="#consumerVar.expirationDate != null">
									<s:date name="#consumerVar.expirationDate" format="dd/MM/yyyy" />
								</s:if>
								<s:else><abbr title="<s:text name="label.none" />">&ndash;</abbr></s:else>
							</td>
							<td class="monospace icon">
								<s:property value="#tokenOccurrencesVar.class"/>
								<s:if test="null == #tokenOccurrencesVar[#consumerKeyVar]" >0</s:if>
								<s:else><s:property value="#tokenOccurrencesVar[#consumerKeyVar]" /></s:else>
							</td>
							<td><a href="<s:url action="trash"><s:param name="consumerKey" value="#consumerKeyVar"/></s:url>" title="<s:text name="label.remove" />: <s:property value="#consumerKeyVar" />"><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.alt.clear" />" /></a></td>
							--%>
						</tr>
					</s:iterator>
				</table>
			</div>

			<div class="text-center">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:form>
</div>
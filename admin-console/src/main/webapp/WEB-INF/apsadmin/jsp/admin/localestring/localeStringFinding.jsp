<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a>
		&#32;/&#32;
		<s:text name="title.languageAdmin.labels" />
	</span>
</h1>
<div id="main" role="main">
	<s:form action="search" cssClass="form-horizontal" role="search">
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
		<div class="form-group">
			<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<span class="input-group-addon" title="<s:text name="label.search.by" />&#32;<s:text name="label.text" />">
						<span class="icon fa fa-file-text-o fa-lg"></span>
					</span>
					<label class="sr-only" for="text"><s:text name="label.search.by" />&#32;<s:text name="label.text" /></label>
					<wpsf:textfield name="text" id="text" cssClass="form-control input-lg" title="%{getText('label.search.by') +' '+ getText('label.text')}" placeholder="%{getText('label.search.by') +' '+ getText('label.text')}" />
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
							<label class="control-label col-sm-2 text-right" for="searchOption"><s:text name="label.type"/></label>
							<div class="col-sm-5 input-group">
								<select name="searchOption" id="searchOption" class="form-control">
									<option <s:if test="searchOption=='all'">selected="selected"</s:if> value="all"><s:text name="label.all" /></option>
									<option <s:if test="searchOption=='labelkey'">selected="selected"</s:if> value="labelkey"><s:text name="label.key" /></option>
									<s:iterator value="systemLangs">
										<option <s:if test="searchOption==code">selected="selected"</s:if> value="<s:property value="code" />"><s:property value="descr" /></option>
									</s:iterator>
								</select>
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
		<a href="<s:url namespace="/do/LocaleString" action="new" />" class="btn btn-default">
			<span class="icon fa fa-plus-circle"></span>
			<s:text name="title.generalSettings.locale.new" />
		</a>
		<s:form action="search" cssClass="form-horizontal margin-large-top">
		<p class="sr-only">
			<wpsf:hidden name="text" />
			<wpsf:hidden name="searchOption" />
		</p>
		<s:set name="currentLocaleStrings" value="localeStrings"/>
		<wpsa:subset source="currentLocaleStrings" count="10" objectName="groupContent" advanced="true" offset="5">
			<s:set name="group" value="#groupContent" />
			<div  class="text-center">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			<p class="sr-only">
				<wpsf:hidden name="lastGroupBy" />
				<wpsf:hidden name="lastOrder" />
			</p>
			<s:if test="%{#currentLocaleStrings.size > 0}" >
				<p class="sr-only"><s:text name="title.generalSettings.locale.installedLabels" /></p>
				<div class="dl-list">
					<s:iterator var="key">
						<dl class="dl-horizontal dl-striped panel padding-base-top padding-base-bottom">
							<s:set name="currentLabel" value="labels[#key]" />
							<dt class="hidden-sm"><s:text name="label.code" /></dt>
								<dd><s:property value="#key" /></dd>
								<s:iterator value="systemLangs" var="lang">
									<dt><abbr title="<s:property value="#lang.descr" />"><s:property value="#lang.descr" /></abbr></dt>
										<dd>
											<s:if test="%{#currentLabel[#lang.code]==null || #currentLabel[#lang.code].length()==0}">
												<abbr title="empty">&ndash;</abbr>
											</s:if>
											<s:else>
												<s:property value="#currentLabel[#lang.code]" />
											</s:else>
										</dd>
								</s:iterator>
							<dt class="sr-only"><s:text name="label.actions" /></dt>
								<dd class="margin-small-top">
									<a class="btn btn-default btn-small"href="<s:url action="edit" namespace="/do/LocaleString" />?key=<s:property value="#key" />"title="<s:text name="label.edit" />: <s:property value="#key" />"> <span class="icon fa fa-pencil-square-o">&#32;<s:text name="label.edit" /></span></a> 
									<a class="btn btn-default btn-small"href="<s:url action="delete" namespace="/do/LocaleString"><s:param name="key" value="#key" /></s:url>"title="<s:text name="label.remove" />: <s:property value="#key" />"> <span class="icon fa fa-times-circle-o">&#32;<s:text name="label.remove" /></a> 
								</dd>
						</dl>
					</s:iterator>
				</div>
			</s:if>
			<div  class="text-center">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:form>
</div>
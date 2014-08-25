<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="
						<s:url action="configure" namespace="/do/Page">
							<s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
						</s:url>
			 " title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget" />
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

<s:url action="saveNavigatorConfig" namespace="do/Page/SpecialWidget/Navigator" id="formAction" anchor="expressions" />
<s:form action="%{'/' + #formAction}" cssClass="form-horizontal">

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
			<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
			<wpsf:hidden name="navSpec" />
		</p>

		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h3 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h3>
				<ul class="margin-base-vertical">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
				</ul>
			</div>
		</s:if>

		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
				<ul class="margin-base-vertical">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
		</s:if>

		<h3 id="expressions"><s:text name="widget.configNavigator.expressionList" /></h3>

		<s:if test="expressions.size != 0">

		<ul class="list-group">
			<s:iterator value="expressions" id="expression" status="rowstatus">
			<li class="list-group-item">
				<div class="row">
				<div class="col-md-6">
					<span class="label label-info"><s:property value="#rowstatus.index + 1"/></span>&#32;
					<label class="label label-default"><s:text name="widget.configNavigator.navSpec" /></label>&#32;
					<s:if test="#expression.specId == 1"><s:text name="widget.configNavigator.currentPage" /></s:if>
					<s:elseif test="#expression.specId == 2"><s:text name="widget.configNavigator.parentCurrent" /></s:elseif>
					<s:elseif test="#expression.specId == 3"><s:text name="widget.configNavigator.parentFromCurrent" />: <s:property value="specSuperLevel" /></s:elseif>
					<s:elseif test="#expression.specId == 4"><s:text name="widget.configNavigator.parentFromRoot" />: <s:property value="specAbsLevel" /></s:elseif>
					<s:elseif test="#expression.specId == 5"><s:text name="widget.configNavigator.specifiedPage" />:
						<s:set var="specPageVar" value="%{getPage(specCode)}" ></s:set>
						<s:property value="#specPageVar.getFullTitle(currentLang.code)" /><s:if test="!#specPageVar.showable"> [i]</s:if>
						<s:if test="null == #specPageVar" ><s:text name="note.noPageSet" /></s:if>
					</s:elseif>
					<s:else>ERROR</s:else>
					&#32;
					<s:if test="#expression.operatorId == -1"></s:if>
					<s:elseif test="#expression.operatorId == 1">
						<label class="label label-default" title="<s:text name="widget.configNavigator.operator" />"><span class="icon fa fa-angle-right"></span></label>&#32;
						<s:text name="widget.configNavigator.allChildren" />
					</s:elseif>
					<s:elseif test="#expression.operatorId == 2">
						<label class="label label-default" title="<s:text name="widget.configNavigator.operator" />"><span class="icon fa fa-angle-right"></span></label>&#32;
						<s:text name="widget.configNavigator.allNodes" />
					</s:elseif>
					<s:elseif test="#expression.operatorId == 3">
						<label class="label label-default" title="<s:text name="widget.configNavigator.operator" />"><span class="icon fa fa-angle-right"></span></label>&#32;
						<abbr title="<s:text name="widget.configNavigator.levelOfNodesTothisLevel" />"><s:text name="widget.configNavigator.nodesTothisLevel" /></abbr>: <s:property value="operatorSubtreeLevel" />
					</s:elseif>
					<s:else>ERROR</s:else>
				</div>
				<div class="col-md-6">
					<div class="btn-toolbar pull-right">
						<div class="btn-group btn-group-xs">
							<wpsa:actionParam action="moveExpression" var="actionName" >
								<wpsa:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
								<wpsa:actionSubParam name="movement" value="UP" />
							</wpsa:actionParam>
							<wpsf:submit action="%{#actionName}" type="button" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
								<span class="icon fa fa-sort-desc"></span>
							</wpsf:submit>

							<wpsa:actionParam action="moveExpression" var="actionName" >
								<wpsa:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
								<wpsa:actionSubParam name="movement" value="DOWN" />
							</wpsa:actionParam>
							<wpsf:submit action="%{#actionName}" type="button" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
								<span class="icon fa fa-sort-asc"></span>
							</wpsf:submit>
						</div>
						<div class="btn-group btn-group-xs">
							<wpsa:actionParam action="removeExpression" var="actionName" >
								<wpsa:actionSubParam name="expressionIndex" value="%{#rowstatus.index}" />
							</wpsa:actionParam>
							<wpsf:submit action="%{#actionName}" type="button" title="%{getText('label.remove')}" cssClass="btn btn-warning">
								<span class="icon fa fa-times-circle-o"></span>
							</wpsf:submit>
						</div>
					</div>
				</div>
				</div>
			</li>
			</s:iterator>
		</ul>
		</s:if>
		<s:else>
			<div class="alert alert-info">
				<p><s:text name="note.noRuleSet" /></p>
			</div>
		</s:else>

		<hr />

		<fieldset class="margin-large-top">
			<h4><s:text name="widget.configNavigator.navSpec" /></h4>
			<div class="form-group">
				<div class="col-xs-12">
					<div class="btn-group" data-toggle="buttons">

						<label class="btn btn-default">
							<input type="radio" name="specId" id="specId1" value="1" />
							<s:text name="widget.configNavigator.currentPage" />
						</label>

						<label class="btn btn-default">
							<input type="radio" name="specId" id="specId2" value="2" />
							<s:text name="widget.configNavigator.parentCurrent" />
						</label>

						<div class="btn btn-default btn-with-form-control">
							<input type="radio" name="specId" id="specId3" value="3" />
							<label class="sr-only" for="specId3">
								<s:text name="widget.configNavigator.parentFromCurrent" />
							</label>
							<label class="label-btn-with-form-control" for="specSuperLevel">
								<s:text name="widget.configNavigator.parentFromCurrent" />
							</label>&#32;<wpsf:select name="specSuperLevel" id="specSuperLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" cssClass="form-control" />
						</div>

						<div class="btn btn-default btn-with-form-control">
							<input type="radio" name="specId" id="specId4" value="4" />
							<label class="sr-only" for="specId4">
								<s:text name="widget.configNavigator.parentFromRoot" />
							</label>
							<label class="label-btn-with-form-control">
								<s:text name="widget.configNavigator.parentFromRoot" />
							</label>&#32;<wpsf:select name="specAbsLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" cssClass="form-control" />
						</div>

						<div class="btn btn-default btn-with-form-control">
							<input type="radio" name="specId" id="specId5" value="5" />
							<label class="sr-only" for="specId5">
								<s:text name="widget.configNavigator.specifiedPage" />
							</label>
							<label class="label-btn-with-form-control">
								<s:text name="widget.configNavigator.specifiedPage" /></label>&#32;<select name="specCode" class="form-control">
							<s:iterator var="page" value="pages">
								<option value="<s:property value="#page.code"/>"><s:property value="#page.getShortFullTitle(currentLang.code)"/><s:if test="!#page.showable"> [i]</s:if></option>
							</s:iterator>
							</select>
						</div>

					</div>
				</div>
			</div>
		</fieldset>

		<fieldset class="margin-large-top">
			<h4><s:text name="widget.configNavigator.operator" /></h4>
			<div class="form-group">
				<div class="col-xs-12">
					<label for="operatorId"><s:text name="widget.configNavigator.operatorType" /></label>
					<select name="operatorId" id="operatorId" class="form-control">
						<option value="0"><s:text name="widget.configNavigator.none" /></option>
						<option value="1"><s:text name="widget.configNavigator.allChildren" /></option>
						<option value="2"><s:text name="widget.configNavigator.allNodes" /></option>
						<option value="3"><s:text name="widget.configNavigator.nodesTothisLevel" /></option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-12">
					<label for="operatorSubtreeLevel"><s:text name="widget.configNavigator.levelOfNodesTothisLevel" />:</label>
					<wpsf:select name="operatorSubtreeLevel" id="operatorSubtreeLevel" headerKey="-1" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}" cssClass="form-control" />
				</div>
			</div>
		</fieldset>

		<p>
			<wpsf:submit action="addExpression" type="button" cssClass="btn btn-default">
				<span class="icon fa fa-plus-square"></span>
				<s:text name="widget.configNavigator.addExpression" />
			</wpsf:submit>
		</p>

	</div>
</div>
<div class="form-group">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<wpsf:submit action="saveNavigatorConfig" type="button" cssClass="btn btn-primary btn-block">
			<span class="icon fa fa-floppy-o"></span>&#32;
			<s:text name="label.save" />
		</wpsf:submit>
	</div>
</div>

</s:form>

</div>
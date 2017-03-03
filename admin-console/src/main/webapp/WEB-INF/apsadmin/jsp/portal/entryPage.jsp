<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageTree" />"><s:text name="title.pageTree" /></a></li>
    <li>
    	<s:if test="strutsAction == 1">
			<s:text name="title.newPage" />
		</s:if>
		<s:elseif test="strutsAction == 2">
			<s:text name="title.editPage" />
		</s:elseif>
		<s:elseif test="strutsAction == 3">
			<s:text name="title.pastePage" />
		</s:elseif>
    </li>
</ol>

<style>
    .treeInteractionButtons{
        font-size: 16px;
        font-weight: bold;
    }
    .green{
        color: green;
    }
    
   #pageTree .statusField i.fa {
        font-size: 15px;
        margin-top: 6px;
    }
    
    .table-view-pf-actions .btn, .table-view-pf-actions .dropdown-toggle{
        text-align: center;
    }
</style>

<h1>
<s:if test="strutsAction == 1">
	<s:text name="title.newPage" />
</s:if>
<s:elseif test="strutsAction == 2">
	<s:text name="title.editPage" />
</s:elseif>
<s:elseif test="strutsAction == 3">
	<s:text name="title.pastePage" />
</s:elseif>
</h1>

<div id="main" role="main">
    <s:if test="hasErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <s:text name="message.title.ActionErrors" />
            <ul>
    		<s:if test="hasActionErrors()">
                <s:iterator value="actionErrors">
                    <li><s:property escape="false" /></li>
				</s:iterator>
    		</s:if>
    		<s:if test="hasFieldErrors()">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
    		</s:if>
            </ul>
        </div>
    </s:if>
	
	<s:if test="#passwordHasFieldErrorVar || #usernameFieldErrorsVar">
	   <div class="alert alert-danger alert-dismissable">
	       <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
	           <span class="pficon pficon-close"></span>
	       </button>
	       <span class="pficon pficon-error-circle-o"></span>
	       <ul class="margin-base-vertical">
	           <s:text name="error.user.login.credentialsEmpty" />
	       </ul>
	   </div>
	</s:if>

<s:if test="strutsAction == 2"><s:set var="breadcrumbs_pivotPageCode" value="pageCode" /></s:if>
<s:else><s:set var="breadcrumbs_pivotPageCode" value="parentPageCode" /></s:else>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:form action="save" cssClass="form-horizontal">
	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="copyPageCode" />
		<wpsf:hidden name="groupSelectLock" />
		<s:if test="strutsAction == 2">
			<wpsf:hidden name="parentPageCode" />
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="group" />
		</s:if>
		<s:iterator value="extraGroups" id="groupName"><wpsf:hidden name="extraGroups" value="%{#groupName}" /></s:iterator>
		<s:if test="strutsAction == 3">
			<wpsf:hidden name="group" />
			<wpsf:hidden name="model" />
			<wpsf:hidden name="defaultShowlet" />
			<wpsf:hidden name="showable" />
			<wpsf:hidden name="useExtraTitles" />
			<wpsf:hidden name="charset" />
			<wpsf:hidden name="mimeType" />
		</s:if>
	</p>
	<fieldset class="col-xs-12 settings-form">

        <div class="form-group">
            <div class="row">
                <div class="col-xs-2">
                    <div class="form-group-label"><s:text name="label.info" /></div>
                </div>
                <div class="col-xs-10">
                    <div class="form-group-separator">* <s:text name="label.requiredFields" /></div>
                </div>
            </div>
        </div>

	<s:iterator value="langs">
	    <%-- lang --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['lang'+code]}" />
		<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
	    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
	    
	    <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="lang<s:property value="code" />">
	        	<abbr title="<s:property value="descr" />"><code class="label label-info" ><s:property value="code" /></code></abbr>&#32;<s:text name="name.pageTitle" />
            	<i class="fa fa-asterisk required-icon"></i>
            </label>
	        <div class="col-sm-10">
               	<wpsf:textfield name="%{'lang'+code}" id="%{'lang'+code}" value="%{titles.get(code)}" cssClass="form-control" />
	            <s:if test="#fieldHasFieldErrorVar">
	                <span class="help-block text-danger">
	                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
	            </s:if>
	        </div>
        </div>
	</s:iterator>
	    <%-- pageCode --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['pageCode']}" />
		<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
	    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
	    
	    <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="pageCode">
            	<s:text name="name.pageCode" />
            	<i class="fa fa-asterisk required-icon"></i>
            </label>
	        <div class="col-sm-10">
				<wpsf:textfield name="pageCode" id="pageCode" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
	            <s:if test="#fieldHasFieldErrorVar">
	                <span class="help-block text-danger">
	                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
	                    </span>
	            </s:if>
	        </div>
        </div>
        
	<s:if test="strutsAction != 2">
		<s:set var="fieldErrorsVar" value="%{fieldErrors['parentPageCode']}" />
		<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
	    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
	    
	    <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="pageCode">
            	Tree Positions
            	<i class="fa fa-asterisk required-icon"></i>
            </label>
	        <div class="col-sm-10">
	    
	<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
	<div class="table-responsive overflow-visible">
		<table id="pageTree"
			class="table table-bordered table-hover table-treegrid">
			<thead>
				<tr>
					<th style="width: 68%;">Tree Pages <s:if test="#pageTreeStyleVar == 'classic'">
							<button type="button" class="btn-no-button expand-button" id="expandAll">
								<i class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Expand All
							</button>
							<button type="button" class="btn-no-button" id="collapseAll">
								<i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;Collapse All
							</button>
						</s:if>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:set var="inputFieldName" value="%{'parentPageCode'}" />
				<s:set var="selectedTreeNode" value="%{parentPageCode}" />
                <s:set var="selectedPage" value="%{getPage(parentPageCode)}" />
				<s:set var="liClassName" value="'page'" />
				<s:set var="treeItemIconName" value="'fa-folder'" />
				
				<wpsa:groupsByPermission permission="managePages" var="groupsByPermission" />
				
				<s:if test="#pageTreeStyleVar == 'classic'">
					<wpsa:pageTree allowedGroups="${groupsByPermission}" var="currentRoot" online="false" />
					<s:include value="include/entryPage_treeBuilderPages.jsp" />
				</s:if>
				<s:elseif test="#pageTreeStyleVar == 'request'">
					<style>
					.table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
						cursor: pointer;
						display: none;
					}
					</style>
					<s:set var="treeNodeActionMarkerCode" value="treeNodeActionMarkerCode" />
					<s:set var="targetNode" value="%{parentPageCode}" />
					<s:set var="treeNodesToOpen" value="treeNodesToOpen" />

					<wpsa:pageTree allowedGroups="${groupsByPermission}" var="currentRoot" online="false" onDemand="true" 
							open="${treeNodeActionMarkerCode!='close'}" targetNode="${targetNode}" treeNodesToOpen="${treeNodesToOpen}" />
					<s:include value="include/entryPage_treeBuilder-request-linksPages.jsp" />
				</s:elseif>
			</tbody>
		</table>
	</div>
		<s:if test="#hasFieldErrorVar">
			<p class="text-danger padding-small-vertical"><s:iterator value="#fieldErrorsVar"><s:property /> </s:iterator></p>
		</s:if>
	        </div>
        </div>
	</s:if>

<s:if test="strutsAction != 3">
    <%-- ownerGroup --%>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['ownerGroup']}" />
	<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
           <label class="col-sm-2 control-label" for="ownerGroup">
           	<s:text name="label.ownerGroup" />
           	<i class="fa fa-asterisk required-icon"></i>
           </label>
        <div class="col-sm-10">
			<wpsf:select name="group" id="group" list="allowedGroups" listKey="name" listValue="descr" disabled="%{groupSelectLock}" cssClass="form-control"></wpsf:select>
            <s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
            </s:if>
        </div>
	</div>
	
    <%-- extraGroups --%>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['extraGroups']}" />
	<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
           <label class="col-sm-2 control-label" for="extraGroups">
           	<s:text name="label.join" />&#32;<s:text name="label.group" />
           	<i class="fa fa-asterisk required-icon"></i>
           </label>
        <div class="col-sm-10">
			<div class="input-group">
				<wpsf:select name="extraGroupName" id="extraGroups" list="groups" listKey="name" listValue="descr" cssClass="form-control" />
				<span class="input-group-btn">
					<wpsf:submit type="button" action="joinExtraGroup" cssClass="btn btn-default">
						<span class="icon fa fa-plus"></span>&#32;
						<s:property value="label.join" />
					</wpsf:submit>
				</span>
			</div>
            <s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
            </s:if>
        </div>
        <div class="col-sm-12">
		<s:if test="extraGroups.size() != 0">
			<s:iterator value="extraGroups" id="groupName">
				<wpsa:actionParam action="removeExtraGroup" var="actionName" >
					<wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
				</wpsa:actionParam>
				<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
					<span class="icon fa fa-tag"></span>&#32;
					<s:property value="%{getSystemGroups()[#groupName].getDescr()}"/>&#32;
					<wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-default btn-xs badge">
						<span class="icon fa fa-times"></span>
						<span class="sr-only">x</span>
					</wpsf:submit>
				</span>
			</s:iterator>
		</s:if>
        </div>
	</div>
	
    <%-- pageModel --%>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['model']}" />
	<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
           <label class="col-sm-2 control-label" for="ownerGroup">
           	<s:text name="name.pageModel" />
           	<i class="fa fa-asterisk required-icon"></i>
           </label>
        <div class="col-sm-10">
			<wpsf:select name="model" id="model" list="pageModels" listKey="code" listValue="descr" cssClass="form-control"></wpsf:select>
            <s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
            </s:if>
        </div>
	</div>
	
	<ul>
		<li class="checkbox">
		</li>
		<li class="checkbox">
		</li>
		<li class="checkbox">
		</li>
		<li class="checkbox"></li>
	</ul>
	
	
	

    <div class="form-group">
        <div class="row">
            <div class="col-xs-4 col-label">
				<label class="display-block" for="defaultShowlet"><s:text name="name.hasDefaultWidgets" /></label>
            </div>
            <div class="col-xs-2">
            	<wpsf:checkbox name="defaultShowlet" id="defaultShowlet" cssClass="bootstrap-switch" />
            </div>
        </div>
    </div>

    <div class="form-group">
        <div class="row">
            <div class="col-xs-4 col-label">
				<label class="display-block" for="viewerPage"><s:text name="name.isViewerPage" /></label>
            </div>
            <div class="col-xs-2">
				<wpsf:checkbox name="viewerPage" id="viewerPage" cssClass="bootstrap-switch" />
            </div>
        </div>
    </div>
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
		<label class="col-sm-2 control-label" for="showable">Main Menu</label>
        <div class="col-sm-10">
        	<wpsf:checkbox name="showable" id="showable" />&#32;<s:text name="name.isShowablePage" />
        </div>
	</div>
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
		<label class="col-sm-2 control-label" for="useExtraTitles">SEO</label>
        <div class="col-sm-10">
        	<wpsf:checkbox name="useExtraTitles" id="useExtraTitles" />&#32<abbr lang="en" title="<s:text name="name.SEO.full" />"><s:text name="name.SEO.short" /></abbr>
        </div>
	</div>
	
	
	
	
    <%-- charset --%>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['charset']}" />
	<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
           <label class="col-sm-2 control-label" for="ownerGroup">
           	<s:text name="name.charset" />
           	<i class="fa fa-asterisk required-icon"></i>
           </label>
        <div class="col-sm-10">
			<wpsf:select name="charset" id="charset"
					 headerKey="" headerValue="%{getText('label.default')}" list="allowedCharsets" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
            </s:if>
        </div>
	</div>
	
    <%-- mimeType --%>
	<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['mimeType']}" />
	<s:set var="hasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
    
    <div class="form-group<s:property value="#controlGroupErrorClass" />">
           <label class="col-sm-2 control-label" for="ownerGroup">
           	<s:text name="name.mimeType" />
           	<i class="fa fa-asterisk required-icon"></i>
           </label>
        <div class="col-sm-10">
			<wpsf:select name="mimeType" id="mimeType"
					 headerKey="" headerValue="%{getText('label.default')}" list="allowedMimeTypes" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
            </s:if>
        </div>
	</div>
</s:if>
	
	<wpsa:hookPoint key="core.entryPage" objectName="hookPointElements_core_entryPage">
	<s:iterator value="#hookPointElements_core_entryPage" var="hookPointElement">
		<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
	</s:iterator>
	</wpsa:hookPoint>
	
    <div class="col-md-12"> 
        <div class="form-group pull-right "> 
            <div class="btn-group">
                <wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </div>
	
	</fieldset>
</s:form>

</div>
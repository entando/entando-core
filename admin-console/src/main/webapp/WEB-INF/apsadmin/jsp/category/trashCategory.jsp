<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li><s:text name="title.categoryManagement"/></li>
    <li class="page-title-container"><s:text name="title.deleteCategory"/></li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.deleteCategory"/>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o"
                                                                                         aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div class="text-center">
    <s:form action="delete">
        <s:set var="categoryToDelete" value="getCategory(selectedNode)"/>
        <p class="sr-only"><wpsf:hidden name="selectedNode"/></p>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline"><s:text name="category.delete"/></p>
        <p>
            <s:text name="category.delete.confirm"/>&#32;
            <s:property value="getCategory(selectedNode).code"/>

        </p>
        <div class="text-center margin-large-top">
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="label.delete"/>
            </wpsf:submit>
        </div>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width"
               href="<s:url action="viewTree" namespace="/do/Category"><s:param name="selectedNode"><s:property value="%{#categoryToDelete.code}" /></s:param></s:url>"/>
            <s:text name="title.categoryManagement"/>
            </a>
        </div>

        </s:form>
</div>

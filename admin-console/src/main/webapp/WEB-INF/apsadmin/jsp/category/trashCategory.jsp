<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="menu.configure"/></li>
    <li>
        <a href="<s:url namespace="/do/Category" action="viewTree" />">
            <s:text name="title.categoryManagement"/>
        </a>
    </li>
    <li class="page-title-container"><s:text name="title.deleteCategory"/></li>

</ol>
<h1 class="page-title-container">
    <s:text name="title.deleteCategory"/>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div class="text-center">
    <s:form action="delete">
        <s:set var="categoryToDelete" value="getCategory(selectedNode)"/>
        <p class="sr-only">
        <wpsf:hidden name="selectedNode"/>
    </p>

    <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
    <p class="esclamation-underline">
        <s:text name="category.delete"/>
    </p>
    <p class="esclamation-underline-text">
        <s:text name="category.delete.confirm"/>
        &#32;
        <s:property value="getCategory(selectedNode).code"/>?
    </p>
    <div class="text-center margin-large-top">
        <a class="btn btn-default button-fixed-width"
           href="<s:url action="viewTree" namespace="/do/Category"><s:param name="selectedNode"><s:property value="%{#categoryToDelete.code}" /></s:param></s:url>"/>
        <s:text name="label.back"/>
        </a>
        <wpsf:submit type="button"
                     cssClass="btn btn-danger button-fixed-width">
            <s:text name="label.delete"/>
        </wpsf:submit>
    </div>
</s:form>
</div>

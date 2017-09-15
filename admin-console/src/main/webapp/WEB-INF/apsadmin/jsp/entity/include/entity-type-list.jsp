<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <s:text name="%{'title.' + entityManagerName + '.menu'}"/>
    <li class="page-title-container">
    <s:text name="%{'title.' + entityManagerName + '.management'}"/>
</li>
</ol>

<h1 class="page-title-container">
    <s:text name="%{'title.' + entityManagerName + '.management'}"/>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="%{'page.' + entityManagerName + '.help'}"/>" data-placement="left" data-original-title="">
           <i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<s:include value="/WEB-INF/apsadmin/jsp/entity/include/entity-type-list-body.jsp"/>

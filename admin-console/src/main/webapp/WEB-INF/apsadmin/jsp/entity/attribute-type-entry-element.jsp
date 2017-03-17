<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
            <s:text name="%{'title.' + entityManagerName + '.management'}" />
        </a>
    </li>

    <li class="page-title-container">
        <s:if test="strutsAction == 2">
            <a href="<s:url action="initEditEntityType" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="entityType.typeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityTypes.editType.edit" />">
            </s:if>
            <s:text name="title.entityTypes.editType.edit" />: 
            <s:property value="entityType.typeCode" /> - <s:property value="entityType.typeDescr" />
            <s:if test="strutsAction == 2">
            </a>
        </s:if>
    </li>
</ol>


<h1 class="page-title-container">
    <s:if test="strutsAction == 2">
        <a href="<s:url action="initEditEntityType" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param><s:param name="entityTypeCode"><s:property value="entityType.typeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityTypes.editType.edit" />">
        </s:if>
        <s:text name="title.entityTypes.editType.edit" />: 
        <s:property value="entityType.typeCode" /> - <s:property value="entityType.typeDescr" />
        <s:if test="strutsAction == 2">
        </a>
    </s:if>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>    

<div class="text-right">
    <div class="form-group-separator"><s:text name="label.requiredFields" /></div>
</div>
<br>


<div id="main" role="main">

    <s:set name="listAttribute" value="listAttribute" />
    <s:set name="attributeElement" value="attributeElement" />

    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <s:text name="note.workingOnAttribute" />:&#32;
        <s:property value="#attributeElement.type" />,&#32;
        <s:text name="note.workingOnAttributeIn" />&#32;
        <s:property value="#listAttribute.name" />&#32;
        (<s:property value="#listAttribute.type" />)
    </div>

    <s:form action="saveListElement" cssClass="form-horizontal">

        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>	

        <p class="sr-only">
            <wpsf:hidden name="attributeName"/>
            <wpsf:hidden name="attributeTypeCode"/>
            <wpsf:hidden name="strutsAction"/>
        </p>

        <s:if test="isEntityManagerSearchEngineUser() && isIndexableOptionSupported(attributeTypeCode)">
            <fieldset class="col-xs-12">
                <legend><s:text name="label.info" /></legend>

                <div class="form-group">
                    <label class="col-sm-2 control-label" for="attributeTypeCode"><s:text name="label.type" />:</label>
                   
                    <wpsf:textfield id="attributeTypeCode" name="attributeTypeCode" value="%{attributeTypeCode}" disabled="true" cssClass="form-control" />
                </div>

                <div class="form-group">
                    <wpsf:checkbox name="indexable" id="indexable" />
                    <label for="indexable"><s:text name="Entity.attribute.flag.indexed.full" /></label>
                </div>

            </fieldset>
        </s:if>

        <s:if test="#attributeElement.textAttribute">
            <s:set var="attribute" value="#attributeElement"></s:set>
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-text.jsp"/>
        </s:if>

        <s:elseif test="#attributeElement.type == 'Number'">
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-number.jsp"/>
        </s:elseif>

        <s:elseif test="#attributeElement.type == 'Date'">
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-date.jsp"/>
        </s:elseif>

        <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-ognl.jsp"/>
        <fieldset class="col-xs-12">
            <div class="form-group">
                <div class="col-xs-12 ">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.continue" />
                    </wpsf:submit>
                </div>
            </div>
    </div>
</s:form>
</div>

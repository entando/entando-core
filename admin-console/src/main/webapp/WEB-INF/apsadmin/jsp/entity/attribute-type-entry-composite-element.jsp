<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <s:text name="%{'title.' + entityManagerName + '.menu'}"/>
    <li>
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: 
           <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
    </li>
    <li>
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
            <s:text name="%{'title.' + entityManagerName + '.management'}" />
        </a>
    </li>
    <li class="page-title-container">
        <a href="<s:url action="initEditEntityType" namespace="/do/Entity"><s:param name="entityManagerName"><s:property value="entityManagerName" />
               </s:param><s:param name="entityTypeCode">
                   <s:property value="entityType.typeCode" />
               </s:param>
           </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityTypes.editType.edit" />">
            <s:text name="title.entityTypes.editType.edit" />:
            <span>
                <s:property value="entityType.typeCode" /> - <s:property value="entityType.typeDescr" />
            </span>
        </a>
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.entityTypes.editType.edit" />
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div class="mb-20">

    <s:set var="listAttribute" value="listAttribute" />
    <s:set var="compositeAttribute" value="compositeAttributeOnEdit" />

    <div class="alert alert-success">
        <span class="pficon pficon-info"></span>
        <s:text name="note.workingOnAttribute" />:&#32;
        <s:if test="null != #listAttribute">
            <span><s:property value="#compositeAttribute.type" /></span>,&#32;
            <s:text name="note.workingOnAttributeIn" />&#32;
            <span><s:property value="#listAttribute.name" /></span>&#32;
            (<span><s:property value="#listAttribute.type" /></span>)
        </s:if>
        <s:else>
            <span><s:property value="#compositeAttribute.name" /></span>
        </s:else>
    </div>

    <s:set var="attribute" value="getAttributePrototype(attributeTypeCode)" />

    <s:form action="saveAttributeElement" cssClass="form-horizontal">

        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong>
                    <s:text name="message.title.FieldErrors" />
                </strong>
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
            <wpsf:hidden name="attributeTypeCode" />
            <wpsf:hidden name="strutsAction" />
        </p>
        <fieldset class="col-xs-12">
            <legend><s:text name="label.info" />
            </legend>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="attributeType"><s:text name="label.type" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield  cssClass="form-control" id="attributeType" name="attributeType" value="%{attributeTypeCode}" disabled="true" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="attributeName">
                        <s:text name="label.code" />
                    </label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="attributeName" id="attributeName" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="attributeDescription"><s:text name="label.description" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="attributeDescription" id="attributeDescription" cssClass="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-xs-2 control-label ">

                    <label for="required">
                        <s:text name="Entity.attribute.flag.mandatory.full" />
                    </label>
                </div>
                <div class="col-xs-10">
                    <wpsf:checkbox name="required" id="required" cssClass="bootstrap-switch"/>
                </div>
            </div>
            <s:if test="isEntityManagerSearchEngineUser() && isIndexableOptionSupported(attributeTypeCode)">
                <div class="form-group">
                    <div class="col-xs-2 control-label ">

                        <label for="indexable">
                            <s:text name="Entity.attribute.flag.indexed.full" />
                        </label>
                    </div>
                    <div class="col-xs-10">
                        <wpsf:checkbox name="indexable" id="indexable" cssClass="bootstrap-switch"/>
                    </div>
                </div>
            </s:if>

        </fieldset>

        <s:if test="#attribute.textAttribute">
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-text.jsp" />
        </s:if>

        <s:elseif test="#attribute.type == 'Number'">
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-number.jsp" />
        </s:elseif>

        <s:elseif test="#attribute.type == 'Date'">
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-date.jsp" />
        </s:elseif>

        <s:include value="/WEB-INF/apsadmin/jsp/entity/include/validation-rules-ognl.jsp" />

        <div class="col-xs-12">
            <wpsf:submit type="button" cssClass="btn btn-primary pull-right" >
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </s:form>
</div>

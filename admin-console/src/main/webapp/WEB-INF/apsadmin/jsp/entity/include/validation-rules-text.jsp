<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12">
    <legend><s:text name="label.settings" /></legend>
    <s:if test="#attribute.type == 'Enumerator' || #attribute.type == 'EnumeratorMap'">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="enumeratorStaticItems"><s:text name="Entity.attribute.setting.enumerator.items" />
                <i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i>
            </label>
            <div class="col-sm-10">
                <s:if test="#attribute.type == 'Enumerator'">
                    <s:set var="enumeratorItemsFieldTitle" value="'label.entity.enumerator.itemsField.title'" />
                    <s:set var="enumeratorItemsFieldPlaceholder" value="'label.entity.enumerator.itemsField.placeholder'" />
                </s:if>
                <s:else>
                    <s:set var="enumeratorItemsFieldTitle" value="'label.entity.enumeratorMap.itemsField.title'" />
                    <s:set var="enumeratorItemsFieldPlaceholder" value="'label.entity.enumeratorMap.itemsField.placeholder'" />
                </s:else>
                <wpsf:textfield
                    name="enumeratorStaticItems" id="enumeratorStaticItems" cssClass="form-control"
                    title="%{getText(#enumeratorItemsFieldTitle)}"
                    placeholder="%{getText(#enumeratorItemsFieldPlaceholder)}"
                    />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="enumeratorStaticItemsSeparator"><s:text name="Entity.attribute.setting.enumerator.separator" />
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="enumeratorStaticItemsSeparator" id="enumeratorStaticItemsSeparator" cssClass="form-control" />
            </div>
        </div>
        <s:if test="#attribute.type == 'Enumerator'">
            <s:set var="enumeratorExtractorBeansVar" value="enumeratorExtractorBeans" />
        </s:if>
        <s:else>
            <s:set var="enumeratorExtractorBeansVar" value="enumeratorMapExtractorBeans" />
        </s:else>
        <s:if test="null != #enumeratorExtractorBeansVar && #enumeratorExtractorBeansVar.size() > 0">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="enumeratorExtractorBean"><s:text name="Entity.attribute.setting.enumerator.extractorBean" /></label>
                <div class="col-sm-10">
                    <wpsf:select list="#enumeratorExtractorBeansVar" name="enumeratorExtractorBean" id="enumeratorExtractorBean" headerKey="" headerValue="%{getText('note.choose')}" cssClass="form-control"/>
                </div>
            </div>
        </s:if>
    </s:if>
    <s:elseif test="#attribute.textAttribute">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="minLength"><s:text name="Entity.attribute.flag.minLength.full" />
            </label>

            <div class="col-sm-10">
                <wpsf:textfield name="minLength" id="minLength" cssClass="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="maxLength"><s:text name="Entity.attribute.flag.maxLength.full" />
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="maxLength" id="maxLength" cssClass="form-control" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="regexp"><s:text name="Entity.attribute.setting.regexp.full" />
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="regexp" id="regexp" cssClass="form-control" />
            </div>
        </div>
    </s:elseif>
</fieldset>

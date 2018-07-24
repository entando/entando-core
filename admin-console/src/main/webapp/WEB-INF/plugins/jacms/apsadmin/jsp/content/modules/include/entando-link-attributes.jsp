<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

    <h2 class="col-xs-12">
        <s:text name="linkAdditionalAttributes.formTitle" />
    </h2>

    <div class="form-group<s:property value="#controlGroupErrorClassVar" /> mt-20">
        <div class="col-xs-12">
            <label class="col-sm-2 text-right" for="linkAttributeRel"><s:text name="linkAdditionalAttributes.label.attributeRel" /></label>
            <div class="col-sm-10">
                <wpsf:textfield id="linkAttributeRel" name="linkAttributeRel" cssClass="form-control" />
                <span class="help help-block"><s:text name="linkAdditionalAttributes.help.attributeRel" /></span>
            </div>
        </div>

         <div class="col-xs-12">
            <label class="col-sm-2 text-right" for="linkAttributeTarget"><s:text name="linkAdditionalAttributes.label.attributeTarget" /></label>
            <div class="col-sm-10">
                <wpsf:textfield id="linkAttributeTarget" name="linkAttributeTarget" cssClass="form-control" />
                <span class="help help-block"><s:text name="linkAdditionalAttributes.help.attributeTarget" /></span>
            </div>
         </div>

         <div class="col-xs-12">
            <label class="col-sm-2 text-right" for="linkAttributeHRefLang"><s:text name="linkAdditionalAttributes.label.attributeHRefLang" /></label>
            <div class="col-sm-10">
                <wpsf:textfield id="linkAttributeHRefLang" name="linkAttributeHRefLang" cssClass="form-control" />
                <span class="help help-block"><s:text name="linkAdditionalAttributes.help.attributeHRefLang" /></span>
            </div>
         </div>

    </div>

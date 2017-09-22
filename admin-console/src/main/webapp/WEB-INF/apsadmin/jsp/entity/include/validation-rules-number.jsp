<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12 naumber-validation">
    <legend><s:text name="label.settings" /></legend>

    <s:set var="sameAttributesList" value="sameAttributes" />

    <div class="form-group">
        <label class="col-sm-2 control-label" for="rangeStartNumber"><s:text name="note.range.from" />
        </label>

        <div class="col-sm-3">
            <wpsf:textfield name="rangeStartNumber" id="rangeStartNumber" cssClass=" form-control "/>
        </div>

    </div>
    <s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="rangeStartNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.from.attribute" />:</label>

            <div class="col-sm-3">
                <wpsf:select name="rangeStartNumberAttribute" id="rangeStartNumberAttribute"
                             list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass=" form-control "/>
            </div>
        </div>
    </s:if>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="rangeEndNumber"><s:text name="note.range.to" />
        </label>
        <div class="col-sm-3">
            <wpsf:textfield name="rangeEndNumber" id="rangeEndNumber" cssClass=" form-control " />
        </div>
    </div>
    <s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="rangeEndNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.to.attribute" />:</label>
            <div class="col-sm-3">
                <wpsf:select name="rangeEndNumberAttribute" id="rangeEndNumberAttribute"
                             list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass=" form-control "/>
            </div>
        </div>
    </s:if>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="equalNumber"><s:text name="note.equals.to" />
        </label>
        <div class="col-sm-3">
            <wpsf:textfield name="equalNumber" id="equalNumber" cssClass=" form-control " />
        </div>
    </div>
    <s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="equalNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.equals.to.attribute" />:</label>
            <div class="col-sm-3">
                <wpsf:select name="equalNumberAttribute" id="equalNumberAttribute"
                             list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass=" form-control "/>
            </div>
        </div>
    </s:if>

</fieldset>

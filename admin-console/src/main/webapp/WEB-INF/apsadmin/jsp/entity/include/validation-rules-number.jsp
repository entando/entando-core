<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12 naumber-validation">
    <legend><s:text name="label.settings" /></legend>

    <s:set var="sameAttributesList" value="sameAttributes" />

    <div class="form-group">
        <label class="col-sm-2 control-label" for="rangeStartNumber"><s:text name="note.range.from" />
            <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-placement="top" data-content="to be inserted   " data-original-title="">
                <span class="fa fa-info-circle"></span></a>
        </label>

        <div class="col-sm-3">
            <wpsf:textfield name="rangeStartNumber" id="rangeStartNumber" cssClass=" form-control bootstrap-datepicker"/>
        </div>

    </div>
    <s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="rangeStartNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.from.attribute" />:</label>

            <div class="col-sm-3">
                <wpsf:select name="rangeStartNumberAttribute" id="rangeStartNumberAttribute" 
                             list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass=" form-control bootstrap-datepicker"/>
            </div>
        </div>			
    </s:if>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="rangeEndNumber"><s:text name="note.range.to" />
            <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-placement="top" data-content="to be inserted   " data-original-title="">
                <span class="fa fa-info-circle"></span></a>
        </label>
        <div class="col-sm-3">
            <wpsf:textfield name="rangeEndNumber" id="rangeEndNumber" cssClass=" form-control bootstrap-datepicker" />
        </div>
    </div>
    <s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="rangeEndNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.range.to.attribute" />:</label>	
            <div class="col-sm-3">
                <wpsf:select name="rangeEndNumberAttribute" id="rangeEndNumberAttribute" 
                             list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass=" form-control bootstrap-datepicker"/>
            </div>
        </div>
    </s:if>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="equalNumber"><s:text name="note.equals.to" />
            <a role="button" tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-placement="top" data-content="to be inserted   " data-original-title="">
                <span class="fa fa-info-circle"></span></a>
        </label>	
        <div class="col-sm-3">
            <wpsf:textfield name="equalNumber" id="equalNumber" cssClass=" form-control bootstrap-datepicker" />
        </div>
    </div>
    <s:if test="#sameAttributesList != null && #sameAttributesList.size() > 0">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="equalNumberAttribute"><s:text name="note.or" />&#32;<s:text name="note.equals.to.attribute" />:</label>	
            <div class="col-sm-3">
                <wpsf:select name="equalNumberAttribute" id="equalNumberAttribute" 
                             list="#sameAttributesList" headerKey="" headerValue="%{getText('label.none')}" listKey="name" listValue="name" cssClass=" form-control bootstrap-datepicker"/>
            </div>
        </div>
    </s:if>

</fieldset>


<script>
//-- Boostrap-datepicker ---
    $('.bootstrap-datepicker').datepicker({
        autoclose: true,
        todayBtn: "linked",
        todayHighlight: true
    });
</script>
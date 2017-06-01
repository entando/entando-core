<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>


<div class="form-group">
    <label class="col-sm-2 control-label" for="attributeTypeCode">
        <s:text name="label.type" />
    </label>
    <div class="col-sm-10">
        <div class="input-group">
            <wpsf:select id="attributeTypeCode" list="attributeTypes"
                         name="attributeTypeCode" listKey="type" listValue="type"
                         cssClass="form-control" headerKey=""
                         headerValue="%{getText('note.choose')}" />
            <span class="input-group-btn">
                <wpsf:submit type="button" action="addAttribute" cssClass="btn btn-primary"  value="%{getText('label.add')}" />
            </span>
        </div>
    </div>
</div>

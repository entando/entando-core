<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<fieldset class="col-xs-12">
    <legend>
        <s:text name="label.metadata" />
    </legend>

    <s:set var="freeViewerPagesVar" value="freeViewerPages" />

    <div class="form-group">
        <label class="col-xs-2 control-label" for="viewPageCode">
            <s:text name="name.viewPageCode" /></label>
        <div class="col-xs-10">
            <s:if test="#freeViewerPagesVar.size() > 0">
                <wpsf:select name="viewPageCode" id="viewPageCode" list="#freeViewerPagesVar" listKey="code"
                             listValue="%{getShortFullTitle(currentLang.code)}"
                             value="%{#entityType.viewPage}" cssClass="form-control"
                             headerKey="" headerValue="%{getText('note.choose')}" />
            </s:if>
            <s:else>
                <s:text name="label.none" />
            </s:else>
        </div>
    </div>

    <s:set var="contentModelsVar"
           value="%{getContentModels(#entityType.typeCode)}" />

    <div class="form-group">
        <label class="col-xs-2 control-label" for="defaultModelId"><s:text
                name="name.defaultModelId" /></label>
        <div class="col-xs-10">
            <s:if test="#contentModelsVar.size() > 0">
                <wpsf:select name="defaultModelId" id="defaultModelId"
                             list="#contentModelsVar" listKey="id" listValue="description"
                             value="%{#entityType.defaultModel}" cssClass="form-control"
                             headerKey="" headerValue="%{getText('note.choose')}" />
            </s:if>
            <s:else>
                <abbr title="<s:text name="note.defaultModelId.notAvailable" />">&ndash;</abbr>
            </s:else>
        </div>
    </div>

    <div class="form-group">
        <label class="col-xs-2 control-label" for="listModelId"><s:text
                name="name.listModelId" /></label>
        <div class="col-xs-10">
            <s:if test="#contentModelsVar.size() > 0">
                <wpsf:select name="listModelId" id="listModelId"
                             list="#contentModelsVar" listKey="id" listValue="description"
                             value="%{#entityType.listModel}" cssClass="form-control"
                             headerKey="" headerValue="%{getText('note.choose')}" />
            </s:if>
            <s:else>
                <abbr title="<s:text name="note.listModelId.notAvailable" />">&ndash;</abbr>
            </s:else>
        </div>
    </div>

</fieldset>

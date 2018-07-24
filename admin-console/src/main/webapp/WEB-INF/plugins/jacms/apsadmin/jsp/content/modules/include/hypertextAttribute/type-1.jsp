<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>


<s:form cssClass="action-form form-horizontal" id="form_externalUrl">

    <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/info-prev-value.jsp" />

    <p class="sr-only"><wpsf:hidden name="contentOnSessionMarker" /></p>

    <div class="form-group<s:property value="#controlGroupErrorClassVar" /> mt-20">
        <div class="col-xs-12">
            <label class="col-sm-2 text-right" for="url"><s:text name="label.url" /></label>
            <div class="col-sm-10">
                <wpsf:textfield id="txtName" name="txtName" cssClass="form-control" />
                <span class="help help-block"><s:text name="note.typeValidURL" /></span>
            </div>
        </div>
    </div>


    <!-- Link attributes -->
    <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/entando-link-attributes.jsp" />

    <div class="form-group">
        <div class="col-xs-12">
            <div class="col-sm-10 col-sm-offset-2 text-right">
                <button type="submit" id="button_externalURL" name="button_externalURL" class="btn btn-primary">
                    <s:text name="label.save" />
                </button>
            </div>
        </div>
    </div>
</s:form>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div class="input-group col-md-offset-3 col-md-6 text-center">
    <s:form action="search" class="search-pf has-button " >
        <div class="input-group">
            <label for="pageCodeToken" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="name.pageCode"/></label>
            <wpsf:textfield name="pageCodeToken" id="pageCodeToken" value="%{pageCodeToken}" cssClass="form-control" placeholder="%{getText('label.pageCode')}" />
            <div class="input-group-btn">
                <wpsf:submit type="button" cssClass="btn btn-default">
                    <span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
                </wpsf:submit>
            </div>
        </div>
        <s:text name="note.pageTree.intro" />
    </s:form>
</div>
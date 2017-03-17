<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div class="col-xs-12  ">
    <div class="well col-md-offset-3 col-md-6  ">
        <p class="search-label"><s:text name="note.pageTree.intro" /></p>
        <s:form action="search" class="search-pf has-button " cssClass="form-horizontal" >
            <div class="form-group">
                <label class="col-sm-2 control-label" for="pageCodeToken" class="sr-only"><s:text name="label.pageCode"/></label>
                <div class="col-sm-9">
                    <wpsf:textfield name="pageCodeToken" id="pageCodeToken" value="%{pageCodeToken}" cssClass="form-control" placeholder="%{getText('label.pageCode')}" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2"></label>
                <div class="col-sm-9">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.search" />
                    </wpsf:submit>
                </div>
            </s:form>
        </div>
    </div>
</div>



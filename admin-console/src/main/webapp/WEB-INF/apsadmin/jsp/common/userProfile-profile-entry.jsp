<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:form namespace="/do/currentuser/profile" action="save" cssClass="form-horizontal">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escapeHtml="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>

	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/profile-formFields.jsp" />

            <div class="col-md-12"> 
                <div class="form-group pull-right "> 
                    <div class="btn-group">
                        <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>    

</s:form>

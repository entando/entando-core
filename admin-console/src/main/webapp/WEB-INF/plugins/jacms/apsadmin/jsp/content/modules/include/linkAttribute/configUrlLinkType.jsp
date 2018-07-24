<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="thirdTitleVar">
	<s:text name="title.configureLinkAttribute" />
</s:set>
<s:include value="linkAttributeConfigIntro.jsp" />
<s:include value="linkAttributeConfigReminder.jsp" />

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/linkAttribute/chooseLink-menu.jsp">
    <s:param name="linkTypeVar" value="1"/>
</s:include>

<s:form cssClass="action-form form-horizontal">
	<p class="sr-only"><wpsf:hidden name="contentOnSessionMarker" /></p>
	
	<p class="sr-only"><s:text name="title.insertURL" /></p>
	<s:set var="currentFieldErrorsVar" value="%{fieldErrors['url']}" />
	<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
	<s:set var="controlGroupErrorClassVar" value="%{#currentFieldHasFieldErrorVar ? ' has-error'  : ''}" />
	
	<div class="panel panel-default no-top-border">
		<div class="panel-body">
			<div class="form-group<s:property value="#controlGroupErrorClassVar" /> mt-20">
				<div class="col-xs-12">
					<label class="col-sm-2 text-right" for="url"><s:text name="label.url" />:</label>
					<div class="col-sm-10">
						<wpsf:textfield name="url" id="url" cssClass="form-control" />
						<span class="help help-block"><s:text name="note.typeValidURL" />
						<s:if test="#currentFieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical">
								<s:iterator value="#currentFieldErrorsVar"><s:property />&#32;</s:iterator>
							</p>
						</s:if>
					</div>
				</div>
			</div>

        	<!-- Link attributes -->
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/entando-link-attributes.jsp" />

			<div class="form-group">
                <div class="col-xs-12">
					<div class="col-sm-11 col-sm-offset-1 text-right">
						<a href="${contentEntryURL}" title="<s:text name="label.cancel" />" class="btn btn-default mr-10">
						   <s:text name="label.cancel" />
						</a>
						<wpsf:submit type="button" action="joinUrlLink" cssClass="btn btn-primary">
							<s:text name="label.save" />
						</wpsf:submit>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:form>

<jsp:include page="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/link-attributes-autocomplete.jsp" />

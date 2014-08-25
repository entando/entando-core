<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="thirdTitleVar">
	<s:text name="title.configureLinkAttribute" />
</s:set>
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/linkAttribute/linkAttributeConfigIntro.jsp" />
<s:form action="configLink" cssClass="action-form form-horizontal">
	<p class="sr-only"><wpsf:hidden name="contentOnSessionMarker" /></p>
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h4>
			<ul class="margin-none margin-base-top">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:set var="linkDestinations" value="linkDestinations" />
		<div class="form-group">
			<div class="col-xs-12">
				<label class="display-block"><s:text name="title.chooseLinkType" /></label>
				<div class="btn-group" data-toggle="buttons">
					<s:iterator value="#linkDestinations" var="typeId">
						<s:if test="#typeId != 4">
							<s:if test="#typeId == 1">
								<s:set var="statusIconVar">icon fa fa-globe</s:set>
								<s:set name="linkDestination" value="%{getText('note.URLLinkTo')}" />
							</s:if>
							<s:elseif test="#typeId == 2">
								<s:set var="statusIconVar">icon fa fa-folder</s:set>
								<s:set name="linkDestination" value="%{getText('note.pageLinkTo')}" />
							</s:elseif>
							<s:elseif test="#typeId == 3 || #typeId == 4">
								<s:set var="statusIconVar">icon fa fa-file-text-o</s:set>
								<s:set name="linkDestination" value="%{getText('note.contentLinkTo')}" />
							</s:elseif>
							<label class="btn btn-default <s:if test="#typeId == symbolicLink.destType || (symbolicLink.destType == 4 && #typeId == 3)"> active </s:if>" for="linkType_<s:property value="#typeId"/>">
								<input
									type="radio"
									<s:if test="#typeId == symbolicLink.destType || (symbolicLink.destType == 4 && #typeId == 3)"> checked="checked" </s:if>
									name="linkType"
									id="linkType_<s:property value="#typeId"/>"
									value="<s:property value="#typeId"/>" />
								 <span class="<s:property value="#statusIconVar" />"></span>&#32;<s:property value="linkDestination" />
							</label>
						</s:if>
					</s:iterator>
				</div>
				<div class="help help-block">
					<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/linkAttribute/linkAttributeConfigReminder.jsp"/>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" title="%{getText('label.continue')}" cssClass="btn btn-primary btn-block">
					<s:text name="label.continue" />&#32;
					<span class="icon fa fa-long-arrow-right"></span>
				</wpsf:submit>
			</div>
		</div>
</s:form>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<s:set var="currentResource" value="#attribute.resources[#lang.code]" />
<s:set var="defaultResource" value="#attribute.resource" />

<%-- default language --%>
<s:if test="#lang.default">
	<%-- resource filled --%>
		<s:if test="#currentResource != null">
			<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
				<div class="panel panel-default margin-small-top">
				<%-- remove resource button --%>
					<div class="panel-heading text-right">
						<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/removeResourceSubmit.jsp">
							<s:param name="resourceTypeCode">Image</s:param>
						</s:include>
					</div>
			</s:if>
			<div class="row panel-body">
				<%-- download --%>
					<div class="col-xs-12 col-sm-3 col-lg-2 text-center">
						<a href="<s:property value="#defaultResource.getImagePath('0')" />" title="<s:text name="label.img.original" />">
							<img class="img-thumbnail" src="<s:property value="#defaultResource.getImagePath('1')"/>" alt="<s:property value="#defaultResource.descr"/>" style="height:90px; max-width:130px" />
						</a>
					</div>
				<%-- label and input --%>
					<div class="col-xs-12 col-sm-9 col-lg-10 form-horizontal margin-large-top">
				  	<div class="form-group">
							<label class="col-xs-2 control-label text-right" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
								<abbr title="<s:text name="label.img.text.long" />"><s:text name="label.img.text.short" /></abbr>
							</label>
							<div class="col-xs-10">
								<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
							</div>
						</div>
					</div>
			</div>
			<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
				</div>
			</s:if>
		</s:if>
	<%-- resource empty --%>
		<s:else>
			<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/chooseResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Image</s:param>
			</s:include>
		</s:else>
</s:if>
<%-- Not-default lang --%>
	<s:else>
		<%-- empty resource --%>
			<s:if test="#defaultResource == null">
				<span class="form-control-static text-info"><s:text name="note.editContent.doThisInTheDefaultLanguage" />.</span>
			</s:if>
			<s:else>
				<s:set var="currentResourceIsEmptyVar" value="%{false}" />
				<s:set var="langResourceVar" value="#currentResource" />
				<s:if test="#currentResource == null">
					<s:set var="langResourceVar" value="#defaultResource" />
					<s:set var="currentResourceIsEmptyVar" value="%{true}" />
				</s:if>
				<s:set var="attributeIsNestedVar" value="%{
					(#attributeTracer.monoListElement && #attributeTracer.compositeElement)
					||
					(#attributeTracer.monoListElement==true && #attributeTracer.compositeElement==false)
					||
					(#attributeTracer.monoListElement==false && #attributeTracer.compositeElement==true)
				}" />
				<%-- attributeIsNestedVar: <s:property value="#attributeIsNestedVar" /><br /> --%>
				<s:if test="!#attributeIsNestedVar">
					<div class="panel panel-default margin-small-top">
				</s:if>
					<div class="<s:if test="#attributeIsNestedVar">pull-right margin-none</s:if><s:else>panel-heading text-right</s:else>">
						<%-- choose resource button --%>
						<s:if test="#currentResourceIsEmptyVar">
							<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/chooseResourceSubmit.jsp">
								<s:param name="resourceTypeCode">Image</s:param>
								<s:param name="buttonCssClass">btn btn-default btn-xs</s:param>
							</s:include>
						</s:if>
						<s:else>
						<%-- remove resource button --%>
							<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/removeResourceSubmit.jsp">
								<s:param name="resourceTypeCode">Image</s:param>
							</s:include>
						</s:else>
					</div><%-- pull-righ / panel-heading end --%>
					<div class="row panel-body">
						<%-- download icon + button --%>
							<div class="col-xs-12 col-sm-3 col-lg-2 text-center">
								<a href="<s:property value="#langResourceVar.getImagePath('0')" />" title="<s:text name="label.img.original" />">
									<img class="img-thumbnail" src="<s:property value="#langResourceVar.getImagePath('1')"/>" alt="<s:property value="#currentResource.descr"/>" style="height:90px; max-width:130px" />
								</a>
							</div>
						<%-- text field --%>
							<div class="col-xs-12 col-sm-9 col-lg-10 form-horizontal margin-large-top">
								<div class="form-group">
									<label class="col-xs-2 control-label text-right" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
										<s:text name="label.text" />
									</label>
									<div class="col-xs-10">
										<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
									</div>
								</div>
							</div>
					</div>
				<s:if test="!#attributeIsNestedVar">
					</div>
				</s:if>
			</s:else>
	</s:else>
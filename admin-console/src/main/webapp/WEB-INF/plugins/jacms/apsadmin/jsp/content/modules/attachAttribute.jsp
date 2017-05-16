<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<s:set var="currentResource" value="#attribute.resources[#lang.code]" />
<s:set var="defaultResource" value="#attribute.resource" />

<%-- default language --%>
<s:if test="#lang.default">
	<%-- resource filled --%>
		<s:if test="#currentResource != null">
            <s:set var="divClass" value="'no-padding'"/>
            <s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
                <div class="panel panel-default margin-small-top">
                <s:set var="divClass" value="''"/>
            </s:if>
            <div class="panel-body ${divClass}">
				    <%-- download --%>
					<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2 text-center">
						<a href="<s:property value="#defaultResource.attachPath" />" 
						  title="<s:text name="label.download" />:&#32;<s:property value="#defaultResource.descr"/>"
						  class="display-block mt-20 mb-20">
							<span class="icon fa fa-file-text fa-5x fa-lg" ></span>
							<span class="sr-only"><s:text name="label.download" />:&#32;
                                <s:property value="#defaultResource.descr"/>
                            </span>
						</a>
					</div>
				    <%-- label and input --%>
					<div class="col-xs-12 col-sm-8 col-md-9 col-lg-10 form-horizontal margin-large-top">
						<div class="form-group">
							<div class="col-xs-12">
							<p>
								<strong><s:text name="label.description" /></strong>:&nbsp;
								<s:property value="#defaultResource.description" />
                                <br>
	                            <strong><s:text name="label.filename" /></strong>:&nbsp;
	                            <s:property value="#defaultResource.masterFileName" />
	                        </p>
							<label class="col-lg-1 col-md-2 col-sm-3 no-padding text-right pr-10" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
   								<s:text name="label.text" />:
							</label>
							<div class="col-lg-11 col-md-10 col-sm-9 no-padding">
							    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
							</div>
							</div>
						</div>
						
			            <s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
		                    <div class="text-right">
		                        <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/removeResourceSubmit.jsp">
		                            <s:param name="resourceTypeCode">Attach</s:param>
		                        </s:include>
		                    </div>
			            </s:if>

					</div>
			</div>
			<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
				</div>
			</s:if>
		</s:if>
	<%-- resource empty --%>
		<s:else>
			<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/chooseResourceSubmit.jsp">
				<s:param name="resourceTypeCode">Attach</s:param>
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
					<div class="row panel-body">
						<%-- download icon + button --%>
							<div class="col-xs-12 col-sm-3 col-lg-2 text-center">
								<a href="<s:property value="#langResourceVar.attachPath" />" title="<s:text name="label.download" />:&#32;<s:property value="#langResourceVar.descr"/>">
									<span class="icon fa fa-file-text fa-5x fa-lg <s:if test="#currentResourceIsEmptyVar"> text-muted </s:if>"></span>
									<span class="sr-only"><s:text name="label.download" />:&#32;<s:property value="#langResourceVar.descr"/></span>
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
								
		                        <%-- choose resource button --%>
		                        <div class="text-right">
		                        <s:if test="#currentResourceIsEmptyVar">
		                            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/chooseResourceSubmit.jsp">
		                                <s:param name="resourceTypeCode">Attach</s:param>
		                                <s:param name="buttonCssClass">btn btn-default</s:param>
		                            </s:include>
		                        </s:if>
		                        <s:else>
		                        <%-- remove resource button --%>
		                            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/removeResourceSubmit.jsp">
		                                <s:param name="resourceTypeCode">Attach</s:param>
		                            </s:include>
		                        </s:else>
		                        </div>
							</div>
					</div>
				<s:if test="!#attributeIsNestedVar">
					</div>
				</s:if>
			</s:else>
	</s:else>

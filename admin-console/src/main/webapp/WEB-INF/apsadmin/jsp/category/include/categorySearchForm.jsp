<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<%-- COMUNE ROMA - AGGIUNTA RICERCA PER CODICE CATEGORIA --%>

<s:form action="search" cssClass="form-horizontal">
	
	<div class="form-group">
		<label for="categoryCodeToken" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="name.categoryCode"/></label>
		<div class="input-group col-sm-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg"></span>
			</span>
			<wpsf:textfield name="categoryCodeToken" id="categoryCodeToken" value="%{categoryCodeToken}" cssClass="form-control input-lg" placeholder="%{getText('label.categoryCode')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" cssClass="btn btn-primary">
					<span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
				</wpsf:submit>
			</div>
		</div>
	</div>
	
</s:form>
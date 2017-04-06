<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure"/></a></li>
	<li>
		<a href="<s:url namespace="/do/LocaleString" action="list" />">
			<s:text name="title.languageAndLabels" />
		</a>
	</li>
	<li class="page-title-container">
		<s:text name="title.generalSettings.locale.delete" />
	</li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:if test="getStrutsAction() == 1"><s:text name="title.generalSettings.locale.new" /></s:if>
		<s:elseif test="getStrutsAction() == 2"><s:text name="title.generalSettings.locale.edit" /> </s:elseif>
		<span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
			   data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
	</div>
</h1>
<br>


<div class="text-center">
	<s:form action="delete">

		<p class="sr-only"><wpsf:hidden name="selectedNode"/></p>

		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline"><s:text name="label.delete"/></p>
		<p>
			<s:text name="label.delete.confirm"/>&#32;
			<s:property value="key" />&#63;

		</p>
		<div class="btn btn-danger button-fixed-width">
			<a class="btn-danger button-fixed-width"
			   href="<s:url action="delete" namespace="/do/LocaleString"><s:param name="key" value="key" /></s:url>"
			   title="<s:text name="label.remove" />: <s:property value="key" />">
				&#32;<s:text name="label.delete"/>
			</a>
		</div>


		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width" href="<s:url namespace="/do/LocaleString" action="list" />">
				<s:text name="title.languageAndLabels" />
			</a>
		</div>

	</s:form>
</div>

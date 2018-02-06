<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />
<s:if test="htmlEditorCode == 'fckeditor'">
	<script type="text/javascript" src="<wp:resourceURL />administration/js/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="<wp:resourceURL />administration/js/ckeditor/adapters/jquery.js"></script>
</s:if>
<script>
$(function() {
	$('[data-toggle="popover"]').popover();

	<s:if test="htmlEditorCode != 'none'">
		//Hypertext Attribute
		$('[data-toggle="entando-hypertext"]').ckeditor({
			customConfig : '<wp:resourceURL />administration/js/ckeditor/entando-ckeditor_config_nolink.js',
			EntandoLinkActionPath: "<s:url namespace="/do/jacms/Content/Hypertext" action="entandoInternalLink"><s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>",
			language: '<s:property value="locale" />'
		});
	</s:if>
});
</script>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<script>
	var Entando = Entando || {};
	Entando.backoffice = Entando.backoffice || {};
	Entando.backoffice.stream = {};
	Entando.backoffice.stream.list = {};
	Entando.backoffice.stream.list.updateUrl = '<s:url action="update" namespace="/do/ActivityStream" />';
	Entando.backoffice.stream.list.loadMoreUrl = '<s:url action="viewMore" namespace="/do/ActivityStream" />';
	Entando.backoffice.stream.comments = {};
	Entando.backoffice.stream.comments.addUrl = '<s:url action="addComment" namespace="/do/ActivityStream" />';
	Entando.backoffice.stream.comments.deleteUrl = '<s:url action="removeComment" namespace="/do/ActivityStream" />';
</script>
<script>
	jQuery(function(){
		$('#activity-stream [data-toggle="tooltip"]').tooltip({trigger: 'hover'});
	})
</script>
<script src="<wp:resourceURL />administration/js/entando-stream.js"></script>
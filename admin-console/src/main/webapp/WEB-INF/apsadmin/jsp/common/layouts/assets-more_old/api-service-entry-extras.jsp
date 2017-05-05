<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<script>
jQuery(function() {
	var permission = $("#requiredPermission");
	var group = $("#requiredGroup");
	var registered =  $("#requiredAuth");
	$.each([permission, group], function(index, item){
		item.on('change', function(ev) {
			var value= $(ev.target).val();
			if (value!="") {
				registered.prop('checked',true);
			}
		});
	});
	registered.on('change', function(ev) {
		if (!ev.target.checked) {
			permission.val('');
			group.val('');
		}
	});
});
</script>
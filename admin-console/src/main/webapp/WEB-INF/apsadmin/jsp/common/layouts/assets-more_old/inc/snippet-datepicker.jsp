<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<script src="<wp:resourceURL />administration/js/bootstrap-datepicker/bootstrap-datepicker.js"></script>
<link href="<wp:resourceURL />administration/css/bootstrap-datepicker.css" rel="stylesheet" />

<script>
	//one domready to rule 'em all
	$(function() {
		/* TODO
			 see: https://github.com/eternicode/bootstrap-datepicker
			 for options and proper i18n
		*/
		var picked = $(".datepicker").datepicker({
			format: "dd/mm/yyyy"
		});
	});
</script>

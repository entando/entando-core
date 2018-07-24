<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<script>

var attributeRelValues = [
	"alternate",
    "author",
    "bookmark",
    "external",
    "help",
    "license",
    "next",
    "nofollow",
    "noreferrer",
    "noopener",
    "prev",
    "search",
    "tag"
];
$( "#linkAttributeRel" ).autocomplete({
	source: attributeRelValues
});



var attributeTargetValues = [
	"_blank",
    "_parent",
    "_self",
    "_top"
];
$( "#linkAttributeTarget" ).autocomplete({
	source: attributeTargetValues
});
</script>

<%@ taglib uri="/aps-core" prefix="wp" %>
<wp:info key="startLang" var="startLangCodeVar" />
<wp:info key="systemParam" paramName="homePageCode" var="homepageCodeVar" />
<jsp:forward page="/${startLangCodeVar}/${homepageCodeVar}.page"/>
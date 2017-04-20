<%@ taglib prefix="s" uri="/struts-tags" %>
<span class="label label-default"><s:property value="frame" /></span>&#32;<s:property value="currentPage.getDraftMetadata().getModel().getFrames()[frame]"/>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h2><s:text name="title.reload.resources.instance" /></h2>
<p>
	<s:text name="note.resources.images.instances" />
</p>

<s:if test="resourceManagerStatus == 2">
	<p>
		<a href="<s:url action="list" namespace="/do/jacms/Resource"><s:param name="resourceTypeCode" value="resourceTypeCode"></s:param></s:url>" ><s:text name="label.instances.status.wip" /></a>
	</p>
</s:if>
<s:else>
	<p>
		<a href="<s:url action="refreshResourcesInstances" namespace="/do/jacms/Resource/Admin"><s:param name="resourceTypeCode" value="resourceTypeCode"></s:param></s:url>"><s:text name="label.instances.status.ok" /></a>
	</p>
</s:else>
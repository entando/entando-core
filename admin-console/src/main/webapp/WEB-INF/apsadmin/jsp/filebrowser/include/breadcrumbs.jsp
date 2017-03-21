<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set value="breadCrumbsTargets" var="breadCrumbsTargetsVar" />
<p class="sr-only">You are here:</p>
<ol class="breadcrumb margin-none">
	<s:if test="null != #breadCrumbsTargetsVar">
		<li>
			<a href="<s:url namespace="/do/FileBrowser" action="list" />">root</a>
		</li>
		<s:iterator value="#breadCrumbsTargetsVar" var="targetVar" status="statusVar">
			<s:if test="%{0 == #statusVar.index}">
				<s:if test="#targetVar.value.equals('protected')"><s:set var="isProtectedBrCrDirectoryVar" value="'true'" /></s:if>
				<s:else><s:set var="isProtectedBrCrDirectoryVar" value="'false'" /></s:else>
			</s:if>
			<li>
				<s:if test="!#statusVar.last">
					<a href="<s:url namespace="/do/FileBrowser" action="list" >
							<s:param name="currentPath"><s:property escapeHtml="true" value="#targetVar.key"/></s:param>
							<s:param name="protectedFolder"><s:property value="#isProtectedBrCrDirectoryVar"/></s:param>
						</s:url>"><s:property value="#targetVar.value" />
					</a>
				</s:if>
				<s:else>
					<s:property escapeHtml="false" value="#targetVar.value" />
				</s:else>
			</li>
		</s:iterator>
	</s:if>
	<s:else>
		<li>
			root
		</li>
	</s:else>
</ol>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<button type="button" data-toggle="collapse" data-target="#page-info" class="btn btn-sm btn-primary margin-large-bottom">
    <span class="icon fa fa-chevron-down"></span>&nbsp;&nbsp;
    <span class="info-title"><s:text name="label.info" /></span>
</button>

<s:set var="details_pivotPage" value="pageToShow" />
<div class="collapse" id="page-info">

<table class="table">
        <tr>
            <th class="td-pagetree-width"><s:text name="name.categoryCode" /></th>
            <td><s:property value="categoryCode" /></td>
        </tr>
        <tr>
            <th class="td-pagetree-width"><s:text name="name.categoryTitle" /></th>
            <td>
                <s:iterator value="langs" status="categoryInfo_rowStatus" var="lang">
                    <s:if test="#categoryInfo_rowStatus.index != 0">, </s:if> <span class="lang-chips mr-5"><span title="<s:property value="descr" />"><s:property value="code" /></span></span> 
                    <s:property value="titles[#lang.code]" />
                    <s:if test="%{titles[#lang.code] == null}">
                        <abbr title="<s:text name="label.none" />">&ndash;</abbr>
                    </s:if>
                </s:iterator>
            </td>
        </tr>
    </table>
</div>


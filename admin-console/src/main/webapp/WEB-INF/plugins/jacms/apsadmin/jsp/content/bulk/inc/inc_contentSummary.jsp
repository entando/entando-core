<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<s:set var="summary" value="summary" />

<div class="container-fluid container-cards-pf">
	<div class="row row-cards-pf">
		<div class="col-xs-6 col-sm-4 col-md-4">
			<div class="card-pf card-pf-accented card-pf-aggregate-status">
				<h2 class="card-pf-title">
					<span class="fa fa-circle green"></span>
					<span class="card-pf-aggregate-status-count">
					   <s:property value="#summary.aligned.size()"/>&#32;
					</span>
					<s:text name="note.summary.aligned" />
				</h2>
				<div class="card-pf-body">
                    <s:if test="%{selectedIds.size() < 10}" >
					<ul class="no-padding">
		            <s:iterator var="contentId" value="#summary.aligned" >
		                <jacmswpsa:content contentId="${contentId}" var="content" workVersion="true" />
		                <li><s:property value="%{#content.description}" /></li>
		            </s:iterator>
					</ul>
					</s:if>
				</div>
			</div>
		</div>
		<div class="col-xs-6 col-sm-4 col-md-4">
			<div class="card-pf card-pf-accented card-pf-aggregate-status">
                <h2 class="card-pf-title">
                    <span class="fa fa-circle yellow"></span>
                    <span class="card-pf-aggregate-status-count">
                       <s:property value="#summary.workAhead.size()"/>&#32;
                    </span>
                    <s:text name="note.summary.workAhead" />
                </h2>
				<div class="card-pf-body">
		        <s:if test="%{selectedIds.size() < 10}" >
		            <ul class="no-padding">
		            <s:iterator var="contentId" value="#summary.workAhead" >
		                <jacmswpsa:content contentId="${contentId}" var="content" workVersion="true" />
		                <li><s:property value="%{#content.description}" /></li>
		            </s:iterator>
		            </ul>
		        </s:if>
				</div>
			</div>
		</div>
		<div class="col-xs-6 col-sm-4 col-md-4">
			<div class="card-pf card-pf-accented card-pf-aggregate-status">
                <h2 class="card-pf-title">
                    <span class="fa fa-circle gray"></span>
                    <span class="card-pf-aggregate-status-count">
                       <s:property value="#summary.notOnline.size()"/>&#32;
                    </span>
                    <s:text name="note.summary.notOnline" />
                </h2>
				<div class="card-pf-body">
		        <s:if test="%{selectedIds.size() < 10}" >
		            <ul class="no-padding">
		            <s:iterator var="contentId" value="#summary.notOnline" >
		                <jacmswpsa:content contentId="${contentId}" var="content" workVersion="true" />
		                <li><s:property value="%{#content.description}" /></li>
		            </s:iterator>
		            </ul>
		        </s:if>
				</div>
			</div>

		</div>
	</div>
</div>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.uxPatterns" /></li>
    <li class="page-title-container">
        <s:text name="title.pageModelManagement" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="title.pageModelManagement" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="TO be inserted" data-placement="left" data-original-title=""><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main">
    <s:form action="save" cssClass="form-horizontal">
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.ActionErrors" />
                <ul class="margin-base-top">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.FieldErrors" />
                <ul class="margin-base-top">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <p class="sr-only">
            <wpsf:hidden name="strutsAction" />
            <s:if test="getStrutsAction() == 2">
                <wpsf:hidden name="code" />
            </s:if>
        </p>
        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label" for="key"><s:text name="label.code" /></label>
                <div class="col-sm-10">
                    <wpsf:textfield name="code" id="code" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label" for="description"><s:text name="label.name" /></label>
                <div class="col-sm-10">
                    <wpsf:textfield name="description" id="description" cssClass="form-control" />
                </div>
            </div>
        </div>
        <s:if test="getStrutsAction() == 2 && null != pluginCode">
            <div class="form-group">
                <div class="col-xs-12">
                    <label class="col-sm-2 control-label" for="plugin">Plugin</label>
                    <div class="col-sm-10">
                        <s:text name="%{pluginCode+'.name'}" />
                    </div>
                </div>
            </div>
        </s:if>
        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label" for="xmlConfiguration">Xml Configuration</label>
                <div class="col-sm-10">
                    <wpsf:textarea name="xmlConfiguration" id="xmlConfiguration" cssClass="autotextarea form-control" rows="8" />
                    <textarea id="ace_xmlConfiguration" style="display: none;" ></textarea>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label" for="template">Template</label>
                <div class="col-sm-10">
                    <wpsf:textarea name="template" id="template" cssClass="autotextarea form-control" rows="8" />
                    <textarea id="ace_template" style="display: none;" ></textarea>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <label class="col-sm-2 control-label">Template Preview</label>
                <!-- the grid (or alert) will be appended here -->
                <div class="col-sm-10">
                    <div class="grid-container"></div>
                </div>

            </div>
        </div>

        <div class="col-xs-12">
            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save" />
            </wpsf:submit>
        </div>

    </s:form>
</div>

<script>
    jQuery(function () {
        $('.autotextarea').on('focus', function (ev) {
            var t = $(this);
            var h = screen.availHeight;
            if (h) {
                t.css('height', (h / 2) + 'px');
            }
        });
    })
</script>

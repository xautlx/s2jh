<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static-ver.jsp"%>

<script src="${base}/components/jquery-util/jquery.bgiframe.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-util/jquery.form.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-util/jquery.metadata.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-util/jquery.highlight.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-util/jquery.blockUI.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery-utils.js?_=${buildVersion}"></script>


<script src="${base}/components/jquery-ztree/3.5/js/jquery.ztree.all-3.5.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery-treeinput.js?_=${buildVersion}"></script>

<script
	src="${base}/components/jquery.jqGrid/4.5.2/plugins/ui.multiselect.js?_=${pageScope.buildVersion}"></script>
<script src="${base}/components/jquery.jqGrid/4.5.2/js/i18n/grid.locale-cn.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery.jqGrid/4.5.2/js/jquery.jqGrid.src.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery.jqGrid.4.5.2-ext.js?_=${buildVersion}"></script>
<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery.jqGrid/4.5.2/plugins/ui.multiselect.css?_=${pageScope.buildVersion}">

<script src="${base}/components/jquery-ext/jquery-ui-dialog-ext.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery-ui-tabs-ext.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery-comboselect.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery-combotext.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery.dynamictable.js?_=${buildVersion}"></script>

<script src="${base}/components/jquery-validation/1.10.0/jquery.validate.js?_=${buildVersion}"></script>
<script
	src="${base}/components/jquery-validation/1.10.0/jquery.maxlength-1.0.2.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-validation/1.10.0/messages_zh.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/jquery-validation-ext.js?_=${buildVersion}"></script>

<script src="${base}/components/jquery.pagination/2.2/jquery.pagination.js?_=${buildVersion}"></script>

<script src="${base}/components/zebra-dialog/1.2/javascript/zebra_dialog.js?_=${buildVersion}"></script>

<script src="${base}/components/kindeditor/4.1.7/kindeditor-all.js?_=${buildVersion}"></script>

<!-- Pines Notify -->
<script src="${base}/components/jquery.pnotify/1.2.2/jquery.pnotify.js?_=${buildVersion}"></script>

<script type="text/javascript"
	src="${base}/components/My97DatePicker/4.8.Beta2/WdatePicker.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ext/DatePicker-ext.js?_=${buildVersion}"></script>

<script src="${base}/components/jquery-layout/1.2.0/js/jquery.layout-1.2.0.js?_=${buildVersion}"></script>
<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery-layout/1.2.0/css/jquery.layout.css?_=${buildVersion}">
<script src="${base}/components/jquery-ext/jquery-layout-ext.js?_=${buildVersion}"></script>

<script src="${base}/components/jquery.MultiFile/1.48/jquery.MultiFile.js?_=${buildVersion}"></script>

<script
	src="${base}/components/jQuery-File-Upload/8.6.0/js/jquery.iframe-transport.js?_=${buildVersion}"></script>
<script src="${base}/components/jQuery-File-Upload/8.6.0/js/jquery.fileupload.js?_=${buildVersion}"></script>
<link rel="stylesheet" type="text/css"
	href="${base}/components/jQuery-File-Upload/8.6.0/css/jquery.fileupload-ui.css?_=${buildVersion}">

<script src="${base}/components/js-util/pinyin.js?_=${buildVersion}"></script>

<script src="${base}/components/bootstrap/2.2.2/js/bootstrap-carousel.js?_=${buildVersion}"></script>
<script src="${base}/components/bootstrap/2.2.2/js/bootstrap-tooltip.js?_=${buildVersion}"></script>

<script src="${base}/components/bootstrap.wizard/1.0/bootstrap-wizard.js?_=${buildVersion}"></script>
<link rel="stylesheet" type="text/css"
	href="${base}/components/bootstrap.wizard/1.0/bootstrap-wizard.css?_=${buildVersion}">

<script src="${base}/components/jquery.cascade/1.1.1/jquery.cascade.js?_=${pageScope.buildVersion}"
	type="text/javascript"></script>
<script
	src="${base}/components/jquery.cascade/1.1.1/jquery.cascade.ext.js?_=${pageScope.buildVersion}"
	type="text/javascript"></script>
<script
	src="${base}/components/jquery.cascade/1.1.1/jquery.templating.js?_=${pageScope.buildVersion}"
	type="text/javascript"></script>

<script src="${base}/resources/js/biz.js?_=${buildVersion}"></script>

<script type="text/javascript">
    $(function() {

        $.ajaxSetup({
            cache : false,
            timeout : 300000,//超时时间：60秒
            error : function(xhr, textStatus, errorThrown) {
                //alert("textStatus="+textStatus+";xhr.status="+xhr.status+";errorThrown="+errorThrown);
                top.stopProgressBar();
                if (textStatus == 'timeout') {
                    alert("服务请求超时，请尝试重新操作或重新登录访问!");
                } else {
                    if (xhr.status == 404) {
                        alert("404:请求地址未找到,请尝试刷新重试或联系管理员!");
                        return;
                    }
                    var response = jQuery.parseJSON(xhr.responseText);
                    if (response.type == "error") {
                        top.$.publishError(response.message);
                    } else {
                        top.$.publishError(xhr.responseText);
                    }
                }
            }
        });

        //注册ajax Start回调逻辑，启动进度条显示
        $(document).ajaxStart(function() {
            try {
                top.startProgressBar();
            } catch (e) {
            }
        });

        //注册ajax Stop回调逻辑，终止进度条显示
        $(document).ajaxStop(function() {
            try {
                $.unblockUI();
                top.stopProgressBar();
            } catch (e) {
            }
        });

        function reloadCurrentFrame() {
            var url = window.location.href;
            url = AddOrReplaceUrlParameter(url, "_", new Date().getTime());
            window.location.href = url;
        }
    });
</script>

<%@ include file="/common/biz-footer.jsp"%>
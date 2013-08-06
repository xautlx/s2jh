<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/static-ver.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
    var WEB_ROOT = '${pageContext.request.contextPath}';
</script>

<script src="${base}/components/jquery/1.7.2/jquery.js?_=${buildVersion}"></script>
<script src="${base}/components/jquery-ui/1.10.0/js/jquery-ui-1.10.0.custom.js?_=${buildVersion}"></script>

<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery-ui/1.10.0/css/smoothness/jquery-ui-1.10.0.custom.css?_=${buildVersion}">
<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery-ztree/3.5/css/zTreeStyle/zTreeStyle.css?_=${buildVersion}">


<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery.jqGrid/4.5.2/css/ui.jqgrid.css?_=${buildVersion}">

<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery.pagination/2.2/pagination.css?_=${buildVersion}">

<link rel="stylesheet" type="text/css"
	href="${base}/components/zebra-dialog/1.2/css/zebra_dialog.css?_=${buildVersion}">

<link rel="stylesheet" type="text/css"
	href="${base}/components/kindeditor/4.1.7/themes/default/default.css?_=${buildVersion}">

<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery-ext/jquery-ui-dialog-ext.css?_=${buildVersion}">

<!-- Pines Notify -->
<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery.pnotify/1.2.2/jquery.pnotify.default.css?_=${buildVersion}">
<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery.pnotify/1.2.2/jquery.pnotify.default.icons.css?_=${buildVersion}">

<%-- Build from lesscss-maven-plugin --%>
<link rel="stylesheet" type="text/css"
	href="${base}/components/bootstrap/2.2.2/out/bootstrap.css?_=${buildVersion}">

<link rel="stylesheet" type="text/css"
	href="${base}/components/styles/default.css?_=${buildVersion}">

<link rel="stylesheet" type="text/css" href="${base}/resources/styles/biz.css?_=${buildVersion}">

<script type="text/javascript">
    var enumsContainer = {};
    <s:iterator value="#application.enums" id="item">
    var enumData = {
        "" : ""
    };
    <s:iterator value="#item.value" id="data">
    enumData['<s:property value="#data.key" />'] = '<s:property value="#data.value" />';
    </s:iterator>
    enumsContainer['<s:property value="#item.key" />'] = enumData;
    </s:iterator>
</script>

<%@ include file="/common/biz-header.jsp"%>
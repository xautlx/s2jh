<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
    if (lab.s2jh.core.service.PropertiesConfigService.isDevMode()) {
%>
<script src="${base}/assets/app/form-validation.js"></script>
<script src="${base}/assets/app/grid.js"></script>
<script src="${base}/assets/app/dynamic-table.js"></script>
<script src="${base}/assets/app/page.js"></script>
<%
    }
%>

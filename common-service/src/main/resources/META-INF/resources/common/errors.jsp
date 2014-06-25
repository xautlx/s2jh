<%@page import="lab.s2jh.core.exception.ExceptionLogger"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true"%>
<%@ include file="/common/taglibs.jsp"%>
<%
    Throwable e = exception;
    if (e == null) {
        e = (Throwable) pageContext.getAttribute("struts.exception");
    }
    if (e == null) {
        e = (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    if (e == null) {
        e = (Throwable) request.getAttribute("javax.servlet.jsp.jspException");
    }
    if (e == null) {
        e = (Throwable) request.getAttribute("struts.rest.error.exception");
    }
    String responseContentType = null;
    if (request.getRequestURI().endsWith(".json")) {
        responseContentType = "json";
    }
    if (responseContentType == null) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("json") > -1) {
            responseContentType = "json";
        }
    }

    String errorMessage = ExceptionLogger.logForHttpRequest(e, request);

    if ("json".equals(responseContentType)) {
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\":\"error\"");
        sb.append(",\"message\":\"" + errorMessage + "\"");
        Object statusCode = request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            sb.append(",\"status_code\":\"" + statusCode + "\"");
        }
        sb.append("}");
        out.print(sb.toString());
    } else {
        out.print(errorMessage);
    }
%>

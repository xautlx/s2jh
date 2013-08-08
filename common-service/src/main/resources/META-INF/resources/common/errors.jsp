<%@page import="java.text.SimpleDateFormat"%>
<%@page import="lab.s2jh.core.util.DateUtils"%>
<%@page import="lab.s2jh.core.security.AuthContextHolder"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.apache.commons.lang3.time.DateFormatUtils"%>
<%@page import="org.apache.commons.lang3.RandomStringUtils"%>
<%@page import="org.apache.commons.lang3.BooleanUtils"%>
<%@page import="org.slf4j.MDC"%>
<%@page import="lab.s2jh.core.web.interceptor.ExtTokenInterceptor"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true"%>
<%
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("lab.s2jh.errors");
    //异常情况移除用于控制重复提交的Token记录，使得用户可以再次提交
    //@see lab.s2jh.core.web.interceptor.ExtTokenInterceptor
    session.removeAttribute(ExtTokenInterceptor.TOKEN_COUNTER);

    Throwable e = exception;
    if (e == null) {
        e = (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    if (e == null) {
        e = (Throwable) request.getAttribute("javax.servlet.jsp.jspException");
    }

    String rand = DateFormatUtils.format(new java.util.Date(), "yyyyMMddHHmmss")
            + RandomStringUtils.randomNumeric(3);
    boolean skipLog = false;
    String errorTitle = "ERR" + rand + ": ";
    String errorMessage = errorTitle + "系统运行错误，请联系管理员！";
    if (e instanceof lab.s2jh.core.exception.DuplicateTokenException) {
        errorMessage = "请勿快速重复提交表单";
    } else if (e instanceof lab.s2jh.core.exception.BaseRuntimeException) {
        errorMessage = errorTitle + e.getMessage();
    } else if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
        org.springframework.dao.DataIntegrityViolationException dive = (org.springframework.dao.DataIntegrityViolationException) e;
        if (dive.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            org.hibernate.exception.ConstraintViolationException cve = (org.hibernate.exception.ConstraintViolationException) dive
                    .getCause();
            if (cve.getCause() instanceof java.sql.SQLException) {
                java.sql.SQLException sqle = (java.sql.SQLException) cve.getCause();
                String sqlMessage = sqle.getMessage();
                if (sqlMessage != null && (sqlMessage.indexOf("FK") > -1 || sqlMessage.startsWith("ORA-02292"))) {
                    errorMessage = "该数据已被关联使用，请求的操作无效。若有疑问请联系系统管理员。";
                    skipLog = true;
                }
            }
        }
    } else if (e instanceof java.lang.IllegalArgumentException) {
        errorMessage = e.getMessage();
        skipLog = true;
    } else {
        if (request.getAttribute("SPRING_SECURITY_403_EXCEPTION") != null) {
            errorMessage = "访问权限不足，请联系管理员。";
            skipLog = true;
        }
    }
    if (!skipLog) {
        StringBuilder sb = new StringBuilder();

        sb.append("***Request Header Data:***\n");
        java.util.Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            sb.append(" - " + headerName + "=" + request.getHeader(headerName) + "\n");
        }

        sb.append("***Request Attribute Data:***\n");
        java.util.Enumeration attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = (String) attrNames.nextElement();
            Object attr = request.getAttribute(attrName);
            if (attr != null && attr.toString().length() > 100) {
                sb.append(" - " + attrName + "=" + attr.toString().substring(0, 100) + "...\n");
            } else {
                sb.append(" - " + attrName + "=" + attr + "...\n");
            }
        }

        sb.append("***Session Attribute Data:***\n");
        java.util.Enumeration sessionAttrNames = session.getAttributeNames();
        while (sessionAttrNames.hasMoreElements()) {
            String attrName = (String) sessionAttrNames.nextElement();
            Object attr = session.getAttribute(attrName);
            if (attr != null && attr.toString().length() > 100) {
                sb.append(" - " + attrName + "=" + attr.toString().substring(0, 100) + "...\n");
            } else {
                sb.append(" - " + attrName + "=" + attr + "...\n");
            }
        }

        sb.append("***Request Parameter Data:***\n");
        java.util.Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = StringUtils.join(request.getParameterValues(paramName), ",");
            if (paramValue != null && paramValue.length() > 100) {
                sb.append(" - " + paramName + "=" + paramValue.substring(0, 50) + "...\n");
            } else {
                sb.append(" - " + paramName + "=" + paramValue + "\n");
            }
        }

        String userId = AuthContextHolder.getAuthUserPin();
        if (StringUtils.isNotBlank(userId)) {
            MDC.put("USER_ID", userId);
        }
        MDC.put("LOG_DATETIME",
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date()));
        MDC.put("WEB_DATA", sb.toString());

        logger.error(errorMessage, e);

        MDC.clear();
    }

    String responseContentType = null;
    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
    if (requestUri == null) {
        requestUri = (String) request.getAttribute("javax.servlet.forward.request_uri");
    }
    if (requestUri == null) {
        requestUri = request.getRequestURI();
    }
    if (requestUri.endsWith(".json")) {
        responseContentType = "json";
    }
    if (responseContentType == null) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("json") > -1) {
            responseContentType = "json";
        }
    }

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
        out.print("<script type='text/javascript'>alert('" + errorMessage + "')</script>");
    }
%>

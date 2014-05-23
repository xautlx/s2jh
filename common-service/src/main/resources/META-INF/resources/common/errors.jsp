<%@page import="lab.s2jh.core.exception.ExceptionLogger"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true"%>
<%@ include file="/common/taglibs.jsp"%>
<s:set value="exception" name="struts.exception" scope="page" />
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

    out.print(ExceptionLogger.logForHttpRequest(e, request));
%>

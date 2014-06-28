<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>报表打印/导出预览</title>
</head>
<body>
	<div style="text-align: right"><a href="${base}/pub/applet/install.jsp" target="_blank"> 如果无法正常显示报表组件，请点击查看配置说明</a></div>
	<APPLET WIDTH="100%" HEIGHT="100%">
		<PARAM NAME=CODE VALUE="net.sf.jasperreports.swing.PrintViewerApplet.class">
		<PARAM NAME=CODEBASE VALUE="${base}/pub/applet">
		<PARAM NAME=ARCHIVE
			VALUE="
        commons-collections-3.2.1.jar
        ,commons-digester-2.0.jar
        ,commons-lang3-3.1.jar
        ,commons-logging-1.0.4.jar
        ,jasperreports-5.2.0.jar
        ,barcode4j-2.0.jar
        ,batik-anim-1.7.jar
        ,batik-awt-util-1.7.jar
        ,batik-bridge-1.7.jar
        ,batik-css-1.7.jar
        ,batik-dom-1.7.jar
        ,batik-ext-1.7.jar
        ,batik-gvt-1.7.jar
        ,batik-js-1.7.jar
        ,batik-parser-1.7.jar
        ,batik-script-1.7.jar
        ,batik-svg-dom-1.7.jar
        ,batik-svggen-1.7.jar
        ,batik-util-1.7.jar
        ,batik-xml-1.7.jar
        ,jasperreports-extension-1.0.0.jar
        ,xercesImpl-2.11.0.jar
        ,xml-apis-1.4.01.jar
        ,xml-apis-ext-1.3.04.jar
        ,itext-2.1.7.jar
        ,poi-3.7.jar
">
		<PARAM NAME="type" VALUE="application/x-java-applet;version=1.6">
		<PARAM NAME="scriptable" VALUE="false">
		<PARAM NAME="REPORT_URL"
			VALUE="${base}/rpt/jasper-report!generate;jsessionid=<%=request.getSession().getId()%>?preview=true&<%= request.getQueryString() %>">
	</APPLET>
</body>
</html>
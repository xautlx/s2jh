<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<body>
	<h3>Java插件安装说明</h3>
	<p>正常情况浏览器自动检测提示安装插件，只需按照浏览器提示一步步默认安装Java插件即可;</p>
	<p>
		如果出现浏览器无提示或无法自动安装，可点击下载<a href="jre-7u55-windows-i586-iftw.exe" target="">jre-7u55-windows-i586-iftw.exe</a>安装引导程序手工运行安装
	</p>
	<APPLET width="600px" height="300px">
		<PARAM NAME=CODE VALUE="net.sf.jasperreports.swing.InstallVerifyApplet.class">
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
">
		<PARAM NAME="type" VALUE="application/x-java-applet;version=1.6">
		<PARAM NAME="scriptable" VALUE="false">
	</APPLET>
</body>
</html>


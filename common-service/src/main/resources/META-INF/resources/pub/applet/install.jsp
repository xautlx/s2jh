<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<body>
	<h3>Java插件安装说明</h3>
	<p>正常情况浏览器自动检测提示安装插件，只需按照浏览器提示一步步默认安装Java插件即可;</p>
	<p>
		如果出现浏览器无提示或无法自动安装，可自行访问<a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html"
			target="_blank">Oracle JDK 7</a>手工下载安装Java运行环境
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
	<p>
		<b>提示：以下文档和操作，目前在FireFox最新版本浏览器验证过，建议自行访问 http://www.firefox.com.cn/ 安装最新版本FireFox浏览器。</b>
	</p>
	<p>
		Java环境下载安装后，找到Java目录（默认一般为C:\Program Files (x86)\Java，如果找不到在命令行下面执行： java -verbose，最下面信息即可看出java所在目录），
		用文本编辑器打开:JAVA根目录/jre7/lib/security/java.policy ，在如下图位置添加文本：permission java.security.AllPermission; <br> <img
			alt="Java权限配置" src="images/guide_security.png" />
	</p>
	<p>
		Java环境下载安装后，参考如下图配置，1, 把安全级别调至“中”， 2, 把本地或服务器的应用访问url地址添加到“例外站点”： <br> <img alt="Java配置菜单项" src="images/guide_javaconfig.png" /> <img
			alt="Java控制面板设置" src="images/guide_controlpanel.png" />
	</p>
	<p>
		FireFox配置：点击FireFox的菜单“工具”-》“附加组件”，点击左侧的“插件”选项，找到如下图插件项目选择“始终激活”： <br> <img alt="FireFox配置"
			src="images/guide_firefox.png" />
	</p>
	<p>至此环境配置完成，再次打开或刷新报表预览页面，正常情况应该出现Java Applet加载并开始自动下载打印相关组件，耐心等待一段时间后会出现相应的报表数据显示页面。</p>
</body>
</html>


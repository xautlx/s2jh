<%@page import="org.springframework.security.core.userdetails.*"%>
<%@page import="org.springframework.security.core.*"%>
<%@page import="org.springframework.security.web.*"%>
<%@page import="org.springframework.security.authentication.*"%>
<%@page import="lab.s2jh.core.web.captcha.BadCaptchaException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<script src="${base}/components/jquery-ui/1.10.0/js/jquery-ui-1.10.0.custom.js?_=${buildVersion}"></script>
<title>Admin Console Signin</title>
<style type="text/css">
input[type="text"],input[type="password"] {
	height: 31px;
	line-height: 31px;
	min-height: 1px;
	padding: 0 0 0 2px;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="navbar">
			<div class="navbar-inner">
				<a class="brand" href='javascript:void(0)'>&nbsp;S2JH - <small>A Java/J2EE
						development framework for enterprise system based on Struts/Spring/JPA/Hibernate and
						jquery/bootstrap</small></a>
			</div>
		</div>
		<div class="hero-unit">
			<fieldset>
				<legend>系统登录</legend>
				<div class="row-fluid">
					<div class="span4">
						<form id="loginForm" action="${base}/j_spring_security_check" method="post">
							<input type="hidden" name="j_username" id="j_username" />
							<%
							    Exception e = (Exception) session
												.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
										if (e != null) {
											org.slf4j.Logger logger = org.slf4j.LoggerFactory
													.getLogger("lab.s2jh.errors.login");
											if (logger.isDebugEnabled()) {
												logger.debug("login.exception", e);
											}
											String msg = "系统处理错误，请联系管理员";
											if (e instanceof UsernameNotFoundException
													|| (e.getCause() != null && e.getCause() instanceof UsernameNotFoundException)) {
												msg = "账号不存在,请重新输入!";
											} else if (e instanceof DisabledException
													|| (e.getCause() != null && e.getCause() instanceof DisabledException)) {
												msg = "账号已停用,请联系管理员!";
											} else if (e instanceof AccountExpiredException
													|| (e.getCause() != null && e.getCause() instanceof AccountExpiredException)) {
												msg = "账号已过期,请联系管理员!";
											} else if (e instanceof CredentialsExpiredException
													|| (e.getCause() != null && e.getCause() instanceof CredentialsExpiredException)) {
												msg = "密码已过期,请<a href='user!forward?_to_=password-req'>重设密码</a>";
											} else if (e instanceof LockedException
													|| (e.getCause() != null && e.getCause() instanceof LockedException)) {
												msg = "账号已被锁定,请联系管理员!";
											} else if (e instanceof BadCaptchaException
													|| (e.getCause() != null && e.getCause() instanceof BadCaptchaException)) {
												msg = "验证码校验失败，请重试!";
											} else if (e instanceof BadCredentialsException) {
												msg = "登录信息错误,请重新输入!";
											}
											session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
							%>
							<div class="control-group">
								<div class="alert alert-error"><%=msg%></div>
							</div>
							<%
							    }
							%>
							<div class="control-group">
								<label class="control-label" for="login_AclCode"><i class="icon-list-alt"></i>机构代码</label>
								<div class="controls">
									<input class="span12" id="login_AclCode" name="login_AclCode"
										value="${sessionScope['login_AclCode']}" type="text"> <span
										id="login_AclCode_text"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="login_AclCode"><i class="icon-user"></i>登录账号</label>
								<div class="controls">
									<input class="span12" id="login_Username" name="login_Username" type="text"
										value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="login_AclCode"><i class="icon-lock"></i>登录密码</label>
								<div class="controls">
									<input class="span12" id="j_password" name="j_password" value="" type="password">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label"><i class="icon-qrcode"></i>验证码</label>
								<div class="controls">
									<div class="input-prepend">
										<input class="span6" name="j_captcha" value="" type="text" /> <span> <img
											alt="验证码" style="cursor: point" name="j_captcha"
											onclick="this.src='${base}/pub/jcaptcha.servlet?_='+new Date().getTime();return false"
											src="${base}/pub/jcaptcha.servlet" title="看不清？点击刷新" /></span>
									</div>
								</div>
							</div>
							<div class="control-group">
								<div class="controls">
									<label class="checkbox span6"><input type="checkbox"
										name="_spring_security_remember_me" checked="true">记住我(两周内自动登录)</label> <label
										class="pull-right">
										<button type="reset" class="btn btn-large">重置</button>
										<button type="submit" class="btn btn-primary btn-large">登 录</button>
									</label>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span12">
						<s:if test="%{devMode}">
							<script type="text/javascript">
                                function setupDevUser(aclCode, user, password) {
                                    $("#login_AclCode").val(aclCode);
                                    $("#login_Username").val(user);
                                    $("#j_password").val(password);
                                    $("#j_captcha").val('admin');
                                    $("#loginForm").submit();
                                }
                            </script>
							<div class="control-group">
								<small>开发测试登录快速入口:<a href="javascript:void(0)" class=""
									onclick="setupDevUser('','admin','123')">admin</a>
								</small>
							</div>
						</s:if>
					</div>
				</div>
			</fieldset>
		</div>
		<div class="modal-footer">
			<p>
				<span class="pull-left"> <a href="mailto:xautlx@hotmail.com" target="_blank">Contact
						Me</a>
				</span> ©2013 by S2JH
			</p>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $().ready(function() {

            $("#login_AclCode").autocomplete({
                minLength : 2,
                source : WEB_ROOT + '/pub/acl-code!autocomplete.json',
                select : function(event, ui) {
                    $("#login_AclCode_text").html(ui.item.label);
                    $("#login_AclCode").val(ui.item.value);
                    return false;
                }
            }).data("ui-autocomplete")._renderItem = function(ul, item) {
                return $("<li>").append("<a><b>" + item.value + "</b> " + item.label + "</a>").appendTo(ul);
            };

            $("#loginForm").submit(function() {
                if ($("#login_AclCode").val() != "") {
                    $("#j_username").val($("#login_AclCode").val() + "#" + $("#login_Username").val());
                } else {
                    $("#j_username").val($("#login_Username").val());
                }
            });

            $("#login_Username").focus();
        });
    </script>
</body>
</html>

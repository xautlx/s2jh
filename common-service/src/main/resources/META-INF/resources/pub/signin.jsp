<%@page import="org.springframework.security.core.userdetails.*"%>
<%@page import="org.springframework.security.core.*"%>
<%@page import="org.springframework.security.web.*"%>
<%@page import="org.springframework.security.authentication.*"%>
<%@page import="lab.s2jh.core.web.captcha.BadCaptchaException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>Admin Console Login</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${base}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link rel="stylesheet" type="text/css" href="${base}/assets/plugins/select2/select2_metro.css" />
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME STYLES -->
<link href="${base}/assets/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/css/style.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${base}/assets/css/pages/login.css" rel="stylesheet" type="text/css" />
<link href="${base}/assets/app/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
	<!-- BEGIN LOGO -->
	<div class="logo">
		<h1 style="color: #FFFFFF">
			<s:property value="%{systemTitle}" />
		</h1>
	</div>
	<!-- END LOGO -->
	<!-- BEGIN LOGIN -->
	<div class="clearfix" style="padding: 15px">
		<div class="content" style="width: 100%; max-width: 450px">
			<!-- BEGIN LOGIN FORM -->
			<form class="login-form" action="${base}/j_spring_security_check" method="post" id="loginForm">
				<h3 class="form-title">系统登录</h3>
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
									msg = "密码已过期,请联系管理员!";
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
				<div class="alert alert-danger">
					<button class="close" data-close="alert"></button>
					<span><%=msg%></span>
				</div>
				<%
				    }
				%>


				<div class="form-group">
					<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
					<label class="control-label visible-ie8 visible-ie9">登录账号</label>
					<div class="input-icon">
						<i class="fa fa-user"></i> <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
							placeholder="登录账号" name="j_username" id="j_username" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">登录密码</label>
					<div class="input-icon">
						<i class="fa fa-lock"></i> <input class="form-control placeholder-no-fix" type="password" autocomplete="off"
							placeholder="登录密码" name="j_password" id="j_password" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">验证码</label>
					<div class="input-group">
						<div class="input-icon">
							<i class="fa fa-qrcode"></i> <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
								placeholder="验证码...看不清可点击图片可刷新" name="j_captcha" id="j_captcha" />
						</div>
						<span class="input-group-btn" style="cursor: pointer;"> <img alt="验证码" name="j_captcha" height="34px"
							onclick="this.src='${base}/pub/jcaptcha.servlet?_='+new Date().getTime();return false"
							src="${base}/pub/jcaptcha.servlet" title="看不清？点击刷新" />

						</span>
					</div>
				</div>
				<div class="form-actions">
					<label> <input type="checkbox" name="_spring_security_remember_me" checked="true" value="true" />
						记住我(两周内自动登录)
					</label>
					<button type="submit" class="btn blue pull-right">
						登录 <i class="m-icon-swapright m-icon-white"></i>
					</button>
				</div>
				<div class="forget-password hide">
					<p>
						忘记密码 ? 点击 <a href="javascript:;" id="forget-password">找回密码</a> 找回重置你的账号密码.
					</p>
				</div>
				<s:if test="casSupport">
					<div>
						<p>
							<a href='<s:property value="casRedirectUrl"/>'>单点登录</a>
						</p>
					</div>
				</s:if>
				<s:if test="%{devMode}">
					<script type="text/javascript">
                        function setupDevUser(user, password) {
                            $("#j_username").val(user);
                            $("#j_password").val(password);
                            $("#j_captcha").val('admin');
                            $("#loginForm").submit();
                        }
                    </script>
					<div>
						<p>
							开发测试登录快速入口: <a href="javascript:void(0)" class="" onclick="setupDevUser('admin','admin')">admin</a>
						</p>
					</div>
				</s:if>
			</form>
			<!-- END LOGIN FORM -->
			<!-- BEGIN FORGOT PASSWORD FORM -->
			<form class="forget-form" action="index.html" method="post">
				<h3>忘记密码 ?</h3>
				<p>Enter your e-mail address below to reset your password.</p>
				<div class="form-group">
					<div class="input-icon">
						<i class="fa fa-envelope"></i> <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
							placeholder="Email" name="email" />
					</div>
				</div>
				<div class="form-actions">
					<button type="button" id="back-btn" class="btn">
						<i class="m-icon-swapleft"></i> Back
					</button>
					<button type="submit" class="btn blue pull-right">
						Submit <i class="m-icon-swapright m-icon-white"></i>
					</button>
				</div>
			</form>
			<!-- END FORGOT PASSWORD FORM -->
		</div>
	</div>
	<!-- END LOGIN -->
	<!-- BEGIN COPYRIGHT -->
	<div class="copyright" style="color: #FFFFFF">
		2013 &copy;
		<%=request.getServerName()%></div>
	<!-- END COPYRIGHT -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="${base}/assets/plugins/respond.min.js"></script>
	<script src="${base}/assets/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="${base}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
		type="text/javascript"></script>
	<script src="${base}/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="${base}/assets/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${base}/assets/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${base}/assets/plugins/select2/select2.min.js"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="${base}/assets/scripts/app.js" type="text/javascript"></script>
	<script src="${base}/assets/scripts/login-soft.js" type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<script>
        jQuery(document).ready(function() {

            App.init();

            $('.login-form').validate({
                errorElement : 'span', //default input error message container
                errorClass : 'help-block', // default input error message class
                focusInvalid : false, // do not focus the last invalid input
                rules : {
                    j_username : {
                        required : true
                    },
                    j_password : {
                        required : true
                    }
                },

                messages : {
                    j_username : {
                        required : "请填写登录账号"
                    },
                    j_password : {
                        required : "请填写登录密码"
                    }
                },

                invalidHandler : function(event, validator) { //display error alert on form submit   
                    $('.alert-danger', $('.login-form')).show();
                },

                highlight : function(element) { // hightlight error inputs
                    $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                success : function(label) {
                    label.closest('.form-group').removeClass('has-error');
                    label.remove();
                },

                errorPlacement : function(error, element) {
                    error.insertAfter(element.closest('.input-icon'));
                },

                submitHandler : function(form) {
                    form.submit();
                }
            });

            $('.login-form input').keypress(function(e) {
                if (e.which == 13) {
                    if ($('.login-form').validate().form()) {
                        $('.login-form').submit();
                    }
                    return false;
                }
            });

            $.backstretch([ "${base}/assets/img/bg/1.jpg", "${base}/assets/img/bg/2.jpg", "${base}/assets/img/bg/3.jpg", "${base}/assets/img/bg/4.jpg" ], {
                fade : 1000,
                duration : 8000
            });
        });
    </script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
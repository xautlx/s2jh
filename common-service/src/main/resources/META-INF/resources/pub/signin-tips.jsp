<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="form-info" style="height: 270px; margin-top: 50px">
	<h4>访问提示</h4>
	<p>建议使用最新版本Firefox浏览器访问应用以避免不必要的浏览器兼容性问题。</p>
	<p>当前仅为框架原型演示应用，主要目的展示基于框架开发典型企业应用系统的UI交互效果，各业务功能很可能存在功能不完整或不符合实际业务场景以及相关Bug问题，仅供参考。</p>
	<p>项目详情及问题Issue反馈，请访问：
	<ul>
		<li><a href="http://git.oschina.net/xautlx/s2jh" target="_blank">http://git.oschina.net/xautlx/s2jh</a></li>
		<li><a href="https://github.com/xautlx/s2jh" target="_blank">https://github.com/xautlx/s2jh</a></li>
	</ul>
	</p>
	<s:if test="%{devMode||demoMode}">
		<p id="devModeTips" style="padding: 10px">
			<b> 开发/测试/演示登录快速入口: <a href="javascript:void(0)" class="" onclick="setupDevUser('admin','admin')">admin</a>
			</b>
		</p>
		<script type="text/javascript">
            function setupDevUser(user, password) {
                var $form = $("#login-form");
                $("input[name='j_username']", $form).val(user);
                $("input[name='j_password']", $form).val(password);
                $("input[name='j_captcha']", $form).val('admin');
                $form.submit();
            }
            jQuery(document).ready(function() {
                $("#devModeTips").pulsate({
                    color : "#bf1c56",
                    repeat : 10
                });
            });
        </script>
	</s:if>
</div>
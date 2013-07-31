<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
	<div class="well form-horizontal">
		<div class="row-fluid">
			<div class="span6">
				<s2:property value="signinid" label="登录账号" />
			</div>
			<div class="span6">
				<s2:property value="nick" label="昵称" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<s2:property value="email" label="电子邮件" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<s2:property value="#application.enums.booleanLabel[disabled]" label="禁用标识" />
			</div>
			<div class="span6">
				<s2:property value="#application.enums.booleanLabel[accountNonLocked]" label="账户未锁定标志" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<s2:date name="accountExpireTime" format="date" label="账号失效日期" />
			</div>
			<div class="span6">
				<s2:date name="credentialsExpireTime" format="date" label="密码失效日期" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<s2:date name="signupTime" format="timestamp" label="注册时间" />
			</div>
		</div>
	</div>
</div>
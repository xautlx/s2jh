<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<title>Admin Console Setup</title>
</head>
<body>
	<div class="container-fluid">
		<div class="hero-unit">
			<fieldset>
				<legend>系统初始化设置</legend>
				<form class="form-horizontal" action="${base}/pub/setup!init" method="post">
					<s:if test="hasActionErrors()">
						<div class="control-group">
							<div class="alert alert-error">
								<s:actionerror escape="false" />
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="control-group">
							<div class="input-prepend">
								<span class="add-on"><i class="icon-user"></i>&nbsp;系统所辖机构代码</span> <input class="span4"
									name="aclCode" type="text" placeholder="输入系统所辖机构代码">
							</div>
						</div>
						<div class="control-group">
							<div class="input-prepend">
								<span class="add-on"><i class="icon-user"></i>&nbsp;超级管理用户账号</span> <input class="span4"
									name="username" type="text" placeholder="输入登录账号">
							</div>
						</div>
						<div class="control-group">
							<div class="input-prepend">
								<span class="add-on"><i class="icon-lock"></i>&nbsp;超级管理用户密 码</span> <input class="span4"
									name="password" value="" type="password" placeholder="输入登录密码">
							</div>
						</div>
						<div class="control-group span3">
							<button type="submit" class="btn btn-submit">
								<i class="icon-ok"></i> 提交
							</button>
							<button type="reset" class="btn">
								<i class="icon-repeat"></i> 重置
							</button>
						</div>
					</s:else>
				</form>
			</fieldset>
		</div>
		<hr>
	</div>
	<%@ include file="pub-footer.jsp"%>
</body>
</html>

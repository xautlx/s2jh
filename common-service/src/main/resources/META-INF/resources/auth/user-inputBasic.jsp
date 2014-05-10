<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation" action="${base}/auth/user!doSave"
	method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-grid-reload=".grid-auth-user-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">登录帐号</label>
					<div class="controls">
						<s:textfield name="signinid" disabled="%{persistentedModel}" placeholder="创建之后不可修改，请仔细填写" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">机构代码</label>
					<div class="controls">
						<s:textfield name="aclCode" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">昵称</label>
					<div class="controls">
						<s:textfield name="nick" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">电子邮件</label>
					<div class="controls">
						<s:textfield name="email" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">设置密码</label>
					<div class="controls">
						<s:password name="newpassword" requiredLabel="%{!persistentedModel}" autocomplete="off" data-rule-minlength="3" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">确认密码</label>
					<div class="controls">
						<s:password name="cfmpassword" requiredLabel="%{!persistentedModel}" autocomplete="off"
							data-rule-equalToByName="newpassword" data-rule-minlength="3" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">启用状态</label>
					<div class="controls">
						<s:radio name="enabled" list="#application.enums.booleanLabel" />
						<span class="help-block"></span>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">账号失效日期</label>
					<div class="controls">
						<s3:datetextfield name="accountExpireTime" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-grid-reload=".grid-auth-user-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>

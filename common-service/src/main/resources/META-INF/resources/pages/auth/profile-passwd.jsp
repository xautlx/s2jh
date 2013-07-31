<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<script type="text/javascript">
    $().ready(function() {
        $("#changePasswdForm").formvalidate({
            trackFormChange : false,
            successCallback : function(response, submitButton) {
                $("#changePasswdDialog").dialog("close");
            }
        });
    });
</script>
<div class="container-fluid">
	<s:form cssClass="form-horizontal" method="post" id="changePasswdForm"
		action="/auth/profile!doPasswd">
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="reset" class="btn">
						<i class="icon-repeat"></i> 重置
					</button>
				</div>
			</div>
		</div>
		<div class="well">
			<div class="row-fluid">
				<div class="span12">
					<s2:password name="oldpasswd" cssClass="input-xlarge" validator="{required:true}" label="输入原密码"
						requiredLabel="true" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:password name="newpasswd" cssClass="input-xlarge" validator="{required:true,minlength:6}"
						label="输入新密码" requiredLabel="true" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:password name="cfmpasswd" cssClass="input-xlarge"
						validator="{required:true,minlength:6,equalToByName:'newpasswd'}" label="确认新密码"
						requiredLabel="true" />
				</div>
			</div>
		</div>
	</s:form>
</div>
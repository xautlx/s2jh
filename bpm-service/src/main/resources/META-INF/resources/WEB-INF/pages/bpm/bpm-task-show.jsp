<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="s2" uri="/struts2-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="well">
			<s2:form cssClass="form-horizontal">
				<fieldset>
					<legend>
						<s:property value="#request.task.name" />
					</legend>

					<div class="row-fluid">
						<div class="span4">
							<s2:date label="创建时间" name="#request.task.createTime" format="timestamp" />
						</div>
						<div class="span4">
							<s2:date label="到期时间" name="#request.task.dueDate" format="timestamp" />
						</div>
						<div class="span4">
							<s2:property label="优先级" value="#request.task.priority" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
							<s2:property label="任务描述" value="#request.task.description" />
						</div>
					</div>
					<s:if test="#request.task.assignee==null">
						<div class="row-fluid">
							<div class="span12">
								<button class="btn btn-primary btn-large" type="button" id="taskClaimBtn">签收</button>
								当前任务尚未签收，需要先签收之后才能办理后续处理业务
							</div>
						</div>
					</s:if>
				</fieldset>
			</s2:form>
			<fieldset>
				<legend>任务处理 </legend>
				<div id="taskFormDiv"></div>
			</fieldset>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#taskFormDiv").ajaxGetUrl("${base}<s:property value='#request.formKey' />", function() {
                if ($("#taskClaimBtn").size() > 0) {
                    $("#taskFormDiv").find("button,input,radio,select").attr("disabled", true);
                }
            });

            $("#taskClaimBtn").click(function() {
                $.ajaxPostURL({
                    url : "${base}/bpm/bpm-task!claim?id=<s:property value='#request.task.id' />",
                    confirm : "确认签收当前任务？",
                    successCallback : function() {
                        window.location.reload();
                    }
                })
            })
        });
    </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="task-show">
	<div class="form-horizontal form-bordered form-label-stripped form-task-info">
		<div class="portlet">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-reorder"></i>待办任务信息
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;"></a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="tabbable tabbable-custom">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#tab-auto">基本信息</a></li>
						<li><a data-toggle="tab"
							href="${base}/bpm/bpm-task!backActivity?taskId=<s:property value='#request.task.id'/>">任务自由回退</a></li>
						<li><a data-toggle="tab"
							href="${base}/bpm/activiti!showProcessImage?processDefinitionId=<s:property value='#request.task.processDefinitionId'/>&processInstanceId=<s:property value='#request.task.processInstanceId'/>">流程运行图</a></li>
						<li><a data-toggle="tab" href="${base}/bpm/bpm-task!variables?taskId=<s:property value='#request.task.id'/>">流程变量</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label">创建时间</label>
										<div class="controls">
											<p class="form-control-static">
												<s:date name="#request.task.createTime" />
											</p>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label">到期时间</label>
										<div class="controls">
											<p class="form-control-static">
												<s:date name="#request.task.dueDate" format="yyyy-MM-hh" />
											</p>
										</div>
									</div>
								</div>

							</div>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label">任务办理者</label>
										<div class="controls">
											<p class="form-control-static">
												<s:if test="#request.task.assignee== null">
												[待签收]
												</s:if>
												<s:else>
													<a data-name="assignee" data-placement="right" data-original-title="任务转办" data-required="true"
														data-pk="<s:property value='#request.task.id' />" data-url="${base}/bpm/bpm-task!trasfer"
														href="javascript:;" class="editable editable-click x-editable editable-bpm-task-transfer"><s:property
															value="#request.task.assignee" /> </a>
												</s:else>
											</p>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label">优先级</label>
										<div class="controls">
											<p class="form-control-static">
												<s:property value="#request.task.priority" />
											</p>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label class="control-label">任务说明</label>
										<div class="controls">
											<p class="form-control-static">
												<s:date name="#request.task.description" />
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<s:if test="#request.task.assignee==null">
			<div class="form-actions form-actions-task-claim">
				<button class="btn blue btn-post-url btn-task-claim" type="button"
					data-url="${base}/bpm/bpm-task!claim?id=<s:property value='#request.task.id' />" data-confirm="确认签收当前任务？">
					<i class="fa fa-thumb-tack"></i> 签收
				</button>
				<button class="btn default btn-cancel" type="button">取消</button>
				<span class="help-inline">您作为当前任务的候选处理人员，需要先签收才能进行后续实际任务办理</span>
			</div>
		</s:if>
	</div>
	<div class="task-content ajaxify" data-url="${base}<s:property value='#request.formKey' />" style="display: none"></div>
</div>
<script type="text/javascript">
    $(function() {

        App.blockUI(".task-show");

        $(".task-content").data("success", function() {
            var $el = $(this);
            if ($el.parent().find(".btn-task-claim").size() > 0) {
                $el.find(".form-actions").hide();
            }
            $el.show();
            App.unblockUI(".task-show");
        })

        $(".btn-task-claim").data("success", function(json) {
            var $panel = $(this).closest(".panel-content");
            $panel.ajaxGetUrl($panel.attr("data-url"));
        })
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>

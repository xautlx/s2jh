<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/bpm/bpm-task!doBackActivity" method="post" data-editrulesurl="false">
	<s:hidden name="taskId" value="%{#parameters.taskId}" />
	<s:if test="#request.task.assignee==null">
		<div class="note note-warning note-need-claim">
			<h4 class="block">
				请先签收任务 <small>必须先签收任务后才能进行任务回退操作</small>
			</h4>
		</div>
	</s:if>
	<div class="row">
		<div class="col-md-12">
			<div class="form-group">
				<label class="control-label">回退说明</label>
				<div class="controls">
					<s:textarea name="backActivityExplain" requiredLabel="true" />
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-8">
			<div class="form-group">
				<label class="control-label">回退节点</label>
				<div class="controls">
					<s:select list="#request.backActivities" name="activityId" requiredLabel="true" />
				</div>
			</div>
		</div>
		<div class="col-md-4">
			<div class="form-group">
				<div class="controls controls-clearfix">
					<s:if test="#request.task.assignee==null">
						<button class="btn blue btn-back-activity" type="submit" data-ajaxify-reload=".ajaxify-tasks" disabled>
							<i class="fa fa-backward"></i> 任务回退
						</button>
					</s:if>
					<s:else>
						<button class="btn blue btn-back-activity" type="submit" data-ajaxify-reload=".ajaxify-tasks">
							<i class="fa fa-backward"></i> 任务回退
						</button>
					</s:else>
				</div>
			</div>
		</div>
	</div>
	<div class="note note-info">
		<h4 class="block">任务自由回退规则和限制说明：</h4>
		<p>
		<ol>
			<li>只能回退到已处理过的任务节点</li>
			<li>不能回退到“并行分支”类型任务节点，只能向上回退到并行任务之前单任务节点</li>
			<li>如果当前任务处于“并行分支”类型任务，则不允许回退，必须等所有并行分支任务完成后进入单任务节点才能自由回退</li>
		</ol>
		</p>
	</div>
</form>
<%@ include file="/common/ajax-footer.jsp"%>
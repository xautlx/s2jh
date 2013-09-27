<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/schedule/job-bean-cfg!doUpdate':'/schedule/job-bean-cfg!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="jobBeanCfgIndexTabs"
						callback-grid="jobBeanCfgListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close"
						callback-tab="jobBeanCfgIndexTabs" callback-grid="jobBeanCfgListDiv">
						<i class="icon-check"></i> 保存并关闭
					</button>
					<button type="reset" class="btn">
						<i class="icon-repeat"></i> 重置
					</button>
				</div>
			</div>
		</div>
		<div class="alert alert-info">
			<h4>提示</h4>
			新添加计划任务不会立刻安排作业,需要重启应用服务器才能生效. <br>变更运行模式会移除当前计划任务,并需要重启应用服务器才能生效.
		</div>
		<div class="well">
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="jobClass" label="任务类全名" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="cronExpression" label="CRON表达式" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="autoStartup" list="#application.enums.booleanLabel" label="自动初始运行" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="logRunHist" list="#application.enums.booleanLabel" label="启用历史记录" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="runWithinCluster" list="#application.enums.booleanLabel" label="集群运行模式"
						tooltip="变更运行模式会移除当前计划任务,并需要重启应用服务器才能生效" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="description" label="描述" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
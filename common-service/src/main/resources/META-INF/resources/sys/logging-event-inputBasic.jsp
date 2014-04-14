<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="portlet">
	<div class="portlet-title">
		<div class="caption">异常堆栈</div>
		<div class="tools">
			<a class="collapse" href="javascript:;"></a>
		</div>
	</div>
	<div class="portlet-body">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<pre>
					<s:property value="exceptionStack" />
					</pre>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="portlet">
	<div class="portlet-title">
		<div class="caption">关联属性</div>
		<div class="tools">
			<a class="collapse" href="javascript:;"></a>
		</div>
	</div>
	<div class="portlet-body">
		<div class="row">
			<div class="col-md-12">
				<table class="table table-condensed table-bordered table-list">
					<thead>
						<tr>
							<th style="width: 50px">序号</th>
							<th style="width: 180px">属性代码</th>
							<th>属性值</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="loggingEventProperties" status="status" id="item">
							<tr>
								<td style="text-align: center;"><s:property value="%{#status.count}" /></td>
								<td><s:property value="%{#item.id.mappedKey}" /></td>
								<td><pre>
										<s:property value="%{#item.mappedValue}" />
									</pre></td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="portlet">
	<div class="portlet-title">
		<div class="caption">日志处理</div>
		<div class="tools">
			<a class="collapse" href="javascript:;"></a>
		</div>
	</div>
	<div class="portlet-body">
		<form class="form-horizontal form-bordered form-label-stripped form-validation"
			action="${base}/sys/logging-event!doUpdate" method="post">
			<s:hidden name="id" />
			<div class="form-actions">
				<button class="btn blue" type="submit" data-grid-reload=".grid-sys-logging-event-index">
					<i class="fa fa-check"></i> 保存
				</button>
				<button class="btn default btn-cancel" type="button">取消</button>
			</div>
			<div class="form-body">
				<div class="row">
					<div class="col-md-12">
						<div class="form-group">
							<label class="control-label">处理状态</label>
							<div class="controls">
								<s:radio name="state" list="#application.enums.loggingHandleStateEnum" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="form-group">
							<label class="control-label">处理说明</label>
							<div class="controls">
								<s:textarea name="operationExplain" rows="2" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-actions right">
				<button class="btn blue" type="submit" data-grid-reload=".grid-sys-logging-event-index">
					<i class="fa fa-check"></i> 保存
				</button>
				<button class="btn default btn-cancel" type="button">取消</button>
			</div>
		</form>
	</div>
</div>
<%@ include file="/common/ajax-footer.jsp"%>

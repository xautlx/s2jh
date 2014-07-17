<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/biz/purchase/purchase-order!bpmLevel1Audit" method="post">
	<s:hidden name="taskId" value="%{#parameters.taskId}" />
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-ajaxify-reload=".ajaxify-tasks">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">一线审核通过</label>
					<div class="controls">
						<s:radio name="auditLevel1Pass" list="#application.enums.booleanLabel" value="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">一线审核意见</label>
					<div class="controls">
						<s:textarea name="auditLevel1Explain" rows="3" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-ajaxify-reload=".ajaxify-tasks">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<div class="purchase-order-content ajaxify"
	data-url="${base}/biz/purchase/purchase-order!view?id=<s:property value='#parameters.id'/>"></div>
<%@ include file="/common/ajax-footer.jsp"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation form-biz-purchase-purchase-order-bpmPay"
	action="${base}/biz/purchase/purchase-order!bpmPay" method="post">
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
	<div class="form-body control-label-sm">
		<s:set var="taskVariablesVar" value="taskVariables" />
		<div class="row">
			<div class="col-md-8">
				<div class="form-group">
					<label class="control-label">供货商:</label>
					<div class="controls">
						<p class="form-control-static">
							<s:property value="bizTradeUnit.display" />
						</p>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8">
				<div class="form-group">
					<label class="control-label">付款账户</label>
					<div class="controls">
						<s:textfield name="accountSubject.display" requiredLabel="true" />
						<s:hidden name="accountSubject.id" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">(预)付款金额</label>
					<div class="controls">
						<s:textfield name="actualPayedAmount" requiredLabel="true" value="%{totalAmount}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">关联凭证</label>
					<div class="controls">
						<s:textfield name="paymentVouchers" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">付款备注</label>
					<div class="controls">
						<s:textarea name="paymentReference" rows="10" />
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
<script src="${base}/biz/purchase/purchase-order-bpmPay.js" />
<%@ include file="/common/ajax-footer.jsp"%>

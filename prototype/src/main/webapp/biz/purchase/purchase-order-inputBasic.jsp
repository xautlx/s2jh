<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation form-edit-purchase-order"
	action="${base}/biz/purchase/purchase-order!doSave" method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<s:if test="redwordDate==null">
		<div class="form-actions form-inline">
			<button type="submit" class="btn blue" data-grid-reload=".grid-purchase-order-list">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn default btn-cancel" type="button">取消</button>
			<button class="btn red pull-right" type="submit" data-grid-reload=".grid-purchase-order-list"
				data-form-action="${base}/biz/purchase/purchase-order!doRedword" data-confirm="确认冲销当前单据？">红冲</button>
		</div>
	</s:if>
	<div class="form-body">
		<div class="portlet">
			<div class="portlet-title">
				<div class="tools">
					<a class="collapse" href="javascript:;"></a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="row">
					<div class="col-md-4 col-md-offset-8">
						<div class="form-group">
							<label class="control-label">凭证编号</label>
							<div class="controls">
								<s:textfield name="voucher" readonly="true" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">经办人</label>
							<div class="controls">
								<s:select name="voucherUser.id" list="usersMap" />
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">部门</label>
							<div class="controls">
								<s:select name="voucherDepartment.id" list="departmentsMap" />
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">记账日期</label>
							<div class="controls">
								<s3:datetextfield name="voucherDate" format="date" current="true" disabled="true" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8">
				<div class="form-group">
					<label class="control-label">供货商</label>
					<div class="controls">
						<s:select name="supplier.id" list="suppliersMap" disabled="true" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">付款类型</label>
					<div class="controls">
						<s:radio name="payMode" list="#application.enums.purchaseOrderPayModeEnum" disabled="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">标题摘要</label>
					<div class="controls">
						<s:textfield name="title" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="grid-biz-purchase-purchase-order-inputBasic" data-grid="items" data-readonly="true"
					data-pk='<s:property value="#parameters.id"/>'></table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">整单折扣额</label>
					<div class="controls">
						<s:textfield name="totalDiscountAmount" disabled="true" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">折后应付总金额</label>
					<div class="controls">
						<s:textfield name="amount" disabled="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">整单运费</label>
					<div class="controls">
						<s:textfield name="totalDeliveryAmount" disabled="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">备注说明</label>
					<div class="controls">
						<s:textfield name="adminMemo" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="note note-success">
				<h4 class="block">
					订单总价：<span class="span-total-amount"><s:property value="totalAmount" /></span>
				</h4>
			</div>
		</div>
	</div>
	<s:if test="redwordDate==null">
		<div class="form-actions right">
			<button type="submit" class="btn blue" data-grid-reload=".grid-purchase-order-list">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn default btn-cancel" type="button">取消</button>
			<button class="btn red pull-left" type="submit" data-grid-reload=".grid-purchase-order-list"
				data-form-action="${base}/biz/purchase/purchase-order!doRedword" data-confirm="确认冲销当前单据？">红冲</button>
		</div>
	</s:if>
</form>
<script src="${base}/biz/purchase/purchase-order-inputBasic.js" />
<%@ include file="/common/ajax-footer.jsp"%>

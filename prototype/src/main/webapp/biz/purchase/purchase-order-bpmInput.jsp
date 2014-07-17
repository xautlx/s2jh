<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form
	class="form-horizontal form-bordered form-label-stripped form-validation form-biz-purchase-purchase-order-bpmInput"
	action="${base}/biz/purchase/purchase-order!bpmSave?prepare=true" method="post">
	<s:hidden name="taskId" value="%{#parameters.taskId}" />
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-ajaxify-reload=".ajaxify-tasks">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
		<div class="clearfix pull-right" style="margin-left: 5px">
			<div class="input-group">
				<span class="input-group-addon">商品条码 <i class="fa fa-barcode"></i></span> <input type="text" name="barcode"
					class="form-control">
			</div>
		</div>
	</div>
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
								<s:textfield name="voucher" />
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
								<s3:datetextfield name="voucherDate" format="date" current="true" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8">
				<div class="form-group">
					<label class="control-label">往来单位</label>
					<div class="controls">
						<s:select name="bizTradeUnit.id" list="#{}" data-url="/biz/finance/biz-trade-unit!frequentUsedDatas.json" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">付款类型</label>
					<div class="controls">
						<s:radio name="payMode" list="#application.enums.purchaseOrderPayModeEnum" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">标题摘要</label>
					<div class="controls">
						<s:textfield name="title" placeholder="留空会自动基于商品信息生成标题摘要" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="grid-biz-purchase-purchase-order-bpmInput" data-grid="items"
					data-pk='<s:property value="#parameters.id"/>' data-clone='<s:property value="#parameters.clone"/>'></table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">整单折扣额</label>
					<div class="controls">
						<div class="input-group">
							<s:textfield name="totalDiscountAmount" />
							<div class="input-group-btn">
								<button tabindex="-1" class="btn default btn-discount-by-amount" type="button">按金额自动分摊</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">折后应付总金额</label>
					<div class="controls">
						<s:textfield name="amount" readonly="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">整单运费</label>
					<div class="controls">
						<div class="input-group">
							<s:textfield name="totalDeliveryAmount" />
							<div class="input-group-btn">
								<button type="button" class="btn default btn-delivery-by-amount" tabindex="-1">按金额自动分摊</button>
								<button type="button" class="btn default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
									<i class="fa fa-angle-down"></i>
								</button>
								<ul class="dropdown-menu pull-right" role="menu">
									<li><a>按重量自动分摊</a></li>
								</ul>
							</div>
						</div>
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
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">提交审批</label>
					<div class="controls text-warning">
						<s:checkbox name="submitToAudit" disabled="%{'S10N'!=orderStatus.name()}" label="勾选此项将会立即提交审批流程，在此期间不可修改数据！"
							value="true" />
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
					<s:hidden name="totalAmount" value="%{totalAmount}" />
				</h4>
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
<s:if test="%{persistentedModel}">
	<div class="purchase-order-content ajaxify"
		data-url="${base}/biz/purchase/purchase-order!view?id=<s:property value='#parameters.id'/>"></div>
</s:if>
<script src="${base}/biz/purchase/purchase-order-bpmInput.js" />
<%@ include file="/common/ajax-footer.jsp"%>

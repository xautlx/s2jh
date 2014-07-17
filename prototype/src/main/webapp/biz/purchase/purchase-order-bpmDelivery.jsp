<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation"
	action="${base}/biz/purchase/purchase-order!bpmDelivery" method="post">
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
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">物流公司</label>
					<div class="controls">
						<s:select name="logistics.id" list="#{}" data-url="/biz/finance/biz-trade-unit!frequentUsedDatas.json" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">物流单号</label>
					<div class="controls">
						<s:textfield name="logisticsNo" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">发货日期</label>
					<div class="controls">
						<s3:datetextfield format="timestamp" name="deliveryTime" requiredLabel="true" current="true"
							data-timepicker="true" />
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
	<s:if test="%{payMode.name()=='PREV'}">
		<div class="portlet gren">
			<div class="portlet-title">
				<div class="caption">采购付款</div>
			</div>
			<div class="portlet-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">采购总金额：</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="totalAmount" />
								</p>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">已付总金额：</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="actualPayedAmount" />
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">支付凭证号：</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="accountInOut.payNo" />
								</p>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">收款人信息：</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="accountInOut.target" />
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</s:if>
</form>
<div class="purchase-order-content ajaxify"
	data-url="${base}/biz/purchase/purchase-order!view?id=<s:property value='#parameters.id'/>"></div>
<%@ include file="/common/ajax-footer.jsp"%>

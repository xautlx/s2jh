<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form class="form-horizontal form-bordered form-label-stripped form-validation form-biz-sale-sale-delivery-inputBasic"
	action="${base}/biz/sale/sale-delivery!doSave" method="post">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions form-inline">
		<div class="inline" style="width: 100px">
			<s3:button disabled="%{disallowUpdate!=null}" type="submit" cssClass="btn blue"
				data-grid-reload=".grid-biz-sale-sale-delivery">
				<i class="fa fa-check"></i>
				<s:property value="%{disallowUpdate!=null?disallowUpdate:'保存'}" />
			</s3:button>
			<button class="btn default btn-cancel" type="button">取消</button>
			<s:if test="%{voucherState.name()!='DRAFT'}">
				<s3:button disabled="%{disallowChargeAgainst!=null}" cssClass="btn red" type="submit"
					data-grid-reload=".grid-biz-sale-sale-delivery" data-form-action="${base}/biz/sale/sale-delivery!chargeAgainst"
					data-confirm="确认冲销当前销售单？">
					<s:property value="%{disallowChargeAgainst!=null?disallowChargeAgainst:'红冲'}" />
				</s3:button>
			</s:if>
		</div>
		<div class="pull-right">
			<div class="form-group input-medium">
				<div class="input-group ">
					<span class="input-group-addon">商品条码</span> <input type="text" name="barcodeScan" class="form-control"><span
						class="input-group-addon"><i class="fa fa-barcode"></i></span>
				</div>
			</div>
			<a class="btn yellow btn-select-sale-order" href="javascript:;"><i class="fa fa-indent"> 从销售订单选取</i></a>
		</div>
	</div>
	<div class="form-body control-label-sm">
		<div class="portlet">
			<div class="portlet-title">
				<div class="tools">
					<a class="collapse" href="javascript:;"></a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">凭证状态</label>
							<div class="controls">
								<p class="form-control-static">
									<s:property value="#application.enums.voucherStateEnum[voucherState]" />
								</p>
							</div>
						</div>
					</div>
					<div class="col-md-4 ">
						<div class="form-group">
							<label class="control-label">凭证编号</label>
							<div class="controls">
								<s:textfield name="voucher" readonly="%{notNew}" />
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
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">销售客户</label>
					<div class="controls">
						<div class="input-icon right">
							<i class="fa fa-ellipsis-horizontal fa-select-customer-profile"></i>
							<s:textfield name="customerProfile.display" disabled="%{'DRAFT'!=voucherState.name()}" />
							<s:hidden name="customerProfile.id" disabled="%{'DRAFT'!=voucherState.name()}" />
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">关联订单号</label>
					<div class="controls">
						<s:textfield name="referenceVoucher" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">关联订单来源</label>
					<div class="controls">
						<s:select name="referenceSource" list="referenceSourceMap"
							data-profile-param="default_sale_delivery_reference_source" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">销售单名称</label>
					<div class="controls">
						<s:textfield name="title" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">收货人</label>
					<div class="controls">
						<s:textfield name="receivePerson" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">电话</label>
					<div class="controls">
						<s:textfield name="mobilePhone" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="form-group">
					<label class="control-label">邮编</label>
					<div class="controls">
						<s:textfield name="postCode" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">收货地址</label>
					<div class="controls">
						<s:textfield name="deliveryAddr" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">备注说明</label>
					<div class="controls">
						<s:textfield name="memo" />
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 5px">
			<div class="col-md-12">
				<table class="grid-biz-sale-sale-delivery-inputBasic" data-grid="items"
					data-readonly="<s:property value="%{voucherState.name()=='DRAFT'?false:true}"/>"
					data-pk='<s:property value="#parameters.id"/>' data-clone='<s:property value="#parameters.clone"/>'></table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">整单折扣额</label>
					<div class="controls">
						<div class="input-group">
							<s:textfield name="discountAmount" readonly="%{'DRAFT'!=voucherState.name()}" />
							<div class="input-group-btn">
								<button tabindex="-1" class="btn default btn-discount-by-amount" type="button">按金额自动分摊</button>
								<button tabindex="-1" data-toggle="dropdown" class="btn default dropdown-toggle" type="button">
									<i class="fa fa-angle-down"></i>
								</button>
								<ul role="menu" class="dropdown-menu pull-right">
									<li><a href="javascript:;" onclick="alert('TODO')">按重量自动分摊</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">折后金额</label>
					<div class="controls">
						<s:textfield name="commodityAmount" readonly="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">收取运费</label>
					<div class="controls">
						<s:textfield name="chargeLogisticsAmount" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">应收总金额</label>
					<div class="controls">
						<s:textfield name="totalAmount" readonly="true" />
					</div>
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">已收总金额</label>
					<div class="controls">
						<s:textfield name="payedAmount" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">收款账户</label>
					<div class="controls">
						<s:select name="accountSubject.id" list="paymentAccountSubjects"
							data-profile-param="default_sale_delivery_account_subject_id" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">收款备注</label>
					<div class="controls">
						<s:textarea name="paymentReference" rows="3" />
					</div>
				</div>
			</div>
		</div>
	</div>


	<div class="form-actions form-inline right">
		<div class="pull-left">
			<a class="btn yellow btn-select-sale-order" href="javascript:;"><i class="fa fa-indent"> 从销售订单选取</i></a>
		</div>
		<s3:button disabled="%{disallowUpdate!=null}" type="submit" cssClass="btn blue"
			data-grid-reload=".grid-biz-sale-sale-delivery">
			<i class="fa fa-check"></i>
			<s:property value="%{disallowUpdate!=null?disallowUpdate:'保存'}" />
		</s3:button>
		<button class="btn default btn-cancel" type="button">取消</button>
		<s:if test="%{voucherState.name()!='DRAFT'}">
			<s3:button disabled="%{disallowChargeAgainst!=null}" cssClass="btn red"
				data-grid-reload=".grid-biz-sale-sale-delivery" data-form-action="${base}/biz/sale/sale-delivery!chargeAgainst"
				data-confirm="确认冲销当前销售单？">
				<s:property value="%{disallowChargeAgainst!=null?disallowChargeAgainst:'红冲'}" />
			</s3:button>
		</s:if>
	</div>
</form>
<script src="${base}/biz/sale/sale-delivery-inputBasic.js" />
<%@ include file="/common/ajax-footer.jsp"%>

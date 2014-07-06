<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form method="get" class="form-inline form-validation" data-grid-search=".grid-biz-sale-sale-delivery-sum"
			action="#">
			<div class="form-group">
				<input type="text" name="search['CN_voucher']" class="form-control input-large"
					placeholder="销售单号...">
			</div>
			<div class="form-group">
				<input type="text" name="search['BT_voucherDate']"
					class="form-control input-medium input-daterangepicker grid-param-data" placeholder="记账期间" required="true">
			</div>
			<button class="btn default hidden-inline-xs" type="reset">
				<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
			</button>
			<button class="btn green" type="submmit">
				<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
			</button>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="grid-biz-sale-sale-delivery-sum"></table>
	</div>
</div>
<script src="${base}/biz/sale/sale-delivery-sum.js" />
<%@ include file="/common/ajax-footer.jsp"%>
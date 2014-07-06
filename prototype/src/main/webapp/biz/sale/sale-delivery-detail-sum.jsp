<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form method="get" class="form-inline form-validation" data-grid-search=".grid-biz-sale-sale-delivery-detail-sum"
			action="#">
			<div class="form-group">
				<input type="text" name="search['CN_commodity.barcode_OR_commodity.title_OR_commodity.sku']" class="form-control input-large"
					placeholder="商品编码/条码/名称...">
			</div>
			<div class="form-group">
				<input type="text" name="search['BT_saleDelivery.voucherDate']"
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
		<table class="grid-biz-sale-sale-delivery-detail-sum"></table>
	</div>
</div>
<script src="${base}/biz/sale/sale-delivery-detail-sum.js" />
<%@ include file="/common/ajax-footer.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form method="get" class="form-inline form-validation form-search-init"
			data-grid-search=".grid-biz-stock-commodity-stock-sumByStorageLocation" action="#">
			<div class="form-group">
				<s:textfield name="search['CN_commodity.sku_OR_commodity.barcode_OR_commodity.title']"
					cssClass="form-control input-xlarge" placeholder="商品编码/条码/名称..." />
			</div>
			<div class="form-group">
				<s:select list="{}" cssClass="form-control input-medium" data-cache="Biz.getStockDatas()"
					name="search['EQ_storageLocation.id']" />
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
		<table class="grid-biz-stock-commodity-stock-sumByStorageLocation"></table>
	</div>
</div>
<script src="${base}/biz/stock/commodity-stock-sumByStorageLocation.js" />
<%@ include file="/common/ajax-footer.jsp"%>
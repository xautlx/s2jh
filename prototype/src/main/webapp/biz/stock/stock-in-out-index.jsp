<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="row search-form-default">
	<div class="col-md-12">
		<form action="#" method="get" class="form-inline form-validation  form-search-init"
			data-grid-search=".grid-biz-stock-stock-in-out">
			<div class="input-group">
				<div class="input-cont">
					<input type="text"
						name="search['CN_commodityStock.commodity.barcode_OR_commodityStock.commodity.title_OR_commodityStock.storageLocation.code_OR_commodityStock.storageLocation.title_OR_voucher']"
						class="form-control" placeholder="商品名称、编号，库存地、凭证号...">
				</div>
				<span class="input-group-btn">
					<button class="btn green" type="submmit">
						<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
					</button>
					<button class="btn default hidden-inline-xs" type="reset">
						<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
					</button>
				</span>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="grid-biz-stock-stock-in-out"></table>
	</div>
</div>
<script src="${base}/biz/stock/stock-in-out-index.js" />
<%@ include file="/common/ajax-footer.jsp"%>

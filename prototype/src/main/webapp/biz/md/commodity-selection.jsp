<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form action="#" method="get" class="form-inline form-validation form-search form-search-init"
			data-grid-search=".grid-biz-md-commodity-selection">
			<div class="input-group">
				<div class="input-cont">
					<input type="text" name="search['CN_sku_OR_barcode_OR_title']" class="form-control" placeholder="SKU编码/条码/名称">
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
		<table class="grid-biz-md-commodity-selection"></table>
	</div>
</div>
<script src="${base}/biz/md/commodity-selection.js" />
<%@ include file="/common/ajax-footer.jsp"%>

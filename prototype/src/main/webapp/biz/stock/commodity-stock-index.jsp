<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">分批次库存管理</a></li>
		<li><a class="tab-default" data-toggle="tab"
			href="${base}/biz/stock/commodity-stock!forward?_to_=sumByStorageLocation">按库存地汇总库存量</a></li>
		<li><a class="tab-default" data-toggle="tab" href="${base}/biz/stock/commodity-stock!forward?_to_=sumByCommodity">按商品汇总库存量</a></li>
		<li><a class="tab-default" data-toggle="tab" href="${base}/biz/stock/stock-in-out">库存变更流水记录</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form action="#" method="get" class="form-inline form-validation form-search form-search-init"
						data-grid-search=".grid-biz-stock-commodity-stock">
						<div class="input-group">
							<div class="input-cont">
								<input type="text" name="search['CN_commodity.barcode_OR_commodity.title']" class="form-control"
									placeholder="商品编码、名称...">
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
					<table class="grid-biz-stock-commodity-stock"></table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${base}/biz/stock/commodity-stock-index.js" />
<%@ include file="/common/ajax-footer.jsp"%>

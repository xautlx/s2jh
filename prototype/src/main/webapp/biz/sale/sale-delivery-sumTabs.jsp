<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li><a class="tab-default" data-toggle="tab" href="${base}/biz/sale/sale-delivery-detail!forward?_to_=sum">销售商品毛利</a></li>
		<li><a class="tab-default" data-toggle="tab" href="${base}/biz/sale/sale-delivery!forward?_to_=sum">销售单毛利</a></li>
		<li><a class="tab-default" data-toggle="tab" href="${base}/biz/sale/sale-delivery!forward?_to_=saler-sum">销售人员业绩</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
</div>
<%@ include file="/common/ajax-footer.jsp"%>

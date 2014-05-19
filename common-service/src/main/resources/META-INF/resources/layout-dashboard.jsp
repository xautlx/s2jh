<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row">
	<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">&nbsp;</div>
</div>
<div class="row">
	<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
		<div class="dashboard-stat blue">
			<div class="visual">
				<i class="fa fa-tasks"></i>
			</div>
			<div class="details">
				<div class="number">--</div>
				<div class="desc">待办任务</div>
			</div>
			<a href="javascript:;" class="more"> 查看更多 <i class="m-icon-swapright m-icon-white"></i>
			</a>
		</div>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
		<div class="dashboard-stat green">
			<div class="visual">
				<i class="fa fa-comments"></i>
			</div>
			<div class="details">
				<div class="number">--</div>
				<div class="desc">未读消息</div>
			</div>
			<a href="javascript:;" class="more"> 查看更多 <i class="m-icon-swapright m-icon-white"></i>
			</a>
		</div>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
		<div class="dashboard-stat yellow">
			<div class="visual">
				<i class="fa fa-dollar"></i>
			</div>
			<div class="details">
				<div class="number">--</div>
				<div class="desc">今日销售订单数</div>
			</div>
			<a href="javascript:;" class="more"> 查看更多 <i class="m-icon-swapright m-icon-white"></i>
			</a>
		</div>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
		<div class="dashboard-stat purple">
			<div class="visual">
				<i class="fa fa-shopping-cart"></i>
			</div>
			<div class="details">
				<div class="number">--</div>
				<div class="desc">今日采购订单数</div>
			</div>
			<a href="javascript:;" class="more"> 查看更多 <i class="m-icon-swapright m-icon-white"></i>
			</a>
		</div>
	</div>
</div>
<div class="clearfix"></div>
<div class="row ">
	<div class="col-md-6 col-sm-6">
		<div class="ajaxify ajaxify-tasks" data-url="${base}/bpm/bpm-task!userTasks"></div>
	</div>
	<div class="col-md-6 col-sm-6">
	    <div class="ajaxify ajaxify-tasks" data-url="${base}/profile/pub-post!list"></div>
	</div>
</div>
<%@ include file="/common/ajax-footer.jsp"%>
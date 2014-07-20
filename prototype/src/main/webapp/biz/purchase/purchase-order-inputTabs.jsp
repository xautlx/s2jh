<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-custom tabbable-secondary">
	<ul class="nav nav-tabs">
		<li class="tools pull-right"><a href="javascript:;" class="btn default reload"><i class="fa fa-refresh"></i></a></li>
		<li class="active"><a data-toggle="tab"
			href="${base}/biz/purchase/purchase-order!edit?id=<s:property value='#parameters.id'/>&clone=<s:property value='#parameters.clone'/>">基本信息</a></li>
		<li><a data-toggle="tab"
			href="${base}/biz/purchase/purchase-order!revisionIndex?id=<s:property value='#parameters.id'/>">变更记录</a></li>
	</ul>
</div>
<%@ include file="/common/ajax-footer.jsp"%>
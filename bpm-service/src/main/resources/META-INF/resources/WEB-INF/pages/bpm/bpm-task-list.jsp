<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul class="nav nav-tabs nav-stacked" style="margin-bottom: 0px;">
	<s:iterator value="#request.tasks" status="s" var="item">
		<li><a href="javascript:void(0)"
			onclick="$.popupViewDialog('${base}/profile/pub-post!view?id=<s:property value="#item.id" />')"> <s:property
					value="#item.pdname" /> - <s:property value="#item.name" /> <span class="pull-right"> <s:property
						value="#item.createTime" /></span>
		</a></li>
	</s:iterator>
</ul>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid">
	<table class="table">
		<thead>
			<tr>
				<th class="span1">序号</th>
				<th class="span3">时间</th>
				<th class="span2">操作</th>				
				<th class="span3">源状态</th>
				<th class="span3">新状态</th>
				<th>操作摘要</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="item" value="model.xsTransferReqLogs" status="s">
				<tr>
					<td><s:property value="#s.count" /></td>
					<td><s:date name="#item.createdDate"/></td>
					<td><s:property value="#item.lastOperationEventLabel" /></td>
					<td><s:property value="#item.revPreStateLabel" /></td>
					<td><s:property value="#item.revStateLabel" /></td>
					<td><s:property value="#item.lastOperationExplain" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
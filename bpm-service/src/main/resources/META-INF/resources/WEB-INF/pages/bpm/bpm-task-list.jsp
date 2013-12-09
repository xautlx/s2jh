<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="table table-hover" style="width: 100%; margin-bottom: 0px">
	<tbody>
		<s:iterator value="#request.tasks" status="s" var="item">
			<tr>
				<td style="width: 20px; text-align: center"><s:property value="#s.count" /></td>
				<td><a href="${base}/bpm/bpm-task!show?id=<s:property value='#item.id' />" target="_blank"> <s:property
							value="#item.pdname" /> - <s:property value="#item.name" /></a></td>
				<td style="width: 120px; text-align: right"><s:property value="#item.createTime" /></td>
				<td style="width: 60px; text-align: center"><s:if test="%{#item.needClaim}">
						<button class="btn btn-link" type="button">签收</button>
					</s:if> <s:else>
						<button class="btn btn-link" type="button">办理</button>
					</s:else></td>

			</tr>
		</s:iterator>
	</tbody>
</table>
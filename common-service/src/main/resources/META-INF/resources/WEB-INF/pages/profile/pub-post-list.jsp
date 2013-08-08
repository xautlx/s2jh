<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="table table-condensed table-striped">
	<thead>
		<tr>
			<th class="span1">序号</th>
			<th class="span1">状态</th>
			<th>标题</th>
			<th class="span3">发布时间</th>
			<th class="span3">到期时间</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="%{model}" status="s" var="item">
			<tr>
				<td><s:property value="#s.count" /></td>
				<td><s:if test="#item.extraAttributes['readed']">
						<i class="icon-folder-open"></i>
					</s:if> <s:else>
						<i class="icon-folder-close"></i>
					</s:else></td>
				<td><a href="javascript:void(0)"
					onclick="$.popupViewDialog('${base}/profile/pub-post!view?id=<s:property value="#item.id" />')">
						<s:property value="#item.htmlTitle" escapeHtml="false" />
				</a></td>
				<td><s2:date name="#item.publishTime" format="timestamp" /></td>
				<td><s2:date name="#item.expireTime" format="timestamp" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="table table-striped table-advance table-bordered table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>用户登录帐号</th>
			<th>昵称</th>
			<th>关联时间</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="item" value="#request.users" status="s">
			<tr>
				<td><s:property value="%{#s.count}" /></td>
				<td><s:property value="%{#item.user.signinid}" /></td>
				<td><s:property value="%{#item.user.nick}" /></td>
				<td><s:property value="%{#item.createdDate}" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<%@ include file="/common/ajax-footer.jsp"%>
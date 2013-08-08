<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel disableItemsExcludeFirst="%{isBlank(#parameters.id)}">
	<ul>
		<li><a
			href="${base}/auth/user!<s:property value="%{persistentedModel?'update':'create'}"/>?id=<s:property value='#parameters.id'/>">
				<span>基本信息</span>
		</a></li>
		<li><a href="${base}/auth/user!forward?_to_=roles&id=<s:property value='#parameters.id'/>">
				<span>角色关联</span>
		</a></li>
		<li><a
			href="${base}/auth/user!forward?_to_=privileges&id=<s:property value='#parameters.id'/>"> <span>权限汇总</span>
		</a></li>
		<li><a href="${base}/auth/user!forward?_to_=menus&id=<s:property value='#parameters.id'/>">
				<span>菜单汇总</span>
		</a></li>
		<li><a href="${base}/auth/user!revisionIndex?id=<s:property value='#parameters.id'/>"> <span>修改记录</span>
		</a></li>
	</ul>
</s2:tabbedpanel>
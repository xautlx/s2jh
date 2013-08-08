<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel id="%{'reportDefInputTabs'+id}" disableItemsExcludeFirst="%{isBlank(id)}">
	<ul>
		<li><a
			href="${base}/rpt/report-def!<s:property value="%{persistentedModel?'update':'create'}"/>?id=<s:property value='#parameters.id'/>">
				<span>基本信息</span>
		</a></li>
		<li><a
			href="${base}/rpt/report-def!forward?_to_=params&id=<s:property value='#parameters.id'/>"> <span>报表参数</span>
		</a></li>
		<li><a
			href="${base}/rpt/report-def!forward?_to_=roles&id=<s:property value='#parameters.id'/>"> <span>角色关联</span>
		</a></li>
	</ul>
</s2:tabbedpanel>
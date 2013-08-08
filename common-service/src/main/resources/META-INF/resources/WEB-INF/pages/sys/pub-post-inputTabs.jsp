<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel disableItemsExcludeFirst="%{isBlank(#parameters.id)}">
	<ul>
	    <li><a href="${base}/sys/pub-post!<s:property value="%{persistentedModel?'update':'create'}"/>?id=<s:property value='#parameters.id'/>">
	    <span>基本信息</span>
	    </a></li>
	    <li><a href="${base}/sys/pub-post!forward?_to_=readLogs&id=<s:property value='#parameters.id'/>">
	    <span>阅读记录</span>
	    </a></li>
	</ul>
</s2:tabbedpanel>
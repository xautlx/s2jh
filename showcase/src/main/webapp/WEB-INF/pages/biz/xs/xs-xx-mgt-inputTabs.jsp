<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel disableItemsExcludeFirst="%{isBlank(#parameters.id)}">
	<ul>
	    <li><a href="${base}/biz/xs/xs-jbxx!<s:property value="%{isBlank(#parameters.id)?'create':'update'}"/>?id=<s:property value='#parameters.id'/>">
	    <span>基本信息</span>
	    </a></li>
        <li><a href="${base}/biz/xs/xs-fzxx!update?id=<s:property value='#parameters.id'/>">
        <span>辅助信息</span>
        </a></li>
        <li><a href="${base}/biz/xs/xs-kzxx!update?id=<s:property value='#parameters.id'/>">
        <span>扩展信息</span>
        </a></li>        
        <li><a href="${base}/biz/xs/xs-lxxx!update?id=<s:property value='#parameters.id'/>">
        <span>联系信息</span>
        </a></li> 
        <li><a href="${base}/biz/xs/xs-xx-mgt!revisionIndex?id=<s:property value='#parameters.id'/>">
        <span>修改记录</span>
        </a></li>                        
	</ul>
</s2:tabbedpanel>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel>
    <ul>
        <li><a href="${base}/biz/xs/xs-jbxx!view?id=<s:property value='#parameters.id'/>">
        <span>基本信息</span>
        </a></li>
        <li><a href="${base}/biz/xs/xs-fzxx!view?id=<s:property value='#parameters.id'/>">
        <span>辅助信息</span>
        </a></li>
        <li><a href="${base}/biz/xs/xs-kzxx!view?id=<s:property value='#parameters.id'/>">
        <span>扩展信息</span>
        </a></li>
        <li><a href="${base}/biz/xs/xs-lxxx!view?id=<s:property value='#parameters.id'/>">
        <span>联系信息</span>
        </a></li>                
    </ul>
</s2:tabbedpanel>
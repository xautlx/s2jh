<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel>
    <ul>
        <li><a href="${base}${model_path}/${entity_name_field}!view?id=<s:property value='#parameters.id'/>">
        <span>基本信息</span>
        </a></li>
        <%-- 去掉注释添加功能Tab
        <li><a href="${base}${model_path}/${entity_name_field}!forward?_to_=TODO&id=<s:property value='#parameters.id'/>">
        <span>TODO关联</span>
        </a></li>
        --%>
    </ul>
</s2:tabbedpanel>
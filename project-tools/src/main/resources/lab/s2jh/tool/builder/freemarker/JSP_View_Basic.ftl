<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
<#list entityFields as entityField><#if entityField.edit>
        <div class="row-fluid">
            <div class="span6">
                <#if entityField.fieldType=='Boolean'>
                <s2:property value="#application.enums.booleanLabel[${entityField.fieldName}]" label="${entityField.title}"/>
                <#elseif entityField.enumField>
                <s2:property value="#application.${entityField.uncapitalizeFieldType}Map[${entityField.fieldName}]" label="${entityField.title}"/>
                <#elseif entityField.fieldType=='Date'>
                <s2:date name="${entityField.fieldName}" format="timestamp" label="${entityField.title}"/>                       
                <#else>
                <s2:property value="${entityField.fieldName}" label="${entityField.title}"/>
                </#if>
            </div>
        </div>
        </#if>
        </#list>            
    </div>    
</div>
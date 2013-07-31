<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                <s2:property value="code" label="代码"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="title" label="名称"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.enums.booleanLabel[disabled]" label="禁用标识"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.enums.booleanLabel[locked]" label="锁定标识"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="description" label="描述"/>
            </div>
        </div>
    </div>    
</div>
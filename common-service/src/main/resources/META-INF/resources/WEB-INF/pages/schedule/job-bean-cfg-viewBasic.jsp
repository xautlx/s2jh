<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="jobClass" label="任务类全名"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="cronExpression" label="CRON表达式"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.booleanLabelMap[autoStartup]" label="自动初始运行"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.booleanLabelMap[logRunHist]" label="启用历史记录"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.booleanLabelMap[runWithinCluster]" label="集群运行模式"/>
            </div>
        </div>        
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="description" label="描述"/>
            </div>
        </div>
    </div>    
</div>
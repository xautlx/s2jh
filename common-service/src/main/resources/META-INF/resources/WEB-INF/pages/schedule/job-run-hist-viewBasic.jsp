<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="jobName" label="Job名称"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="nodeId" label="触发节点标识"/>
            </div>
        </div>        
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="jobGroup" label="Job分组"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="jobClass" label="Job类"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="triggerName" label="Trigger名称"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="triggerGroup" label="Trigger分组 "/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:date name="fireTime" format="timestamp" label="本次触发时间"/>                       
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:date name="previousFireTime" format="timestamp" label="上次触发时间"/>                       
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:date name="nextFireTime" format="timestamp" label="下次触发时间"/>                       
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="refireCount" label="触发次数"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.enums.booleanLabel[exceptionFlag]" label="异常标识"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="result" label="执行结果"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <s2:property value="exceptionStack" label="异常日志" pre="true"/>
            </div>
        </div>
    </div>    
</div>
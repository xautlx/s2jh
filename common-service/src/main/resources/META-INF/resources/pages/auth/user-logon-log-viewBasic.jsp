<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                <s2:property value="username" label="登录账号"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="userid" label="账户编号"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:date name="logonTime" format="date" label="登录时间"/>                       
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:date name="logoutTime" format="date" label="登出时间"/>                       
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="logonTimeLength" label="登录时长"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="logonTimes" label="登录次数"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="userAgent" label="userAgent"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="xForwardFor" label="xForwardFor"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="localAddr" label="localAddr"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="localName" label="localName"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="localPort" label="localPort"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="remoteAddr" label="remoteAddr"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="remoteHost" label="remoteHost"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="remotePort" label="remotePort"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="serverIP" label="serverIP"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="httpSessionId" label="Session编号"/>
            </div>
        </div>
    </div>    
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
        <div class="row-fluid">
            <div class="span12">
                <s2:property value="htmlTitle" label="标题" escapeHtml="false"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:date name="publishTime" format="timestamp" label="发布时间"/>                       
            </div>
            <div class="span6">
                <s2:date name="expireTime" format="timestamp" label="到期时间"/>                       
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <s2:property value="htmlContent" label="公告内容" escapeHtml="false"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <s2:file value="r2File" label="关联附件"/>
            </div>
        </div>        
    </div>    
</div>
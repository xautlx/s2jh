<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label">代码</label>
                    <div class="controls">
                        <s:property value="code" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label">标题</label>
                    <div class="controls">
                        <s:property value="name" />
                    </div>
                </div> 
            </div>
        </div>
    </div>    
</div>
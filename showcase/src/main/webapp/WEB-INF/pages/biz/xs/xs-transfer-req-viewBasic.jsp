<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="stauts">状态</label>
                    <div class="controls">
                        <s:property value="#application.enums.xsTransferReqStatusEnum[stauts]" />
                    </div>
                </div>
            </div>
        </div>
    </div>    
</div>
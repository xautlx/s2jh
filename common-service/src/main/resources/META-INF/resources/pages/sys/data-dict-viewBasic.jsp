<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                <s2:property value="category" label="分类"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="code" label="代码"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="parentCode" label="父代码"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="key1Value" label="Key1定义"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="key2Value" label="Key2定义"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="data1Value" label="数据1设定"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="data3Value" label="数据3设定"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="data2Value" label="数据2设定"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="#application.enums.booleanLabel[disabled]" label="禁用标识"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <s2:property value="orderRank" label="排序号"/>
            </div>
        </div>
    </div>    
</div>
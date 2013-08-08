<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xh">学号</label>
                    <div class="controls">
                        <s:property value="xh" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfjss">是否寄宿生</label>
                    <div class="controls">
                        <s:property value="#application.enums.booleanLabel[sfjss]" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfdszn">是否独生子女</label>
                    <div class="controls">
                        <s:property value="#application.enums.booleanLabel[sfdszn]" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfldrk">是否流动人口</label>
                    <div class="controls">
                        <s:property value="#application.enums.booleanLabel[sfldrk]" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfsgxqjy">是否受过学前教育 </label>
                    <div class="controls">
                        <s:property value="#application.enums.booleanLabel[sfsgxqjy]" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfsqzn">是否随迁子女 </label>
                    <div class="controls">
                        <s:property value="#application.enums.booleanLabel[sfsqzn]" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="ljzjhcz">离家最近火车站</label>
                    <div class="controls">
                        <s:property value="ljzjhcz" />
                    </div>
                </div> 
            </div>
        </div>
    </div>    
</div>
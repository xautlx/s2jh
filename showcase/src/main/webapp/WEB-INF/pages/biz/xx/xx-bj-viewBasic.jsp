<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="bh">班号</label>
                    <div class="controls">
                        <s:property value="bh" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="bjmc">班级名称</label>
                    <div class="controls">
                        <s:property value="bjmc" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="nj">年级</label>
                    <div class="controls">
                        <s:property value="nj" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxdm">所属学校</label>
                    <div class="controls">
                        <s:property value="xxdm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="zym">专业码</label>
                    <div class="controls">
                        <s:property value="zym" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="ssnj">所属年级</label>
                    <div class="controls">
                        <s:property value="ssnj" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xz">学制</label>
                    <div class="controls">
                        <s:property value="xz" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfssmzsyjxb">是否少数民族双语教学班</label>
                    <div class="controls">
                        <s:property value="#application.enums.booleanLabel[sfssmzsyjxb]" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="syjxmsm">双语教学模式</label>
                    <div class="controls">
                        <s:property value="syjxmsm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="syjxssmzyym">双语教学的少数民族语言</label>
                    <div class="controls">
                        <s:property value="syjxssmzyym" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="bzrgh">班主任工号式</label>
                    <div class="controls">
                        <s:property value="bzrgh" />
                    </div>
                </div> 
            </div>
        </div>
    </div>    
</div>
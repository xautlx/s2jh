<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s2:tabbedpanel>
    <ul>
        <li><a href="#loggingExceptionStack"> <span>异常堆栈</span>
        </a></li>
        <li><a href="#loggingProperties"> <span>关联属性</span>
        </a></li>
        <li><a href="#loggingHandle"> <span>日志处理</span>
        </a></li>
    </ul>
    <div id="loggingExceptionStack">
        <div class="well">
            <div class="row-fluid">
                <div class="span12">
                    <s2:textarea name="exceptionStackView" value="%{exceptionStack}" rows="21" readonly="true" />
                </div>
            </div>
        </div>
    </div>
    <div id="loggingHandle">
        <div class="container-fluid data-edit">
            <s2:form cssClass="form-horizontal" method="post" action="/sys/logging-event!doUpdate">
                <s:hidden name="id" />
                <s:hidden name="version" />
                <s:token />
                <div class="row-fluid">
                    <div class="toolbar">
                        <div class="toolbar-inner">
                            <button type="button" class="btn btn-submit" callback-tab="loggingEventIndexTabs"
                                callback-grid="loggingEventListDiv">
                                <i class="icon-ok"></i> 保存
                            </button>
                            <button type="button" class="btn btn-submit submit-post-close"
                                callback-tab="loggingEventIndexTabs" callback-grid="loggingEventListDiv">
                                <i class="icon-check"></i> 保存并关闭
                            </button>
                            <button type="reset" class="btn">
                                <i class="icon-repeat"></i> 重置
                            </button>
                        </div>
                    </div>
                </div>
                <div class="well">
                    <div class="row-fluid">
                        <div class="span12">
                            <s2:radio name="state" list="#application.enums.loggingHandleStateEnum" label="处理状态" />
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s2:textarea name="operationExplain" rows="2" label="处理说明" />
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span6">
                            <div class="control-group">
                                <label class="control-label">最后更新者</label>
                                <div class="controls">
                                    <s:property value="lastModifiedBy" />
                                </div>
                            </div>
                        </div>
                        <div class="span6">
                            <div class="control-group">
                                <label class="control-label">最后更新时间</label>
                                <div class="controls">
                                    <s:property value="lastModifiedDate" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </s2:form>
        </div>
    </div>
    <div id="loggingProperties">
        <table class="table table-condensed table-bordered table-hover table-list">
            <thead>
                <tr>
                    <th style="width: 5%">序号</th>
                    <th style="width: 30%">属性代码</th>
                    <th>属性值</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="loggingEventProperties" status="status" id="item">
                    <tr>
                        <td style="text-align: center;"><s:property value="%{#status.count}" /></td>
                        <td><s:property value="%{#item.id.mappedKey}" /></td>
                        <td><pre><s:property value="%{#item.mappedValue}" /></pre></td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
    </div>
</s2:tabbedpanel>
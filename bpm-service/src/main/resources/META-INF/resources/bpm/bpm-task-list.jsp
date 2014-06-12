<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="portlet box green tasks-widget portlet-tasks">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-tasks"></i>最近待办任务<span class="badge badge-warning"><s:property value="#request.tasks.size()" /></span>
		</div>
		<div class="tools">
			<a href="" class="reload"></a>
		</div>
		<div class="actions">
			<div class="btn-group">
				<a data-close-others="true" data-hover="dropdown" data-toggle="dropdown" href="javascript:;"
					class="btn btn-xs green"> 过滤 <i class="fa fa-angle-down"></i>
				</a>
				<div class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
					<label><input type="checkbox" checked="true" id="chk-task-user" onclick="clkChkTask('false')" /> 个人任务</label> <label><input
						type="checkbox" checked="true" id="chk-task-candidate" onclick="clkChkTask('true')" /> 候选任务</label>
				</div>
			</div>
		</div>
	</div>
	<div class="portlet-body">
		<div class="scroller" style="height: 300px;" data-always-visible="1" data-rail-visible="0">
			<ul class="feeds" id="dashboard-task-list">
				<s:iterator value="#request.tasks" status="s" var="item">
					<li need-claim="<s:property value='#item.candidate' />" id="<s:property value='#item.id' />"><a
						class="ajaxify" href="javascript:;" id="list-task-<s:property value='#item.id' />"
						rel="address:/bpm/bpm-task!show?taskId=<s:property value='#item.id' />&candidate=<s:property value='#item.candidate' />"
						title='<s:property value="#item.name" />'>
							<div class="col1">
								<div class="cont">
									<div class="cont-col1">
										<s:if test="%{#item.candidate}">
											<div class="label label-sm label-info">
												<i class="fa fa-group"></i>
											</div>
										</s:if>
										<s:else>
											<div class="label label-sm label-primary">
												<i class="fa fa-user"></i>
											</div>
										</s:else>
									</div>
									<div class="cont-col2">
										<div class="desc">
											<s:property value="#item.pdname" />
											-
											<s:property value="#item.name" />
										</div>
										<div class="biz-key" style="padding-left: 35px">
											<s:property value="#item.bizKey" />
										</div>
									</div>
								</div>
							</div>
							<div class="col2">
								<div class="date">
									<s:date name="#item.createTime" format="MM-dd HH:mm" />
								</div>
							</div>
					</a></li>
				</s:iterator>
			</ul>
		</div>
		<div class="scroller-footer">
			<div class="pull-right">
				<a href="javascript:;">查看全部... <i class="m-icon-swapright m-icon-gray"></i></a> &nbsp;
			</div>
		</div>
	</div>
</div>
<script src="${base}/bpm/bpm-task-list.js" type="text/javascript" />
<%@ include file="/common/ajax-footer.jsp"%>
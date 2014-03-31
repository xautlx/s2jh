<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">高级管理功能列表</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active">
			<div class="list-group">
				<a class="list-group-item" href="#" data-toggle="dynamic-tab" data-url="${base}/sys/util!forward?_to_=cache"
					data-title="缓存管理">
					<div class="note note-info" style="margin-bottom: 0">
						<h4 class="block">缓存管理</h4>
						<p>
							为了系统运行效率，系统会基于Hibernate和Spring的Cache支持尽可能缓存数据 <br>此功能主要用于直接修改数据库数据后，通知缓存框架移除选取范围的缓存数据从而加载最新数据库数据.
						</p>
					</div>
				</a> <a class="list-group-item" href="#" data-toggle="dynamic-tab" data-url="${base}/sys/util!forward?_to_=logger"
					data-title="日志管理">
					<div class="note note-info" style="margin-bottom: 0">
						<h4 class="block">日志管理</h4>
						<p>
							此功能主要用于在应用运行过程中动态修改Logger日志级别从而实现在线Debug调试系统日志信息以便实时进行一些线上问题分析排查. <br>在调低日志级别问题排查完毕后，最好把日志级别调整会预设较高级别以避免大量日志信息影响系统运行效率
						</p>
					</div>
				</a>
			</div>
		</div>
	</div>
</div>
<%@ include file="/common/ajax-footer.jsp"%>


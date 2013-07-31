<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
	<div class="well form-horizontal">
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="regionCode">行政区划代码</label>
					<div class="controls">
						<s:property value="regionCode" />
						<s:property value="%{getRegionPaths(regionCode)}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="regionDesc">行政区划名称</label>
					<div class="controls">
						<s:property value="regionDesc" />
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="regionShort">行政区划简称</label>
					<div class="controls">
						<s:property value="regionShort" />
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="regionEdu">教育行政部门名称</label>
					<div class="controls">
						<s:property value="regionEdu" />
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="parentcode">父行政区划代码</label>
					<div class="controls">
						<a href="javascript:void(0)" title="查看行政区划信息"
							onclick="$.popupViewDialog('${base}/biz/sys/region-code!view?regionCode=<s:property value="parentcode" />')">
							<s:property value="parentcode" />
						</a>
						<s:property value="%{getRegionPaths(parentcode)}" />
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="dqlx">地区类型</label>
					<div class="controls">
						<s:property value="dqlx" />
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="control-group">
					<label class="control-label" for="enabled">状态</label>
					<div class="controls">
						<s:property value="booleanLabelMap[enabled]" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
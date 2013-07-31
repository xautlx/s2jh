<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post" action="/biz/xs/xs-transfer-req!doTransferIn">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<s2:button cssClass="btn btn-submit submit-post-close" disabled="disallowTransferIn"
						callback-tab="xsTransferReqIndexTabs" callback-grid="xsTransferReqListDiv">
						<i class="icon-check"></i> 转入学籍
                    </s2:button>
					<s2:button type="reset" cssClass="btn" disabled="disallowTransferIn">
						<i class="icon-repeat"></i> 重置
                    </s2:button>
				</div>
			</div>
		</div>
		<div class="well">
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label">状态</label>
						<div class="controls data-view">
							<s:property value="#application.enums.xsTransferReqStateEnum[state]" />
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label">申请时间</label>
						<div class="controls data-view">
							<s:date name="reqTime" format="yyyy-MM-dd HH:mm:ss" />
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label">异动学生</label>
						<div class="controls data-view">
							<a href="javascript:void(0)" title="查看"
								onclick="$.popupViewDialog('${base}/biz/xs/xs-jbxx!viewTabs?id==<s:property value='xsJbxx.id'/>')"><s:property
									value="xsJbxx.displayLabel" /></a>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label">转出学校</label>
						<div class="controls data-view">
							<a href="javascript:void(0)" title="查看"
								onclick="$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=<s:property value='sourceXx.id'/>')"><s:property
									value="sourceXx.displayLabel" /></a>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label">转出学校主管</label>
						<div class="controls data-view">
							<s:property value="sourceXx.sszgdwm" />
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label">转入学校</label>
						<div class="controls data-view">
							<a href="javascript:void(0)" title="查看"
								onclick="$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=<s:property value='targetXx.id'/>')"><s:property
									value="targetXx.displayLabel" /></a>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label">转入学校主管</label>
						<div class="controls data-view">
							<s:property value="targetXx.sszgdwm" />
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<biz:xxbjinput name="bh" label="转入班级" validator="" requiredLabel="true" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xh" label="新学号" validator="" requiredLabel="true" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textarea name="operationExplain" rows="5" label="说明" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
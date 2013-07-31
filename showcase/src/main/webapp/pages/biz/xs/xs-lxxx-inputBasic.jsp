<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post" action="/biz/xs/xs-lxxx!doUpdate">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="xsJbxxIndexTabs">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="xsJbxxIndexTabs">
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
					<s2:textfield name="xzz" label="现地址" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="yzbm" label="邮政编码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="lxdh" label="联系电话" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="txdz" label="通信地址" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="dzxx" label="电子信箱" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="zydz" label="主页地址" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textfield name="jstxh" label="即时通讯号" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
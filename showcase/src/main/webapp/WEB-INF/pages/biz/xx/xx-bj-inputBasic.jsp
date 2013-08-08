<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/biz/xx/xx-bj!doUpdate':'/biz/xx/xx-bj!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="xxBjIndexTabs"
						callback-grid="xxBjListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="xxBjIndexTabs"
						callback-grid="xxBjListDiv">
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
				<div class="span6">
					<s2:textfield name="bh" label="班号" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="bjmc" label="班级名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="nj" label="年级" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxdm" label="所属学校" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="zym" label="专业码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="ssnj" label="所属年级" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xz" label="学制" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="sfssmzsyjxb" list="#application.enums.booleanLabel" label="是否少数民族双语教学班" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="syjxmsm" label="双语教学模式" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="syjxssmzyym" label="双语教学的少数民族语言" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="bzrgh" label="班主任工号式" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
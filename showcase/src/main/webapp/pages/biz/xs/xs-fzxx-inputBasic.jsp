<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post" action="/biz/xs/xs-fzxx!doUpdate">
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
				<div class="span6">
					<s2:textfield name="ywxm" label="英文姓名" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xmpy" label="姓名拼音" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="cym" label="曾用名" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="sfzjyxq" label="身份证件有效期" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="xyzjm" label="信仰宗教" list="%{findEnumValuesByType('ZD_GB_XYZJM')}" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="xxm" label="血型" list="%{findEnumValuesByType('ZD_GB_XXM')}" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="hkszd" label="户口所在地" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="hkxzm" label="户口性质" list="%{findEnumValuesByType('ZD_GB_HKLBM')}" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textarea name="tc" rows="5" label="特长" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
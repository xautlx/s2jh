<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post" action="/biz/xs/xs-kzxx!doUpdate">
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
					<s2:radio name="sfjss" list="#application.enums.booleanLabel" label="是否寄宿生" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:radio name="sfdszn" list="#application.enums.booleanLabel" label="是否独生子女" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:radio name="sfldrk" list="#application.enums.booleanLabel" label="是否流动人口" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:radio name="sfsgxqjy" list="#application.enums.booleanLabel" label="是否受过学前教育 " />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:radio name="sfsqzn" list="#application.enums.booleanLabel" label="是否随迁子女 " />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:textarea name="ljzjhcz" rows="2" label="离家最近火车站" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
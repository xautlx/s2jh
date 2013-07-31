<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{isBlank(#parameters.id)?'/biz/xx/xx-jcxx!doCreate':'/biz/xx/xx-jcxx!doUpdate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="xxJcxxIndexTabs"
						callback-grid="xxJcxxListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="xxJcxxIndexTabs"
						callback-grid="xxJcxxListDiv">
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
					<s2:textfield name="xxdm" label="学校识别码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxjgdm" label="学校(机构)代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxmc" label="学校名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxywmc" label="学校英文名称" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xzqhm" label="行政区划码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxdz" label="学校地址" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxyzbm" label="学校邮政编码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="sdgljyxzbm" label="属地管理教育行政部门代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="sszgdwm" label="学校所属主管教育行政部门代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxjbzm" label="举办者代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxbxlxm" label="办学类型代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="szdcxlxm" label="城乡分类代码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxbbm2" label="学校举办者类别码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xzxm" label="校长姓名" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xzsjhm" label="校长手机号码" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="lxdh" label="联系电话" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="czdh" label="传真电话" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="dzxx" label="电子信箱" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="zydz" label="主页地址" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="xxbbm" label="学校办别码" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
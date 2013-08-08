<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/biz/xs/xs-jbxx!doUpdate':'/biz/xs/xs-jbxx!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn btn-submit" callback-tab="xsJbxxIndexTabs"
						callback-grid="xsJbxxListDiv">
						<i class="icon-ok"></i> 保存
					</button>
					<button type="button" class="btn btn-submit submit-post-close" callback-tab="xsJbxxIndexTabs"
						callback-grid="xsJbxxListDiv">
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
					<s2:textfield name="xh" label="学号" cssClass="input-xlarge" />
				</div>
				<div class="span6">
					<s2:textfield name="xm" label="姓名" cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:radio name="xbm" label="性别" list="%{findEnumValuesByType('ZD_GB_XBM')}"
						cssClass="input-xlarge" />
				</div>
				<div class="span6">
					<s2:datetextfield name="csrq" format="yyyyMMdd" label="出生日期" cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<s2:autocompletetextfield source="/biz/xx/xx-bj!autocomplete" label="班级" name="xxBjBh"
						value="%{xxBj.bh}" labelValue="%{xxBj.bjmc}" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<biz:regionselect list="#{}" name="csdm" label="出生地" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="jg" label="籍贯" cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="mzm" label="民族" list="%{findEnumValuesByType('ZD_GB_MZM')}"
						cssClass="input-xlarge" />
				</div>
				<div class="span6">
					<s2:comboselect name="gjdqm" label="国籍/地区" list="%{findEnumValuesByType('ZD_GB_GJDQM')}"
						cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="sfzjlxm" label="身份证件类型" list="%{findEnumValuesByType('ZD_BB_SFZJLXM')}"
						cssClass="input-xlarge" />
				</div>
				<div class="span6">
					<s2:textfield name="sfzjh" label="身份证件号" cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="gatqwm" label="港澳台侨外码" list="%{findEnumValuesByType('ZD_BB_GATQWM')}"
						cssClass="input-xlarge" />
				</div>
				<div class="span6">
					<s2:comboselect name="zzmmm" label="政治面貌" list="%{findEnumValuesByType('ZD_BB_ZZMMM')}"
						cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:comboselect name="jkzkm" label="健康状况" list="%{findEnumValuesByType('ZD_GB_JKZKM')}"
						cssClass="input-xlarge" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textfield name="grbsm" label="个人标识码" cssClass="input-xlarge" />
				</div>
			</div>
		</div>
	</s2:form>
</div>
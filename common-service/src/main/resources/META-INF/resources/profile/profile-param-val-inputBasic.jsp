<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<form
	class="form-horizontal form-bordered form-label-stripped form-validation form-profile-profile-param-val-inputBasic"
	action="${base}/profile/profile-param-val!doSave" method="post"
	data-editrulesurl="${base}/profile/profile-param-val!buildValidateRules">
	<s:hidden name="id" />
	<s:hidden name="version" />
	<s:token />
	<div class="form-actions">
		<button class="btn blue" type="submit" data-grid-reload=".grid-profile-profile-param-val-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
	<div class="form-body">
		<s:iterator value="reportParameters" status="s" var="reportParameter">
			<s:if test="%{#reportParameter.disabled==null || #reportParameter.disabled==false}">
				<s:set var="displayParam" value="%{#reportParameter.hidden==null||#reportParameter.hidden==false?'':'none'}"
					scope="page" />
				<s:property value='#displayParam' />
				<div class="row-fluid" style="display: <s:property value='#attr.displayParam'/>;">
					<div class="span6">
						<s:if test="type.name()=='DATE'">
							<s2:datetextfield name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
								validator="%{#reportParameter.fullValidateRules}" label="%{#reportParameter.title}" format="date"
								requiredLabel="%{#reportParameter.required}" />
						</s:if>
						<s:elseif test="type.name()=='TIMESTAMP'">
							<s2:datetextfield name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
								validator="%{#reportParameter.fullValidateRules}" label="%{#reportParameter.title}" format="timestamp"
								requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:elseif test="type.name()=='BOOLEAN'">
							<s2:radio name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
								validator="%{#reportParameter.fullValidateRules}" list="#application.enums.booleanLabel"
								label="%{#reportParameter.title}" requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:elseif test="type.name()=='SQL_LIST'">
							<s2:select validator="%{#reportParameter.fullValidateRules}"
								list="getSQLKeyValueMap(#reportParameter.listDataSource)" name="%{'reportParameterMap[\\''+code+'\\']'}"
								value="%{#reportParameter.defaultValue}" label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
								multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:elseif test="type.name()=='OGNL_LIST'">
							<s2:select validator="%{#reportParameter.fullValidateRules}"
								list="getOGNLKeyValueMap(#reportParameter.listDataSource)" name="%{'reportParameterMap[\\''+code+'\\']'}"
								value="%{#reportParameter.defaultValue}" label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
								multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:elseif test="type.name()=='ENUM'">
							<s2:select validator="%{#reportParameter.fullValidateRules}"
								list="getEnumKeyValueMap(#reportParameter.listDataSource)" name="%{'reportParameterMap[\\''+code+'\\']'}"
								value="%{#reportParameter.defaultValue}" label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
								multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:elseif test="type.name()=='DATA_DICT_LIST'">
							<s2:select validator="%{#reportParameter.fullValidateRules}"
								list="getDataDictKeyValueMap(#reportParameter.listDataSource)" name="%{'reportParameterMap[\\''+code+'\\']'}"
								value="%{#reportParameter.defaultValue}" label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
								multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:elseif test="type.name()=='MULTI_TEXT'">
							<s2:textarea validator="%{#reportParameter.fullValidateRules}" rows="3"
								name="%{'reportParameters[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
								label="%{#reportParameter.title}" requiredLabel="%{#reportParameter.required}" />
						</s:elseif>
						<s:else>
							<s2:textfield validator="%{#reportParameter.fullValidateRules}" name="%{'reportParameters[\\''+code+'\\']'}"
								value="%{#reportParameter.defaultValue}" label="%{#reportParameter.title}"
								requiredLabel="%{#reportParameter.required}" />
						</s:else>
					</div>
				</div>
			</s:if>
		</s:iterator>

		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">profileParamDef</label>
					<div class="controls">
						<s:textfield name="profileParamDef" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">user</label>
					<div class="controls">
						<s:textfield name="user" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn blue" type="submit" data-grid-reload=".grid-profile-profile-param-val-index">
			<i class="fa fa-check"></i> 保存
		</button>
		<button class="btn default btn-cancel" type="button">取消</button>
	</div>
</form>
<script src="${base}/profile/profile-param-val-inputBasic.js" />
<%@ include file="/common/ajax-footer.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<script type="text/javascript">
    $(function() {
        $("#jasperReportShowForm").formvalidate({
            submitHandler : function(form) {
                $(form).find("select[size][id^='right'] option").attr("selected", true);
                form.submit();
            }
        });
    });
</script>
<style type="text/css">
.form-horizontal .control-label {
	width: 100px;
}
</style>
</head>
<body>
	<div class="container-fluid data-edit">
		<s2:form cssClass="form-horizontal" method="get" id="jasperReportShowForm"
			action="/rpt/jasper-report!generate" target="_blank">
			<s:hidden name="report" value="%{#parameters.report}" />
			<div class="row-fluid">
				<div class="toolbar">
					<div class="toolbar-inner">
						<button type="button" class="btn btn-submit">
							<i class="icon-ok"></i> 生成报表
						</button>
						<button type="reset" class="btn">
							<i class="icon-repeat"></i> 重置
						</button>
					</div>
				</div>
			</div>
			<div class="well">
				<fieldset>
					<legend>
						<small>报表输入参数</small>
					</legend>
					<s:iterator value="reportParameters" status="s" var="reportParameter">

						<s:if test="%{#reportParameter.disabled==null || #reportParameter.disabled==false}">
							<s:set var="displayParam"
								value="%{#reportParameter.hidden==null||#reportParameter.hidden==false?'':'none'}"
								scope="page" />
							<s:property value='#displayParam' />
							<div class="row-fluid" style="display: <s:property value='#attr.displayParam'/>;">
								<div class="span6">
									<s:if test="type.name()=='DATE'">
										<s2:datetextfield name="%{'reportParameterMap[\\''+code+'\\']'}"
											value="%{#reportParameter.defaultValue}"
											validator="%{#reportParameter.fullValidateRules}" label="%{#reportParameter.title}"
											format="date" requiredLabel="%{#reportParameter.required}" />
									</s:if>
									<s:elseif test="type.name()=='TIMESTAMP'">
										<s2:datetextfield name="%{'reportParameterMap[\\''+code+'\\']'}"
											value="%{#reportParameter.defaultValue}"
											validator="%{#reportParameter.fullValidateRules}" label="%{#reportParameter.title}"
											format="timestamp" requiredLabel="%{#reportParameter.required}" />
									</s:elseif>
									<s:elseif test="type.name()=='BOOLEAN'">
										<s2:radio name="%{'reportParameterMap[\\''+code+'\\']'}"
											value="%{#reportParameter.defaultValue}"
											validator="%{#reportParameter.fullValidateRules}" list="#application.enums.booleanLabel"
											label="%{#reportParameter.title}" requiredLabel="%{#reportParameter.required}" />
									</s:elseif>
									<s:elseif test="type.name()=='SQL_LIST'">
										<s2:select validator="%{#reportParameter.fullValidateRules}"
											list="getSQLKeyValueMap(#reportParameter.listDataSource)"
											name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
											label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
											multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />

									</s:elseif>
									<s:elseif test="type.name()=='OGNL_LIST'">
										<s2:select validator="%{#reportParameter.fullValidateRules}"
											list="getOGNLKeyValueMap(#reportParameter.listDataSource)"
											name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
											label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
											multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
									</s:elseif>
									<s:elseif test="type.name()=='ENUM'">
										<s2:select validator="%{#reportParameter.fullValidateRules}"
											list="getEnumKeyValueMap(#reportParameter.listDataSource)"
											name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
											label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
											multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
									</s:elseif>
									<s:elseif test="type.name()=='DATA_DICT_LIST'">
										<s2:select validator="%{#reportParameter.fullValidateRules}"
											list="getDataDictKeyValueMap(#reportParameter.listDataSource)"
											name="%{'reportParameterMap[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
											label="%{#reportParameter.title}" size="%{multiSelectFlag?6:1}"
											multiple="%{multiSelectFlag?true:false}" requiredLabel="%{#reportParameter.required}" />
									</s:elseif>
									<s:elseif test="type.name()=='MULTI_TEXT'">
										<s2:textarea validator="%{#reportParameter.fullValidateRules}" rows="3"
											name="%{'reportParameters[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
											label="%{#reportParameter.title}" requiredLabel="%{#reportParameter.required}" />
									</s:elseif>
									<s:else>
										<s2:textfield validator="%{#reportParameter.fullValidateRules}"
											name="%{'reportParameters[\\''+code+'\\']'}" value="%{#reportParameter.defaultValue}"
											label="%{#reportParameter.title}" requiredLabel="%{#reportParameter.required}" />
									</s:else>
								</div>
							</div>
						</s:if>
					</s:iterator>
				</fieldset>
				<fieldset>
					<legend>
						<small>报表输出控制参数</small>
					</legend>
					<div class="row-fluid">
						<div class="span6">
							<s2:radio name="format" list="jasperOutputFormatMap" label="输出类型" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span6">
							<s2:radio name="contentDisposition" label="输出模式"
								list="#{'inline':'浏览器页面直接显示','attachment':'文件下载模式'}" />
						</div>
					</div>
				</fieldset>
			</div>
		</s2:form>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
</body>
</html>
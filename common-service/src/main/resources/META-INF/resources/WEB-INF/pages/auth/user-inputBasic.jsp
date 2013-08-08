<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid">
    <s2:form cssClass="form-horizontal" method="post"
        action="%{persistentedModel?'/auth/user!doUpdate':'/auth/user!doCreate'}">
        <s:hidden name="id" />
        <s:hidden name="version" />
        <s:token />
        <div class="row-fluid">
            <div class="toolbar">
                <div class="toolbar-inner">
                    <button type="button" class="btn btn-submit" callback-tab="userIndexTabs"
                        callback-grid="userListDiv">
                        <i class="icon-ok"></i> 保存
                    </button>
                    <button type="button" class="btn btn-submit submit-post-close" callback-tab="userIndexTabs"
                        callback-grid="userListDiv">
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
                    <s2:textfield name="signinid" label="登录账号" disabled="%{persistentedModel}" />
                </div>          
                <div class="span6">
                    <s2:textfield name="aclCode" label="机构代码" />
                </div>
            </div>
            <s:if test="model.id!=null&&model.id!=''">
                <div class="row-fluid">
                    <div class="span6">
                        <div class="control-group">
                            <label class="control-label" for="updatePassword<s:property value='#parameters.id'/>">
                            </label>
                            <div class="controls">
                                <label class="checkbox inline"> <input
                                    id="updatePassword<s:property value='#parameters.id'/>" type="checkbox" value="op"
                                    name="updatePassword"> 修改密码
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <script type="text/javascript">
                    $("#updatePassword<s:property value='#parameters.id'/>").click(function() {
                        $("#passwordDiv<s:property value='#parameters.id'/>").toggle();
                        $("#passwordDiv<s:property value='#parameters.id'/>").find("input").attr("disabled", !this.checked);
                    });
                </script>
                <div id="passwordDiv<s:property value='#parameters.id'/>" class="row-fluid hide">
                    <div class="span6">
                        <s2:password name="newpassword" label="设置密码" requiredLabel="true" validator="{minlength:3}"
                            disabled="true" />
                    </div>
                    <div class="span6">
                        <s2:password name="cfmpassword" label="重复密码" requiredLabel="true"
                            validator="{equalToByName:'newpassword'}" disabled="true" />
                    </div>
                </div>
            </s:if>
            <s:else>
                <div class="row-fluid">
                    <div class="span6">
                        <s2:password name="newpassword" label="设置密码" requiredLabel="true" validator="{minlength:3}" />
                    </div>
                    <div class="span6">
                        <s2:password name="cfmpassword" label="重复密码" requiredLabel="true"
                            validator="{equalToByName:'newpassword'}" />
                    </div>
                </div>
            </s:else>
            <div class="row-fluid">
                <div class="span6">
                    <s2:textfield name="nick" label="昵称" />
                </div>
                <div class="span6">
                    <s2:textfield name="email" label="电子邮件" />
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <s2:radio name="disabled" list="#application.enums.booleanLabel" label="禁用标识" />
                </div>
                <div class="span6">
                    <s2:datetextfield name="accountExpireTime" label="账号失效日期" format="date" />
                </div>
            </div>
        </div>
    </s2:form>
</div>
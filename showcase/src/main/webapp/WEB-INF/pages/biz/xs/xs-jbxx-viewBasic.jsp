<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxdm">学校代码</label>
                    <div class="controls">
                        <a href="javascript:void(0)" title="查看学校信息" onclick="$.popupViewDialog('${base}/biz/xx/xx-jcxx!view?xxdm=<s:property value="xxdm" />')">
                        <s:property value="xxdm" />
                        </a>
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xh">学号</label>
                    <div class="controls">
                        <s:property value="xh" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xm">姓名</label>
                    <div class="controls">
                        <s:property value="xm" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xbm">性别</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_GB_XBM').get(xbm)}" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="csrq">出生日期</label>
                    <div class="controls">
                        <s:property value="csrq" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="jg">籍贯</label>
                    <div class="controls">
                        <a href="javascript:void(0)" title="查看行政区划信息" onclick="$.popupViewDialog('${base}/biz/sys/region-code!view?regionCode=<s:property value="jg" />')">
                        <s:property value="jg" />
                        </a>
                        <s:property value="%{getRegionPaths(jg)}" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="csdm">出生地</label>
                    <div class="controls">
                        <a href="javascript:void(0)" title="查看行政区划信息" onclick="$.popupViewDialog('${base}/biz/sys/region-code!view?regionCode=<s:property value="csdm" />')">
                        <s:property value="csdm" />
                        </a>
                        <s:property value="%{getRegionPaths(csdm)}" />
                    </div>
                </div> 
            </div>            
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="mzm">民族</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_GB_MZM').get(mzm)}" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="gjdqm">国籍/地区</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_GB_GJDQM').get(gjdqm)}" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="gatqwm">港澳台侨外码</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_BB_GATQWM').get(gatqwm)}" />
                    </div>
                </div> 
            </div>
        </div>        
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfzjlxm">身份证件类型</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_BB_SFZJLXM').get(sfzjlxm)}" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sfzjh">身份证件号</label>
                    <div class="controls">
                        <s:property value="sfzjh" />
                    </div>
                </div> 
            </div>
        </div>

        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="zzmmm">政治面貌</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_BB_ZZMMM').get(zzmmm)}" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="jkzkm">健康状况</label>
                    <div class="controls">
                        <s:property value="%{findEnumValuesByType('ZD_GB_JKZKM').get(jkzkm)}" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="grbsm">个人标识码</label>
                    <div class="controls">
                        <s:property value="grbsm" />
                    </div>
                </div> 
            </div>
        </div>
    </div>    
</div>
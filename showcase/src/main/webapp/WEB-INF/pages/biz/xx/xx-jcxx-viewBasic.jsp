<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<div class="container-fluid data-view">
    <div class="well form-horizontal">
                <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxdm">学校识别码</label>
                    <div class="controls">
                        <s:property value="xxdm" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxjgdm">学校(机构)代码</label>
                    <div class="controls">
                        <s:property value="xxjgdm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxmc">学校名称</label>
                    <div class="controls">
                        <s:property value="xxmc" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxywmc">学校英文名称</label>
                    <div class="controls">
                        <s:property value="xxywmc" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xzqhm">行政区划码</label>
                    <div class="controls">
                        <a href="javascript:void(0)" title="查看行政区划信息" onclick="$.popupViewDialog('${base}/biz/sys/region-code!view?regionCode=<s:property value="xzqhm" />')">
                        <s:property value="xzqhm" />
                        </a>
                        <s:property value="%{getRegionPaths(xzqhm)}" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxdz">学校地址</label>
                    <div class="controls">
                        <s:property value="xxdz" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxyzbm">学校邮政编码</label>
                    <div class="controls">
                        <s:property value="xxyzbm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sdgljyxzbm">属地管理教育行政部门代码</label>
                    <div class="controls">
                        <s:property value="sdgljyxzbm" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="sszgdwm">学校所属主管教育行政部门代码</label>
                    <div class="controls">
                        <s:property value="sszgdwm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxbbm2">学校举办者类别码</label>
                    <div class="controls">
                        <s:property value="xxbbm2" />
                    </div>
                </div> 
            </div>        
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxjbzm">举办者代码</label>
                    <div class="controls">
                        <s:property value="xxjbzm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxbxlxm">办学类型代码</label>
                    <div class="controls">
                        <s:property value="xxbxlxm" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="szdcxlxm">城乡分类代码</label>
                    <div class="controls">
                        <s:property value="szdcxlxm" />
                    </div>
                </div> 
            </div>             
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xzxm">校长姓名</label>
                    <div class="controls">
                        <s:property value="xzxm" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xzsjhm">校长手机号码</label>
                    <div class="controls">
                        <s:property value="xzsjhm" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="lxdh">联系电话</label>
                    <div class="controls">
                        <s:property value="lxdh" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="czdh">传真电话</label>
                    <div class="controls">
                        <s:property value="czdh" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="dzxx">电子信箱</label>
                    <div class="controls">
                        <s:property value="dzxx" />
                    </div>
                </div> 
            </div>
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="zydz">主页地址</label>
                    <div class="controls">
                        <s:property value="zydz" />
                    </div>
                </div> 
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                 <div class="control-group">
                    <label class="control-label" for="xxbbm">学校办别码</label>
                    <div class="controls">
                        <s:property value="xxbbm" />
                    </div>
                </div> 
            </div>
        </div>
    </div>    
</div>
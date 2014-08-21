<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="table table-striped table-advance table-bordered table-hover">
	<thead>
		<tr>
			<th>权限分类</th>
			<th>角色代码</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="item" value="#request.privileges" status="s">
			<tr>
				<td class="active" style="width: 150px"><s:property value="%{#item.key}" /></td>
				<td class="checkbox-privileges"><s:iterator var="child" value="#item.value">
						<div class="col-md-2">
							<span class="<s:property value="%{#child.extraAttributes.related?'label label-success':''}" />"><s:property
									value="%{#child.title}" /></span>
						</div>
					</s:iterator></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<div class="row">
	<div class="col-md-6">
		<ul class="breadcrumb">
			<li class="active">用户菜单列表</li>
		</ul>
		<ul id="userMenuTree<s:property value='#parameters.id'/>" class="ztree"></ul>
	</div>
	<div class="col-md-6">
		<ul class="breadcrumb">
			<li class="active">所有菜单列表</li>
		</ul>
		<ul id="allMenuTree<s:property value='#parameters.id'/>" class="ztree"></ul>
	</div>
</div>
<script type="text/javascript">
    $(function() {

        $.getJSON("${base}/sys/menu!list", function(data) {
            $.fn.zTree.init($("#allMenuTree<s:property value='#parameters.id'/>"), {
                callback : {
                    onClick : function(event, treeId, treeNode) {
                        event.stopPropagation();
                        event.preventDefault();
                        return false;
                    }
                }
            }, data);
        });

        $.getJSON("${base}/auth/user!menus?id=<s:property value='#parameters.id'/>", function(data) {
            $.fn.zTree.init($("#userMenuTree<s:property value='#parameters.id'/>"), {
                callback : {
                    onClick : function(event, treeId, treeNode) {
                        event.stopPropagation();
                        event.preventDefault();
                        return false;
                    }
                }
            }, data);
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
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
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span6">
			<ul class="breadcrumb">
				<li class="active">用户菜单列表</li>
			</ul>
			<ul id="userMenuTree<s:property value='#parameters.id'/>" class="ztree"></ul>
		</div>
		<div class="span6">
			<ul class="breadcrumb">
				<li class="active">所有菜单列表</li>
			</ul>
			<ul id="allMenuTree<s:property value='#parameters.id'/>" class="ztree"></ul>
		</div>
	</div>
</div>
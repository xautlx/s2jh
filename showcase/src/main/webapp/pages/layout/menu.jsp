<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
    $(function() {
        $("#menuAccordion").accordion({
            heightStyle : "fill"
        });
    });

    var zNodes = <s:property value='#request.menuJsonData' escape='false'/>;
</script>
</head>
<body>
	<div id="menuAccordion">
		<s:iterator value="#request.rootMenus" status="status" var="item">
			<h3>
				<s:property value="#item.name" />
			</h3>
			<div>
				<ul id="navMenu<s:property value='#item.id' />" class="ztree">加载数据...
				</ul>
			</div>
			<script type="text/javascript">
                var itemData;
                $.each(zNodes, function(i, item) {
                    if (item.id == "<s:property value='#item.id' />") {
                        itemData = item;
                    }
                });
                $.fn.zTree.init($("#navMenu<s:property value='#item.id'/>"), {
                    callback : {
                        onClick : function(event, treeId, treeNode) {
                            var url = treeNode.url;
                            if (url == undefined || url == '') {
                                //alert("未定义菜单URL");
                                return;
                            }
                            if (url.startWith("/")) {
                                url = "${base}" + url;
                            }
                            url = AddOrReplaceUrlParameter(url, '_', new Date().getTime());
                            tabpanel.addTab({
                                id : 'tabpanel_' + treeNode.tId,
                                title : treeNode.name,
                                closable : true,
                                html : '<iframe src="' + url + '" width="100%" height="100%" frameborder="0" id="' + 'tabpanel_' + treeNode.tId + 'Frame" name="' + 'tabpanel_' + treeNode.tId
                                        + 'Frame"></iframe>'
                            });
                            event.stopPropagation();
                            event.preventDefault();
                            return false;
                        }
                    }
                }, itemData.children);
            </script>
		</s:iterator>
	</div>
</body>
</html>
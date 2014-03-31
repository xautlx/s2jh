$(function() {
    $(".grid-sys-menu-index").data("gridOptions", {
        url : WEB_ROOT + "/sys/menu!findByPage",
        colModel : [ {
            label : '名称',
            name : 'title',
            width : 150,
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.change(function() {
                        $grid.jqGrid("setEditingRowdata", {
                            'filterSpell' : makePy($elem.val())
                        });
                    });
                }
            },
            align : 'left'
        }, {
            label : '代码',
            name : 'code',
            align : 'center',
            editable : true,
            width : 100,
            editoptions : {
                placeholder : '创建可留空自动生成'
            }
        }, {
            label : '图标',
            name : 'style',
            editable : true,
            width : 80,
            align : 'center',
            formatter : function(cellValue, options, rowdata, action) {
                if (cellValue) {
                    return '<i class="fa ' + cellValue + '" icon="' + cellValue + '"></i>';
                } else {
                    return ''
                }
            },
            unformat : function(cellValue, options, cell) {
                return $('i', cell).attr('icon');
            }
        }, {
            label : '菜单URL',
            name : 'url',
            editable : true,
            width : 200,
            align : 'left'
        }, {
            label : '类型',
            name : 'type',
            width : 80,
            editable : true,
            align : 'center',
            stype : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('menuTypeEnum')
            }
        }, {
            label : '展开',
            name : 'initOpen',
            editable : true,
            edittype : "checkbox"
        }, {
            label : '禁用',
            name : 'disabled',
            editable : true,
            edittype : "checkbox"
        }, {
            label : '排序号',
            name : 'orderRank',
            width : 60,
            editable : true,
            sorttype : 'number'
        }, {
            label : '备注说明',
            name : 'description',
            width : 200,
            hidden : true,
            editable : true,
            edittype : 'textarea'
        } ],
        sortorder : "desc",
        sortname : 'orderRank',
        multiselect : false,
        subGrid : true,
        gridDnD : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
        },
        editurl : WEB_ROOT + "/sys/menu!doSave",
        delurl : WEB_ROOT + "/sys/menu!doDelete",
        editcol : 'title',
        inlineNav : {
            add : true
        }
    });
});
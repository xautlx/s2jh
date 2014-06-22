/**
 * Custom module for you to write your own javascript functions
 */

var Biz = function() {

    // private functions & variables

    var CacheDatas = {};

    // public functions
    return {

        init : function() {

        },

        preFetechCacheData : function() {
            setTimeout(function() {
                Biz.initCacheCommodityDatas(true);
                Biz.initCacheCustomerProfileDatas(true);
            }, 2000);
        },

        getGridCommodityEditOptions : function(selectCommodityFunc) {
            return {
                placeholder : '输入编码、名称、首字母拼音过滤...',
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);

                    $elem.wrap('<div class="input-icon right"/>');
                    $elem.before('<i class="fa fa-ellipsis-horizontal fa-select-commodity"></i>');
                    $elem.before('<i class="fa fa-times fa-clear-commodity"></i>');
                    var name = $elem.attr("name");
                    var id = name.replace(".display", ".id");
                    var selectCommodity = selectCommodityFunc;
                    if (selectCommodity == undefined) {
                        selectCommodity = function(item) {
                            var $curRow = $elem.closest("tr.jqgrow");
                            var rowdata = $grid.jqGrid("getEditingRowdata");
                            // 强制覆盖已有值
                            rowdata[id] = item.id, rowdata[name] = item.display
                            $grid.jqGrid("setEditingRowdata", rowdata);
                            $grid.jqGrid("setEditingRowdata", {
                                'commodity.barcode' : item.barcode,
                                'price' : item.salePrice,
                                'measureUnit' : item.measureUnit,
                                'storageLocation.id' : item['defaultStorageLocation.id']
                            }, true);

                            // 如果没有值才覆盖
                            $grid.jqGrid("setEditingRowdata", {
                                'quantity' : 1,
                                'discountRate' : 0
                            }, false);
                            // 更新计算相关价格信息
                            if ($grid.data("gridOptions").updateRowAmount != undefined) {
                                $grid.data("gridOptions").updateRowAmount.call($grid, $curRow);
                            }

                        }
                    }
                    $elem.parent().find(".fa-clear-commodity").click(function() {
                        var rowdata = $grid.jqGrid("getEditingRowdata");
                        // 强制覆盖已有值
                        rowdata[id] = '', rowdata[name] = ''
                        $grid.jqGrid("setEditingRowdata", rowdata);
                    });
                    $elem.parent().find(".fa-select-commodity").click(function() {
                        $(this).popupDialog({
                            url : WEB_ROOT + '/biz/md/commodity!forward?_to_=selection',
                            title : '选取商品',
                            callback : function(item) {
                                $elem.attr("title", item.display);
                                selectCommodity.call($elem, item);
                            }
                        })
                    });

                    $elem.autocomplete({
                        autoFocus : true,
                        source : function(request, response) {
                            var data = Biz.queryCacheCommodityDatas(request.term);
                            return response(data);
                        },
                        minLength : 2,
                        select : function(event, ui) {
                            var item = ui.item;
                            this.value = item.display;

                            selectCommodity(item);
                            event.stopPropagation();
                            event.preventDefault();
                            return false;
                        },
                        change : function(event, ui) {
                            if (ui.item == null || ui.item == undefined) {
                                $elem.val("");
                                $elem.focus();
                            }
                        }
                    }).focus(function() {
                        $elem.select();
                    }).dblclick(function() {
                        $elem.parent().find(".fa-select-commodity").click();
                    });
                }
            }
        },

        getStockDatas : function() {
            if (CacheDatas.Stocks == undefined) {
                var url = WEB_ROOT + "/biz/stock/storage-location!findByPage?rows=-1";
                $("body").ajaxJsonSync(url, {}, function(data) {
                    var options = {};
                    $.each(data.content, function(i, item) {
                        options[item.id] = item.display;
                    })
                    options[''] = '';
                    CacheDatas.Stocks = options;
                });
            }
            return CacheDatas.Stocks;
        },

        getBrandDatas : function() {
            if (CacheDatas.Brands == undefined) {
                var url = WEB_ROOT + "/biz/md/brand!findByPage?rows=-1";
                $("body").ajaxJsonSync(url, {}, function(data) {
                    var options = {
                        '' : ''
                    };
                    $.each(data.content, function(i, item) {
                        options[item.id] = item.display;
                    })
                    CacheDatas.Brands = options;
                })
            }
            return CacheDatas.Brands;
        },

        initCacheCommodityDatas : function(aysnc) {
            var url = WEB_ROOT + "/biz/md/commodity!frequentUsedDatas.json";
            $.ajax({
                async : aysnc,
                type : "GET",
                url : url,
                dataType : 'json',
                success : function(data) {
                    $.each(data, function(i, item) {
                        item.label = item.display;
                        item.value = item.display;
                        item.filterSpell = makePy(item.label);
                        if (item.filterSpell == undefined) {
                            item.filterSpell = "";
                        } else {
                            item.filterSpell = item.filterSpell.join(",");
                        }
                    });
                    CacheDatas.Commodities = TAFFY(data);
                    CacheDatas.Commodities.sort("barcode");
                }
            });
        },

        queryCacheCommodityDatas : function(term) {
            if (CacheDatas.Commodities == undefined) {
                Biz.initCacheCommodityDatas(false);
            }
            var query = null;
            if ($.isNumeric(term)) {
                query = [ {
                    barcode : {
                        like : term
                    }
                }, {
                    commodityBarcode : {
                        like : term
                    }
                } ];
            } else {
                query = [ {
                    title : {
                        like : term
                    }
                }, {
                    filterSpell : {
                        likenocase : term
                    }
                } ];
            }
            var result = CacheDatas.Commodities(query).order("barcode");
            return result.get();
        },

        initCacheCustomerProfileDatas : function(aysnc) {
            var url = WEB_ROOT + "/biz/customer/customer-profile!frequentUsedDatas.json";
            $.ajax({
                async : aysnc,
                type : "GET",
                url : url,
                dataType : 'json',
                success : function(data) {
                    $.each(data, function(i, item) {
                        item.id = item.id;
                        item.label = item.display;
                        item.value = item.display;
                        item.filterSpell = makePy(item.label);
                        if (item.filterSpell == undefined) {
                            item.filterSpell = "";
                        } else {
                            item.filterSpell = item.filterSpell.join(",");
                        }
                    });
                    CacheDatas.CustomerProfiles = TAFFY(data);
                    CacheDatas.CustomerProfiles.sort("value");
                }
            });
        },

        queryCacheCustomerProfileDatas : function(term) {
            if (CacheDatas.CustomerProfiles == undefined) {
                Biz.initCacheCustomerProfileDatas(false);
            }
            var query = [ {
                label : {
                    like : term
                }
            }, {
                filterSpell : {
                    likenocase : term
                }
            } ];
            var result = CacheDatas.CustomerProfiles(query).order("value");
            return result.get();
        },

        setupCustomerProfileSelect : function($form) {
            // 客户元素处理
            $form.find(".fa-select-customer-profile").click(function() {
                $(this).popupDialog({
                    url : WEB_ROOT + '/biz/finance/biz-trade-unit!forward?_to_=selection',
                    title : '选取客户',
                    callback : function(rowdata) {
                        $form.find("input[name='customerProfile.display']").val(rowdata.display);
                        $form.find("input[name='customerProfile.id']").val(rowdata.id);
                    }
                })
            });
            $form.find("input[name='customerProfile.display']").autocomplete({
                autoFocus : true,
                source : function(request, response) {
                    var data = Biz.queryCacheCustomerProfileDatas(request.term);
                    return response(data);
                },
                minLength : 2,
                select : function(event, ui) {
                    var item = ui.item;
                    this.value = item.display;
                    $form.find("input[name='customerProfile.display']").val(item.display);
                    $form.find("input[name='customerProfile.id']").val(item.id);
                    event.stopPropagation();
                    event.preventDefault();
                    return false;
                },
                change : function(event, ui) {
                    if (ui.item == null || ui.item == undefined) {
                        $(this).val("");
                        $(this).focus();
                    }
                }
            }).focus(function() {
                $(this).select();
            }).dblclick(function(event) {
                $form.find(".fa-select-customer-profile").click();
            });
        },

        setupBizTradeUnitSelect : function($form, selectCallback) {
            // 往来单位选取
            $form.find(".fa-select-biz-trade-unit").each(function() {
                var $trigger = $(this);
                var $text = $trigger.parent().find('input[type="text"]');
                var $hidden = $trigger.parent().find('input[type="hidden"]');

                if (BooleanUtil.toBoolean($text.attr("readonly"))) {
                    return;
                }

                $trigger.click(function() {
                    $(this).popupDialog({
                        url : WEB_ROOT + '/biz/finance/biz-trade-unit!forward?_to_=selection',
                        title : '选取往来单位',
                        callback : function(rowdata) {
                            $text.val(rowdata.display);
                            $hidden.val(rowdata.id);
                            if (selectCallback) {
                                selectCallback.call($trigger, rowdata)
                            }
                        }
                    })
                });

                $text.dblclick(function(event) {
                    $trigger.click();
                });
            })
        }
    };

}();

/*******************************************************************************
 * Usage
 ******************************************************************************/
// Custom.init();
// Custom.doSomeStuff();

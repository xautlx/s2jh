$(function() {
    $(".form-stock-commodity-stock-inventory").data("formOptions", {
        bindEvents : function() {
            var $form = $(this);

            //扫描枪输入处理
            $form.find("input[name='commodityBarcode'],input[name='batchNo']").barcodeScanSupport({
                onEnter : function() {
                    var $el = $(this);
                    var sid = $form.find("select[name='storageLocation.id']").val();
                    var batchNo = $form.find("input[name='batchNo']").val();
                    if (batchNo == undefined) {
                        batchNo = "";
                    }
                    var barcode = $el.val();
                    if (sid == '' || barcode == '') {
                        alert("必须选取库存地和输入商品条码");
                        return;
                    }
                    if (barcode != '') {
                        var url = WEB_ROOT + "/biz/stock/commodity-stock!findForInventory";
                        url += "?barcode=" + barcode + "&storageLocationId=" + sid + "&batchNo=" + batchNo;
                        $el.ajaxJsonUrl(url, function(data) {
                            $form.find(".commodity-display").html(data.commodity.display);

                            if (data.curStockQuantity) {
                                $form.find(".commodity-quantity").html('<span class="label label-danger">' + data.curStockQuantity + '</span>');
                                $form.find("input[name='curStockQuantity']").val(data.curStockQuantity);
                                $form.find("input[name='preStockQuantity']").val(data.curStockQuantity);
                                $form.find("input[name='costPrice']").val(data.costPrice);
                                $form.find(".form-group-cost-price").hide();
                            } else {
                                $form.find(".commodity-quantity").html('<span class="label label-danger">无记录</span>');
                                $form.find("input[name='preStockQuantity']").val(0);
                                $form.find("input[name='costPrice']").val('');
                                $form.find(".form-group-cost-price").show();
                                $form.find("input[name='curStockQuantity']").val('');
                            }
                            $form.find("input[name='id']").val(data.id);
                            $form.find("input[name='commodity.id']").val(data.commodity.id);
                            $form.find('[type="submit"]').attr("disabled", false);

                            $form.find(".commodity-info").removeClass("hide");
                        })
                        $el.focus();
                    }
                }
            });
        },
        successCallback : function() {
            var $form = $(this);

            $form.find(".commodity-info").addClass("hide");
            $form.find(".portlet-stock-inventory-logs").removeClass("hide");

            var $display = $form.find(".commodity-display");
            var $quantity = $form.find("input[name='curStockQuantity']");
            var logs = $form.find(".list-stock-inventory-logs");
            logs.append('<li class="list-group-item">' + $display.html() + ' <span class="badge badge-default">' + $quantity.val() + '</span></li>')

            $form.find('[type="submit"]').attr("disabled", true);
            var $barcode = $form.find("input[name='commodityBarcode']");
            $barcode.val("");
            $barcode.focus();
        },
        preValidate : function() {
            var $form = $(this);
            var preStockQuantity = $form.find("input[name='preStockQuantity']").val();
            var curStockQuantity = $form.find("input[name='curStockQuantity']").val();
            var inventoryExplain = $form.find("input[name='inventoryExplain']").val();
            if (curStockQuantity != preStockQuantity && (inventoryExplain == undefined || inventoryExplain == '')) {
                bootbox.alert("如果盘存有数量差异必须填写损益说明");
                return false;
            }
        }
    });

});
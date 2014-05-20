$(function() {
    $(".form-stock-commodity-stock-inventory").data("formOptions", {
        bindEvents : function() {
            var $form = $(this);

            //扫描枪输入处理
            $form.find("input[name='commodityBarcode']").barcodeScanSupport({
                onEnter : function() {
                    var $el = $(this);
                    var sid = $form.find("select[name='storageLocation.id']").val();
                    var barcode = $el.val();
                    if (sid == '' || barcode == '') {
                        alert("必须选取库存地和输入商品条码");
                        return;
                    }
                    if (barcode != '') {
                        var url = WEB_ROOT + "/biz/stock/commodity-stock-inventory!findForInventory";
                        url += "?barcode=" + barcode + "&storageLocationId=" + sid;
                        $el.ajaxJsonUrl(url, function(data) {
                            $form.find(".commodity-display").html(data.commodity.display);

                            if (data.curStockQuantity) {
                                $form.find(".commodity-quantity").html('<span class="label label-danger">' + data.curStockQuantity + '</span>');
                                $form.find("input[name='curStockQuantity']").val(data.curStockQuantity);
                            } else {
                                $form.find(".commodity-quantity").html('<span class="label label-danger">无记录</span>');
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
        }
    });

});
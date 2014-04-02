jQuery(document).ready(function() {

    App.init();

    $('#login-form').validate({
        errorElement : 'span', //default input error message container
        errorClass : 'help-block', // default input error message class
        focusInvalid : false, // do not focus the last invalid input
        rules : {
            j_username : {
                required : true
            },
            j_password : {
                required : true
            },
            j_captcha : {
                required : true
            }
        },

        messages : {
            j_username : {
                required : "请填写登录账号"
            },
            j_password : {
                required : "请填写登录密码"
            },
            j_captcha : {
                required : "请填写登录验证码"
            }
        },

        highlight : function(element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },

        errorPlacement : function(error, element) {
            error.appendTo(element.closest('.form-group'));
        },

        submitHandler : function(form) {
            form.submit();
        }
    });

    var imageHV = 'h';
    if ($(window).height() > $(window).width()) {
        imageHV = 'v';
        //$(".content").removeClass('pull-left');
    }

    $.backstretch([ "../resources/images/what01_" + imageHV + ".jpg", "../resources/images/what02_" + imageHV + ".jpg" ], {
        fade : 1000,
        duration : 8000
    });

    $('#forget-form').validate({
        errorElement : 'span', //default input error message container
        errorClass : 'help-block', // default input error message class
        focusInvalid : false, // do not focus the last invalid input
        ignore : "",
        rules : {
            email : {
                required : true,
                email : true
            }
        },

        messages : {
            email : {
                required : "请填写电子邮箱地址"
            }
        },

        highlight : function(element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },

        errorPlacement : function(error, element) {
            error.appendTo(element.closest('.form-group'));
        },

        submitHandler : function(form) {
            form.submit();
        }
    });

    $('#register-form').validate({
        errorElement : 'span', //default input error message container
        errorClass : 'help-block', // default input error message class
        focusInvalid : false, // do not focus the last invalid input
        ignore : "",
        rules : {
            signinid : {
                required : true
            },
            password : {
                required : true
            },
            rpassword : {
                required : true,
                equalTo : "#password"
            },
            email : {
                required : true,
                email : true
            },
            tnc : {
                required : true
            }
        },

        messages : { // custom messages for radio buttons and checkboxes
            signinid : {
                required : "请填写登录账号"
            },
            password : {
                required : "请输入登录密码"
            },
            rpassword : {
                required : "请再次输入登录密码",
                equalTo : "确认密码必须与登录密码一致"
            },
            email : {
                required : "请填写注册电子邮件",
                email : "请填写有效格式的电子邮件地址"
            },
            tnc : {
                required : "注册账号必须勾选同意协议."
            }
        },

        highlight : function(element) { // hightlight error inputs
            if ($(element).attr("name") == "tnc") { // insert checkbox errors after the container                  
                $(element).closest('.row').addClass('has-error');
            } else {
                $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
            }
        },

        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },

        errorPlacement : function(error, element) {
            if (element.attr("name") == "tnc") { // insert checkbox errors after the container                  
                error.insertAfter($('#register_tnc_error'));
            } else if (element.closest('.input-icon').size() === 1) {
                error.insertAfter(element.closest('.input-icon'));
            } else {
                error.insertAfter(element);
            }
        },

        submitHandler : function(form) {
            App.blockUI($(form));
            $(form).ajaxSubmit({
                dataType : "json",
                method : "post",
                success : function(response) {
                    App.unblockUI($(form));
                    if (response.type == "success") {
                        $("#register-form").find('button[data-dismiss="modal"]').click();
                        bootbox.dialog({
                            message : "注册账号已提交成功，请耐心等待管理员审核授权！<br/>审核通过后会自动发送通知邮件到您的注册邮箱！",
                            title : "恭喜，注册提交完成",
                            buttons : {
                                main : {
                                    label : "关闭",
                                    className : "blue"
                                }
                            }
                        });
                    } else if (response.type == "failure") {
                        bootbox.alert(response.message);
                    } else {
                        bootbox.alert("表单处理异常，请联系管理员");
                    }
                },
                error : function(xhr, e, status) {
                    App.unblockUI($form);
                    var response = jQuery.parseJSON(xhr.responseText);
                    if (response.type == "error") {
                        bootbox.alert(response.message);
                    } else {
                        bootbox.alert("表单处理异常，请联系管理员");
                    }
                }
            })
            return false;
        }
    });

});
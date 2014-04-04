jQuery(document).ready(function() {

    App.init();
    Util.init();
    FormValidation.init();

    $('#login-form').find("input:first").focus();

    jQuery('body').on('click', '.captcha-img', function(e) {
        $(".captcha-img").each(function() {
            $(this).attr('src', WEB_ROOT + '/assets/img/captcha_placeholder.jpg');
        })
        $(this).attr('src', WEB_ROOT + '/pub/jcaptcha.servlet?_=' + new Date().getTime());
        var $captchaText = $(this).closest(".form-group").find(".captcha-text");
        $captchaText.val("");
        $captchaText.focus();
        return false;
    });

    jQuery('body').on('focus', '.captcha-text', function(e) {
        var $captchaImg = $(this).closest(".form-group").find(".captcha-img");
        if ($captchaImg.attr("src") == WEB_ROOT + '/assets/img/captcha_placeholder.jpg') {
            $captchaImg.click();
        }
    });

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

    $.backstretch([ WEB_ROOT + "/assets/img/bg/1.jpg", WEB_ROOT + "/assets/img/bg/2.jpg", WEB_ROOT + "/assets/img/bg/3.jpg", WEB_ROOT + "/assets/img/bg/4.jpg" ], {
        fade : 1000,
        duration : 8000
    });

    $('#forget-form').validate({
        errorElement : 'span', //default input error message container
        errorClass : 'help-block', // default input error message class
        focusInvalid : false, // do not focus the last invalid input
        ignore : "",
        rules : {
            uid : {
                required : true
            },
            j_captcha : {
                required : true
            }
        },

        messages : {
            uid : {
                required : "请填写登录账号或注册邮箱"
            },
            j_captcha : {
                required : "请填写验证码"
            },
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
            $(form).ajaxPostForm(function(data) {
                $(".captcha-img").each(function() {
                    $(this).attr('src', WEB_ROOT + '/assets/img/captcha_placeholder.jpg');
                })
                $(form).find('button[data-dismiss="modal"]').click();
                bootbox.dialog({
                    message : "系统已发送重置密码邮件至：<span class='text-primary'>" + data.userdata + "</span><br/>请稍后访问此邮箱按照邮件内容提示操作重新设置密码！",
                    title : "恭喜，找回密码请求成功",
                    buttons : {
                        main : {
                            label : "关闭",
                            className : "blue"
                        }
                    }
                });
            }, false, function() {
                var $captchaImg = $(form).find(".captcha-img");
                $captchaImg.click();
            });
            return false;
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
                equalToByName : "password"
            },
            email : {
                required : true,
                email : true
            },
            j_captcha : {
                required : true
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
            j_captcha : {
                required : "请填写验证码"
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
            $(form).ajaxPostForm(function() {
                $(".captcha-img").each(function() {
                    $(this).attr('src', WEB_ROOT + '/assets/img/captcha_placeholder.jpg');
                })
                $(form).find('button[data-dismiss="modal"]').click();
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
            }, false, function() {
                var $captchaImg = $(form).find(".captcha-img");
                $captchaImg.click();
            });
            return false;
        }
    });

    $('#reset-form').validate({
        errorElement : 'span', //default input error message container
        errorClass : 'help-block', // default input error message class
        focusInvalid : false, // do not focus the last invalid input
        ignore : "",
        rules : {
            password : {
                required : true
            },
            rpassword : {
                required : true,
                equalToByName : "password"
            }
        },

        messages : {
            password : {
                required : "请输入登录密码"
            },
            rpassword : {
                required : "请再次输入登录密码",
                equalTo : "确认密码必须与登录密码一致"
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
            error.insertAfter(element);
        },

        submitHandler : function(form) {
            $(form).ajaxPostForm(function() {
                $(form).find('button[data-dismiss="modal"]').click();
                bootbox.dialog({
                    message : "您可以马上使用新设定密码登录系统啦",
                    title : "恭喜，密码设置成功",
                    buttons : {
                        main : {
                            label : "关闭",
                            className : "blue"
                        }
                    }
                });
            });
            return false;
        }
    });
});
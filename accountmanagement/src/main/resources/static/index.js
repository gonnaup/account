var mainMenu_show = true//是否展示主菜单
function listenEvents() {
    var $ = layui.jquery
    var layer = layui.layer

    //菜单收缩按钮监听
    $("#main-flexible").click(function (e) {
        e.preventDefault()
        $("#main-menu span").each(function () {
            if ($(this).is(':hidden')) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
        if (mainMenu_show) {
            //伸缩图标处理
            $("#icon-flexible").removeClass("layui-icon-shrink-right")
            $("#icon-flexible").addClass("layui-icon-spread-left")
            //logo处理
            $("#main-applicationName").hide()
            //主菜单宽度处理
            $("#main-menu").animate({width: 60}, 200)
            $("#main-logo").animate({width: 60}, 200)
            //将footer和body的宽度修改
            $('#flexible-header').animate({left: 60}, 200)
            $('.layui-body').animate({left: 60}, 200)
            $('.layui-footer').animate({left: 60}, 200)
            //将二级导航栏隐藏
            $('dd span').each(function(){
                $(this).hide();
            });
            mainMenu_show = false
        } else {
            $("#icon-flexible").addClass("layui-icon-shrink-right")
            $("#icon-flexible").removeClass("layui-icon-spread-left")
            $("#main-menu").animate({width: 200}, 200)
            $("#main-logo").animate({width: 200}, 200)
            $('#flexible-header').animate({left: 200}, 200)
            $('.layui-body').animate({left: 200}, 200)
            $('.layui-footer').animate({left: 200}, 200)
            $("#main-applicationName").show()
            $('dd span').each(function(){
                $(this).show();
            });
            mainMenu_show = true
        }
    })

    //监听登录按钮点击事件
    $("#login_click_button").click(function (e) {
        e.preventDefault()
        openLoginPage()
    })

    //退出按钮监听
    $("#signout_entrance").click(function (e) {
        e.preventDefault()
        layer.confirm('是否注销登录？', {
                icon: 3,
                btn: ['注销', '点错了']
            }, function (index) {
                signout()
            }
        )
    })

    //菜单导航点击事件监听
    $("[router-url]").click(function (e) {
        e.preventDefault();
        routerTo($(this).attr("router-url"))
    })

    //请求错误拦截处理
    $(document).ajaxError(function (event, xhr, options, exc) {
        //无权限
        if (xhr.status == 403) {
            var code = xhr.responseJSON.code; //401:未登录，402：已登录但权限不够
            if (code == '401' && openLoginTipsPage) {//未登录
                layer.confirm(xhr.responseJSON.data + '，是否现在登陆？', {
                    icon: 3,
                    btn: ['现在就去', '等会再说'] //按钮
                }, function (index) {
                    layer.close(index)//关闭上层layler
                    openLoginPage()
                });
            } else if (code == '402' && openLoginTipsPage) {//权限不够
                layer.alert('您的权限不够，请联系管理员')
            } else {
                openLoginTipsPage = true//当此值为false时赋值为true，只打断一次提示
            }
        }
    });
}


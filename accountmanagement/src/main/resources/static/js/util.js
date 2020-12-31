function login_jwt() {
    var jwt = localStorage.getItem(jwt_localStorageName)
    //存在jwt
    if (jwt != undefined) {
        var $ = layui.jquery;
        $("#login_entrance_logo").html("<i class='layui-icon layui-icon-loading'></i>")
        $.ajax({
            url: '../api/authenticate/authenticationJWT',
            type: 'get',
            headers: {token_jwt: jwt},
            success: function (data) {
                if (data.code == SUCCESS) {
                    fillAccountInformation(data.data)
                }
                if (data.code == LOGIN_ERROR) {
                    var layer = layui.layer
                    layer.confirm(data.message + '，是否现在登陆？', {
                        icon: 3,
                        btn: ['现在就去', '等会再说'] //按钮
                    }, function (index) {
                        layer.close(index)//关闭上层layler
                        $.ajax({
                            url: '../html/login.html',
                            type: 'get',
                            success: function (data) {
                                layer.open({
                                    type: 1,
                                    area: '450px',
                                    title: ['登录'],
                                    content: data
                                })
                            },
                            error: function () {
                                layer.msg('获取登录页面失败!')
                            }
                        })

                    });
                }
            },
            error: function () {

            }

        })
    }
}

/**
 * 填充账号信息
 * @param account
 */
function fillAccountInformation(account) {
    var $ = layui.jquery;
    $("#login_entrance").css('display', 'none')
    $("#account_information").css('display', 'inline-block')
    // $("#account_avatar").attr('src', account.accountAvatar)
    $("#account_nickname").html(account.accountNickname)
    $("#login_entrance_logo").html("请登录")
}
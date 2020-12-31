var jwt_headerName = 'token_jwt'
var jwt_localStorageName = 'authentication_token'
var SUCCESS = '200';//成功
var FAIL = '400';//失败
var NOTLOGIN_ERROR = '401';//未登录
var AUTH_ERROR = '402';//鉴权失败
var LOGIN_ERROR = '403';//登录失败
var SYSTEM_ERROR = '500';//服务器异常

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
                    $("#login_entrance_logo").html("请登录")
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
                $("#login_entrance_logo").html("请登录")
            }

        })
    }
}


/**
 * 打开登录页面
 */
function openLoginPage() {
    var $ = layui.jquery
    var layer = layui.layer
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
}
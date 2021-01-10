/**
 * 请求api
 */

var JWT_HEADERNAME = 'token_jwt'
var JWT_LOCALSTORAGENAME = 'authentication_token'
var SUCCESS = '200';//成功
var FAIL = '400';//失败
var NOTLOGIN_ERROR = '401';//未登录
var AUTH_ERROR = '402';//鉴权失败
var LOGIN_ERROR = '403';//登录失败
var SYSTEM_ERROR = '500';//服务器异常

var openLoginTipsPage = true//是否打开登录提示layer，用于注销重新加载页面时不显示此页面



/**
 * 进入系统尝试登陆
 */
function login_jwt() {
    var jwt = obtainJwt()
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
    openPage('../html/login.html')
}

/**
 * 注销
 */
function signout() {
    var jwt = obtainJwt()
    if (jwt != undefined) {
        var layer = layui.layer
        var $ = layui.jquery
        $.ajax({
            url: '../api/authenticate/signout',
            type: 'delete',
            headers: {token_jwt: jwt},
            success: function (data) {
                //当有加载的页面时重新加载页面
                if (current_page != undefined) {
                    openLoginTipsPage = false;//不打开提示登录layer
                    routerTo(current_page)
                }
                localStorage.removeItem(JWT_LOCALSTORAGENAME)//移除jwt
                layer.msg('账号注销成功!', {icon: 1})
                cleanAccountInformation()//清除登录账号信息
            },
            error: function (xhr) {

            }
        })
    }
}

/**
 * 对账户增删改操作验证，根据用户权限禁用按钮
 */
function accountOperateValidate() {
    var $ = layui.jquery
    var jwt = obtainJwt() || ''
    $.ajax({
        url: '../api/authenticate/simplePermission',
        type: 'get',
        headers: {token_jwt: jwt},
        success: function (data) {
            var permission = data.data
            if (!permission.add) {
                disabeButton('#accountbar_add')
            }
            if (!permission.delete) {
                disabeButton('#accountbar_delete')
            }
            if (!permission.update) {
                disabeButton('#accountbar_update')
            }
        }
    })
}
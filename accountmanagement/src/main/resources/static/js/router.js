/**
 * 加载页面js
 *
 */

var pagepermission_urlMap = new Map()
pagepermission_urlMap.set('../html/operationlog.html', '../api/operationlog/display')
pagepermission_urlMap.set('../html/account.html', '../api/account/display')
pagepermission_urlMap.set('../html/applicationcode.html', '../api/applicationCode/display')
pagepermission_urlMap.set('../html/applicationsequence.html', '../api/applicationSequence/display')
pagepermission_urlMap.set('../html/role.html', '../api/role/display')
pagepermission_urlMap.set('../html/permission.html', '../api/permission/display')
pagepermission_urlMap.set('../html/authentication.html', '../api/authentication/display')
var current_page//当前页面缓存

/**
 * 路由方法
 * @param url
 */
var routerTo = function (url) {
    current_page = url
    var $ = layui.jquery;
    var pagepermission_url = pagepermission_urlMap.get(url)
    if (pagepermission_url != undefined) {
        var jwt = localStorage.getItem(JWT_LOCALSTORAGENAME);
        $.ajax({
            url: pagepermission_url,
            type: 'get',
            headers: {token_jwt: jwt == undefined ? '' : jwt},
            success: function (reData) {
                loadContentPage(url)
            },
            error: function (reData) {
                loadContentPage('../html/403.html')
            }
        })
    } else {
        loadContentPage(url)
    }
}

/**
 * 加载主窗口页面
 * @param url
 */
function loadContentPage(url) {
    var $ = layui.jquery;
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'html',
        success: function (rsData) {
            $("#content-container").html(rsData);
        },
        error: function () {
            return layui.layer.msg('无法获取页面：' + url);
        }
    });
}

/**
 * 加载悬浮页面
 * @param url
 */
function openPage(url, title) {
    var $ = layui.jquery
    var layer = layui.layer
    $.ajax({
        url: url,
        type: 'get',
        success: function (data) {
            layer.open({
                type: 1,
                area: '450px',
                title: title,
                content: data
            })
        },
        error: function () {
            layer.msg('获取登录页面失败!')
        }
    })
}
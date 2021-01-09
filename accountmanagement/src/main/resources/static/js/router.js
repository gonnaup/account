/**
 * 加载页面js
 *
 */

var pagepermission_urlMap = new Map()
pagepermission_urlMap.set('../html/operationlog.html', '../api/operationlog/display')
pagepermission_urlMap.set('../html/account.html', '../api/account/display')
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

///////////////////// account.html /////////////////////////
//账号添加按钮点击
function accountAddClicked() {
    console.log("a")
}

//账号删除按钮点击
function accountDeleteClicked(rowData) {
    console.log("d")
}

//账号更新按钮点击
function accountUpdateClicked(rowData) {
    console.log("u")
}




///////////////////////////// end /////////////////////////////


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
function openPage(url) {
    var $ = layui.jquery
    var layer = layui.layer
    $.ajax({
        url: url,
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
var pagepermission_urlMap = new Map()
pagepermission_urlMap.set('../html/operationlog.html', '../api/operationlog/display')
pagepermission_urlMap.set('../html/account.html', '../api/account/display')
var current_page//当前页面缓存

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
                loadpage(url)
            },
            error: function (reData) {
                loadpage('../html/403.html')
            }
        })
    } else {
        loadpage(url)
    }
}

function loadpage(url) {
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